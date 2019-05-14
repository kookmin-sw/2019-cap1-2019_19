package com.kmu.bangbang;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class RecordActivity extends AppCompatActivity {

    private static String TAG = "RecordActivity";

    private static final String TAG_JSON="records";
    private static final String TAG_rIDX = "rIdx";
    private static final String TAG_NAME = "name";
    private static final String TAG_DATE = "date";
    private static final String TAG_BELONG ="belong";

    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;
    String mJsonString;
    String rIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // 이전 액티비티로부터 선택된 카테고리 받아오기
        Intent intent = getIntent();
        String category = intent.getExtras().getString("category");

        Log.v("recieved category", category);

        //mTextViewResult = (TextView)findViewById(R.id.textView_main_result);
        mlistView = (ListView) findViewById(R.id.listView_main_list);
        mArrayList = new ArrayList<>();

        GetData task = new GetData();

        task.execute("http://52.78.219.61/VisitRecord.php?category="+category);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RecordDetailActivity.class);

                // 선택된 Record idx 전달(int로 형변환 후 전달)
                HashMap selected_record = mArrayList.get(position);
                rIdx = selected_record.get("rIdx").toString();
                intent.putExtra("rIdx", rIdx);

                Log.v("Selected rIdx : ", rIdx);
                startActivity(intent);
            }
        });

        Log.v(TAG, "finised");
    }


    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(RecordActivity.this,
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
                Toast.makeText(RecordActivity.this, errorString, Toast.LENGTH_SHORT).show();
            }
            else {

                mJsonString = result;
                // 데이터 유무 검사
                if(!mJsonString.equals("")){
                    showResult();
                }
                else{
                    Toast.makeText(RecordActivity.this, "방문기록이 없습니다!", Toast.LENGTH_SHORT).show();
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
            JSONArray jsonArray = jsonObject.getJSONArray("records");

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                rIdx = item.getString(TAG_rIDX);
                String name = item.getString(TAG_NAME);
                String date = item.getString(TAG_DATE);
                String belong = item.getString(TAG_BELONG);

                HashMap<String,String> hashMap = new HashMap<>();

                Log.d(TAG, "rIdx : "+rIdx);

                hashMap.put(TAG_rIDX, rIdx);
                hashMap.put(TAG_NAME, name);
                hashMap.put(TAG_DATE, date);
                hashMap.put(TAG_BELONG, belong);



                mArrayList.add(hashMap);
            }

            ListAdapter adapter = new SimpleAdapter(
                    RecordActivity.this, mArrayList, R.layout.item_record,
                    new String[]{TAG_NAME,TAG_DATE, TAG_BELONG},
                    new int[]{R.id.textView_list_name, R.id.textView_list_date, R.id.textView_list_belong}
            );
            mlistView.setAdapter(adapter);

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }



    }
}
