package learn.wxq.socketapplication.socketservice.PacketModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vincent on 2016/3/4.
 * 单聊
 * ChatMessageReq cr=new ChatMessageReq(imuid,imuid,"我在发聊天的消息");
 */
public class ChatMessageReq extends Packet{
    protected static final String CMD = "PrivateChat";
    public static int actiontype=2;
    protected String datastr="";
    public ChatMessageReq(String uid, String toUid,String data) {
        super(CMD,uid,toUid,actiontype,0);
        this.datastr=data;
    }
    @Override
    public String encodeArgs() {
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
