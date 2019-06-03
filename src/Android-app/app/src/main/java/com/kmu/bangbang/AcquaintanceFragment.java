package com.kmu.bangbang;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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


public class AcquaintanceFragment extends Fragment {

    private static String TAG = "AcquaintanceFragment";

    private static final String TAG_JSON="Acquaintances";
    private static final String TAG_aIDX = "aIdx";
    private static final String TAG_NAME = "name";
    private static final String TAG_BELONG = "belong";

    String mJsonString, aIdx, auto_id;

    ArrayList<HashMap<String, String>> mArrayList;
    ListView mlistView;
    View view;
    ListAdapter adapter;

    GetData task;
    SharedPreferences auto;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_acquaintance, null) ;

        auto = getActivity().getSharedPreferences("auto", Context.MODE_PRIVATE);
        auto_id = auto.getString("auto_id", null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mlistView = (ListView) view.findViewById(R.id.acquaintance_list);
        mArrayList = new ArrayList<>();

        task = new GetData();
        task.execute("http://52.78.219.61/AcqRecord.php?id="+auto_id);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(getActivity(), AcqDetailActivity.class);
                // 선택된 Record idx 전달(int로 형변환 후 전달)
                HashMap selected_acq = mArrayList.get(position);
                aIdx = selected_acq.get("aIdx").toString();
                intent.putExtra("aIdx", aIdx);
                Log.v("Selected aIdx : ", aIdx);
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

            progressDialog = ProgressDialog.show(getActivity(),
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
                Toast.makeText(getActivity(), errorString, Toast.LENGTH_SHORT).show();
            }
            else {

                mJsonString = result;
                // 데이터 유무 검사
                if(!mJsonString.equals("")){
                    showResult();
                }
                else{
                    mlistView.setAdapter(null);
                    Toast.makeText(getActivity(), "등록된 지인이 없습니다!", Toast.LENGTH_SHORT).show();
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
            JSONArray jsonArray = jsonObject.getJSONArray("Acquaintances");

            adapter = null;
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                aIdx = item.getString(TAG_aIDX);
                String name = item.getString(TAG_NAME);
                String belong = item.getString(TAG_BELONG);

                HashMap<String,String> hashMap = new HashMap<>();

                Log.d(TAG, "aIdx : "+aIdx);

                hashMap.put(TAG_aIDX, aIdx);
                hashMap.put(TAG_NAME, name);
                hashMap.put(TAG_BELONG, belong);

                mArrayList.add(hashMap);
            }

            adapter = new SimpleAdapter(
                    getActivity(), mArrayList, R.layout.item_acq,
                    new String[]{TAG_NAME, TAG_BELONG},
                    new int[]{R.id.textView_list_name, R.id.textView_list_belong}
            );
            mlistView.setAdapter(adapter);

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
