package learn.wxq.socketapplication.socketservice;


import learn.wxq.socketapplication.socketservice.PacketModel.RePacket;

/**
 * Created by Vincent on 2016/3/2.
 */
public interface DataParseListener {


    public void connectionClosed(int socketState);
    public void loadingReconnect(int socketState);
    public void connectionClosedOnError(Exception e);

    public void reconnectionSuccessful(int socketState);

    public void dataReceiveSuccessful(RePacket rePacket);

    public void cancleAsyncTask();
}
