package learn.wxq.socketapplication.socketservice.PacketModel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vincent on 2016/3/8.
 * 好友审核
 * FriendActionReq cr=new FriendActionReq(imuid,"18d051fc-0d59-4305-81bd-4d55fa87f224","2|317");
 *  内容:操作类型|操作id
    操作类型
    申请中 = 1,
    已通过申请 = 2,
    已拒绝 = 3,
    不在接收此人邀请 = 4
 */
public class FriendActionReq extends Packet{
    protected static final String CMD = "FriendAction";
    protected static int actiontype=1;
    protected String datastr="";
    public FriendActionReq( String uid, String toUid,String data) {
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
