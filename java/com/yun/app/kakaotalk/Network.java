package com.yun.app.kakaotalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Network extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network);

        WebView webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();

        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.addJavascriptInterface(new JavascriptInterface(this),"hybrid");
        /*webView.loadUrl("https://www.naver.com/");*/
        webView.loadUrl("file:///android_asset/main.html");
    }
}
