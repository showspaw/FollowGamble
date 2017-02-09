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
import java.util.concurrent.TimeUnit;

/**
 * Created by root on 2017/2/7.
 */
public class Client {
    public class ConnectionListener implements ChannelFutureListener {
        private Client client;

        public ConnectionListener(Client client) {
            this.client = client;
        }

        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            if (!channelFuture.isSuccess()) {
                System.out.println("Reconnect");
                final EventLoop loop = channelFuture.channel().eventLoop();
                loop.schedule(new Runnable() {
                    @Override
                    public void run() {
                        client.createBootstrap(new Bootstrap(), loop);
                    }
                }, 1L, TimeUnit.SECONDS);
            }
        }
    }

    private EventLoopGroup loop = new NioEventLoopGroup();
    private Channel channel;
    private Thread thread;
    public Bootstrap createBootstrap(Bootstrap bootstrap, EventLoopGroup eventLoop) {
        if (bootstrap != null) {
            EchoClientHandler handler = new EchoClientHandler(this);
            bootstrap
                    .group(eventLoop)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
                                    .addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(handler);
                        }
                    });
            channel = bootstrap.connect("localhost", 2234).addListener(new ConnectionListener(this)).channel();
//            createController();
        }
        return bootstrap;
    }

    public void createController(Channel ch) {
        thread = new Thread() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                try {
                    for (; ; ) {
                        String line = in.readLine();
                        if (line == null) {
                            continue;
                        }
                /*
                 * 向服务端发送在控制台输入的文本 并用"\r\n"结尾
                 * 之所以用\r\n结尾 是因为我们在handler中添加了 DelimiterBasedFrameDecoder 帧解码。
                 * 这个解码器是一个根据\n符号位分隔符的解码器。所以每条消息的最后必须加上\n否则无法识别和解码
                 * */
                        ch.writeAndFlush(line + "\n");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        thread.run();
    }

    public void run() {
        createBootstrap(new Bootstrap(), loop);
    }

    public static void main(String[] args) throws Exception {
        new Client().run();
    }

    private class EchoClientHandler extends SimpleChannelInboundHandler<String> {
        private Client client;

        public EchoClientHandler(Client client) {
            this.client = client;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
            System.out.println("Server reply:" + s);
//            channelHandlerContext.channel().writeAndFlush(s + "\r\n");
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
//            createController(ctx.channel());
            System.out.println(ctx.channel().localAddress() + " active.");
//            thread.run();
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            if(thread!=null)
            thread.interrupt();
            System.out.println(ctx.channel().localAddress() + " inactive.");
            final EventLoop eventLoop = ctx.channel().eventLoop();
            eventLoop.schedule(new Runnable() {
                @Override
                public void run() {
                    client.createBootstrap(new Bootstrap(), eventLoop);
                }
            }, 1L, TimeUnit.SECONDS);
//            thread.interrupt();
            super.channelInactive(ctx);
        }
    }
}
