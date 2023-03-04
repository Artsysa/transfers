package com.lyq.syncdata.netty;

import com.lyq.syncdata.constant.CommandEnum;
import com.lyq.syncdata.pojo.SyncDataCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestManager {
    private static final Logger log = LoggerFactory.getLogger("RequestManager");

    private final AtomicInteger commandIdIncr = new AtomicInteger(0);
    private final Map<Integer, RequestFuture> requestFutureMap;
    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public RequestManager() {
        this.requestFutureMap = new ConcurrentHashMap<>();
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2, (r) -> {
            Thread thread = new Thread(r);
            thread.setName("scheduled-requestManager");
            return thread;
        });
        clean();
    }

    public RequestFuture getByCommandId(Integer id){
        return requestFutureMap.get(id);
    }

    public void put(Integer commandId, RequestFuture requestFuture){
        requestFutureMap.put(commandId, requestFuture);
    }

    public void remove(Integer id){
        requestFutureMap.remove(id);
    }

    public void asyncInvoke(Runnable runnable){
        scheduledThreadPoolExecutor.execute(runnable);
    }

    public void clean(){
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            int i = 0;
            for (Map.Entry<Integer, RequestFuture> entry : requestFutureMap.entrySet()) {
                RequestFuture requestFuture = entry.getValue();
                Long createTime = requestFuture.getCreateTime();
                if (createTime != null && (System.currentTimeMillis() - createTime) > 60000) {
                    //超时
                    i++;
                    asyncInvoke(() -> {
                        SyncDataCommand syncDataCommand = new SyncDataCommand();
                        syncDataCommand.setCode(CommandEnum.TIMEOUT.getCode());
                        requestFuture.setResponse(syncDataCommand);
                        asyncInvoke(() -> {
                            try {
                                requestFuture.invoke();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        });
                    });
                    requestFutureMap.remove(entry.getKey());
                }
            }
            if(i > 0){
                log.info("当前超时请求数：{}" , i);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    public AtomicInteger getCommandIdIncr() {
        return commandIdIncr;
    }
}
