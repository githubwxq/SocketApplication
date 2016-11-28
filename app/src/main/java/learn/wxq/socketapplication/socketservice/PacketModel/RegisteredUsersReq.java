package learn.wxq.socketapplication.socketservice.PacketModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vincent on 2016/3/8.
 */
public class RegisteredUsersReq  extends Packet {
    protected static final String CMD = "RegisteredUsers";
    protected static int actiontype=1;
    protected String datastr="";
    public RegisteredUsersReq( String uid, String toUid,String data) {
        super(CMD,uid,toUid,actiontype,0);
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
