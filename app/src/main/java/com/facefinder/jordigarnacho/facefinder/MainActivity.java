package com.facefinder.jordigarnacho.facefinder;

import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import android.os.Environment;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.util.Date;
import android.content.Intent;
import android.provider.MediaStore;


import java.io.*;
import java.net.*;
import java.io.*;
import java.net.*;


public class MainActivity extends AppCompatActivity {

    private String mCurrentPhotoPath;
    private Camera mCamera = null;
    private CameraView mCameraView = null;
    Camera.PictureCallback myPicture;
    private String serverIP = "192.168.1.1";
    private int serverPort = 8000;
    private SocketManagement mySocket;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button findFaceButton = (Button) findViewById(R.id.FindFaceButton);
        Button insertFaceButton = (Button) findViewById(R.id.InsertFaceButton);
        Button settingsButton = (Button) findViewById(R.id.SettingsButton);
        Button aboutButton = (Button) findViewById(R.id.AboutButton);

        findFaceButton.setOnClickListener(new OnClickListener() {
                                              public void onClick(View v) {
                                                  setContentView(R.layout.find_my_face);
                                                  try {
                                                      mCamera = Camera.open();//you can use open(int) to use different cameras
                                                  } catch (Exception e) {
                                                  }

                                                  if (mCamera != null) {
                                                      mCameraView = new CameraView(MainActivity.super.getApplicationContext(), mCamera);//create a SurfaceView to show camera data
                                                      FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
                                                      camera_view.addView(mCameraView);//add the SurfaceView to the layout
                                                      ImageButton takePictureButton = (ImageButton) findViewById(R.id.TakePictureButton);

                                                      takePictureButton.setOnClickListener(new View.OnClickListener() {
                                                                                               @Override
                                                                                               public void onClick(View v) {
                                                                                                   mCamera.takePicture(null, null, myPicture);

                                                                                                   String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                                                                                   String imageFileName = "JPEG_" + timeStamp + "_";
                                                                                                   File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                                                                                   try {
                                                                                                       File image = File.createTempFile(imageFileName, ".jpg", storageDir);
                                                                                                       mCurrentPhotoPath = "file:"+image.getAbsolutePath();
                                                                                                   } catch (IOException e) {
                                                                                                       e.printStackTrace();
                                                                                                   }
                                                                                               }

                                                                                           }
                                                      );
                                                  }
                                              }
                                          });


        insertFaceButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.insert_new_face);
                try {
                    mCamera = Camera.open();//you can use open(int) to use different cameras
                } catch (Exception e) {
                }

                if (mCamera != null) {
                    mCameraView = new CameraView(MainActivity.super.getApplicationContext(), mCamera);//create a SurfaceView to show camera data
                    FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
                    camera_view.addView(mCameraView);//add the SurfaceView to the layout
                    ImageButton takePictureButton2 = (ImageButton) findViewById(R.id.TakePictureButton2);
                    takePictureButton2.setOnClickListener(new View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 mCamera.takePicture(null, null, myPicture);

                                                                 String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                                                                 String imageFileName = "JPEG_" + timeStamp + "_";
                                                                 File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                                                 try {
                                                                     File image = File.createTempFile(imageFileName, ".jpg", storageDir);
                                                                     mCurrentPhotoPath = "file:"+image.getAbsolutePath();
                                                                 } catch (IOException e) {
                                                                     e.printStackTrace();
                                                                 }
                                                             }

                     }
                );
            }
        }
    });

        settingsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.settings);
                Button validSettingsButton  = (Button) findViewById(R.id.validSettingsButton);
                validSettingsButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        serverIP = findViewById (R.id.ipServer1).toString()+findViewById (R.id.ipServer2).toString()+findViewById (R.id.ipServer3).toString()+findViewById (R.id.ipServer4).toString();
                        serverPort = Integer.valueOf(findViewById (R.id.portServer).toString());
                        try {
                            mySocket.socketUpdate(serverIP, serverPort);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
             };
        });

        aboutButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.about);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
        try {
            mySocket = new SocketManagement(serverIP, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        try {
            mySocket.socketStop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
