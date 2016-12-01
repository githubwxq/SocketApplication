package com.example.nettyclientandroid.exapmle;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.nettyclientandroid.R;

public class ClientActivity extends Activity {
	private static final String TAG = "MainActivity";
	public static final String HOST = "192.168.50.110";
	public static int PORT = 7878;
	private NioEventLoopGroup group;
	private Button sendButton;
	private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
	//	sendButton = (Button) findViewById(R.id.netty_send_button);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				connected();
			}
		});
	}

	// ���ӵ�Socket�����
	private void connected() {
		new Thread() {
			@Override
			public void run() {
				 group = new NioEventLoopGroup();
				try {
					// Client���������� 3.x��ClientBootstrap
					// ��ΪBootstrap���ҹ��캯���仯�ܴ��������޲ι��졣
					Bootstrap bootstrap = new Bootstrap();
					// ָ��channel����
					bootstrap.channel(NioSocketChannel.class);
					// ָ��Handler
					bootstrap.handler(new MyClientInitializer(context));
					// ָ��EventLoopGroup
					bootstrap.group(group);
					// ���ӵ����ص�8000�˿ڵķ����
					Channel channel = bootstrap.connect(new InetSocketAddress(HOST,
											PORT)).sync().channel();
					channel.writeAndFlush("���ǿͻ��ˣ����ǿͻ��ˣ�\r\n");
					channel.read();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(group!=null){
			group.shutdownGracefully();
		}
	}
	
}
