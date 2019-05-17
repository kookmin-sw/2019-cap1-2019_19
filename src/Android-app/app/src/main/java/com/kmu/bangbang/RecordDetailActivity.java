package com.kmu.bangbang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class RecordDetailActivity extends AppCompatActivity {

    private static String TAG = "RecordDetailActivity";

    private static final String TAG_JSON="records";
    private static final String TAG_rIDX = "rIdx";
    private static final String TAG_NAME = "name";
    private static final String TAG_DATE = "date";
    private static final String TAG_BELONG ="belong";
    private static final String TAG_VIDEO_URL ="video_url";

    ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString;
    String rIdx;
    String name;
    String date ;
    String belong;
    String video_url;

    TextView nameText;
    TextView dateText;
    TextView belongText;
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        //mArrayList = new ArrayList<>();

        nameText = (TextView)findViewById(R.id.nameText);
        dateText = (TextView)findViewById(R.id.dateText);
        belongText = (TextView)findViewById(R.id.belongText);

        if (mWebView != null) {
            mWebView.destroy();
        }
        mWebView = (WebView) findViewById(R.id.videoView);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setBackgroundColor(255);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        WebSettings websettings = mWebView.getSettings();
        websettings.setJavaScriptEnabled(true);
        websettings.setMediaPlaybackRequiresUserGesture(true);

        // rIdx 받아오기
        Intent intent = getIntent();
        String rIdx = intent.getExtras().getString("rIdx");

        Log.v("Recieved record : ", rIdx);

        RecordDetailActivity.GetData task = new RecordDetailActivity.GetData();
        task.execute("http://52.78.219.61/DetailRecord.php?rIdx="+rIdx);




        Log.v(TAG, "finised");
    }


    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(RecordDetailActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            // mTextViewResult.setText(result);
            Log.d(TAG, "response  - " + result);
            System.out.print("result ="+result);

            // SQL문 오류 검사
            if (result == null){
                Toast.makeText(RecordDetailActivity.this, errorString, Toast.LENGTH_SHORT).show();
            }
            else {

                mJsonString = result;
                // 데이터 유무 검사
                if(!mJsonString.equals("")){
                    showResult();
                }
                else{
                    Toast.makeText(RecordDetailActivity.this, "방문기록이 없습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();


                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("record");
            JSONObject item = jsonArray.getJSONObject(0);

            name = item.getString(TAG_NAME);
            date = item.getString(TAG_DATE);
            belong = item.getString(TAG_BELONG);
            video_url = item.getString(TAG_VIDEO_URL);

            Log.d(TAG, "rIdx : "+rIdx);
            Log.d(TAG, "name : "+name);

            nameText.setText(name);
            dateText.setText(date);
            belongText.setText(belong);

            Log.d(TAG, "video_url"+video_url);

//            if(video_url == null || video_url.length() == 0){
//                mWebView.loadUrl("http://52.78.219.61/recordVideo/default.png");
//            }
//            else{
//                mWebView.loadUrl("http://52.78.219.61/"+video_url);
//            }


        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    public void playVideo(View view){
        if(video_url == null || video_url.length() == 0){
            mWebView.loadUrl("http://52.78.219.61/recordVideo/default.png");
        }
        else{
            mWebView.loadUrl("http://52.78.219.61/"+video_url);
        }
    }

}
