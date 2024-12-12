package com.example.autochat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.autochat.data.model.Message

class AutoChatViewModel : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _typeSelected = MutableLiveData<String>()
    val typeSelected: LiveData<String> = _typeSelected

    private val _linkAi = MutableLiveData<String>()
    val linkAi: LiveData<String> = _linkAi

    init {
        _message.value = ""
        _typeSelected.value = ""
    }

    fun setMessage(message: String) {
        _message.value = message
    }

    fun setType(type: String) {
        _typeSelected.value = type
        // link example
        when (type) {
            "ChatGPT" -> _linkAi.value = "https://chatgpt.com/"
            "Gemini" -> _linkAi.value = "https://www.youtube.com/watch?v=ZEmJBl9I4IA"
            "Google" -> _linkAi.value = "https://google.com/"
            "ai" -> _linkAi.value =
                "https://auth.openai.com/authorize?client_id=TdJIcbe16WoTHtN95nyywh5E4yOo6ItG&scope=openid%20email%20profile%20offline_access%20model.request%20model.read%20organization.read%20organization.write&response_type=code&redirect_uri=https%3A%2F%2Fchatgpt.com%2Fapi%2Fauth%2Fcallback%2Flogin-web&audience=https%3A%2F%2Fapi.openai.com%2Fv1&device_id=840eb45e-d8b8-460c-bcf7-897b615310d6&prompt=login&screen_hint=login&ext-oai-did=840eb45e-d8b8-460c-bcf7-897b615310d6&country_code=VN&state=9AiQXuiABWWp5S9-_RwlXdJ_xg0spp0-4b3KA-k6XtA&code_challenge=hP6iBH4LKkYIIjEDmTPhox-NiZfuJbsrTlCWtH5Kq4k&code_challenge_method=S256"
        }
    }

    fun getTypeList(): List<String> {
        // type example
        return listOf(
            "ChatGPT", "Gemini", "Google", "ai"
        )
    }

    fun sendMessage() {
        // TODO: Implement sending message logic
        // get Message from outside App
        val message = Message("Hello", "ChatGpt")
        // set data


    }
}