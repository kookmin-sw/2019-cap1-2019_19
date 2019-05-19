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

public class AcqDetailActivity extends AppCompatActivity {

    private static String TAG = "RecordDetailActivity";

    private static final String TAG_JSON="records";
    private static final String TAG_aIDX = "aIdx";
    private static final String TAG_NAME = "name";
    private static final String TAG_BELONG ="belong";
    private static final String TAG_ALARM = "alarm";

    ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString;
    String aIdx;
    String name;
    String alarm ;
    String belong;

    TextView nameText;
    TextView alarmText;
    TextView belongText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquaintance_detail);

        nameText = (TextView)findViewById(R.id.nameText);
        alarmText = (TextView)findViewById(R.id.alarmText);
        belongText = (TextView)findViewById(R.id.belongText);


        // rIdx 받아오기
        Intent intent = getIntent();
        aIdx = intent.getExtras().getString("aIdx");

        Log.v("Recieved acq : ", aIdx);

        AcqDetailActivity.GetData task = new AcqDetailActivity.GetData();
        task.execute("http://52.78.219.61/DetailAcq.php?aIdx="+aIdx);

        Log.v(TAG, "finised");
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(AcqDetailActivity.this,
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
                Toast.makeText(AcqDetailActivity.this, errorString, Toast.LENGTH_SHORT).show();
            }
            else {

                mJsonString = result;
                // 데이터 유무 검사
                if(!mJsonString.equals("")){
                    showResult();
                }
                else{
                    Toast.makeText(AcqDetailActivity.this, "방문기록이 없습니다!", Toast.LENGTH_SHORT).show();
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
            JSONArray jsonArray = jsonObject.getJSONArray("Acquaintance");
            JSONObject item = jsonArray.getJSONObject(0);

            name = item.getString(TAG_NAME);
            belong = item.getString(TAG_BELONG);
            alarm = item.getString(TAG_ALARM);

            Log.d(TAG, "aIdx : "+aIdx);
            Log.d(TAG, "name : "+name);
            Log.d(TAG, "alarm : "+alarm);

            nameText.setText(name);
            belongText.setText(belong);
            alarmText.setText(alarm);

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
