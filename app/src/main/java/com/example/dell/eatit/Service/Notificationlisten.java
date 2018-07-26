package com.example.dell.eatit.Service;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;

import com.example.dell.eatit.Common.Common;
import com.example.dell.eatit.OrderStatus;
import com.example.dell.eatit.R;
import com.example.dell.eatit.modelRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Notificationlisten extends Service implements ChildEventListener {

    DatabaseReference myref;
    public Notificationlisten() {
    }

    @Override
    public IBinder onBind(Intent intent) {
      return  null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myref= FirebaseDatabase.getInstance().getReference("Requests");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        myref.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        modelRequest request=dataSnapshot.getValue(modelRequest.class);
        showNotification(dataSnapshot.getKey(),request);

    }

    private void showNotification(String key, modelRequest request) {
        Intent intent =new Intent(getBaseContext(), OrderStatus.class);
        intent.putExtra("userphone",request.getPhone());
        PendingIntent contentintent=PendingIntent.getActivity(getBaseContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("EatIt")
                .setContentInfo("Your Order was Updated")
                .setContentText("Order #"+key+"was update status to"+ Common.convertstatus(request.getStatus()))
                .setContentIntent(contentintent)
                .setContentInfo("Info")
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager manager=(NotificationManager)getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,builder.build());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
