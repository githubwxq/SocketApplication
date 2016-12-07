package learn.wxq.socketapplication.netty;


import android.content.Context;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import learn.wxq.socketapplication.socketservice.DataParseListener;
import learn.wxq.socketapplication.socketservice.PacketModel.NetworkRequestUsingHttpDNS;
import learn.wxq.socketapplication.socketservice.SocketGlobal;

/**
 * Created by zpp on 2015/9/23.
 */
public class NettyClientBootstrap {
    private int port=9090; //bobo
    private Context context;
   // private String host="58.211.191.72";
    private String host="192.168.0.116";  //bobo
    private NettyClientHandler.DataListener dataListener;
    public SocketChannel socketChannel;
    public EventLoopGroup eventLoopGroup;
    public Bootstrap bootstrap;
    ChannelFuture future = null ;
    NettyClientHandler nettyClientHandler;
    public boolean isneedConnect=true;
   private boolean flag;//默认是逻辑socket 为 false  风流的为true

    private DataParseListener dataParseListener;
    public  void startNetty() throws InterruptedException {
        System.out.println("长链接开始");
        if(start()){
            System.out.println("长链接成功");

        }else{

            System.out.println("长链接失败");
        }
    }
    public NettyClientBootstrap(Context context ,String host,int port,NettyClientHandler.DataListener listener) {
        this.context = context;
        this.port=port;
        this.host=host;
        dataListener=listener;//回调数据




    }
     public NettyClientBootstrap(Context context ,String host,int port,NettyClientHandler.DataListener listener, DataParseListener dataParseListener,boolean flag) {
         this.context = context;
         this.port=port;
         this.host=host;
         dataListener=listener;//回调数据
         this.dataParseListener=dataParseListener;
         this.flag=flag;

    }

     public NettyClientBootstrap() {

     }

    public void sendMessage(final byte[]  msg)throws InterruptedException {
        System.out.println("发送各种消息信息");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                try {
                    if(socketChannel==null){

                        if(start()){
                            ByteBuf bb = Unpooled.wrappedBuffer(msg);
                            socketChannel.writeAndFlush(bb);
                        }
                    }else{

                        ByteBuf bb = Unpooled.wrappedBuffer(msg);
                        socketChannel.writeAndFlush(bb);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//        }
//        }).start();
    }


    public Boolean start() throws InterruptedException {
        eventLoopGroup=new NioEventLoopGroup();
        bootstrap=new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(host, port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new IdleStateHandler(20, 10, 0));  //配置心跳间隔
                /*socketChannel.pipeline().addLast(new ObjectEncoder());
                socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));*/
                nettyClientHandler = new NettyClientHandler(NettyClientBootstrap.this, dataListener);
                socketChannel.pipeline().addLast(nettyClientHandler);
            }
        });

        try {
            future =bootstrap.connect(new InetSocketAddress(host,port)).sync();

            if (future.isSuccess()) {
                socketChannel = (SocketChannel)future.channel();
                System.out.println("connect server  成功---------");
                return true;
            }else{
                System.out.println("connect server  失败---------");
                if(isneedConnect)
                startNetty();
                return false;
            }
        } catch (Exception e) {
            System.out.println("无法连接----------------");
             if(isneedConnect)
             startNetty();
             return false;
        }
    }

public void stopPing(){
    nettyClientHandler.setIsPing(false);

}

   public void  closeConnection(){

       socketChannel.closeFuture();
       eventLoopGroup.shutdownGracefully();
       socketChannel.close();
    }

    public void closeChannel() {
        if (socketChannel != null) {
            socketChannel.close();
        }
    }

    public boolean isOpen() {
        if (socketChannel != null) {
            System.out.println(socketChannel.isOpen());
            return socketChannel.isOpen();
        }
        return false;
    }

}