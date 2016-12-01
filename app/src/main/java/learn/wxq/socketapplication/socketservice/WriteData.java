package learn.wxq.socketapplication.socketservice;



import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import learn.wxq.socketapplication.netty.NettyClientBootstrap;
import learn.wxq.socketapplication.socketservice.PacketModel.Packet;

/**
 * Created by Vincent on 2016/3/1.
 */
public class WriteData {
    private static WriteData writeData;
    private OutputStream os;

    private WriteData() {
    }

    public static WriteData getInstance() {
        if (writeData == null) {
            writeData = new WriteData();
        }
        return writeData;
    }

    public void setOutputStream(OutputStream os) {
        if (this.os != null) {
            this.os = null;
        }
        this.os = os;
    }

    public void encode(final Packet packet) throws IOException, Exception {
        String args = null;
        synchronized (os) {
            args = packet.encodeArgs();
            final String finalArgs = args;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        os.write(int2Bytes(SocketGlobal.TITLE,1));
                        os.write(int2Bytes(SocketGlobal.version,2));
                        os.write(int2Bytes(packet.actiontype, 1));
                        byte[] cmdByte = packet.cmd.getBytes();
                        os.write(int2Bytes(cmdByte.length,1));
                        byte[] uid=new byte[36];
                        uid=packet.uid.getBytes();
                        os.write(uid);
                        byte[] touid=new byte[36];
                        touid=packet.uid.getBytes();
                        os.write(touid);
//                      os.write(DataUtil.longtoLH(packet.time));
                        os.write(DataUtil.longtoLH(packet.time));
                        os.write(int2Bytes(SocketGlobal.ostype, 1));
                        int count = finalArgs.getBytes().length + cmdByte.length;
                        os.write(DataUtil.inttoLH(count));
                        byte[] cmdAnddata = DataUtil.byteMerger(cmdByte, finalArgs.getBytes());
                        os.write(cmdAnddata);
                        os.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
        os.flush();
    }


    public static byte[] int2Bytes(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte)((value >> 8 * i) & 0xff);
        }
        return b;
    }

    public static byte[] longToByte(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
        return b;
    }
    public void deleteData() {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            os = null;
        }
        if (writeData != null) {
            writeData = null;
        }
    }


    public void nettyEncode(Packet packet, NettyClientBootstrap shuntNetty) {

        byte[] biaoshi = WriteData.int2Bytes(SocketGlobal.TITLE, 1);
        byte[] version = WriteData.int2Bytes(SocketGlobal.version,2);

        byte[] hebing1=  byteMerger(biaoshi, version);


        byte[] actiontype= WriteData.int2Bytes(packet.actiontype, 1);
        byte[] cmdLength=WriteData.int2Bytes(packet.cmd.getBytes().length, 1);

        byte[] hebing2=  byteMerger(actiontype, cmdLength);



        byte[] packetuid=new byte[36];
        packetuid=packet.uid.getBytes();

        byte[] packettouid=new byte[36];
        packettouid=packet.uid.getBytes();

        byte[] hebing3=  byteMerger(packetuid, packettouid);


        byte[] packettime=DataUtil.longtoLH(packet.time);
        byte[] ostype=WriteData.int2Bytes(SocketGlobal.ostype, 1);

        byte[] hebing4= byteMerger(packettime, ostype);


        byte[] dataLength=new byte[4];
        int count = packet.encodeArgs().getBytes().length +  packet.cmd.getBytes().length;
        dataLength=DataUtil.inttoLH(count);

        //总共90个字节 为协议 剩下位数据

        byte[] cmdAnddata = DataUtil.byteMerger(packet.cmd.getBytes(), packet.encodeArgs().getBytes());

        byte[] hebing5= byteMerger(dataLength, cmdAnddata);

        //组合成一个字节数组

        byte[] merger1= byteMerger(hebing1,hebing2);
        byte[] merger2=byteMerger(merger1,hebing3);
        byte[] merger3=byteMerger(merger2,hebing4);
        //最后字节
        byte[] merger4=byteMerger(merger3,hebing5);


        try {
            if(shuntNetty!=null)
            shuntNetty.sendMessage(merger4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    //java 合并两个byte数组
    public  byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}
