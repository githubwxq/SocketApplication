package learn.wxq.socketapplication.socketservice.PacketModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vincent on 2016/3/4.
 * 获取好友列表命令
 * GetFriendListReq cr=new GetFriendListReq(imuid,imuid,"");
 */

public class GetFriendListReq extends Packet{
    protected static final String CMD = "GetFriendList";
    public static int actiontype=1;
    protected String datastr="";
    public GetFriendListReq(String uid, String toUid,String data) {
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
