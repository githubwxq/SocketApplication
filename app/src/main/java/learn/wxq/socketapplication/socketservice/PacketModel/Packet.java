package learn.wxq.socketapplication.socketservice.PacketModel;

/**
 * Created by Vincent on 2016/3/1.
 */
public abstract class Packet {


    public String cmd="";
    public String uid;
    public String toUid;
    public   long time=0000;
    protected   int count=0;
    public  int actiontype;

    public Packet(String cmd,String uid,String toUid,int type,long time){
        this(cmd);
        this.uid=uid;
        this.toUid=toUid;
        this.actiontype=type;
        if(time!=0){
            this.time=time;
        }


    }

    public Packet(String cmd) {
        if (null == cmd) {
            throw new NullPointerException("cmd can not be null");
        }
        this.cmd = cmd;
    }
    public abstract String encodeArgs() ;

    public void setTime(long time){
        this.time=time;

    }
}
