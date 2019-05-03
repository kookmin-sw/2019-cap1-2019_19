package com.kmu.bangbang;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
//import android.view.Menu;
//import android.view.MenuItem;

import java.io.IOException;


public class CameraActivity extends AppCompatActivity{

    SurfaceView surfaceView;
    SurfaceHolder holder;
    MediaRecorder recorder;


    String path ="/sdcard/recorded_video.mp4";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);

        holder = surfaceView.getHolder();


    }
    public void onButtonClicked(View v){

        try {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
                recorder = null;
            }


            recorder = new MediaRecorder();

            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

            recorder.setOutputFile(path);
            recorder.setPreviewDisplay(holder.getSurface());
            recorder.prepare();
            recorder.start();

            Toast.makeText(getApplicationContext(),"녹화를 시작합니다.",Toast.LENGTH_LONG).show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void onButton2Clicked(View v){
        if(recorder!=null){
            recorder.stop();
            recorder.release();
            recorder = null;
        }

        Toast.makeText(getApplicationContext(),"녹화를 중지합니다.",Toast.LENGTH_LONG).show();

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu_main,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        int id = item.getItemId();
//        if(id==R.id.action_settings){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}

