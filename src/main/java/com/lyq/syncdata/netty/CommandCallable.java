package com.lyq.syncdata.netty;

import com.lyq.syncdata.pojo.SyncDataCommand;

import java.io.IOException;

public interface CommandCallable {

    void invoke(SyncDataCommand command) throws IOException;
}
