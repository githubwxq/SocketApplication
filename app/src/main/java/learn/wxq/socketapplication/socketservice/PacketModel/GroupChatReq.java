package learn.wxq.socketapplication.socketservice.PacketModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vincent on 2016/3/8.
 * 群聊
 *  GroupChatReq cr=new GroupChatReq(uid,touid,"时光荏苒岁月如梭");
 *
 */
public class GroupChatReq extends Packet{
    protected static final String CMD = "GroupChat";
    public static int actiontype=2;
    protected String datastr="";
    protected String touid="";
    public GroupChatReq(String uid, String toUid,String data) {
        super(CMD,uid,toUid,actiontype,0);
        this.datastr=data;
        this.touid=toUid;
    }
    @Override
    public String encodeArgs() {
        JSONObject obj=new JSONObject();
        try {
            obj.put("dataState","1");
            obj.put("dataModel","0");

            JSONObject dataobj=new JSONObject();
            dataobj.put("GroupGuid",touid);
            dataobj.put("Content",datastr);
            obj.put("data",dataobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
