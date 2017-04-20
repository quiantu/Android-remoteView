package com.example.remoteview.androidremoteview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import static com.example.remoteview.androidremoteview.R.*;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private RemoteViews mRemoteViews;
    private Notification mNotification;
    private NotificationManager manager;
    private LinearLayout mainConstLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        init();
    }
    private void init(){
        mainConstLayout = (LinearLayout) findViewById(id.mainConstLayout);
        mRemoteViews = new RemoteViews(getPackageName(), layout.layout);
        mRemoteViews.setImageViewResource(id.imageView, drawable.la);
        mRemoteViews.setTextViewText(id.button, "PLS CLICK ME!");

        PendingIntent mRemoteIntent = PendingIntent.getBroadcast(MainActivity.this, 0, new Intent(LocalConst.MYAPPLICTION_ACTION), PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(id.button, mRemoteIntent);
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this)
                .setTicker("hello RemotesViews")
                .setSmallIcon(drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContent(mRemoteViews)
                .setContentIntent(mPendingIntent);
        mNotification = builder.build();
        manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationforReceiver myReceiver = new NotificationforReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocalConst.MYAPPLICTION_ACTION);
        intentFilter.addAction(LocalConst.ACROSS_PROCESSES_TO_UPDATE);
        registerReceiver(myReceiver, intentFilter);
    }
    public void add(View view){
        manager.notify(1, mNotification);
    }
    public void modify(View view){
        mRemoteViews.setTextViewText(id.button2, "点我点我");
        mRemoteViews.setTextViewText(id.button, "点我");
        mRemoteViews.setImageViewResource(id.imageView, drawable.timg);
        manager.notify(1, mNotification);
    }
    public class NotificationforReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "getAction" + intent.getAction());
            String action = intent.getAction();
            if(action.equals(LocalConst.MYAPPLICTION_ACTION)){
                mRemoteViews.setTextViewText(id.button2, "是我被点击了吗");
                mRemoteViews.setTextViewText(id.button, "不是");
                mRemoteViews.setImageViewResource(id.imageView, drawable.xx);
                manager.notify(1, mNotification);
            }else if(action.equals(LocalConst.ACROSS_PROCESSES_TO_UPDATE)){
                RemoteViews remoteViews = intent.getParcelableExtra("RemoteView");
                if(remoteViews != null){
                    View view = remoteViews.apply(MainActivity.this, mainConstLayout);
                    mainConstLayout.addView(view);
                }
            }
        }
    }
}
