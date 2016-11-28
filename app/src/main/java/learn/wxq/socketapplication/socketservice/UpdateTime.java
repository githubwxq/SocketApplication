package learn.wxq.socketapplication.socketservice;

import android.content.Context;



import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import learn.wxq.socketapplication.socketservice.PacketModel.NetworkRequestUsingHttpDNS;
import learn.wxq.socketapplication.socketservice.PacketModel.RePacket;

/**
 * Created by Vincent on 2016/2/23.
 */
public class UpdateTime {

    private volatile Socket mainSocket;
    public volatile InputStream is;
    private volatile InputStream istime;
    public volatile OutputStream os;
    private static UpdateTime updateTime;


    private int socketState = -1;

    private Context context;

    private long timedifference;

    private UpdateTime(Context context) {
        this.context = context;
    }

    public static UpdateTime getInstance(Context context) {
        if (updateTime == null) {
            updateTime = new UpdateTime(context);
        }
        return updateTime;
    }

    public int getSocketState() {
        return socketState;
    }


    public long getconnectMainTime() {
        mainSocket = new Socket();
        try {
            String ip = NetworkRequestUsingHttpDNS.mainSocket(context, SocketGlobal.HOST);
            mainSocket.connect(new InetSocketAddress(ip, SocketGlobal.PORT), SocketGlobal.CONNECT_TIMEOUT);
            istime = mainSocket.getInputStream();
            if (istime != null) {
                return decodeMainTimeDifference();
            }
        } catch (ConnectException e) {
            if (mainSocket != null) {
                mainSocket = null;
                System.gc();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //读取主服务器返回的分流服务器的ip地址，端口号
    private long decodeMainTimeDifference() throws IOException, JSONException, Exception {
        try {
            int cmdlength = 0;
            int cmdAndDatalength = 0;
            boolean readflag = false;

            byte[] buf = new byte[90];
            int n = 0;
            boolean found = false;
            RePacket rpacketTime = new RePacket();

            while (true) {
//                int b = is.read();
                int b = istime.read();
                if (-1 == b) {// 读到末尾
                    break;
                }
                if (n == 89) {
                    buf[n] = (byte) b;
                    break;
                }
                buf[n++] = (byte) b;

            }
            byte[] titleb = Arrays.copyOfRange(buf, 0, 1);
            int titlestr = DataUtil.toInt(titleb);
            if (titlestr == SocketGlobal.TITLE) {
                readflag = true;
            }
            titleb = null;

            if (readflag) {
                byte[] versionB = Arrays.copyOfRange(buf, 1, 3);
                short versions = DataUtil.byte2short(versionB);
                titleb = null;

                byte[] cmdB = Arrays.copyOfRange(buf, 4, 5);
                int cmdint = DataUtil.toInt(cmdB);
                cmdlength = cmdint;
                cmdB = null;

                byte[] uidB = Arrays.copyOfRange(buf, 5, 41);
                String uidlong = new String(uidB).toString();
                rpacketTime.uid = uidlong;
                uidB = null;

                byte[] touidB = Arrays.copyOfRange(buf, 41, 77);
                String touidlong = new String(touidB).toString();
                rpacketTime.toUid = touidlong;
                touidB = null;

                byte[] timeB = Arrays.copyOfRange(buf, 77, 85);
                long timelong = DataUtil.byteToLong(timeB);
                rpacketTime.time = DataUtil.dateType(Long.toString(timelong));
                long currentTime= System.currentTimeMillis();
                timedifference=timelong-currentTime;
                timeB = null;

                byte[] cmdAndDatalengthB = Arrays.copyOfRange(buf, 86, 90);
                int cmdAndDatalengthint = DataUtil.toInt(cmdAndDatalengthB);
                cmdAndDatalength = cmdAndDatalengthint;
                cmdAndDatalengthB = null;

            }
//            byte[] temp = new byte[cmdAndDatalength];
//            int indexint = 0;
//            while (indexint < temp.length) {
//                int m = istime.read(temp, indexint, temp.length - indexint);
//                if (-1 == m) {
//                    break;
//                }
//                indexint += m;
//            }
//            temp = null;
            buf = null;
            System.gc();
            closeMainSocketTime();
        }catch (OutOfMemoryError o){
            o.printStackTrace();
            System.gc();
        }
        return timedifference;
    }

    public int closeMainSocketTime() {
        if (istime != null) {
            try {
                mainSocket.shutdownInput();
                istime = null;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }

        }
        if (mainSocket != null) {
            try {
                mainSocket.close();
                mainSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }
        System.gc();
        return 1;
    }

}
