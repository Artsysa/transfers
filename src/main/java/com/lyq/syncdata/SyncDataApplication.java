package com.lyq.syncdata;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class SyncDataApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SyncDataApplication.class).headless(false).run(args);
    }

}
