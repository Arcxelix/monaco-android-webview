package com.example.monacoembed

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebViewAssetLoader
import java.io.File

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webview)

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .addPathHandler("/res/", WebViewAssetLoader.ResourcesPathHandler(this))
            .build()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(request.url)
            }
        }
        webView.webChromeClient = WebChromeClient()

        webView.settings.javaScriptEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.domStorageEnabled = true

        // Android <-> JS bridge
        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun saveText(text: String) {
                val outDir = File(filesDir, "notes").apply { mkdirs() }
                val outFile = File(outDir, "monaco_note.txt")
                outFile.writeText(text)
                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Saved to: " + outFile.absolutePath,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }, "AndroidBridge")

        // Load the local HTML via https origin so Web Workers work
        webView.loadUrl("https://appassets.androidplatform.net/assets/editor.html")
    }
}
