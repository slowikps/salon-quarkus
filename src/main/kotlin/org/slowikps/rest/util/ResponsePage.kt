package org.slowikps.rest.util

data class Cursor(val next: String, val prev: String) //etc
class ResponsePage<T>(val items: List<T>, cursors: Cursor? = null)