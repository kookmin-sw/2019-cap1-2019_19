package com.kmu.bangbang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class StreamingFragment extends Fragment {

    private WebView mWebView = null;
    private String User_Addr;
    String users_id;
    String ip;
    TextView text;
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

        text = (TextView)fl.findViewById(R.id.error_text);

        Context context = getActivity();
        SharedPreferences auto = context.getSharedPreferences("auto", Context.MODE_PRIVATE);
        users_id= auto.getString("auto_id", null);

        Response.Listener<String> responseListener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
//                  디버깅
//                ip = response;
//                text.setText(ip);

                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    ip = jsonResponse.getString("ip");
                    User_Addr = "http://" + ip + ":8080/stream/video.mjpeg";
                    mWebView.loadUrl(User_Addr);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        IpRequest ipRequest = new IpRequest(users_id,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(ipRequest);



        return fl;

    }



}
