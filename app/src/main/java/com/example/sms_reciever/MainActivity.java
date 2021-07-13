package com.example.sms_reciever;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String NUMBER="sender_number";
    public static final String MESSAGE="message_content";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS},1000);
        }

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            TextView addressField = findViewById(R.id.smsnum);
            TextView messageField = findViewById(R.id.smsbody);

            Intent intent = getIntent();
            String num = intent.getExtras().getString(NUMBER);
            String msg = intent.getExtras().getString(MESSAGE);

            Log.d("Sender Num", num);
            Log.d("Sender Message", msg);

            addressField.setText("From : " + num);
            messageField.setText("Message:"+"\n\n" + msg);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1000) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Sms Receiver Permission Granted!!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Sms Receiver Permission Denied..", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}