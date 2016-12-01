package learn.wxq.socketapplication.socketservice;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * Created by 郑建 on 2016/2/23.
 */
public class SavTask extends AsyncTask<Void, Void, Void> {
    private final ShakeAndVibrate sav;
    private Context context;
    private Handler handler;
    private String time;
    private String logintype;
    private String device;
    public SavTask( ShakeAndVibrate sav,Context context,Handler handler,String time,String logintype,String device) {
        super();
        this.sav = sav;
        this.context=context;
        this.handler=handler;
        this.time=time;
        this.logintype=logintype;
        this.device=device;
    }
    @Override
    protected Void doInBackground(Void... params) {
//        sav.loop();
        int a=sav.connectMain(); // 连接分流服务器


        if(a==1){
          int b=sav.loginAction(time,logintype,device);

      //      sav.nettyLoginAction();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

    }
}
