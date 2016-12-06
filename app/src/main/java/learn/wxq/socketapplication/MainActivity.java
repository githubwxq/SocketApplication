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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import learn.wxq.socketapplication.netty.NettyClientBootstrap;
import learn.wxq.socketapplication.socketservice.DataUtil;
import learn.wxq.socketapplication.socketservice.PacketModel.AddFriendsReq;
import learn.wxq.socketapplication.socketservice.PacketModel.ChatMessageReq;
import learn.wxq.socketapplication.socketservice.PacketModel.ConnectReq;
import learn.wxq.socketapplication.socketservice.PacketModel.GroupChatReq;
import learn.wxq.socketapplication.socketservice.PacketModel.LoginOffReq;
import learn.wxq.socketapplication.socketservice.PacketModel.Packet;
import learn.wxq.socketapplication.socketservice.ShakeAndVibrate;
import learn.wxq.socketapplication.socketservice.SocketGlobal;
import learn.wxq.socketapplication.socketservice.WriteData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView connect;
    private TextView loginoff;
    private TextView  addfriend;
    private TextView  sing_chat;
    private TextView   group_chat;
    NettyClientBootstrap nettyStart;
   String classid="c5a08452-af06-4526-a3a7-e92fb4fd1494";
    String uid="e303fe70-5104-474d-a41e-87e79ec01b17";//wxq 账号
    String uid2="5b0f35c5-311a-46f1-8b94-1306b4c8bc3e";//ty 账号
    String photonumber1="13222200760";

    String photonumber2="18862005675";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



      //  startService(new Intent(this, MessageService.class)); //启动服务
   //     nettyStart=new NettyClientBootstrap(this,"58.211.191.72",10000);
        initView();
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                connectSocket();
            }
        });
        loginoff.setOnClickListener(this);
        addfriend.setOnClickListener(this);
        sing_chat.setOnClickListener(this);
        group_chat.setOnClickListener(this);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    nettyStart.startNetty();
//
//                    nettyStart.sendMessage("wxq");
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();


        // 数据包


        //    String data=getPacketData();



    }

    private  byte[]  getPacketData() {
        String account="13222200760";
        String uid = "000000000000000000000000000000000000";
        String key = "accountnumber=" + "13222200760" + "&token=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&timestamp=" + getCurrentTime();
        String device="867516023966902";
        long timesTamp=new Date().getTime();
        String time=getCurrentTime();
        Packet packet = new ConnectReq(uid, uid, account + "|" + key + "|" + " " + "|" + time + "|" + "0" + "|" + device, timesTamp);
        byte[] biaoshi = WriteData.int2Bytes(SocketGlobal.TITLE, 1);
        byte[] version = WriteData.int2Bytes(SocketGlobal.version,2);


        byte[] hebing1=  byteMerger(biaoshi, version);


        byte[] actiontype= WriteData.int2Bytes(packet.actiontype, 1);
        byte[] cmdLength=WriteData.int2Bytes(packet.cmd.getBytes().length, 1);

        byte[] hebing2=  byteMerger(actiontype, cmdLength);



        byte[] packetuid=new byte[36];
        packetuid=packet.uid.getBytes();

        byte[] packettouid=new byte[36];
        packettouid=packet.uid.getBytes();

        byte[] hebing3=  byteMerger(actiontype, cmdLength);

        byte[] packettime=DataUtil.longtoLH(packet.time);
        byte[] ostype=WriteData.int2Bytes(SocketGlobal.ostype, 1);

        byte[] hebing4= byteMerger(packettime, ostype);


        byte[] dataLength=new byte[4];
        int count = packet.encodeArgs().getBytes().length +  packet.cmd.getBytes().length;
        dataLength=DataUtil.inttoLH(count);

        //总共90个字节 为协议 剩下位数据

        byte[] cmdAnddata = DataUtil.byteMerger(packet.cmd.getBytes(), packet.encodeArgs().getBytes());

        byte[] hebing5= byteMerger(dataLength, cmdAnddata);

        //组合成一个字节数组

        byte[] merger1= byteMerger(hebing1,hebing2);
        byte[] merger2=byteMerger(merger1,hebing3);
        byte[] merger3=byteMerger(merger2,hebing4);
        //最后字节
        byte[] merger4=byteMerger(merger3,hebing5);


        return merger4;  //最后协议和数据


    }
    public  String getCurrentTime() {
        Date nowTime = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS");
        String nowTimeStr = dateFormat.format(nowTime);
        return nowTimeStr;
    }



    //java 合并两个byte数组
    public  byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
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
        addfriend=(TextView) findViewById(R.id.addfriend);
        sing_chat=(TextView) findViewById(R.id.sing_chat);
        group_chat=(TextView) findViewById(R.id.group_chat);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginoff:
                showToast("socket退出登录");
                LoginOffReq cr = new LoginOffReq(uid, uid, "0");
                ShakeAndVibrate.getInstance(getApplicationContext()).addNettyPacket(cr);
 //            ShakeAndVibrate.getInstance(this).anewConnectSocker(0);



            break;

            case R.id.addfriend:
                showToast("添加朋友");
                AddFriendsReq cr2 = new AddFriendsReq(uid, uid2,photonumber2);
        //      ShakeAndVibrate.getInstance(this).addPacket(cr2);
           ShakeAndVibrate.getInstance(this).addNettyPacket(cr2);
//               ShakeAndVibrate.getInstance(this).anewConnectSocker(0);

                break;


            case R.id.sing_chat:
                showToast("单聊");
//{"content":"是是是","nickname":"王小清","type":"0","timeSend":"2016-12-06 09:37:05.306","face":"\/data\/psmg\/2016\/09\/08\/201609081512371237kxvtgz.png"}
   //         {"dataState":1,"dataModel":0,"data":{"PrivateChat":[{"id":154868,"P_SaveTime":"2016-12-06 10:22:08.447","P_SendContent":"{\"content\":\"刚给他\",\"nickname\":\"王小清\",\"type\":\"0\",\"timeSend\":\"2016-12-06 10:22:06.341\",\"face\":\"\\/data\\/psmg\\/2016\\/09\\/08\\/201609081512371237kxvtgz.png\"}","P_SenderGuid":"e303fe70-5104-474d-a41e-87e79ec01b17"}],"GroupChat":[],"Radio":[]}}
      //      {"dataState":1,"dataModel":0,"data":"{\"content\":\"如风达\",\"nickname\":\"王小清\",\"type\":\"0\",\"timeSend\":\"2016-12-06 10:23:00.213\",\"face\":\"\\/data\\/psmg\\/2016\\/09\\/08\\/201609081512371237kxvtgz.png\"}"}
                JSONObject json = new JSONObject();
                try {
                    json.put("content","大家好我是拉阿拉拉了");
                    json.put("type","0");
                    json.put("timeSend","2016-12-06 10:55:00.213");
                    json.put("nickname","王晓清");
                    json.put("face","/data/psmg/2016/09/08/201609081512371237kxvtgz.png");
                    ChatMessageReq singchat = new ChatMessageReq(uid,uid2,json.toString());
                    boolean flag = ShakeAndVibrate.getInstance(this).addNettyPacket(singchat);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                AddFriendsReq cr = new AddFriendsReq(uid, uid2,photonumber2);
//                ShakeAndVibrate.getInstance(this).addPacket(cr);
//                ShakeAndVibrate.getInstance(this).anewConnectSocker(0);

                break;
            case R.id.group_chat:
                showToast("群聊");
//{"content":"7744","nickname":"小糖","Roomname":"wxq","type":"0","timeSend":"2016-12-06 15:52:05.966","face":"\/data\/psmg\/2016\/11\/22\/201611221052215221lahirn.png"}
                JSONObject json2 = new JSONObject();
                try {
                    json2.put("content","群聊");
                    json2.put("type","0");
                    json2.put("Roomname","wxq");
                    json2.put("timeSend", "2016-12-06 10:55:00.213");
                    json2.put("nickname","王晓清");
                    json2.put("face","/data/psmg/2016/09/08/201609081512371237kxvtgz.png");
                    GroupChatReq groupChatReq = new GroupChatReq(uid,classid,json2.toString());
                    boolean flag = ShakeAndVibrate.getInstance(this).addNettyPacket(groupChatReq);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                AddFriendsReq cr = new AddFriendsReq(uid, uid2,photonumber2);
//                ShakeAndVibrate.getInstance(this).addPacket(cr);
//                ShakeAndVibrate.getInstance(this).anewConnectSocker(0);

                break;



        }
    }


}
