package com.kmu.bangbang;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class StreamingFragment extends Fragment {

    private WebView mWebView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        //xml의 webview를 감싼 fragmentlayout을 불러온다.
        FrameLayout fl = (FrameLayout) inflater.inflate(R.layout.fragment_streaming, container, false);

        if (mWebView != null) { mWebView.destroy(); }
        //Fragment는 가끔 webView가 중복되어 에러가 나서 만일을 위해 리셋한다.
        mWebView = (WebView) fl.findViewById(R.id.webView);


        //WebView webView = (WebView)findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setBackgroundColor(255);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        WebSettings websettings = mWebView.getSettings();
        websettings.setJavaScriptEnabled(true);

        mWebView.loadUrl("http://211.179.123.91:8080/stream/video.mjpeg");

        return fl;

    }



}
