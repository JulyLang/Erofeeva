package com.apps.developerslife.okhttp

object OkHttpConnectionFactory {
    @JvmStatic val client = UnsafeOkHttpClient.unsafeOkHttpClient
}