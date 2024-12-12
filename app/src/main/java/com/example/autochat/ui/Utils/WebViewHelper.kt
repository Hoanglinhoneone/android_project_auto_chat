package com.example.autochat.ui.Utils

import android.webkit.ValueCallback
import android.webkit.WebView
import timber.log.Timber

class WebViewHelper(private val webView: WebView) {

    companion object {
        private const val CLICK_LOGIN = "CLICK_LOGIN"
        private const val CLICK_CONTINUE_FOR_EMAIL = "CLICK_CONTINUE_FOR_EMAIL"
        private const val CLICK_CONTINUE_FOR_PASS = "CLICK_CONTINUE_FOR_PASS"
        private const val CLICK_GOOGLE = "CLICK_GOOGLE"
        private const val EDIT_MESSAGE = "EDIT_MESSAGE"
        private const val EDIT_EMAIL = "EDIT_EMAIL"
        private const val EDIT_PASS = "EDIT_PASS"
        private const val SEND_MESSAGE = "SEND_MESSAGE"
    }
//    fun CheckLogin() : Boolean {
//        val script = """
//            var loginBtn = document.querySelector('button[data-testid="login-button"]');
//            if(loginBtn) {
//                window.AndroidInterface.sendMessage("loginBtn found!");
//            } else {
//                window.AndroidInterface.sendMessage("loginBtn not found!");
//            }
//        """.trimIndent()
//    }

    fun onClickLogin() {
        // nút login có 2 trường hợp là welcome login button và login button
        val script = """
            var loginButton = document.querySelector('button[data-testid="welcome-login-button"]');
            var loginBtn = document.querySelector('button[data-testid="login-button"]');
            
            if (loginBtn) {
                window.AndroidInterface.sendMessage("loginBtn found!");
                loginBtn.click();
            } else if (loginButton) {
                window.AndroidInterface.sendMessage("loginBtn not found!");
                window.AndroidInterface.sendMessage("loginButton  found!");
                loginButton.click();
            }
            else {
                window.AndroidInterface.sendMessage("Login Button not found!");
            }
        """.trimIndent()

        evaluateJs(script, CLICK_LOGIN)
    }

    fun onEditEmail(email: String) {
        val script = """    
            var emailInput = document.getElementById('email-input');
            if(emailInput) {
                emailInput.value = '$email';
                window.AndroidInterface.sendMessage("EditEmail found and edited!");
            } else {
                window.AndroidInterface.sendMessage("EditEmail not found!");
            }
        """.trimIndent()
        evaluateJs(script, EDIT_EMAIL)
    }

    fun onEditPassword(password: String) {
        val script = """
            let passwordField = document.getElementById('password');
            if (passwordField) {
                passwordField.value = '$password';
                window.AndroidInterface.sendMessage("EditPass found and edited!");
            } else {
                window.AndroidInterface.sendMessage("EditPass not found!");
            }
        """.trimIndent()
        evaluateJs(script, EDIT_PASS)
    }

    fun onClickContinueForEmail() {
        val script = """
            setTimeout(function() {
                let button = document.querySelector('.continue-btn');
                if(button) {
                    button.removeAttribute('disabled');
                    button.click();
                    window.AndroidInterface.sendMessage("ContinueForEmail found!");
                } else {
                    window.AndroidInterface.sendMessage("ContinueForEmail not found!");
                }
            }, 2000);

        """.trimIndent()
        evaluateJs(script, CLICK_CONTINUE_FOR_EMAIL)
    }

    fun onClickContinueForPass() {
        val script = """
            setTimeout(function() {
                let button = document.querySelector('button._button-login-password[data-action-button-primary="true"]');
                if (button) {
                    button.click();
                    window.AndroidInterface.sendMessage("ContinueForPass found");
                } else {
                    window.AndroidInterface.sendMessage("ContinueForPass not found.");
                }
            }, 1000);
        """.trimIndent()
        evaluateJs(script, CLICK_CONTINUE_FOR_PASS)
    }

    fun onClickLoginWithGoogle(textNameButton: String) {
        val script = """
            setTimeout(function() {
                var buttons = document.querySelectorAll('.social-text');
                buttons.forEach(function(button) {
                    if (button.textContent.includes('$textNameButton')) {
                        var parentButton = button.closest('.social-btn');
                        var clickEvent = new MouseEvent('click', {
                            bubbles: true,
                            cancelable: true,
                            view: window,
                        });
                        parentButton.dispatchEvent(clickEvent);
                    }
                });
            }, 2000);                                                              
        """.trimIndent()
        evaluateJs(script, CLICK_GOOGLE)
    }

    fun onEditMessage(message: String) {
        val script = """
            
            var editable = document.querySelector('div[contenteditable="true"]');
            if (editable) {
                editable.innerHTML = '$message';
                window.AndroidInterface.sendMessage("EditMessage found");
            } else {
                window.AndroidInterface.sendMessage("EditMessage not success");
            }
        """.trimIndent()
        evaluateJs(script, EDIT_MESSAGE)
    }

    fun onSendMessage() {
        val script = """
        setTimeout(function() {
            var button = document.querySelector('[data-testid="send-button"]');
            if (button && !button.disabled) {
                button.click();   
                window.AndroidInterface.sendMessage("SendMessage found");
            }
            else {
                window.AndroidInterface.sendMessage("SendMessage not found");
            }
        }, 1000);  
        """.trimIndent()
        evaluateJs(script, SEND_MESSAGE)
    }

    private fun evaluateJs(script: String, type: String) {
        webView.evaluateJavascript(script, object : ValueCallback<String> {
            override fun onReceiveValue(result: String?) {
                Timber.d("Result of $type: $result")
            }
        })
    }
}