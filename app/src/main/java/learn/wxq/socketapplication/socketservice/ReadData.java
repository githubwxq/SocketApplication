package learn.wxq.socketapplication.socketservice;



import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import learn.wxq.socketapplication.socketservice.PacketModel.RePacket;

/**
 * Created by Vincent on 2016/3/1.
 */
public class ReadData {
    public  InfoLinkedBlockingQueue readinfolink;
    private static ReadData readData;
    private DataParseListener dataParseListener;

    private InputStream is;

    private RePacket rpacket;

    private ReadData(DataParseListener dataParseListener){
        this.dataParseListener=dataParseListener;
        readinfolink=new InfoLinkedBlockingQueue(this.dataParseListener);
    }
    public static ReadData getInstance(DataParseListener dataParseListener){
        if(readData==null){
            readData=new ReadData(dataParseListener);
        }
        return readData;
    }
    public void setInputStream(InputStream is){
        if(this.is!=null){
            this.is=null;
        }
        this.is=is;
    }
    public  void decode() throws IOException,NullPointerException,NegativeArraySizeException {
        while(SocketGlobal.flag){
            int cmdlength=0;
            int cmdAndDatalength=0;
            boolean readflag=false;

            byte[] buf = new byte[90];
            int n = 0;
            boolean found = false;

            while (true) {
                int b = is.read();
                if (-1 == b) {// 读到末尾
                    break;
                }
                if(n==89){
                    buf[n]=(byte)b;
                    break;
                }
                buf[n++]=(byte)b;

            }
            byte[] titleb= Arrays.copyOfRange(buf, 0, 1);
            int titlestr= DataUtil.toInt(titleb);
            if (titlestr==SocketGlobal.TITLE){
                readflag=true;
            }
            titleb=null;
            if(readflag){
                rpacket=new RePacket();
                byte[] versionB= Arrays.copyOfRange(buf, 1, 3);
                short versions=DataUtil.byte2short(versionB);
                versionB=null;

                byte[] cmdB= Arrays.copyOfRange(buf, 4, 5);
                int cmdint= DataUtil.toInt(cmdB);
                cmdlength=cmdint;
                cmdB=null;

                byte[] uidB= Arrays.copyOfRange(buf, 5, 41);
                String uidlong= new String(uidB).toString();
                rpacket.uid=uidlong;
                uidB=null;


                byte[] touidB= Arrays.copyOfRange(buf, 41, 77);
                String touidlong= new String(touidB).toString();
                rpacket.toUid=touidlong;
                touidB=null;

                byte[] timeB= Arrays.copyOfRange(buf, 77, 85);
                long timelong= DataUtil.byteToLong(timeB);
                rpacket.time=DataUtil.dateType(Long.toString(timelong));
                timeB=null;


                byte[] cmdAndDatalengthB= Arrays.copyOfRange(buf, 86, 90);
                int cmdAndDatalengthint= DataUtil.toInt(cmdAndDatalengthB);
                cmdAndDatalength=cmdAndDatalengthint;
                cmdAndDatalengthB=null;

            }
            byte[] temp = new byte[cmdAndDatalength];
            int indexint=0;
            while(indexint<temp.length){
                int m=is.read(temp,indexint,temp.length-indexint);
//                int m=ShakeAndVibrate.getInstance().is.read(temp,indexint,temp.length-indexint);
                if(-1==m){
                    break;
                }
                indexint+=m;
            }
            byte[] cmdS= Arrays.copyOfRange(temp, 0, cmdlength);
            String cmdstr=new String(cmdS).toString();
            byte[] datastrB= Arrays.copyOfRange(temp, cmdlength, cmdAndDatalength);
            String datastr=new String(datastrB).toString();

            temp=null;
            cmdS=null;
            datastrB=null;
            buf=null;
            if(rpacket!=null){
                rpacket.cmd=cmdstr;
                rpacket.datastr=datastr;
                dataParse(rpacket);
            }
            System.gc();



        }
    }

    public  void dataParse(RePacket rpacket) {
        //处理返回的数据
        if(readinfolink!=null){
            try {
                if(rpacket!=null){
                    readinfolink.put(rpacket);  //
                }
            } catch (Exception e) {//等待
                e.printStackTrace();
            }
        }else{
        }

    }

    public  void  deleteData(){
        if(is!=null){
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            is=null;
        }
        if(dataParseListener!=null){
            dataParseListener=null;
        }
        if(readinfolink!=null){
            readinfolink.deleteData();
            readinfolink=null;
        }
        if(readData!=null){
            readData=null;
        }
    }
}
