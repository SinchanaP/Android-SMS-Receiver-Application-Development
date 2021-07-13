package com.example.sms_reciever;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class SmsReciever extends BroadcastReceiver {
    String num;
    String msg ;

    public SmsReciever(){}

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        try
        {
            if(bundle!=null)
            {
                Object[] pdusObj=(Object[])bundle.get("pdus");
                if(pdusObj!=null)
                {
                    String senderNum = "";
                    String message = "";
                    for(Object obj:pdusObj) {
                        SmsMessage messages = SmsMessage.createFromPdu((byte[]) obj);
                        senderNum = messages.getDisplayOriginatingAddress();
                        message = message + messages.getDisplayMessageBody();
                    }
                    Log.d("Rec : Sender Num", senderNum);
                    Log.d("Rec : Sender Message", message);

                    Intent directIntent = new Intent(context, MainActivity.class);
                    directIntent.putExtra(MainActivity.NUMBER, senderNum);
                    directIntent.putExtra(MainActivity.MESSAGE, message);
                    sendNotification(context, directIntent);

                    Toast.makeText(context, "SMS Received!!", Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(context, "onReceive:SMS is Null ", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e)
        {
            Toast.makeText(context, "Exception SMS Receiver" + e, Toast.LENGTH_SHORT).show();
        }

    }

    protected void sendNotification(Context context,Intent intent) {

        num =intent.getExtras().getString(MainActivity.NUMBER);
        msg =intent.getExtras().getString(MainActivity.MESSAGE);

        NotificationCompat.Builder builder= new NotificationCompat.Builder(context,"My Notification");
        builder.setContentTitle(num);
        builder.setContentText(msg);
        builder.setSmallIcon(R.drawable.chat);
        builder.setAutoCancel(true);

        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(0, builder.build());
    }

}