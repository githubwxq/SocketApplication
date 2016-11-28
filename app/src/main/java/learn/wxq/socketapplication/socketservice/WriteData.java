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
                        os.write(SocketGlobal.TITLE);
                        os.write(DataUtil.shorttoLH(SocketGlobal.version));
                        os.write(DataUtil.char2H(packet.actiontype));
                        byte[] cmdByte = packet.cmd.getBytes();
                        os.write(cmdByte.length);
                        os.write(packet.uid.getBytes());
                        os.write(packet.toUid.getBytes());
//                      os.write(DataUtil.longtoLH(packet.time));
                        os.write(DataUtil.longtoLH(packet.time));
                        os.write(SocketGlobal.ostype);
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
