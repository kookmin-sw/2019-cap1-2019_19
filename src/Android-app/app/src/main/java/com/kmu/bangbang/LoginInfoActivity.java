package com.kmu.bangbang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class LoginInfoActivity extends AppCompatActivity {

    private static String TAG = "LoginInfoActivity";

    private static final String TAG_JSON="info";
    private static final String TAG_IP = "ip";
    private static final String TAG_I_IP = "i_ip";
    private static final String TAG_M_IP = "m_ip";
    private static final String TAG_CUR_PW ="cur_pw";

    Button openBtn, closeBtn, changeBtn;
    EditText pw_now, pw_change;

    TextView idText, ipText, i_ipText, m_ipText;

    SharedPreferences auto;
    String auto_id, mJsonString, ip, i_ip, m_ip,cur_pw;

    GetData task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_info);

        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        auto_id = auto.getString("auto_id", null);

        openBtn = (Button) findViewById(R.id.openBtn);
        closeBtn = (Button)findViewById(R.id.closeBtn);
        changeBtn = (Button) findViewById(R.id.changeBtn);

        pw_now = (EditText) findViewById(R.id.pw_now);
        pw_change = (EditText) findViewById(R.id.pw_change);

        idText = (TextView) findViewById(R.id.idText);
        ipText = (TextView) findViewById(R.id.ipText);
        i_ipText = (TextView) findViewById(R.id.i_ipText);
        m_ipText = (TextView) findViewById(R.id.m_ipText);

        idText.setText(auto_id);

        task = new LoginInfoActivity.GetData();
        task.execute("http://52.78.219.61/UserInfo.php?id="+auto_id);




    }


    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LoginInfoActivity.this,
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
                Toast.makeText(LoginInfoActivity.this, errorString, Toast.LENGTH_SHORT).show();
            }
            else {

                mJsonString = result;
                // 데이터 유무 검사
                if(!mJsonString.equals("")){
                    showResult();
                }
//                else{
//                    Toast.makeText(LoginInfoActivity.this, "방문기록이 없습니다!", Toast.LENGTH_SHORT).show();
//                }
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
            JSONArray jsonArray = jsonObject.getJSONArray("info");
            JSONObject item = jsonArray.getJSONObject(0);


            ip = item.getString(TAG_IP);
            i_ip = item.getString(TAG_I_IP);
            m_ip = item.getString(TAG_M_IP);
            cur_pw = item.getString(TAG_CUR_PW);


            ipText.setText(ip);
            i_ipText.setText(i_ip);
            m_ipText.setText(m_ip);


        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

}
