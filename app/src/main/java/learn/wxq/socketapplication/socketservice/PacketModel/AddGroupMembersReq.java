package learn.wxq.socketapplication.socketservice.PacketModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vincent on 2016/3/18.
 */
public class AddGroupMembersReq extends Packet{
    protected static final String CMD = "AddGroupMembers";
    protected static int actiontype=1;
    protected String datastr="";
    public AddGroupMembersReq( String uid, String toUid,String data) {
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
            JSONArray guidListarr=new JSONArray();
            guidListarr.put("26c29ccb-8e02-4986-b589-e245aeaad605");
            guidListarr.put("4bbb8081-ef9f-4238-b326-301bf7156901");
            data.put("guidList",guidListarr);
            data.put("groupGuid","cbde80c8-4400-40df-bc85-f81f30b643cd");

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
