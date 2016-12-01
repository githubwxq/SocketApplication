package learn.wxq.socketapplication.socketservice;

/**
 * Created by Vincent on 2016/3/1.
 */
public class SocketGlobal {

//    public static final String HOST = "58.211.191.72";
//    public static String HOST = "58.211.191.72";//im.imexue.com
    public static String HOST = "im.imexue.com";

 //   public static final int PORT = 12345;
    public static final int PORT = 10000;//测试 10000    bobo  9090


    public static final int CONNECT_TIMEOUT = 120 * 1000;// socket连接超时设置
    public static final int SO_TIMEOUT = 350 * 1000;// socket请求超时设置
    public static final int KEEP_ALIVE =30 * 1000;// 发心跳包时间间隔

    public static final int TITLE=170;
    public static  int version=1;
    public static  int ostype=1;

    public static boolean flag=false;
}
