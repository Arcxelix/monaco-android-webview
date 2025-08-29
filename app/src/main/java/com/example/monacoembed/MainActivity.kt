package com.example.yourapp  // 👈 make sure this matches your app’s package name

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var fab: FloatingActionButton

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Connect XML views
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        fab = findViewById(R.id.fab)

        // WebView settings
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
            }
        }

        // Load Monaco editor (you can change this URL later)
        webView.loadUrl("https://microsoft.github.io/monaco-editor/")

        // FAB action (for now, reload page)
        fab.setOnClickListener {
            webView.reload()
        }
    }
}

