package com.example.changethevoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@SuppressWarnings("deprecation")
public class VoiceActivity extends AppCompatActivity {

    File file = new File(Environment.getExternalStorageDirectory(),"test.mp3");
    public static Boolean reccording;
    private Spinner spinner ;
    public ImageView imv1, startrec, playback ;
    ArrayAdapter <String> adapter;
    AudioTrack audioTrack;

    private final int REQUEST_CODE = 123;
    //private static final int PERMISSION_REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] arrayofString = new String[8];

        arrayofString[0] ="Ghost";
        arrayofString[1] ="Normal";
        arrayofString[2] ="Slowmotion";
        arrayofString[3] ="Robot";
        arrayofString[4] ="Chipmuk";
        arrayofString[5] ="Funny";
        arrayofString[6] ="Bee";
        arrayofString[7] ="Elephant";

        startrec = findViewById(R.id.record_vc);
        playback = findViewById(R.id.play_records);
        imv1 = findViewById(R.id.mike_image);
        spinner = findViewById(R.id.sp1);


        //requestStoragePermission();
        requestPermissions();



        startrec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(VoiceActivity.this, Recordingdailog.class);
                startActivity(in);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        reccording = true;

                        //Code is Closed for Testing
                        try {

                            StartRecord();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });


        playback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(VoiceActivity.this,"Only Click"+file,Toast.LENGTH_LONG).show();

                if(file.exists()){

                    Toast.makeText(VoiceActivity.this,"file exist"+file,Toast.LENGTH_LONG).show();

                    try {
                        playrecords();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        adapter = new ArrayAdapter <>(this,R.layout.support_simple_spinner_dropdown_item,arrayofString);
        //adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        imv1.setVisibility(View.VISIBLE);
    }


    private void playrecords() throws IOException {

        //Play..

        int i=0;
        String str = (String) spinner.getSelectedItem();

        int shortSizeInBytes = Short.SIZE / Byte.SIZE;
        int bufferdSizeInBytes = (int) (file.length() / shortSizeInBytes);
        short[] audioData = new short[bufferdSizeInBytes];

        InputStream inputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);

        int j = 0;
        while(dataInputStream.available() > 0){
            audioData[j]= dataInputStream.readShort();
            j++;
        }
        dataInputStream.close();

        if(str.equals("Ghost")){
            i= 5000;
        }
        if(str.equals("Slowmotion")){
            i= 6050;
        }
        if(str.equals("Robot")){
            i= 8500;
        }
        if(str.equals("Normal")){
            i= 11025;
        }
        if(str.equals("Chipmuk")){
            i= 16000;
        }
        if(str.equals("Funny")){
            i= 22050;
        }
        if(str.equals("Bee")){
            i= 41000;
        }
        if(str.equals("Elephant")){
            i= 30000;
        }
        audioTrack = new AudioTrack(3,i,2,2,bufferdSizeInBytes,1);
        Log.e("BufferSize" ,"Buffersize" +bufferdSizeInBytes);
        audioTrack.play();
        audioTrack.write(audioData,0,bufferdSizeInBytes);
    }


    private void StartRecord() throws IOException {


        //File myfile= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"test.pcm");
        File myfile= new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"test.mp3");
        myfile.createNewFile();
        OutputStream outputStream = new FileOutputStream(myfile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);


        int minbuffersize = AudioRecord.getMinBufferSize(11025,2,2);

        short[] audioData = new short[minbuffersize];
        AudioRecord audioRecord = new AudioRecord(1,11025,2,2,minbuffersize);
        audioRecord.startRecording();

        while(reccording){
            int numberofshort = audioRecord.read(audioData,0,minbuffersize);

            for(int i =0; i<numberofshort; i++){

                dataOutputStream.writeShort(audioData[i]);
            }

        }

        if(!reccording.booleanValue()){
            audioRecord.stop();
            dataOutputStream.close();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        reccording = false;
        if(audioTrack!= null){
            audioTrack.release();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //End of All permisssion ....

    public void requestPermissions(){

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)+
                     ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)+
        ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            //When Permission Not Granted..
            if(ActivityCompat.shouldShowRequestPermissionRationale(VoiceActivity.this,Manifest.permission.RECORD_AUDIO) ||
            ActivityCompat.shouldShowRequestPermissionRationale(VoiceActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) ||
            ActivityCompat.shouldShowRequestPermissionRationale(VoiceActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Create Alert Dialog...
                AlertDialog.Builder builder = new AlertDialog.Builder(VoiceActivity.this);
                builder.setTitle("Greand Those Permisiion");
                builder.setMessage("Microphone,External Storage");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(VoiceActivity.this, new String[]{

                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.RECORD_AUDIO,
                                }, REQUEST_CODE
                        );

                    }
                });
                builder.setNegativeButton("Cancel", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else {
                ActivityCompat.requestPermissions(VoiceActivity.this, new String[]{

                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO,
                        }, REQUEST_CODE
                );
            }

        }else{
            //When Permisiion are already Given
            Toast.makeText(VoiceActivity.this,"Permisiion Already Granted",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if(requestCode ==REQUEST_CODE){
           if ((grantResults.length>0)
               && (grantResults[0]
               +grantResults[1]
               +grantResults[2]== PackageManager.PERMISSION_GRANTED)){
                   //Permisiion Are Granted...

                 Toast.makeText(getApplicationContext(),"Permisiion are granted",Toast.LENGTH_LONG).show();
               }else {
                    //Permisiion are denied...
               Toast.makeText(getApplicationContext(),"Permisiion are denied",Toast.LENGTH_SHORT).show();

           }
           }
       }



    }
