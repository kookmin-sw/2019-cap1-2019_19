package com.kmu.bangbang;
import java.io.IOException;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class CameraActivity extends Activity implements SurfaceHolder.Callback{

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;

    String stringPath ="/sdcard/samplevideo.3gp";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

      if(!previewing){
          camera = Camera.open();
          if(camera !=null){
              try{
                  camera.setPreviewDisplay(surfaceHolder);
                  camera.startPreview();
                  previewing=true;
              }catch (IOException e){
                  e.printStackTrace();
              }
          }
      }


    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

    }
}
