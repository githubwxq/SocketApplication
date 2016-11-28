package learn.wxq.socketapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import learn.wxq.socketapplication.socketservice.PacketModel.LoginOffReq;
import learn.wxq.socketapplication.socketservice.ShakeAndVibrate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView connect;
    private TextView loginoff;
    String uid="e303fe70-5104-474d-a41e-87e79ec01b17";//wxq 账号
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  startService(new Intent(this, MessageService.class)); //启动服务

        initView();
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
               connectSocket();
            }
        });
        loginoff.setOnClickListener(this);
    }

    private void connectSocket() {
        //socket 连接
        showToast("连接socket");

//        LoginOffReq cr = new LoginOffReq(uid, uid, "1");
//        ShakeAndVibrate.getInstance(getApplicationContext()).addPacket(cr);
     //   new Intent()


stopService(new Intent(this, MessageService.class));
        startService(new Intent(this, MessageService.class));






    }
    public void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        connect = (TextView) findViewById(R.id.connect);
        loginoff = (TextView) findViewById(R.id.loginoff);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginoff:
                showToast("socket退出登录");
//
//                LoginOffReq cr = new LoginOffReq(uid, uid, "0");
//                ShakeAndVibrate.getInstance(getApplicationContext()).addPacket(cr);
                ShakeAndVibrate.getInstance(this).anewConnectSocker(0);

                break;


        }
    }
}
