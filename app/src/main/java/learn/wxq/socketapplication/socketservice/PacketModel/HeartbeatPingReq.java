package learn.wxq.socketapplication.socketservice.PacketModel;

/**
 * Created by Vincent on 2016/3/11.
 */
public class HeartbeatPingReq extends Packet {
    protected static final String CMD = "Ping";
    public static int actiontype=1;
    protected String datastr="";
    public HeartbeatPingReq( String uid, String toUid,String data) {
        super(CMD, uid, toUid,actiontype,0);
        this.datastr=data;
    }

    @Override
    public String encodeArgs() {
        return "";
    }
}
