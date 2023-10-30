package com.lyq.transfer;

import com.lyq.transfer.netty.NettyServerConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * created by lyq
 */
@SpringBootApplication
public class TransferApplication {
  public static void main(String[] args) {
    NettyServerConfig.startNettyServer();
    new SpringApplicationBuilder(TransferApplication.class).headless(false).run(args);
  }

}
