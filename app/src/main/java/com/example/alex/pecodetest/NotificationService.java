package com.example.alex.pecodetest;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationService extends IntentService {


    public NotificationService(String name) {
        super(name);
    }

    public NotificationService(){
        super("Service");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent == null ) return;

        String action = intent.getStringExtra(MainActivity.SERVICE_ACTION);
        int pageNum = intent.getIntExtra(MainActivity.PAGE_NUMBER, 0);
        switch (action){
            case MainActivity.ACTION_ADD_NOTIFICATION:
                createNotification(pageNum);
                break;
            case MainActivity.ACTION_REMOVE_NOTIFICATION:
                removeNotification(pageNum);
                break;
        }


    }

    private void createNotification(int pageNum) {
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.STARTING_PAGE, pageNum);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, pageNum, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "PecodeTest")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("PecodeTest")
                .setContentText("Notification " + pageNum)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        manager.notify(pageNum, mBuilder.build());
    }

    private void removeNotification(int pageNum) {
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(pageNum);
    }

}
