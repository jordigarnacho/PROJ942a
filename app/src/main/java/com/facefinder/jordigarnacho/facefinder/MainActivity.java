package com.facefinder.jordigarnacho.facefinder;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import android.widget.TextView;
import android.widget.Toast;


import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;


public class MainActivity extends AppCompatActivity {

    private String mCurrentPhotoPath;
    private Camera mCamera = null;
    private CameraView mCameraView = null;
    public File imageFile = null;
    public String serverIP1 = "192";
    public String serverIP2 = "168";
    public String serverIP3 = "118";
    public String serverIP4 = "102";
    private int serverPort = 8000;
    private Socket nsocket; //Network Socket
    private InputStream nis; //Network Input Stream
    private OutputStream nos; //Network Output Stream
    //private SocketManagement mySocket;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainPageActions();

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
                settingsActions();
                return true;
            case R.id.menuAboutButton:
                aboutActions();
        }

        return super.onOptionsItemSelected(item);
    }

    public void mainPageActions(){
        setContentView(R.layout.activity_main);

        Button findFaceButton = (Button) findViewById(R.id.FindFaceButton);
        Button insertMyFaceButton = (Button) findViewById(R.id.InsertFaceButton);
        Button settingsButton = (Button) findViewById(R.id.SettingsButton);
        Button aboutButton = (Button) findViewById(R.id.AboutButton);

        findFaceButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                findMyFaceActions();
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
        if (testPresenceCamera() != true)
        {
            Toast.makeText(MainActivity.super.getApplicationContext(), "Votre appareil n'a pas de caméra", Toast.LENGTH_SHORT).show();
            mainPageActions();
        }
        else {
            try {
                mCamera = Camera.open();//you can use open(int) to use different cameras
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (testPresenceCamera() != true || mCamera == null) {
                // Message d'information.
                Toast.makeText(MainActivity.super.getApplicationContext(), "Votre appareil n'a pas de caméra", Toast.LENGTH_SHORT).show();
                mainPageActions();
            }

            // Si l'appareil possède de caméra, on lance le layout 'insert_my_face'
            // afin de capturer une photo.
            else {


                imageFile = null;

                // Lancement du layout 'insert_my_face'
                setContentView(R.layout.find_my_face);

                final ImageButton TakePictureButton = (ImageButton) findViewById(R.id.TakePictureButton);
                final ImageButton OKButton = (ImageButton) findViewById(R.id.TakeOkPictureButton);
                final ImageButton AgainButton = (ImageButton) findViewById(R.id.TakeAgainPictureButton);
                TakePictureButton.setVisibility(View.VISIBLE);
                OKButton.setVisibility(View.INVISIBLE);
                AgainButton.setVisibility(View.INVISIBLE);
                TakePictureButton.setEnabled(true);
                OKButton.setEnabled(false);
                AgainButton.setEnabled(false);

                // On créer une surface d'affichage du flux de la caméra.


                mCameraView = new CameraView(MainActivity.super.getApplicationContext(), mCamera);
                final FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
                camera_view.addView(mCameraView);


                // Prise d'une photo.
                TakePictureButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Enregistrement de la photo au format JPEG.

                        mCamera.takePicture(null, null, mPicture);

                        if (imageFile != null) {
                            Toast.makeText(MainActivity.super.getApplicationContext(), "La photo a été prise", Toast.LENGTH_SHORT).show();
                        }

                        TakePictureButton.setVisibility(View.INVISIBLE);
                        OKButton.setVisibility(View.VISIBLE);
                        AgainButton.setVisibility(View.VISIBLE);
                        TakePictureButton.setEnabled(false);
                        OKButton.setEnabled(true);
                        AgainButton.setEnabled(true);
                    }
                });

                // Prise d'une photo.
                AgainButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        mCamera.startPreview();

                        TakePictureButton.setVisibility(View.VISIBLE);
                        OKButton.setVisibility(View.INVISIBLE);
                        AgainButton.setVisibility(View.INVISIBLE);
                        TakePictureButton.setEnabled(true);
                        OKButton.setEnabled(false);
                        AgainButton.setEnabled(false);
                    }
                });

                OKButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        setContentView(R.layout.result_find_face);
                        String myName = "My User";
                        try {
                            myName = receiveTextServer();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        TextView userFindField = (TextView) findViewById(R.id.userFindField);
                        userFindField.setText(myName);
                    }
                });
            }
        }
    }
    // Actions
    public void insertMyFaceActions() {
        setContentView(R.layout.insert_new_face);
        if (testPresenceCamera() != true)
        {
            Toast.makeText(MainActivity.super.getApplicationContext(), "Votre appareil n'a pas de caméra", Toast.LENGTH_SHORT).show();
            mainPageActions();
        }
        else {
            try {
                mCamera = Camera.open();//you can use open(int) to use different cameras
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (testPresenceCamera() != true || mCamera == null) {
                // Message d'information.
                Toast.makeText(MainActivity.super.getApplicationContext(), "Votre appareil n'a pas de caméra", Toast.LENGTH_SHORT).show();
                mainPageActions();
            }
            else {

                imageFile = null;
                setContentView(R.layout.insert_new_face);
                // Prise de la photo.

                if (mCamera != null) {
                    mCameraView = new CameraView(MainActivity.super.getApplicationContext(), mCamera);
                    FrameLayout camera_view = (FrameLayout) findViewById(R.id.camera_view);
                    camera_view.addView(mCameraView);//add the SurfaceView to the layout


                    Button SendNewUserButton = (Button) findViewById(R.id.SendNewUserButton);
                    ImageButton TakePictureButton2 = (ImageButton) findViewById(R.id.TakePictureButton2);
                    ImageButton TakeAgainPictureButton2 = (ImageButton) findViewById(R.id.TakeAgainPictureButton2);

                    TakePictureButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // Enregistrement de la photo au format JPEG.
                            mCamera.takePicture(null, null, mPicture);
                            if (imageFile != null) {
                                Toast.makeText(MainActivity.super.getApplicationContext(), "La photo a été prise", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    TakeAgainPictureButton2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCamera.startPreview();
                        }
                    });

                    SendNewUserButton.setOnClickListener(new View.OnClickListener() {
                       @Override
                        public void onClick(View v) {

                            if (imageFile == null) {
                                Toast.makeText(MainActivity.super.getApplicationContext(), "Veuillez prendre un photo", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String newUserFirstName = ((EditText) findViewById (R.id.newFaceFirstname)).getText().toString();
                                String newUserLastName = ((EditText) findViewById (R.id.newFaceLastname)).getText().toString();
                                File newUserPhoto = imageFile;

                                Toast.makeText(MainActivity.super.getApplicationContext(), "Envoi en cours...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        }
    }

    // Actions pour les parametres
    public void settingsActions(){
        setContentView(R.layout.settings);
        EditText ipServer1Field  = (EditText) findViewById(R.id.ipServer1);
        ipServer1Field.setText(serverIP1);
        EditText ipServer2Field  = (EditText) findViewById(R.id.ipServer2);
        ipServer2Field.setText(serverIP2);
        EditText ipServer3Field  = (EditText) findViewById(R.id.ipServer3);
        ipServer3Field.setText(serverIP3);
        EditText ipServer4Field  = (EditText) findViewById(R.id.ipServer4);
        ipServer4Field.setText(serverIP4.toString());
        EditText portServerField  = (EditText) findViewById(R.id.portServer);
        portServerField.setText(String.valueOf(serverPort));


        Button validSettingsButton  = (Button) findViewById(R.id.validSettingsButton);
        validSettingsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                serverIP1 = ((EditText) findViewById (R.id.ipServer1)).getText().toString();
                serverIP2 = ((EditText) findViewById (R.id.ipServer2)).getText().toString();
                serverIP3 = ((EditText) findViewById (R.id.ipServer3)).getText().toString();
                serverIP4 = ((EditText) findViewById (R.id.ipServer4)).getText().toString();
                EditText portServerField  = (EditText) findViewById(R.id.portServer);
                serverPort = Integer.parseInt(portServerField.getText().toString());
                Toast.makeText(MainActivity.super.getApplicationContext(), "Paramètres sauvegardés", Toast.LENGTH_SHORT).show();
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

    private String receiveTextServer() throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String myHost = serverIP1.toString()+"."+serverIP2.toString()+"."+serverIP3.toString()+"."+serverIP4.toString();
        String inputLine = "";
        Socket mySocket = new Socket(myHost, serverPort);

        if (mySocket.isConnected() == true)
        {
            Toast.makeText(MainActivity.super.getApplicationContext(), "Connexion au serveur réussi", Toast.LENGTH_SHORT).show();

            OutputStream outstream = mySocket.getOutputStream();
            PrintWriter out = new PrintWriter(outstream, true);
            out.println("2");
            BufferedReader bis = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            inputLine = bis.readLine();
        }
        else
        {
            Toast.makeText(MainActivity.super.getApplicationContext(), "La connexion au serveur a échouée", Toast.LENGTH_SHORT).show();
        }
        mySocket.close();
        return inputLine;
    }

    private void sendImageToServer() throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String myHost = serverIP1.toString()+"."+serverIP2.toString()+"."+serverIP3.toString()+"."+serverIP4.toString();

        Socket mySocket = new Socket(myHost, serverPort);

        if (mySocket.isConnected() == true)
        {
            Toast.makeText(MainActivity.super.getApplicationContext(), "Connexion au serveur réussi", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(MainActivity.super.getApplicationContext(), "La connexion au serveur a échouée", Toast.LENGTH_SHORT).show();
        }
        mySocket.close();
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            imageFile = pictureFile;
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {
            }
        }
    };

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "FaceFinderApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        return mediaFile;
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}