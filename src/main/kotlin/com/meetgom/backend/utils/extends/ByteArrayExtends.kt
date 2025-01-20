package com.meetgom.backend.utils.extends

fun ByteArray.toHexString(): String {
    return this.joinToString("") { "%02x".format(it) }
}