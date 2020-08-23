package com.amnesica.adsb_exchange_map

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * Class holds an simple webView and displays the map from ADS-B Exchange
 */
class MapActivity : AppCompatActivity() {
    //url to display
    private val url = "https://globe.adsbexchange.com/"

    //webView to load url
    private var myWebView: WebView? = null


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        //link webView to layout
        myWebView = findViewById(R.id.webView)

        //floatingActionButton to display help
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.legend))
                .setMessage(getString(R.string.messageLegend))
                .setPositiveButton(getString(R.string.OK), null)
                .show()
        }

        //set webView settings
        myWebView?.settings?.javaScriptEnabled = true
        myWebView?.settings?.domStorageEnabled = true
        myWebView?.settings?.builtInZoomControls = true
        myWebView?.settings?.displayZoomControls = false

        //load url
        myWebView?.loadUrl(url)
        myWebView?.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (Uri.parse(url).host == getString(R.string.rawUrl)) {
                    //let WebView load the page
                    if (url != null) {
                        view?.loadUrl(url)
                    }
                }
                //link is not for a page on my site, so launch another Activity that handles URLs
                Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                    startActivity(this)
                }
                return true
            }
        }
    }

    //override onBackPressed to show exit-dialog
    override fun onBackPressed() {
        //show dialog when user is trying to exit application
        val builder = AlertDialog.Builder(this@MapActivity)

        //set title
        builder.setTitle(getString(R.string.titleCloseDialog))

        //display message
        builder.setMessage(getString(R.string.messageCloseDialog))

        //set positive button for okay
        builder.setPositiveButton(getString(R.string.YES)) { dialog, which ->
            dialog.dismiss()
            //exit application
            finish()
        }

        //set negative button
        builder.setNegativeButton(getString(R.string.NO)) { _, _ ->
            //no action here - just close dialog
        }

        //create the dialog from the builder
        val dialog: AlertDialog = builder.create()

        //display dialog
        dialog.show()
    }
}
