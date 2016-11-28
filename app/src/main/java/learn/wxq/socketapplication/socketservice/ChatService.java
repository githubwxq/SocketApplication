package learn.wxq.socketapplication.socketservice;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by 郑建 on 2016/2/23.
 */
public class ChatService extends Service {
    private SavTask task;
    private ShakeAndVibrate sav;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != task && task.getStatus() != AsyncTask.Status.RUNNING) {
            task.execute();
        }
        return START_FLAG_REDELIVERY;
    }

}
