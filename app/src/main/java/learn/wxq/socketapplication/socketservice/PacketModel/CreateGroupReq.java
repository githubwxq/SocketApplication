package learn.wxq.socketapplication.socketservice.PacketModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vincent on 2016/3/16.
 * 创建群
 * 8481cc6e-5f73-4095-987a-0b28ab11d0e6
 */
public class CreateGroupReq extends Packet{
    protected static final String CMD = "CreateGroup";
    protected static int actiontype=1;
    protected String datastr="";
    public CreateGroupReq( String uid, String toUid,String data) {
        super(CMD,uid,toUid,actiontype,0);
        this.datastr=data;
    }

    @Override
    public String encodeArgs() {

//            {"dataState":1,"dataModel":0,"data":"54321|f7fd3065-96da-42c3-83b1-5da2ce92a822"}
        JSONObject obj=new JSONObject();
        JSONArray objarr=new JSONArray();

        try {
            JSONObject data=new JSONObject();
            data.put("Name","大一班");
            data.put("Guid","cbde80c8-4400-40df-bc85-f81f30b645cd");
            data.put("GroupType","1");
            data.put("MaxMembers",500);
            objarr.put(data);
            obj.put("dataState","1");
            obj.put("dataModel","0");
            obj.put("data",objarr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
