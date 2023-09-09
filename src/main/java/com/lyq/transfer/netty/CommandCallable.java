package com.lyq.transfer.netty;


import com.lyq.transfer.pojo.Command;

public interface CommandCallable {

    void invoke(Command command);
}
