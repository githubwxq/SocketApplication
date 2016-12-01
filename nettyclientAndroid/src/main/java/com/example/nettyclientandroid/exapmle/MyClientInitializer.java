package com.example.nettyclientandroid.exapmle;

import java.nio.charset.Charset;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import android.content.Context;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
	private Context context;
	public MyClientInitializer(Context ctx){
		this.context = ctx;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		/**
		 * ����ط��ı���ͷ���˶�Ӧ�ϡ������޷���������ͱ���
		 */
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
		pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
		//�ͻ��˵��߼�
		pipeline.addLast("handler",new MyClientHandler(context));
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println("---channelRead--- msg="+msg);
		super.channelRead(ctx, msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("---channelReadComplete---");
		super.channelReadComplete(ctx);
	}
	
}
