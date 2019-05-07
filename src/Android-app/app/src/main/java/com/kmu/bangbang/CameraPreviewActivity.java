package com.kmu.bangbang;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Button;

public class CameraPreviewActivity extends Activity {
    private CameraPreview mPreview;
    private RelativeLayout mLayout;
    //private ConstraintLayout mLayout;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);
        mLayout = (RelativeLayout) findViewById(R.id.layout1);
       // mLayout = (ConstraintLayout) findViewById(R.id.layout1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreview = new CameraPreview(this, 0, CameraPreview.LayoutMode.FitToParent);
        LayoutParams previewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // Un-comment below lines to specify the size.
        previewLayoutParams.height = 1500;
        previewLayoutParams.width = 1500;

        // Un-comment below line to specify the position.
        // mPreview.setCenterPosition(300, 300);
        
        mLayout.addView(mPreview, 0, previewLayoutParams);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
        mLayout.removeView(mPreview); // This is necessary.
        mPreview = null;
    }
}
