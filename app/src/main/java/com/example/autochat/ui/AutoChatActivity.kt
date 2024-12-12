package com.example.autochat.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.autochat.R
import com.example.autochat.databinding.ActivityAutoChatBinding
import com.example.autochat.ui.Utils.WebView
import com.example.autochat.viewmodel.AutoChatViewModel
import timber.log.Timber


class AutoChatActivity : AppCompatActivity() {
    private lateinit var typeList: List<String>
    private lateinit var viewModel: AutoChatViewModel
    private lateinit var binding: ActivityAutoChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        enableEdgeToEdge()

        binding = ActivityAutoChatBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        Timber.d("binding.root is null: ${false}")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[AutoChatViewModel::class.java]

        Timber.e("onCreate")
        initView()
        onClick()
//        setUpCookie()
        observeData()
    }

//    private fun setUpCookie() {
//        val cookieStore = BasicCookieStore()
//        val localContext: HttpContext = BasicHttpContext()
//        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore)
//
//        val httpclient: CloseableHttpClient = HttpClients.createDefault()
//
//        val httpget = HttpGet("https://www.youtube.com/watch?v=ZEmJBl9I4IA")
//        println("Executing request: ${httpget.uri}")
//
//        val response: HttpResponse = httpclient.execute(httpget, localContext)
//
//        val cookies: List<Cookie> = cookieStore.cookies
//        if (cookies.isNotEmpty()) {
//            for (cookie in cookies) {
//                println("Cookie: ${cookie.name} = ${cookie.value}")
//            }
//        } else {
//            println("No cookies found.")
//        }
//
//        httpclient.close()
//
//    }

    private fun onClick() {
        binding.btnSend.setOnClickListener {
            Timber.d("onCLick send")
            viewModel.setMessage(binding.editTextText.text.toString())
            Timber.d("selected message: ${binding.editTextText.text}")
//            viewModel.sendMessage()
            Toast.makeText(
                this, "Send Message: ${binding.editTextText.text} \n" +
                        " Type: ${binding.spTypeAi.selectedItem}", Toast.LENGTH_LONG
            ).show()
            binding.wbChatAi.visibility = View.VISIBLE

            openViewChat()
        }
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
    }

    private fun openViewChat() {
        Timber.e("openViewChat")
        WebView.sendChat(
            viewModel.linkAi.value.toString(),
            viewModel.message.value.toString(), binding.wbChatAi
        )
    }

    private fun initView() {
        Timber.e("initView")

        binding.tvLinkAi.movementMethod = ScrollingMovementMethod()

        typeList = viewModel.getTypeList()
        binding.spTypeAi.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, typeList).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        binding.spTypeAi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedType = typeList[position]
                viewModel.setType(selectedType)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.setType(typeList[0])
            }
        }
    }

    private fun observeData() {
        viewModel.linkAi.observe(this) { data ->
            binding.tvLinkAi.text = data
        }
    }

}