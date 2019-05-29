package com.kmu.bangbang;

import android.Manifest;
//import android.app.Activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private Camera camera;
    private MediaRecorder mediaRecorder;
    private Button btn_record;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean recording = false;
    int counter = 8;
    TextView timer;
    Handler handler;

//    디버깅
    private TextView textViewResponse;
//    private TextView id_C;

    private Button buttonUpload;
    String check, auto_id;


    private String name;
    private String selectedPath="/storage/emulated/0/";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
//        업로드 코드 추가 부분
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        timer = (TextView)findViewById(R.id.timer);
//        디버깅
        textViewResponse = (TextView) findViewById(R.id.textViewResponse);
//        id_C = (TextView)findViewById(R.id.id_c);
        Intent intent = new Intent(this.getIntent());
        name = intent.getStringExtra("text");
        check = intent.getStringExtra("value");


        name=name+".mp4";
        Toast.makeText(CameraActivity.this,name,Toast.LENGTH_LONG).show();

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        auto_id = auto.getString("auto_id", null);
        if(check.equals("User")){
            auto_id = intent.getStringExtra("id");
        }
//        id_C.setText(auto_id);


        TedPermission.with(CameraActivity.this)
                .setPermissionListener(permission)
                .setRationaleMessage("녹화 위하여 권한을 허용해주세요.")
                .setDeniedMessage("거부되었습니다. 설정 > 권한에서 허용해주세요.")
                .setPermissions(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .check();

        btn_record = (Button)findViewById(R.id.btn_record);
        buttonUpload = (Button)findViewById(R.id.buttonUpload);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == buttonUpload) {
                    uploadVideo();
                    Intent intent = new Intent(CameraActivity.this,CameraActivity.class);
                    startActivity(intent);
                    buttonUpload.setEnabled(false);

                }
                    else if(v == btn_record){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CameraActivity.this,"녹화가 시작되었습니다.",Toast.LENGTH_LONG).show();
                                try {
                                    mediaRecorder = new MediaRecorder();
                                    camera.unlock();
                                    mediaRecorder.setCamera(camera);
                                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER); //버튼 눌렀을때 동영상 녹화 시작 소리
                                    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                                    mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));//화질
                                    mediaRecorder.setOrientationHint(270);
                                    mediaRecorder.setOutputFile("/sdcard/"+name);
                                    mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
                                    mediaRecorder.prepare();
                                    mediaRecorder.start();
                                    recording =true;
                                    btn_record.setText("재촬영하기");
                                    btn_record.setEnabled(false);


                                    Response.Listener<String> responseListener = new Response.Listener<String>(){

                                        @Override
                                        public void onResponse(String response) {
//
                                        }
                                    };


                                    handler = new Handler(){
                                      public void handleMessage (Message msg){
                                          counter--;
                                          if(counter <= 8){
                                              timer.setText("남은 촬영 시간 : "+counter);
                                              handler.sendEmptyMessageDelayed(0,1000);
                                          }
                                          if(counter <= 0){
                                              timer.setText("촬영이 종료되었습니다.");
                                          }
                                      }
                                    };
                                    handler.sendEmptyMessage(0);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mediaRecorder.stop();
                                            mediaRecorder.release();
                                            camera.lock();
                                            recording = false;
                                            btn_record.setEnabled(true);
                                            Toast.makeText(CameraActivity.this,"녹화가 중지되었습니다.",Toast.LENGTH_LONG).show();
                                        }
                                    },8000);


                                }catch (Exception e){
                                    e.printStackTrace();
                                    mediaRecorder.release(); //동영상 촬영을 급하게 꺼라
                                }
                            }
                        });
                    }
                }
//            }
        };
        btn_record.setOnClickListener(listener);
        buttonUpload.setOnClickListener(listener);

    }


    PermissionListener permission = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(CameraActivity.this,"권한 허가",Toast.LENGTH_LONG).show();

            camera = Camera.open(1);
            camera.setDisplayOrientation(90);
            surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(CameraActivity.this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            camera.startPreview();


        }//퍼미션들이 허용이 되었을때

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(CameraActivity.this,"권한 거부",Toast.LENGTH_LONG).show();

        }//퍼미션들이 거부된 것이 있을 때
    };

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }//서페이스 뷰가 만들어진 시점

    private void refreshCamera(Camera camera) { //카메라 초기화 작업
//        if(surfaceHolder.getSurface() == null){
//            return;
//        }

        try{
            camera.stopPreview();
        }catch (Exception e){
            e.printStackTrace();
        }
        setCamera(camera);
    }

    private void setCamera(Camera cam) {

        camera = cam;

    }

    private void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                uploading = ProgressDialog.show(CameraActivity.this, "Uploading File", "Please wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                uploading.dismiss();
                // 디버깅
                textViewResponse.setText(Html.fromHtml("<b>Uploaded at <a href='" + s + "'>" + s + "</a></b>"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
//                uploading = ProgressDialog.show(CameraActivity.this, "Uploading File", "Please wait...", false, false);
                Toast.makeText(CameraActivity.this,"등록이 완료되었습니다.",Toast.LENGTH_LONG).show();
                Log.i("bang bang ","finished ");
            }

            @Override
            protected String doInBackground(Void... params) {
                Upload u = new Upload();
                selectedPath = selectedPath+name;
                String msg = u.uploadVideo(selectedPath,check, auto_id);
//                uploading = ProgressDialog.show(CameraActivity.this, "Uploading File", "Please wait...", false, false);
                return msg;
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera(camera);
    }//서페이스에 변화가 일어나면 감지해서 바뀔때마다 호출되는것.(초기화)

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public void backHome(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void back(View view){
        finish();
    }
}
