package com.kmu.bangbang;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

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

    private static String TAG = "AcqDetailActivity";

    private static final String TAG_JSON="records";
    private static final String TAG_aIDX = "aIdx";
    private static final String TAG_NAME = "name";
    private static final String TAG_BELONG ="belong";
    private static final String TAG_ALARM = "alarm";

    ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString, aIdx, name, belong;
    TextView nameText, alarmText, belongText;
    EditText nameEdit, belongEdit, alarmEdit;
    Button updateBtn, confirmBtn;
    int alarm;

    RadioButton yes_btn, no_btn;

    AcqDetailActivity.GetData task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquaintance_detail);

        nameText = (TextView)findViewById(R.id.nameText);
        belongText = (TextView)findViewById(R.id.belongText);

        // 라디오 버튼 설정
        yes_btn = (RadioButton) findViewById(R.id.yesRadioBtn);
        no_btn = (RadioButton) findViewById(R.id.noRadioBtn);

        // aIdx 받아오기
        Intent intent = getIntent();
        aIdx = intent.getExtras().getString("aIdx");

        Log.v("Recieved acq : ", aIdx);

        task = new AcqDetailActivity.GetData();
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
            alarm = item.getInt(TAG_ALARM);

            Log.d(TAG, "aIdx : "+aIdx);
            Log.d(TAG, "name : "+name);
            Log.d(TAG, "alarm : "+alarm);

            nameText.setText(name);
            belongText.setText(belong);
            if(alarm == 1){
                yes_btn.setChecked(true);
            }
            else{
                no_btn.setChecked(true);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    public void deleteAcq(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("삭제 알림");
        builder.setMessage("정말로 삭제하시겠습니까?");
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        task = new AcqDetailActivity.GetData();
                        task.execute("http://52.78.219.61/DeleteAcq.php?aIdx="+aIdx);
                        Toast.makeText(getApplicationContext(),"삭제가 완료되었습니다.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    public void updateAcq(View view){
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        belongEdit = (EditText) findViewById(R.id.belongEdit);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);

        nameText.setVisibility(View.GONE);
        belongText.setVisibility(View.GONE);
        updateBtn.setVisibility(View.GONE);

        nameEdit.setVisibility(View.VISIBLE);
        belongEdit.setVisibility(View.VISIBLE);
        confirmBtn.setVisibility(View.VISIBLE);

        nameEdit.setText(name);
        belongEdit.setText(belong);
    }

    public void confirmAcq(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("수정 알림");
        builder.setMessage("정말로 수정하시겠습니까?");
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        task = new AcqDetailActivity.GetData();
                        task.execute("http://52.78.219.61/UpdateAcq.php?aIdx="+aIdx
                                +"&name="+nameEdit.getText().toString()
                                +"&belong="+belongEdit.getText().toString()
                                +"&alarm="+(yes_btn.isChecked() ? "1" : "0"));

                        Toast.makeText(getApplicationContext(),"수정이 완료되었습니다.",Toast.LENGTH_LONG).show();

                        task = new AcqDetailActivity.GetData();
                        task.execute("http://52.78.219.61/DetailAcq.php?aIdx="+aIdx);

                        nameEdit.setVisibility(View.GONE);
                        belongEdit.setVisibility(View.GONE);
                        confirmBtn.setVisibility(View.GONE);

                        nameText.setVisibility(View.VISIBLE);
                        belongText.setVisibility(View.VISIBLE);
                        updateBtn.setVisibility(View.VISIBLE);
                    }
                });
        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    public void backHome(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
