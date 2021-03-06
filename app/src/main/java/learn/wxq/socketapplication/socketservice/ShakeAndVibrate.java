package learn.wxq.socketapplication.socketservice;

import android.content.Context;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import learn.wxq.socketapplication.netty.NettyClientBootstrap;
import learn.wxq.socketapplication.netty.NettyClientHandler;
import learn.wxq.socketapplication.socketservice.PacketModel.ConnectReq;
import learn.wxq.socketapplication.socketservice.PacketModel.HeartbeatPingReq;
import learn.wxq.socketapplication.socketservice.PacketModel.NetworkRequestUsingHttpDNS;
import learn.wxq.socketapplication.socketservice.PacketModel.Packet;
import learn.wxq.socketapplication.socketservice.PacketModel.RePacket;

/**
 * Created by Vincent on 2016/2/23.
 */
public class ShakeAndVibrate {

    public String shuntHost = ""; //逻辑服务器端口
    public int shuntPort = 0;
    public volatile Socket shuntSocket;
    private volatile Socket mainSocket;

    public NettyClientBootstrap shuntNetty;
    public NettyClientBootstrap mainNetty;

    public volatile InputStream is;
    private volatile InputStream istime;
    public volatile OutputStream os;
    public static Timer timerPing;
    public Timer timeReconnection;
    public TimerTask taskPing, taskReconnection;

    private Packet packet;
    private Packet heartbeatPacket;
    private WriteData writeData;
    private ReadData readData;
    private static ShakeAndVibrate shakeAndVibrate;

    private DataParseListener dataParseListener;

    private String account = "";
    private String key = "";
    //-1是初始化状态，
    // 1代表分流服务器连接成功，
    // 0分流服务器连接失败或者断开联系需要从头开始连接，
    // 2代表逻辑服务器连接成功，
    // 3代表逻辑服务器连接成功并登录或者重连成功，
    // 4代表socket响应超时，需要执行重连操作
    // 5代表登录的时候socket响应超时
    private int socketState = -1;

    private long timesTamp;

    private Context context;

    private String deviceN = "";
    private String logintype = "";
    private String offtime = "";

    private long timedifference;

    private ShakeAndVibrate(Context context) {
        this.context = context;
    }

    public void initData(String offtime, String logintype, String deviceN, String account) {
        this.offtime = offtime;
        this.logintype = logintype;
        this.deviceN = deviceN;
        this.account = account;

    }


    public static ShakeAndVibrate getInstance(Context context) {
        if (shakeAndVibrate == null) {
            shakeAndVibrate = new ShakeAndVibrate(context);
        }
        return shakeAndVibrate;
    }

    public int getSocketState() {
        return socketState;
    }

    //数据监听器
    public void addDataParseListener(DataParseListener listener) {
        dataParseListener = listener;


    }

    public void setAccountAndKey(String account) {
        this.account = account;

    }

    //运行在子线程开始连接以及登陆
    public int connectMain() {
        // 连接重连
        if(mainNetty!=null){

           mainNetty.closeChannel();
            mainNetty=null;
        }

        if(shuntNetty!=null){
            shuntNetty.closeChannel();
            shuntNetty=null;
        }
        //    mainSocket = new Socket();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String ip = NetworkRequestUsingHttpDNS.mainSocket(context, SocketGlobal.HOST); // 获取ip
                    mainNetty = new NettyClientBootstrap(context, ip, SocketGlobal.PORT, new DataListenerImpl());

                    if (mainNetty.start()) {
                        System.out.println("netty 连接分流服务器成功");

                        //   return 1;
                    } else {
                        System.out.println("netty 连接分流服务器失败");

                    }

                } catch (Exception e) {

                    dataParseListener.connectionClosed(socketState);
                    e.printStackTrace();
                }


            }
        }).start();


        return 0;
    }

    //读取解析主服务器返回的分流服务器的ip地址，端口号
//    private int decodeMain() throws IOException, JSONException, Exception {
//        int cmdlength = 0;
//        int cmdAndDatalength = 0;
//        boolean readflag = false;
//
//        byte[] buf = new byte[90];
//        int n = 0;
//        boolean found = false;
//        RePacket rpacket = new RePacket(); // 包
//
//        while (true) {
////                int b = is.read();
//            int b = is.read();
//            if (-1 == b) {// 读到末尾
//                break;
//            }
//            if (n == 89) {
//                buf[n] = (byte) b;
//                break;
//            }
//            buf[n++] = (byte) b;
//
//        }
//        byte[] titleb = Arrays.copyOfRange(buf, 0, 1);
//        int titlestr = DataUtil.toInt(titleb);
//        if (titlestr == SocketGlobal.TITLE) {
//            readflag = true;
//        }
//        titleb = null;
//
//        if (readflag) {
//            byte[] versionB = Arrays.copyOfRange(buf, 1, 3);
//            short versions = DataUtil.byte2short(versionB);
//            titleb = null;
//
//            byte[] cmdB = Arrays.copyOfRange(buf, 4, 5);
//            int cmdint = DataUtil.toInt(cmdB);
//            cmdlength = cmdint;
//            cmdB = null;
//
//            byte[] uidB = Arrays.copyOfRange(buf, 5, 41);
//            String uidlong = new String(uidB).toString();
//            rpacket.uid = uidlong;
//            uidB = null;
//
//            byte[] touidB = Arrays.copyOfRange(buf, 41, 77);
//            String touidlong = new String(touidB).toString();
//            rpacket.toUid = touidlong;
//            touidB = null;
//
//            byte[] timeB = Arrays.copyOfRange(buf, 77, 85);
//            long timelong = DataUtil.byteToLong(timeB);
//            rpacket.time = DataUtil.dateType(Long.toString(timelong));
//            timesTamp = timelong;
//            long currentTime = System.currentTimeMillis();
//            timedifference = timesTamp - currentTime;
//            timeB = null;
//
//            byte[] cmdAndDatalengthB = Arrays.copyOfRange(buf, 86, 90);
//            int cmdAndDatalengthint = DataUtil.toInt(cmdAndDatalengthB);
//            cmdAndDatalength = cmdAndDatalengthint;
//            cmdAndDatalengthB = null;
//
//        }
//        byte[] temp = new byte[cmdAndDatalength];
//        int indexint = 0;
//        while (indexint < temp.length) {
//            int m = is.read(temp, indexint, temp.length - indexint);
//            if (-1 == m) {
//                break;
//            }
//            indexint += m;
//        }
//        byte[] cmdS = Arrays.copyOfRange(temp, 0, cmdlength);
//        String cmdstr = new String(cmdS).toString();
//        rpacket.cmd = cmdstr;
//        byte[] datastrB = Arrays.copyOfRange(temp, cmdlength, cmdAndDatalength);
//        String datastr = new String(datastrB).toString();
//        rpacket.datastr = datastr;
//        temp = null;
//        cmdS = null;
//        datastrB = null;
//        buf = null;
//        System.gc();
//        if (rpacket.cmd.equals("toConnect")) {
//            JSONObject dataObj = new JSONObject(rpacket.datastr);
//            if (dataObj != null) {
//                String ipstr = dataObj.getString("data");
//                String[] ipArray = ipstr.split(":");
//                if (ipArray != null && ipArray.length > 1) {
//                    shuntHost = ipArray[0];
//                    shuntPort = Integer.parseInt(ipArray[1]);
//                    ipArray = null;
//                    return closeMainSocket();
//                } else {
//                    return 0;
//                }
//            } else {
//                return 0;
//            }
//
//        } else {
//            return 0;
//        }
//    }

    public int closeMainSocket() {
        SocketGlobal.flag = false;
        if (is != null) {
            try {
                mainSocket.shutdownInput();
                is = null;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }

        }
        if (os != null) {
            try {
                mainSocket.shutdownOutput();
                os = null;
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
//        System.gc();
        return connecShount();
    }

    //连接逻辑服务器
    private int connecShount() {
        int b = -1;
        shuntSocket = new Socket();
        try {
            String ip = NetworkRequestUsingHttpDNS.mainSocket(context, shuntHost);
            shuntSocket.setSoTimeout(SocketGlobal.SO_TIMEOUT);
            shuntSocket.connect(new InetSocketAddress(ip, shuntPort), SocketGlobal.CONNECT_TIMEOUT);
            b = 1;
            SocketGlobal.flag = true;
            socketState = 2;//2代表逻辑服务器连接成功
            dataParseListener.reconnectionSuccessful(socketState);
        } catch (SocketException e) {
            inAnewConnectSocker(0);
            dataParseListener.connectionClosed(socketState);
            e.printStackTrace();
            b = 0;
            return b;
        } catch (IOException e) {
            inAnewConnectSocker(0);
            dataParseListener.connectionClosed(socketState);
            e.printStackTrace();
            b = 0;
            return b;
        } catch (Exception e) {
            if (dataParseListener != null) {
                dataParseListener.connectionClosed(socketState);
            }
            e.printStackTrace();
            b = 0;
            return b;
        }


        return b;
    }

    //执行登录，并打开心跳计时器 和数据读取器
    public int loginAction(String time, String logintype, String device) {
        try {
            deviceN = device;
            offtime = time;
            writeData = WriteData.getInstance();
            readData = ReadData.getInstance(dataParseListener);
//            is = shuntSocket.getInputStream();
//            os = shuntSocket.getOutputStream();
//            writeData.setOutputStream(os);
//            readData.setInputStream(is);
            String uid = "000000000000000000000000000000000000";
            String key = "accountnumber=" + account + "&token=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&timestamp=" + timesTamp;
            key = sha1(key);
            UserPreference.getInstance(context).storeIMKey(key);
            if (account != null && !account.equals("") && key != null && !key.equals("")) {
                if (packet != null) {
                    packet = null;
                }
                packet = new ConnectReq(uid, uid, account + "|" + key + "|" + " " + "|" + time + "|" + logintype + "|" + device, timesTamp);
            } else {
                throw new NullPointerException();
            }
            if (packet != null) {

                //  writeData.encode(packet); //给netty使用
                writeData.nettyEncode(packet, shuntNetty);
            } else {

            }
            socketState = 3;//3代表逻辑服务器练级成功并登录成功
//            if (readData.readinfolink == null) {
//                readData = ReadData.getInstance(dataParseListener);
//                readData.readinfolink.writeThread.start();
//            } else if (readData.readinfolink != null && readData.readinfolink.writeThread.isAlive()) {
//            } else if (readData.readinfolink != null && !readData.readinfolink.writeThread.isAlive()) {
//                readData.readinfolink.writeThread.start();
//            }
//
//            readData.decode();
        }
//        catch (SocketException e) {
//            inAnewConnectSocker(0);
//            dataParseListener.connectionClosed(socketState);
//            e.printStackTrace();
//        }
//        catch (SocketTimeoutException e) {
//            if (packet != null && packet.cmd.equals("Login")) {
//                inAnewConnectSocker(5);
//                dataParseListener.connectionClosed(socketState);
//                closeTimeTaskPing();
//            } else {
//                inAnewConnectSocker(4);
//                dataParseListener.loadingReconnect(socketState);
//            }
//            e.printStackTrace();
//        }
//        catch (IOException e) {
//            inAnewConnectSocker(0);
//            dataParseListener.connectionClosed(socketState);
//            e.printStackTrace();
//        }
        catch (Exception e) {
            inAnewConnectSocker(0);
            dataParseListener.connectionClosed(socketState);
            e.printStackTrace();
        }
        return 0;
    }

    //重新从主服务器开始连接的准备工作
    public void anewConnectSocker(int state) {
        try {
            SocketGlobal.flag = false;
            socketState = state;//状态为0表示连接断开
            if (readData != null) {
                RePacket rePacket = new RePacket();
                rePacket.cmd = "SocketException";
                readData.dataParse(rePacket);//给队列添加特殊包，让队列循环退出
            }
            outCloseSocket();
            if (readData != null) {
                cancleData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void outCloseSocket() {//关闭socket对象
        SocketGlobal.flag = false;
        try {
            if (is != null && shuntSocket != null && !shuntSocket.isInputShutdown()) {
                shuntSocket.shutdownInput();
                is = null;
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        try {
            if (os != null && shuntSocket != null && !shuntSocket.isOutputShutdown()) {
                shuntSocket.shutdownOutput();
                os = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (shuntSocket != null) {
                shuntSocket.close();
                shuntSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();
    }

    //内部出现异常重新从主服务器开始连接的准备工作
    public void inAnewConnectSocker(int state) {
        SocketGlobal.flag = false;
        socketState = state;//状态为0表示连接断开
        RePacket rePacket = new RePacket();
        try {
            if (rePacket != null) {
                rePacket.cmd = "SocketException";
                if (readData != null) {
                    readData.dataParse(rePacket);//给队列添加特殊包，让队列循环退出
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        inCloseSocket();
        if (readData != null) {
            cancleData();
        }
        closeTimeTaskPing();
    }

    public void inCloseSocket() {//关闭socket对象
        SocketGlobal.flag = false;
        try {
            if (istime != null && mainSocket != null && !mainSocket.isInputShutdown()) {
                istime.close();
                istime = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (is != null && shuntSocket != null && !shuntSocket.isInputShutdown()) {
                is.close();
                is = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (os != null && shuntSocket != null && !shuntSocket.isOutputShutdown()) {
                os.close();
                os = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (shuntSocket != null) {
                shuntSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.gc();

    }


    public void openTimetask() {
        socketState = 3;//3代表逻辑服务器练级成功并登录成功
        dataParseListener.reconnectionSuccessful(socketState);

        timeTaskPing();
    }


    private void closeTimeTaskPing() {
        try {
            if (timerPing != null && taskPing != null) {
                taskPing.cancel();
                timerPing.cancel();
                taskPing = null;
                timerPing = null;
                System.gc();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancleData() {
        try {
            if (readData != null && readData.readinfolink != null && readData.readinfolink.writeThread.isAlive()) {
                readData.readinfolink.writeThread.interrupt();
                readData.readinfolink.writeThread = null;
            }
            if (writeData != null) {
                WriteData.getInstance().deleteData();
                writeData = null;
            }
            if (readData != null) {
                ReadData.getInstance(dataParseListener).deleteData();
                readData = null;
            }
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void timeTaskPing() {
        if (timerPing == null) {
            timerPing = new Timer();
            taskPing = new TimerTask() {
                @Override
                public void run() {
                    if (socketState != 3) {
                        closeTimeTaskPing();
                    } else {
                        String uid = "000000000000000000000000000000000000";
                        if (packet != null) {
                            packet = null;
                        }
                        System.gc();
                        if (heartbeatPacket == null) {
                            heartbeatPacket = new HeartbeatPingReq(uid, uid, "");
                        }
                        packet = heartbeatPacket; //心跳包
                        try {
                            if (heartbeatPacket != null) {
                                if (writeData != null) {
                                    writeData.encode(packet);  // 写给服务器的数据我们还连着
                                } else {
                                    writeData = WriteData.getInstance();

                                    if (os == null) {
                                        os = shuntSocket.getOutputStream();
                                        writeData.setOutputStream(os);
                                    } else {
                                        writeData.setOutputStream(os);
                                    }
                                    writeData.encode(packet);
                                }

                            } else {
                            }
                        } catch (SocketException e) {
                            inAnewConnectSocker(0);
                            dataParseListener.connectionClosed(socketState);
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            timerPing.schedule(taskPing, SocketGlobal.KEEP_ALIVE, SocketGlobal.KEEP_ALIVE);
        }
    }

    public boolean addPacket(Packet packetdata) {
        try {
            if (packet != null) {
                packet = null;
                System.gc();
            }

            this.packet = packetdata;
            if (writeData != null) {
                packet.setTime(System.currentTimeMillis() + timedifference);
                writeData.encode(packet);
            } else {
                writeData = WriteData.getInstance();

                if (os == null) {
                    os = shuntSocket.getOutputStream();
                    writeData.setOutputStream(os);
                } else {
                    writeData.setOutputStream(os);
                }
                writeData.encode(packet);
            }
        } catch (IOException e) {
            inAnewConnectSocker(0);
            dataParseListener.connectionClosed(socketState);
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean addNettyPacket(Packet packetdata) {  //发送数据
        writeData = WriteData.getInstance();
        packetdata.setTime(System.currentTimeMillis() + timedifference);
        writeData.nettyEncode(packetdata, shuntNetty);//shuntNetty
        return true;
    }


    //网络切换处理


    public String sha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes());

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    public void updateTime() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                timedifference = UpdateTime.getInstance(context).getconnectMainTime();
                System.out.println("timedifference==========" + timedifference);
            }
        }.start();
    }


    class DataListenerImpl implements NettyClientHandler.DataListener {
        boolean isConnected = true;

        @Override
        public void dealWithData(String data) {
            System.out.println("我是来之分流服务器数据==========" + data);
            String ip = NetworkRequestUsingHttpDNS.mainSocket(context, "im.exiaoxin.com"); // 获取ip
//            String host="im.exiaoxin.com";
//            int port=20000;
            //重新连接逻辑服务器

            //   关闭分流netty
            mainNetty.stopPing(); //不需要心跳
            mainNetty = null;
            System.gc();
            shuntNetty = new NettyClientBootstrap(context, ip, 20000, new DataListenerImpl2());


        }

        @Override
        public void dealWithByteData(byte[] data) {
            System.out.println("我是来之分流服务器数据==========字节属猪" + data.toString());
            //处理分流服务器返回数据
            System.out.println("数据为" + new String(Arrays.copyOfRange(data, 90, data.length)));
            int cmdlength = 0;
            int cmdAndDatalength = 0;
            boolean readflag = false;
            byte[] buf = new byte[90];
            buf = Arrays.copyOfRange(data, 0, 90);
            RePacket rpacket = new RePacket(); // 包


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
                rpacket.uid = uidlong;
                uidB = null;

                byte[] touidB = Arrays.copyOfRange(buf, 41, 77);
                String touidlong = new String(touidB).toString();
                rpacket.toUid = touidlong;
                touidB = null;

                byte[] timeB = Arrays.copyOfRange(buf, 77, 85);
                long timelong = DataUtil.byteToLong(timeB);
                rpacket.time = DataUtil.dateType(Long.toString(timelong));
                timesTamp = timelong;
                long currentTime = System.currentTimeMillis();
                timedifference = timesTamp - currentTime;
                timeB = null;

                byte[] cmdAndDatalengthB = Arrays.copyOfRange(buf, 86, 90);
                int cmdAndDatalengthint = DataUtil.toInt(cmdAndDatalengthB);

                System.out.println("数据长度" + cmdAndDatalengthint);
                cmdAndDatalength = cmdAndDatalengthint;
                cmdAndDatalengthB = null;

            }

            byte[] temp = Arrays.copyOfRange(data, 90, data.length);

            //     temp= Arrays.copyOfRange(buf, 90, data.length);//数据字节数组
            byte[] cmdS = Arrays.copyOfRange(temp, 0, cmdlength);// 命令字节
            String cmdstr = new String(cmdS).toString();
            //     System.out.println("cmdstr:+++" + cmdstr.toString());
            rpacket.cmd = cmdstr;
            byte[] datastrB = Arrays.copyOfRange(temp, cmdlength, cmdAndDatalength);
            String datastr = new String(datastrB).toString();
            System.out.println("data:+++" + datastr.toString());
            rpacket.datastr = datastr;
            temp = null;
            cmdS = null;
            datastrB = null;
            buf = null;
            System.gc();
            try {
                if (rpacket.cmd.equals("toConnect")) {
                    JSONObject dataObj = null;
                    dataObj = new JSONObject(rpacket.datastr);

                    if (dataObj != null) {
                        String ipstr = dataObj.getString("data");
                        String[] ipArray = ipstr.split(":");
                        if (ipArray != null && ipArray.length > 1) {
                            shuntHost = ipArray[0];
                            shuntPort = Integer.parseInt(ipArray[1]);
                            // 关闭分流 启动逻辑
                            mainNetty.stopPing(); //不需要心跳
                            mainNetty = null;
                            System.gc();
                            //
                            shuntNetty = new NettyClientBootstrap(context, shuntHost, shuntPort, new DataListenerImpl2());
                            if (shuntNetty.start()) {
                                // 逻辑连接成功 然后登陆
                                loginAction(offtime, logintype, deviceN);
                            } else {


                            }
                            //等露逻辑服务器

                        } else {

                        }
                    } else {

                    }

                } else {

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class DataListenerImpl2 implements NettyClientHandler.DataListener {
        boolean isConnected = true;

        @Override
        public void dealWithData(String data) {
            System.out.println("逻辑服务器返回数据为DataListenerImpl2==========" + data);
            //拿到返回数据

        }

        @Override
        public void dealWithByteData(byte[] data) {
            System.out.println("逻辑服务器返回数据为DataListenerImpl2" + new String(Arrays.copyOfRange(data, 90, data.length)));
            int cmdlength = 0;
            int cmdAndDatalength = 0;
            boolean readflag = false;
            byte[] buf = new byte[90];
            buf = Arrays.copyOfRange(data, 0, 90);
            RePacket rpacket = new RePacket(); // 包


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
                rpacket.uid = uidlong;
                uidB = null;

                byte[] touidB = Arrays.copyOfRange(buf, 41, 77);
                String touidlong = new String(touidB).toString();
                rpacket.toUid = touidlong;
                touidB = null;

                byte[] timeB = Arrays.copyOfRange(buf, 77, 85);
                long timelong = DataUtil.byteToLong(timeB);
                rpacket.time = DataUtil.dateType(Long.toString(timelong));
                timesTamp = timelong;
                long currentTime = System.currentTimeMillis();
                timedifference = timesTamp - currentTime;
                timeB = null;

                byte[] cmdAndDatalengthB = Arrays.copyOfRange(buf, 86, 90);
                int cmdAndDatalengthint = DataUtil.toInt(cmdAndDatalengthB);

                //          System.out.println("数据长度" + cmdAndDatalengthint);
                cmdAndDatalength = cmdAndDatalengthint;
                cmdAndDatalengthB = null;

            }

            byte[] temp = Arrays.copyOfRange(data, 90, data.length);

            //     temp= Arrays.copyOfRange(buf, 90, data.length);//数据字节数组
            byte[] cmdS = Arrays.copyOfRange(temp, 0, cmdlength);// 命令字节
            String cmdstr = new String(cmdS).toString();
            //      System.out.println("cmdstr:+++" + cmdstr.toString());
            rpacket.cmd = cmdstr;
            byte[] datastrB = Arrays.copyOfRange(temp, cmdlength, cmdAndDatalength);
            String datastr = new String(datastrB).toString();
            //       System.out.println("data:+++" + datastr.toString());
            rpacket.datastr = datastr;
            temp = null;
            cmdS = null;
            datastrB = null;
            buf = null;
            System.gc();
            dataParseListener.dataReceiveSuccessful(rpacket); //返回成功数据
        }


    }

    public String getCurrentTime() {
        Date nowTime = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS");
        String nowTimeStr = dateFormat.format(nowTime);
        return nowTimeStr;
    }

    //java 合并两个byte数组
    public byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public void closeAll() {

        shuntNetty = null;
        mainNetty = null;

        System.gc();
    }
}
