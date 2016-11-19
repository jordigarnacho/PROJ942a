package com.facefinder.jordigarnacho.facefinder;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.Toast;


import java.io.*;
import java.net.*;
import java.io.*;
import java.net.*;


public class MainActivity extends AppCompatActivity {

    private String mCurrentPhotoPath;
    private Camera mCamera = null;
    private CameraView mCameraView = null;
    private String serverIP = "192.168.1.1";
    private int serverPort = 8000;
    private SocketManagement mySocket;

    private Button findFaceButton = (Button) findViewById(R.id.FindFaceButton);
    private Button insertMyFaceButton = (Button) findViewById(R.id.InsertFaceButton);
    private Button settingsButton = (Button) findViewById(R.id.SettingsButton);
    private Button aboutButton = (Button) findViewById(R.id.AboutButton);

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuHomeButton:
                mainPageActions();
                return true;
            case R.id.menuFindMyFaceButton:
                findMyFaceActions();
                return true;
            case R.id.menuInsertMyFaceButton:
                insertMyFaceActions();
                return true;
            case R.id.menuSettingsButton:
                insertMyFaceActions();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void mainPageActions(){
        setContentView(R.layout.activity_main);
        findFaceButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                insertMyFaceActions();
            }
        });

        insertMyFaceButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                insertMyFaceActions();
            }
        });

        settingsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                settingsActions();
            };
        });

        aboutButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                aboutActions();
            }
        });
    }

    public void findMyFaceActions() {

        // Vérification que l'appareil possède une caméra.
        // Si l'appareil n'a pas de caméra, on ne lance pas le layout 'insert_my_face'
        // et on informe l'utilisateur.

        // Essai d'accès à la caméra de l'appareil.
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (testPresenceCamera() != true || mCamera == null ) {
            // Message d'information.
            Toast.makeText(MainActivity.super.getApplicationContext(), "Votre appareil n'a pas de caméra", Toast.LENGTH_SHORT).show();
        }

        // Si l'appareil possède de caméra, on lance le layout 'insert_my_face'
        // afin de capturer une photo.
        else {

            // Lancement du layout 'insert_my_face'
            setContentView(R.layout.find_my_face);

            // On créer une surface d'affichage du flux de la caméra.
            mCameraView = new CameraView(MainActivity.super.getApplicationContext(), mCamera);
            final FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);
            ImageButton takePictureButton = (ImageButton) findViewById(R.id.TakePictureButton);

            // Prise d'une photo.
            takePictureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Camera.PictureCallback imageFile = null;

                    // Enregistrement de la photo au format JPEG.
                    mCamera.takePicture(null, null, imageFile);
                    mCamera.stopPreview();
                    if (imageFile != null) {
                        Toast.makeText(MainActivity.super.getApplicationContext(), "La photo a été prise", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }

    // Actions
    public void insertMyFaceActions() {
        setContentView(R.layout.insert_new_face);
        try {
            mCamera = Camera.open();//you can use open(int) to use different cameras
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Prise de la photo.
        if (mCamera != null) {
            mCameraView = new CameraView(MainActivity.super.getApplicationContext(), mCamera);
            FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
            ImageButton takePictureButton2 = (ImageButton) findViewById(R.id.TakePictureButton2);
            takePictureButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Camera.PictureCallback imageFile = null;

                    // Enregistrement de la photo au format JPEG.
                    mCamera.takePicture(null, null, imageFile);
                    mCamera.stopPreview();
                    if (imageFile != null) {
                        Toast.makeText(MainActivity.super.getApplicationContext(), "La photo a été prise", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // Actions pour les parametres
    public void settingsActions(){
        setContentView(R.layout.settings);
        Button validSettingsButton  = (Button) findViewById(R.id.validSettingsButton);
        validSettingsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                serverIP = findViewById (R.id.ipServer1).toString()+findViewById (R.id.ipServer2).toString()+findViewById (R.id.ipServer3).toString()+findViewById (R.id.ipServer4).toString();
                serverPort = Integer.valueOf(findViewById (R.id.portServer).toString());
            }
        });
    }

    public void aboutActions(){
        setContentView(R.layout.about);
    }

    // Test de présence d'une caméra sur l'appareil.
    public boolean testPresenceCamera() {
        PackageManager packageManager = getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    // Ajout du des boutons clikables dans l'Action Bar (menu).
    public boolean onCreateOptionsMenu(Menu myMenu) {
        getMenuInflater().inflate(R.menu.menu, myMenu);
        return true;
    }

    // Création d'un nom de fichier unique
    public File createImageFile() throws IOException {
        // Création d'un nom d'image unique.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
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
    }

    @Override
    public void onStop() {
        super.onStop();

        //On stop la connection au socket si elle existe.
        if (mySocket != null){
            try {
                mySocket.socketStop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}