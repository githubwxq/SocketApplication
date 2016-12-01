package com.example.nettyclientandroid.exapmle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class MyClientHandler extends SimpleChannelInboundHandler<String> {
	private Context ctx;

	public MyClientHandler(Context context) {
		this.ctx = context;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		Log.d("MyHelloClientHandler", "channelRead0->msg="+msg);
		Looper.prepare();
		Toast.makeText(this.ctx, "qweqweqwe" + msg, Toast.LENGTH_SHORT).show();
		Looper.loop();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Client active");
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Client close ");
		super.channelInactive(ctx);
	}

}
