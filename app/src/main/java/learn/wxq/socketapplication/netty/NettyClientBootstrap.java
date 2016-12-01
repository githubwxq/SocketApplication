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
import learn.wxq.socketapplication.socketservice.PacketModel.NetworkRequestUsingHttpDNS;
import learn.wxq.socketapplication.socketservice.SocketGlobal;

/**
 * Created by zpp on 2015/9/23.
 */
public class NettyClientBootstrap {
    private int port=10000;
    private Context context;
 // private String host="58.211.191.72";
 private String host="im.exiaoxin.com";

    public SocketChannel socketChannel;
    public  void startNetty() throws InterruptedException {
        System.out.println("长链接开始");
        if(start()){
            System.out.println("长链接成功");
            ByteBuf bb = Unpooled.wrappedBuffer(("tableIP=asdf".getBytes(CharsetUtil.UTF_8)));
            socketChannel.writeAndFlush(bb);
        }
    }
     public NettyClientBootstrap(Context context) {
        this.context = context;
    }
    NettyClientBootstrap() {

    }

    public void sendMessage(String msg){

            System.out.println("长链接成功");
            ByteBuf bb = Unpooled.wrappedBuffer((msg.getBytes(CharsetUtil.UTF_8)));
            socketChannel.writeAndFlush(bb);

    }


    private Boolean start() throws InterruptedException {

      String host = NetworkRequestUsingHttpDNS.mainSocket(context, SocketGlobal.HOST); // 获取ip
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(host, port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new IdleStateHandler(20, 10, 0));
                /*socketChannel.pipeline().addLast(new ObjectEncoder());
                socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));*/
                socketChannel.pipeline().addLast(new NettyClientHandler());
            }
        });
        ChannelFuture future = null ;
        try {
            future =bootstrap.connect(new InetSocketAddress(host,port)).sync();
            if (future.isSuccess()) {
                socketChannel = (SocketChannel)future.channel();
                System.out.println("connect server  成功---------");
                return true;
            }else{
                System.out.println("connect server  失败---------");
                startNetty();
                return false;
            }
        } catch (Exception e) {
            System.out.println("无法连接----------------");
            startNetty();
            return false;
        }
    }
}