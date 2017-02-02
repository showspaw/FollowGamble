package com.heros.follow.SocketServer;

import com.heros.follow.SocketServer.Command.collection.CommandController;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetAddress;

/**
 * Created by Show on 2017/1/23.
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    public void run() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new EchoServerInitializer());

            Channel channel = bootstrap.bind(port).sync().channel();
            channel.closeFuture().sync();
        } finally {
            close();
        }
    }

    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    private class EchoServerHandler extends SimpleChannelInboundHandler<String> {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName()+"\r\n");
            ctx.flush();
            super.channelActive(ctx);
        }
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String request) throws Exception {
            System.out.println(channelHandlerContext.channel().remoteAddress()+":"+request);
            boolean close = false;
            CommandController commandController = new CommandController();
            String response=commandController.call(request);
            ChannelFuture future = channelHandlerContext.write("\r\n"+response+"\r\n");
            channelHandlerContext.flush();
            if(request.trim().equals("exit"))
                close = true;
            if (close) {
                future.addListener(ChannelFutureListener.CLOSE);
            }

        }

    }

    private class EchoServerInitializer extends ChannelInitializer<SocketChannel> {
        private final StringDecoder decoder = new StringDecoder();
        private final StringEncoder encoder = new StringEncoder();
        @Override
        public void initChannel(SocketChannel channel) {
            channel.pipeline()
                                .addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
                                .addLast(decoder)
                                .addLast(encoder)
                                .addLast(new EchoServerHandler());
        }
    }
}
