package learn.wxq.socketapplication.socketservice;



import java.io.IOException;
import java.io.OutputStream;

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
                        os.write(int2Bytes(SocketGlobal.ostype,1));
                        int count = finalArgs.getBytes().length + cmdByte.length;
                        os.write(DataUtil.inttoLH(count));
                        byte[] cmdAnddata = DataUtil.byteMerger(cmdByte, finalArgs.getBytes());
                        os.write(cmdAnddata);

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
}
