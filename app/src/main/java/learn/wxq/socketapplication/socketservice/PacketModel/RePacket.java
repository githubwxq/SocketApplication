package learn.wxq.socketapplication.socketservice.PacketModel;

import java.io.Serializable;

/**
 * Created by Vincent on 2016/2/25.
 */
public class RePacket implements Serializable {
    public String cmd = "";
    public String uid;
    public String toUid;
    public String time = "";
    public String datastr = "";
}
