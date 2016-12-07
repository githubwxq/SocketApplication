package learn.wxq.socketapplication.netty;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import learn.wxq.socketapplication.socketservice.PacketModel.HeartbeatPingReq;
import learn.wxq.socketapplication.socketservice.PacketModel.Packet;
import learn.wxq.socketapplication.socketservice.WriteData;

/**
 * Created by zpp on 2015/9/23.
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<Object> {
	//设置心跳时间  开始
    public static final int MIN_CLICK_DELAY_TIME = 100*5;
    private long lastClickTime =0;
    private DataListener dataListener;
    //设置心跳时间   结束
    NettyClientBootstrap nettyClient;

    public void setIsPing(boolean isPing) {
        this.isPing = isPing;
    }

    boolean isPing=true;

    public NettyClientHandler(NettyClientBootstrap netty,DataListener listener) {
        nettyClient =netty;
        this.dataListener=listener;
    }

    //利用写空闲发送心跳检测消息
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                	 long currentTime = System.currentTimeMillis();
                     if(currentTime - lastClickTime > MIN_CLICK_DELAY_TIME){
                         lastClickTime = System.currentTimeMillis();
                         if(isPing){

                             String uid = "000000000000000000000000000000000000";
                             Packet heartbeatPacket = new HeartbeatPingReq(uid, uid, "");
                           byte[] heart=  WriteData.getInstance().nettyEncodeByte(heartbeatPacket);

                         ByteBuf bb = Unpooled.wrappedBuffer(heart);
                         ctx.writeAndFlush(bb);
                         System.out.println("send ping to server----------");}
                     }
                    break;
                default:
                    break;
            }
        }
    }
    //这里是接受服务端发送过来的消息
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object baseMsg) throws Exception {
        ByteBuf buf  = (ByteBuf) baseMsg;
        byte[] bytes = new byte[ buf.readableBytes()];
        buf.readBytes(bytes);
//        String msg1=((ByteBuf)baseMsg).toString(CharsetUtil.UTF_8).trim();
//        dataListener.dealWithData("data:" + msg1);
        dataListener.dealWithByteData(bytes);
        ReferenceCountUtil.release(bytes);
    }

    
    //这里是断线要进行的操作
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
  //      System.out.println("重连了。---------");
//        if(isPing){
//        nettyClient.startNetty();}else{
//            ctx.channel().close();
//        }
        //
    }
    //这里是出现异常的话要进行的操作
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
		super.exceptionCaught(ctx, cause);
        if(isPing){  //l逻辑服务器出现的异常

        }
        System.out.println("出现异常了。。。。。。。。。。。。。");
        Channel channel = ctx.channel();
        if(channel.isActive())ctx.close();
	//	cause.printStackTrace();
	}

	public interface DataListener{
       void  dealWithData(String data);
        void  dealWithByteData(byte[] data);


    }


    public DataListener getDataListener() {
        return dataListener;
    }

    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }

}
