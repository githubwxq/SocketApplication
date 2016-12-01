package learn.wxq.socketapplication.netty;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by zpp on 2015/9/23.
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<Object> {
	//设置心跳时间  开始
    public static final int MIN_CLICK_DELAY_TIME = 100*5;
    private long lastClickTime =0;
    //设置心跳时间   结束
	
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
                         ByteBuf bb = Unpooled.wrappedBuffer("ping".getBytes(CharsetUtil.UTF_8));
                         ctx.writeAndFlush(bb);
                         System.out.println("send ping to server----------");
                     }
                    break;
                default:
                    break;
            }
        }
    }
    //这里是接受服务端发送过来的消息
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object baseMsg) throws Exception {
//        ByteBuf buf  = (ByteBuf) baseMsg;
//        byte[] bytes = new byte[ buf.readableBytes()];
//        buf.readBytes(bytes);

        String msg1=((ByteBuf)baseMsg).toString(CharsetUtil.UTF_8).trim();
        System.out.println("1111111111111111------------------------"+msg1);
        ReferenceCountUtil.release(msg1);
    }
    NettyClientBootstrap nettyClient=new NettyClientBootstrap();
    
    //这里是断线要进行的操作
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("重连了。---------");
        nettyClient.startNetty();
        //ctx.channel().eventLoop().schedule();
    }
    //这里是出现异常的话要进行的操作
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
		super.exceptionCaught(ctx, cause);
		System.out.println("出现异常了。。。。。。。。。。。。。");
		cause.printStackTrace();
	}

	

}
