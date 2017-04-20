package com.example.remoteview.androidremoteview;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RemoteViews;
/*
 *用来做跨进程测试RemoteView的
 */
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    public void updateView(View view){
        RemoteViews mRemoteViews = new RemoteViews(Main2Activity.this.getPackageName(), R.layout.layout);

        mRemoteViews.setTextViewText(R.id.button2, "Back Main2Activity");
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(Main2Activity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.button2,pendingIntent);

        mRemoteViews.setTextViewText(R.id.button, "Back MainActivity");
        Intent intent2 = new Intent(Main2Activity.this, MainActivity.class);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(Main2Activity.this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.button,pendingIntent2);

        Intent broacastIntent = new Intent(LocalConst.ACROSS_PROCESSES_TO_UPDATE);
        Bundle bundle = new Bundle();
        bundle.putParcelable("RemoteView", mRemoteViews);
        broacastIntent.putExtras(bundle);
        sendBroadcast(broacastIntent);
        finish();
    }

}
