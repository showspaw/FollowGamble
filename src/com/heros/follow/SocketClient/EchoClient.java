package com.heros.follow.SocketClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by root on 2017/2/7.
 */
public class EchoClient {
    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private String host;
    private int port;
    private Channel channel;
    public void run() throws Exception{
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootStrap =new Bootstrap();
            bootStrap.group(workGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(new LoggingHandler(LogLevel.INFO))
            .handler(new EchoClientInitializer());
//            ChannelFuture channelFuture = bootStrap.connect(host, port).sync();
//            channelFuture.channel().closeFuture().sync();
            // 连接服务端
//            channel = bootStrap.connect(host, port).sync().channel();
//            do {
//                channel.writeAndFlush("710" + "\n");
//            } while (true);

            // 控制台输入
//            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//            for (;;) {
//                String line = in.readLine();
//                if (line == null) {
//                    continue;
//                }
//                /*
//                 * 向服务端发送在控制台输入的文本 并用"\r\n"结尾
//                 * 之所以用\r\n结尾 是因为我们在handler中添加了 DelimiterBasedFrameDecoder 帧解码。
//                 * 这个解码器是一个根据\n符号位分隔符的解码器。所以每条消息的最后必须加上\n否则无法识别和解码
//                 * */
//                channel.writeAndFlush(line + "\n");
//            }
        }finally {
            workGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        EchoClient echoClient=new EchoClient("127.0.0.1",2234);
        echoClient.run();
    }

    private class EchoClientHandler extends SimpleChannelInboundHandler<String>   {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
            System.out.println("Server reply:"+s);
//            channelHandlerContext.channel().writeAndFlush(s + "\r\n");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println(ctx.channel().localAddress()+" active.");
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println(ctx.channel().localAddress()+" inactive.");
            super.channelInactive(ctx);
        }
    }

    private class EchoClientInitializer extends ChannelInitializer<SocketChannel> {
        private final StringDecoder decoder = new StringDecoder();
        private final StringEncoder encoder = new StringEncoder();
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline()
                        .addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
                        .addLast(decoder)
                        .addLast(encoder)
                        .addLast(new EchoClientHandler());
            }
    }
}
