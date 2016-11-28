package learn.wxq.socketapplication.socketservice;



import java.util.concurrent.LinkedBlockingQueue;

import learn.wxq.socketapplication.socketservice.PacketModel.RePacket;

/**
 * Created by Vincent on 2016/2/27.
 */
public class InfoLinkedBlockingQueue extends LinkedBlockingQueue {
    public Thread writeThread;
    private DataParseListener dataParseListener;
    private int n;

    public InfoLinkedBlockingQueue(DataParseListener dataParseListener) {
        this.dataParseListener = dataParseListener;
        writeThread = new Thread(new ReadPacket(this));
    }

    private void dataWith(RePacket repacket) {
        if (dataParseListener != null && repacket != null) {
            dataParseListener.dataReceiveSuccessful(repacket);
        }

    }

    public class ReadPacket implements Runnable {

        public ReadPacket(InfoLinkedBlockingQueue readp) {

        }

        @Override
        public void run() {
            while (true) {
                RePacket repacket = null;
                try {
                    repacket = (RePacket) InfoLinkedBlockingQueue.this.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (repacket != null) {
                    if (repacket.cmd.equals("SocketException")) {
                        break;
                    } else {
                        dataWith(repacket);
                    }
                }
            }
        }
    }

    public void deleteData() {
        if (writeThread != null) {
            writeThread = null;
        }
    }


}
