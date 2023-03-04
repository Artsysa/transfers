package com.lyq.syncdata.netty;

import com.lyq.syncdata.service.BigDataService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * created by lyq
 */
@Component
public class NettyServerConfig {


    private final RequestManager requestManager;

    private final ThreadPoolExecutor commonThreads;

    private final BigDataService bigDataService;


    public NettyServerConfig() {
        requestManager = new RequestManager();
        bigDataService = new BigDataService();
        commonThreads  =  new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors(), 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20000),
                (r) -> {
                    Thread thread = new Thread(r);
                    thread.setName("commonThreads");
                    return thread;
                }
        );
        startNettyServer();
    }

    public void startNettyServer(){
        NioEventLoopGroup work = new NioEventLoopGroup();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() { //添加处理器
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new CommandDecoder()); //
                        ch.pipeline().addLast(new CommandEncoder()); //
                        ch.pipeline().addLast(new SyncDataServerHandler(commonThreads, requestManager, bigDataService)); //
                    }
                })
                .bind(8787);
    }
}
