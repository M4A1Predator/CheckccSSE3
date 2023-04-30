package com.gamitology.checkcc.util

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

fun createPostReq(endpoint: String, data: String): HttpURLConnection {
    val url = URL(endpoint)
    val conn = url.openConnection() as HttpURLConnection
    conn.requestMethod = "POST"
    conn.doOutput = true
    conn.setRequestProperty("Content-Type", "application/json")
    conn.setRequestProperty("Content-Length", data.length.toString())
    conn.useCaches = false
    DataOutputStream(conn.outputStream).use { it.writeBytes(data) }
    return conn
}

fun getStringParams(params: Map<String, String>): String {
    val result = StringBuilder()

    for ((key, value) in params.entries) {
        result.append(URLEncoder.encode(key, "UTF-8"))
        result.append("=")
        result.append(URLEncoder.encode(value, "UTF-8"))
        result.append("&")
    }

    return result.toString()
}