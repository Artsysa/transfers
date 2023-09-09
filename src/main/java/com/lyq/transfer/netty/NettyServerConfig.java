package com.lyq.transfer.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * created by lyq
 */
public class NettyServerConfig {

    public static void main(String[] args) {
        startNettyServer();
    }

    public static void startNettyServer(){
        NioEventLoopGroup work = new NioEventLoopGroup();
        NioEventLoopGroup boss = new NioEventLoopGroup(4);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() { //添加处理器
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(
                                new CommandDecoder(),
                                new CommandEncoder(),
                                new CommonHandler()
                        );
                    }
                })
                .bind(14352);
    }

}
