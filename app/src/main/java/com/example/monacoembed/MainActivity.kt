package com.example.monacoembed

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var fab: FloatingActionButton

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find views from XML
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        fab = findViewById(R.id.fab)

        // Setup WebView
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE // Hide when done loading
            }
        }
        webView.webChromeClient = WebChromeClient()

        // Load Monaco Editor (index.html inside assets/www)
        webView.loadUrl("file:///android_asset/www/index.html")

        // FAB click → Save editor content to a file
        fab.setOnClickListener {
            webView.evaluateJavascript("editor.getValue();") { code ->
                saveCodeToFile(code)
            }
        }
    }

    private fun saveCodeToFile(code: String) {
        try {
            val cleanCode = code.trim('"') // Remove quotes from JS result
            val file = File(getExternalFilesDir(null), "saved_code.txt")
            val fos = FileOutputStream(file)
            fos.write(cleanCode.toByteArray())
            fos.close()

            Toast.makeText(this, "Code saved to: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving file: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
