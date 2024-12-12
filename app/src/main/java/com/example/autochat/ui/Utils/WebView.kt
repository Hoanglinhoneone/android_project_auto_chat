package com.example.autochat.ui.Utils

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.autochat.data.model.Account
import timber.log.Timber

object WebView {
    @SuppressLint("SetJavaScriptEnabled")
    fun sendChat(webUrl: String, message: String, webView: WebView) {

        // flag check
        var currentState = LoginState.NOT_LOGGED_IN

        // account test
        // tài khoản google: testchat098@gmail.com, password : testchat098@aZ
        // tai khoản chatgpt : testchat089@gmail.com, password : qwer1234@QAZ
        val account = Account("testchat089@gmail.com", "qwer1234@QAZ")

        // control
        val webViewHelper = WebViewHelper(webView)

        // setting WebView
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(MyJavaScriptInterface(), "AndroidInterface")
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.settings.domStorageEnabled = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.userAgentString =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"

        // setUp cookie
        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(webUrl)
        if (cookies != null) {
            cookieManager.setAcceptCookie(true)
            cookieManager.setCookie(webUrl, cookies)
            cookieManager.flush()
            Timber.e("Cookies: $cookies")
        } else {
            Timber.e("No cookies found")
        }

        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Timber.e("onPageFinished: $url")

                // Kịch bản 1: Login with account Google đã login
                // click Login => click continue with google => edit message => click send

                // Kịch bản 2 : Login with account ChatGpt created with email.
                // click Login  => edit email => click continue => edit ps => click continue => edit message => click send

                Handler(Looper.getMainLooper()).postDelayed({
                    when (currentState) {
                        LoginState.NOT_LOGGED_IN -> {
                            webViewHelper.onClickLogin()
                            Timber.d("onClickLogin")
                            currentState = LoginState.EMAIL_ENTERED
//                            currentState = LoginState.LOGIN_WITH_GOOGLE
                        }
//                        LoginState.LOGIN_WITH_GOOGLE -> {
//                            val textNameButton = "Tiếp tục với Google"
//                            webViewHelper.onClickLoginWithGoogle(textNameButton)
//                            Timber.d("onClickLoginWithGoogle")
//                            currentState = LoginState.LOGIN_FINISHED
//                        }

                        LoginState.EMAIL_ENTERED -> {
                            webViewHelper.onEditEmail(account.email)
                            Timber.d("onEditEmail")
                            webViewHelper.onClickContinueForEmail()
                            Timber.d("onClickContinueForEmail")
                            currentState = LoginState.PASSWORD_ENTERED
                        }

                        LoginState.PASSWORD_ENTERED -> {
                            webViewHelper.onEditPassword(account.password)
                            Timber.d("onEditPassword")
                            webViewHelper.onClickContinueForPass()
                            Timber.d("onClickContinueForPass")
                            currentState = LoginState.LOGIN_FINISHED
                        }

                        else -> {
                            Timber.d("currentState: $currentState")
                            webViewHelper.onEditMessage(message)
                            webViewHelper.onSendMessage()
                            currentState = LoginState.FINISHED
                            Timber.d("Send Message Success!")
                        }
                    }

                }, 5000)
            }
        }
        webView.loadUrl(webUrl)
    }
}
