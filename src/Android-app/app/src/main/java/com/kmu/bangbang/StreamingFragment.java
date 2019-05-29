package com.kmu.bangbang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class StreamingFragment extends Fragment {

    private WebView mWebView = null;
    private String User_Addr;
    String users_id;
    String ip;
    TextView text;




    /*************************** 여기부터 MIC & SPEAKER ***************************/
    String TAG = "sujin ";

    static final String RECORDED_FILE = "/sdcard/test_recorded_test.mp3";
    static final String RECEIVED_FILE = "/sdcard/test_received.mp3";

    MediaPlayer player;
    MediaRecorder recorder;
    Handler handler;

    PrintWriter out;

    boolean record_state = false;
    boolean send_state = false;

    boolean close_state = false;

    int count = 0;

    TextView recieveText;
    EditText editTextAddress, editTextPort;
    Button clearBtn;

    String response = "";

    // Socket socket = null;

    String i;

    DataInputStream diss;
    FileOutputStream output;
    DataInputStream dis;
    DataOutputStream dos;

    MyClientTask myClientTask;

    String i_ip;
    int m_port = 3077;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        //xml의 webview를 감싼 fragmentlayout을 불러온다.
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fragment_streaming, container, false);

        if (mWebView != null) { mWebView.destroy(); }
        //Fragment는 가끔 webView가 중복되어 에러가 나서 만일을 위해 리셋한다.
        mWebView = (WebView) view.findViewById(R.id.webView);


        //WebView webView = (WebView)findViewById(R.id.webView);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setBackgroundColor(255);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        WebSettings websettings = mWebView.getSettings();
        websettings.setJavaScriptEnabled(true);

//        text = (TextView)fl.findViewById(R.id.error_text);

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

        /*************************** 여기부터 MIC & SPEAKER ***************************/

        i_ip = auto.getString("auto_i_ip", null);
        Log.v(TAG, "내부 아이피 주소는 "+i_ip);

        /* port 설정 */
        editTextPort = (EditText) view.findViewById(R.id.editTextPort);

        //connect 버튼 클릭
        Button connectBtn = (Button) view.findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClientTask myClientTask = new MyClientTask(i_ip, Integer.parseInt(editTextPort.getText().toString()));
                myClientTask.execute();
            }
        });

        final Button recordBtn = (Button) view.findViewById(R.id.recordBtn);
        final Button pauseBtn = (Button) view.findViewById(R.id.pauseBtn);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record_state = true;
                killMediaPlayer();
//                recordBtn.setVisibility(View.GONE);
//                pauseBtn.setVisibility(View.VISIBLE);
                Log.v(TAG, "녹음 시작");

                // 녹음 시작
                if(recorder != null){
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                }// TODO Auto-generated method stub
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                recorder.setOutputFile(RECORDED_FILE);
                try{
                    Toast.makeText(getActivity(),
                            "녹음을 시작합니다.", Toast.LENGTH_LONG).show();
                    recorder.prepare();
                    recorder.start();

                }catch (Exception ex){
                    Log.e("SampleAudioRecorder", "Exception : ", ex);
                    recorder.release();
                }

            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                pauseBtn.setVisibility(View.GONE);
//                recordBtn.setVisibility(View.VISIBLE);

                // 녹음 정지
                recorder.stop();
                recorder.release();
                recorder = null;
                Log.v(TAG, "녹음을 정지합니다");

                send_state = true;
                record_state = false;

                Log.v(TAG, "녹음 끝");
                Toast.makeText(getActivity(),
                        "녹음을 전송합니다.", Toast.LENGTH_LONG).show();

                //send_state = true;
            }
        });

        return view;

    }

    public class MyClientTask extends AsyncTask<Void, Void, Void> {
        String dstAddress;
        int dstPort;
        String response = "";

        //constructor
        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                // 소켓 생성
                Socket socket = null;
                try {
                    // 소켓 연결
                    socket = new Socket(dstAddress, dstPort);

                    // conncet 메시지 전송
                    Log.v(TAG, "conncet 전송");
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    out.println("connect");
                    out.flush();
                    Log.v(TAG, "conncet 전송 완료");
                    Thread.sleep(1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // state에 따라 send/receive
                while(true){

                    Log.v(TAG, "1");

                    if(close_state == true){
                        // call 메시지 전송
                        out.println("close");
                        out.flush();
                        Log.v(TAG, "close 신호 보냈다");
                        break;
                        //Thread.sleep(1000);
                    }

                    Log.v(TAG, "2");

                    if(record_state == true && !close_state){

                        Log.v(TAG, "call 신호 보냈다");
                        out.println("call");
                        out.flush();
                        Thread.sleep(1000);

                        while(send_state == false){
                            Log.v(TAG, "녹음 중");
                        }
                        // state -> send

                        if(send_state == true){

                            Log.v(TAG, "파일 전송");


                            try{
                                File inFile = new File(RECORDED_FILE);
                                long fileSize = inFile.length();
                                Log.v(TAG, Long.toString(fileSize));
                                long totalReadBytes = 0;
                                int readBytes;

                                Log.v(TAG,"전송할 test_recorded_test.mp3의 크기 = "+Long.toString(fileSize));
                                Log.v(TAG, "파일 사이즈 보냈다");
                                // 전송할 파일 크기 전송
                                out.println(Long.toString(fileSize));
                                out.flush();

                                Thread.sleep(1000);

                                // 앱 녹음 데이터 input스트림 - 파일 데이터 읽기
                                dis = new DataInputStream(new FileInputStream(inFile));
                                // 앱 녹음 데이터 output스트림(앱 -> 인터폰) - 전송
                                dos = new DataOutputStream(socket.getOutputStream());

                                byte[] buf = new byte[65536];

                                // 녹음 전송
                                while((readBytes = dis.read(buf))>0)
                                {
                                    totalReadBytes += readBytes;
                                    Log.v(TAG, "current ReadBytes = "+Integer.toString(readBytes));
                                    dos.write(buf);
                                    dos.flush();
                                }

                                Log.v(TAG, "파일 전송 Finished");
                                Log.v(TAG, "totalReadBytes = "+Long.toString(totalReadBytes));

                                // Thread.sleep(2000);
                                send_state = false;

                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }

                        }
                    }

                    Log.v(TAG, "3");

                    if(!record_state && !send_state && !close_state){
                        Log.v(TAG, "파일 받기");
                        try {
                            //Thread.sleep(2000);

                            i = Integer.toString((count%3) +1);

                            String fileName = "/sdcard/test_received" + i + ".mp3";

                            Log.v(TAG, "fileName = "+fileName);

                            // File outFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_received.mp3") ;
                            File outFile = new File(fileName);

                            int cutSize;
                            int totalSize = 0;


                            // 인터폰 녹음 데이터 입력 스트림(인터폰 -> 앱)
                            diss = new DataInputStream(socket.getInputStream());
                            // 인터폰 녹음 데이터 fileOutput스트림 - 파일 데이터 쓰기
                            output = new FileOutputStream(outFile);

                            // 데이터 길이 버퍼
                            byte[] bs = new byte[5];
                            // 데이터 버퍼
                            byte[] bf = new byte[65536];

                            // 데이터의 길이를 받는다.
                            diss.read(bs);
                            String s_len = new String(bs);
                            Log.v(TAG, "전달 받을 파일의 길이는 " + s_len);

                            int len_data = Integer.parseInt(s_len);
                            Thread.sleep(2000);

                            while ((cutSize = diss.read(bf)) > 0) {
                                Log.v(TAG, "cutSize = " + Integer.toString(cutSize));
                                output.write(bf);
                                output.flush();
                                totalSize += cutSize;
                                Log.v(TAG, "현재 totalSize = " + Integer.toString(totalSize));

                                if(totalSize >= len_data){
                                    break;
                                }
                            }

                            Log.v(TAG, "전달 받은 파일의 total size "+i +"= "+ Integer.toString(totalSize));

                            killMediaPlayer();

                            // 전송 받은 녹음 파일 재생
                            if(record_state == false && send_state == false && close_state  == false){
                                player = new MediaPlayer();
                                player.setDataSource(fileName);
                                player.prepare();
                                player.start();
                            }
                            count++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

//        @Override
//        protected void onPostExecute(Void result) {
//            recieveText.setText(response);
//
//            super.onPostExecute(result);
//        }
    }

    public void onPause(){
        Log.v(TAG, "pause");
        if(recorder != null){
            recorder.release();
            recorder = null;
        }
        if (player != null){
            player.release();
            player = null;
        }
        super.onPause();

        close_state = true;
    }

    public void onDetach(){
        Log.v(TAG, "detach");
        if(recorder != null){
            recorder.release();
            recorder = null;
        }
        if (player != null){
            player.release();
            player = null;
        }
        super.onDetach();
    }

    private void killMediaPlayer() {
        if(player != null){
            try {
                player.release();
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
