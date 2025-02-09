#!/bin/bash
# Text
function text_effect {
  case "$1" in
    default) echo 0 ;;
    bold) echo 1 ;;
    underline) echo 4 ;;
    invisible) echo 8 ;;
  esac
}

function text_color {
  case "$1" in
    black) echo 30 ;;
    red) echo 31 ;;
    green) echo 32 ;;
    yellow) echo 33 ;;
    blue) echo 34 ;;
    magenta) echo 35 ;;
    cyan) echo 36 ;;
    white) echo 0 ;;
  esac
}

function styled_text {
  effect="0" color="37" text=""
  while [[ "$#" -gt 0 ]]; do
    case "$1" in
      -e)
        effect=$(text_effect "$2")
        shift 2
        ;;
      -c)
        color=$(text_color "$2")
        shift 2
        ;;
      *)
        text="$1"
        shift
        ;;
    esac
  done
  echo -e "\033[${effect};${color}m${text}\033[0m"
}

function strip {
  echo "$output" | awk '{$1=$1; print}'
}

function default_message {
  message="$1"
  styled_text -c white -e default "$message"
}

function status_message {
  message="$1"
  styled_text -c cyan -e bold "$message"
}

function success_message {
  message="$1"
  styled_text -c green -e bold "$message"
}

function error_message {
  exit=0 prefix="Error: " effect=""
  error_code=1 message=""
  while [[ "$#" -gt 0 ]]; do
    case "$1" in
      -sp)
        prefix=""
        shift
        ;;
      -b)
        effect="-e bold"
        shift
        ;;
      -e)
        exit=1
        shift
        ;;
      -c)
        error_code="$2"
        shift 2
        ;;
      *)
        if [[ -z $message ]]; then
          message="$1"
        fi
        shift
        ;;
    esac
  done
  if [[ -z "$message" ]]; then
    message="An error has occurred."
  fi
  styled_text -c red "$effect" "$prefix$message\n"
  if [[ "$exit" -eq 1 ]]; then
    exit "$error_code"
  fi
}

function validate_not_empty {
  value="$1" message="$2"
  if [[ -z "$value" ]]; then
    error_message "$message" -e
  fi
}

function message_validator {
  value="" exit_opt="" error_code=1
  error_code=1 message=""
  while [[ "$#" -gt 0 ]]; do
    case "$1" in
      -c)
        error_code="$2"
        shift 2
        ;;
      -e)
        exit_opt="-e"
        shift
        ;;
      *)
        if [[ -z $value ]]; then
          value="$1"
        fi
        shift
        ;;
    esac
  done
  errors=$(echo "$value" | awk '{print tolower($0)}' | awk '/error|fail|fatal/ {print $0}')
  if [[ -z "$errors" ]]; then
    default_message "$value"
  else
    error_message -sp "$value" "$exit_opt"
  fi
}

# Help
function help {
  echo "Usage: $script_name [options] [path]"
  echo "Options:"
  echo "  -s <session>  Run the server in the specified session."
  echo "  -b            Run the server in the background."
  echo "  -o <file>     Output the background process to the specified file. default: out.log"
  echo "  -p <port>     Specify the port number. default: 8080"
  echo "  -sp           Skip the git pull process."
  echo "  -r            Reboot the server."
  echo "  -h            Display this help message."
}

# Utils
function read_os {
  os_type=$(uname)
  case "$os_type" in
    Linux*) echo "linux" ;;
    Darwin*) echo "mac" ;;
    *) echo "unknown" ;;
  esac
}

function sudo_cmd {
  if [[ $EUID -ne 0 ]]; then
    echo "sudo"
  else
    echo ""
  fi
}

# Features
### Validate the operating system.
# - If the operating system is not Linux or Mac, exit with an error.
function validate_os {
  os=$(read_os)
  case "$os" in
    linux*) ;;
    mac*) ;;
    *) error_message "Unsupported OS." -e ;;
  esac
}

### git pull
# - Pull changes from the remote repository.
function git_pull {
  status_message "Pulling changes from the remote repository..."
  path="$1" exit_opt=""
  while [[ "$#" -gt 0 ]]; do
    case "$1" in
      -e)
        exit_opt="-e"
        shift
        ;;
      *)
        if [[ -z $path ]]; then
          path="$1"
        fi
        shift
        ;;
    esac
  done
  current=$(pwd)
  cd "$path" || return
  pw=$(pwd)
  echo "git pull path: $pw"
  output=$(git pull 2>&1)
  if [[ "$output" != "Already up to date." ]]; then
    no_changed=1
  fi
  message_validator "$output" "$exit_opt"
  cd "$current" || return
}

### build server
# - Build the java server.
function build_server {
  status_message "Build the java server..."
  path="" clean="" exit_opt=""
  while [[ "$#" -gt 0 ]]; do
    case "$1" in
      -e)
        exit_opt="-e"
        shift
        ;;
      -c)
        clean="clean"
        shift
        ;;
      *)
        if [[ -z $path ]]; then
          path="$1"
        fi
        shift
        ;;
    esac
  done
  validate_not_empty "$path" "Path argument is required."

  current=$(pwd)
  echo "gradlew build path: $current"
  output=$("$path"/gradlew "$clean"build 2>&1)
  message_validator "$output" "$exit_opt"
}

### shutdown server
# - Shut down the server running on the specified port.
function shutdown_server {
  status_message "Shutting down the server..."
  port="" program="java" exit_opt=""
  while [[ "$#" -gt 0 ]]; do
    case "$1" in
      -g)
        program="$2"
        shift 2
        ;;
      -e)
        exit_opt="-e"
        shift
        ;;
      *)
        if [[ -z $port ]]; then
          port="$1"
        fi
        shift
        ;;
    esac
  done
  validate_not_empty "$port" "Port argument is required."
  validate_not_empty "$program" "Program argument is required."

  lsof_output=$(lsof -i :"$port")
  current_program=$(echo "$lsof_output" | awk 'NR==2{print $1}')
  current_pid=$(echo "$lsof_output" | awk 'NR==2{print $2}')

  if [[ "$current_program" = "$program" ]]; then
    status_message "Running process: $current_pid/$current_program"
    default_message "Shutting down the $current_program process..."
    kill -9 "$current_pid"
    default_message "Shut downed."
  elif [[ -z "$current_program" ]]; then
    default_message "No running process."
  else
    error_message "Another process($current_pid/$current_program) is in use." "$exit_opt"
  fi
}

function run_server {
  status_message "Run the java server..."
  path="" exit_opt=""
  session_name=""
  background=0 background_out="out.log"
  while [[ "$#" -gt 0 ]]; do
    case "$1" in
      -s)
        session_name="$2"
        shift 2
        ;;
      -b)
        background=1
        shift
        ;;
      -o)
        background_out=$2
        shift 2
        ;;
      -e)
        exit_opt="-e"
        shift
        ;;
      *)
        if [[ -z $path ]]; then
          path="$1"
        fi
        shift
        ;;
    esac
  done
  validate_not_empty "$path" "Path argument is required."

  build_file_dir="$path/build/libs"
  jar_files=("$build_file_dir"/*"[^-plain]".jar)
  jar_file="${jar_files[0]}"

  # first matched jar file
  for file in "${jar_file[@]}"; do
    if [[ -f "$file" ]]; then
      jar_file="$file"
      break
    fi
  done
  run_cmd="java -jar $jar_file"

  # background process
  if [[ $background -eq 1 ]]; then
    run_cmd="nohup $run_cmd > $background_out &"
  fi

  # session
  if [[ -n "$session_name" ]]; then
    session=$(screen -ls | awk '/\t/ {print $1}' | grep "$session_name")
    validate_not_empty "$session" "Session name not found."
    status_message "[$session] run server"
    screen -S "$session" -X stuff "$(printf "%s\n" "$run_cmd")"
    message_validator "$output" "$exit_opt"
  else
    eval "$run_cmd"
  fi
}

# -------------------------------
# main
# -------------------------------
### arguments
#sudo_command=$(sudo_cmd)
script_name=$(basename "${BASH_SOURCE[0]}")

current_path=$(pwd)
project_path=$current_path
port="8080"
session=""
background=0 background_out=""
sp=0 cb=""
no_changed=0 reboot=0
while [[ "$#" -gt 0 ]]; do
  case "$1" in
    -h)
      help
      exit 0
      ;;
    -s)
      session="-s $2"
      shift 2
      ;;
    -b)
      background="-b"
      shift
      ;;
    -cb)
      cb="-c"
      shift
      ;;
    -o)
      background_out="-o $2"
      shift 2
      ;;
    -p)
      port="$2"
      shift 2
      ;;
    -sp)
      sp=1
      shift
      ;;
    -r)
      reboot=1
      shift
      ;;
    *)
      if [[ "$project_path" == "$current_path" && -n "$1" ]]; then
        project_path="$1"
      fi
      shift
      ;;
  esac
done

### process
validate_os
[ "$sp" -eq 0 ] && git_pull "$project_path" -e
[ "$no_changed" -eq 0 ] && build_server "$project_path" "$cb" -e
if [ "$reboot" -eq 1 ] || { [ "$no_changed" -eq 0 ] && [ "$reboot" -ne 1 ]; }; then
  shutdown_server "$port" -e
  run_server "$project_path" "$exit_opt" "$session" "$background"
fi
status_message "Finished."
