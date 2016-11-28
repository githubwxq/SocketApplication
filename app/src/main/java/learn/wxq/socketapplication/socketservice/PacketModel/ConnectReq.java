package learn.wxq.socketapplication.socketservice.PacketModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vincent on 2016/3/1.
 * 登录连接
 *
 */
public class ConnectReq extends Packet {
    protected static final String CMD = "Login";
    protected static int actiontype=1;
    protected String datastr="";
    public ConnectReq( String uid, String toUid,String data,long time) {
        super(CMD,uid,toUid,actiontype,time);
        this.datastr=data;
    }


    @Override
        public String encodeArgs() {
//            {"dataState":1,"dataModel":0,"data":"54321|f7fd3065-96da-42c3-83b1-5da2ce92a822"}
            JSONObject obj=new JSONObject();
                try {
                    obj.put("dataState","1");
                    obj.put("dataModel","0");
                    obj.put("data",datastr);
                } catch (JSONException e) {
                    e.printStackTrace();
            }
            return obj.toString();
        }
}
