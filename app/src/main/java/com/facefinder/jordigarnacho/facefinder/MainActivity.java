package com.facefinder.jordigarnacho.facefinder;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import java.text.SimpleDateFormat;
import android.os.Environment;
import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera = null;
    private CameraView mCameraView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button findFaceButton = (Button) findViewById(R.id.FindFaceButton);
        Button insertFaceButton = (Button) findViewById(R.id.InsertFaceButton);
        Button settingsButton = (Button) findViewById(R.id.SettingsButton);
        Button aboutButton = (Button) findViewById(R.id.AboutButton);

        findFaceButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                setContentView(R.layout.find_my_face);
                try{
                    mCamera = Camera.open();//you can use open(int) to use different cameras
                } catch (Exception e){
                }

                if(mCamera != null) {
                    mCameraView = new CameraView(MainActivity.super.getApplicationContext(), mCamera);//create a SurfaceView to show camera data
                    FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
                    camera_view.addView(mCameraView);//add the SurfaceView to the layout
                    ImageButton takePictureButton = (ImageButton) findViewById(R.id.TakePictureButton);
/*
                    takePictureButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            String imageFileName = "JPEG_" + timeStamp + "_";
                            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            //File image = File.createTempFile(imageFileName, ".jpg",storageDir);

                            // Save a file: path for use with ACTION_VIEW intents
                           // mCurrentPhotoPath = "file:" + image.getAbsolutePath();
                        }

                        }*/


                }
            }
        });

        insertFaceButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                setContentView(R.layout.insert_new_face);
            }
        });

        settingsButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                setContentView(R.layout.settings);
            }
        });

        aboutButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                setContentView(R.layout.about);
            }
        });
    }
}
