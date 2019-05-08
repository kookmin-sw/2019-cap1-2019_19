package com.kmu.bangbang;

import android.Manifest;
//import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private Camera camera;
    private MediaRecorder mediaRecorder;
    private Button btn_record;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean recording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        TedPermission.with(CameraActivity.this)
                .setPermissionListener(permission)
                .setRationaleMessage("녹화 위하여 권한을 허용해주세요.")
                .setDeniedMessage("거부되었습니다. 설정 > 권한에서 허용해주세요.")
                .setPermissions(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .check();


        btn_record = (Button)findViewById(R.id.btn_record);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recording){ //녹화중지
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    camera.lock();
                    recording = false;
                }else{
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
                                mediaRecorder.setOrientationHint(90);
                                mediaRecorder.setOutputFile("/sdcard/test.mp4");
                                mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                                recording =true;
                            }catch (Exception e){
                                e.printStackTrace();
                                mediaRecorder.release(); //동영상 촬영을 급하게 꺼라
                            }
                        }
                    });
                }
            }
        });

    }

    PermissionListener permission = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(CameraActivity.this,"권한 허가",Toast.LENGTH_LONG).show();

            camera = Camera.open();
            camera.setDisplayOrientation(90);
            surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(CameraActivity.this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

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
        if(surfaceHolder.getSurface() == null){
            return;
        }

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


    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        refreshCamera(camera);
    }//서페이스에 변화가 일어나면 감지해서 바뀔때마다 호출되는것.(초기화)

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}