#!/bin/bash
# MIT License
#
# Copyright (c) 2025 Jeonhui Lee
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.

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
        effect=("-e" "bold")
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
  styled_text -c red "${effect[@]}" "$prefix$message\n"
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
  success=$(echo "$value" | awk '{print tolower($0)}' | awk '/success/ {print $0}')
  errors=$(echo "$value" | awk '{print tolower($0)}' | awk '/error:|fail|fatal/ {print $0}')
  if [[ -n "$errors" ]]; then
    error_message -sp "$value" "$exit_opt"
  elif [[ -n "$success" ]]; then
    success_message "$value"
  else
    default_message "$value"
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
  default_message "git pull path: $pw"
  output=$(git pull 2>&1)
  if [[ "$output" == "Already up to date." ]]; then
    no_changed=1
  fi
  message_validator "$output\n" "$exit_opt"
  cd "$current" || return
}

### build server
# - Build the java server.
function build_server {
  status_message "Build the java server..."
  path="" clean="" exit_opt="" build_test=("-x" "test")
  while [[ "$#" -gt 0 ]]; do
    case "$1" in
      -e)
        exit_opt="-e"
        shift
        ;;
      -t)
        build_test=()
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
  cd "$path" || return
  pw=$(pwd)
  default_message "gradlew build path: $pw"
  output=""
  while IFS= read -r line; do
    message_validator "$line"
  done < <(./gradlew $clean build "${build_test[@]}" 2>&1)
  echo ""
  cd "$current" || return
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
    success_message "Running process: $current_pid/$current_program"
    default_message "Shutting down the $current_program process..."
    kill -9 "$current_pid"
    error_message -sp "Shut downed.\n"
  elif [[ -z "$current_program" ]]; then
    default_message "No running process.\n"
  else
    error_message "Another process($current_pid/$current_program) is in use.\n" "$exit_opt"
  fi
}

function run_server {
  status_message "Run the java server..."
  path="" exit_opt=""
  session_name=
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
  jar_files=("$build_file_dir"/*[^-plain].jar)
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

  default_message "command: $run_cmd\n"

  # session
  if [[ -n "$session_name" ]]; then
    session=$(screen -ls | awk '/\t/ {print $1}' | grep "$session_name")
    validate_not_empty "$session" "Session name not found.\n"
    status_message "[$session] run server"
    screen -S "$session" -X stuff "$(printf "%s\r" "$run_cmd")"
    success_message "Successfully sent command to session[$session]\n"
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
session=()
background=0 background_out=()
sp=0 cb=""
no_changed=0 build=0 bt="" reboot=0
while [[ "$#" -gt 0 ]]; do
  case "$1" in
    -h)
      help
      exit 0
      ;;
    -s)
      session=("-s" "$2")
      shift 2
      ;;
    -b)
      build=1
      shift
      ;;
    -bt)
      bt="-t"
      shift
      ;;
    -bc)
      background="-b"
      shift
      ;;
    -cb)
      cb="-c"
      shift
      ;;
    -o)
      background_out=("-o" "$2")
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

if [ "$reboot" -eq 1 ] || { [ "$no_changed" -eq 0 ] && [ "$reboot" -ne 1 ]; }; then
  shutdown_server "$port" -e
fi

if [ "$no_changed" -eq 0 ] || [ "$build" -eq 1 ]; then
  build_server "$project_path" "$cb" "$bt" -e
fi

if [ "$reboot" -eq 1 ] || { [ "$no_changed" -eq 0 ] && [ "$reboot" -ne 1 ]; }; then
  run_server "$project_path" "${session[@]}" "$exit_opt" "$background" "${background_out[@]}"
fi

status_message "Finished.\n"
