package com.example.autochat.ui.Utils

import android.webkit.JavascriptInterface
import timber.log.Timber

class MyJavaScriptInterface {
    @JavascriptInterface
    fun sendMessage(message: String) {
        Timber.d("Received from JavaScript: $message")
    }
}