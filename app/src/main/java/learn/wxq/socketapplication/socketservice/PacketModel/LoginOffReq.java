package learn.wxq.socketapplication.socketservice.PacketModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ztn on 2016/8/4.
 * LoginOff
 * LoginOffReq cr=new LoginOffReq(uid,touid,touid);
 */
public class LoginOffReq extends Packet {
    protected static final String CMD = "LoginOff";
    protected static int actiontype = 1;
    protected String datastr = "";

    public LoginOffReq(String uid, String toUid, String data) {
        super(CMD, uid, toUid, actiontype, 0);
        this.datastr = data;
    }


    @Override
    public String encodeArgs() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("dataState", "1");
            obj.put("dataModel", "0");
            obj.put("data", datastr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}