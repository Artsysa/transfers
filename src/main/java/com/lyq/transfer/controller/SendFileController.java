package com.lyq.transfer.controller;

import com.lyq.transfer.netty.service.ClientManager;
import com.lyq.transfer.pojo.SendFileVO;
import com.lyq.transfer.service.DownloadFileService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/send")
public class SendFileController {


    @RequestMapping(value = "/getClient", method = RequestMethod.GET)
    public String getClientId(){
        return ClientManager.getCurrentClientInfo();
    }

    @RequestMapping(value = "/sendFile", method = RequestMethod.POST)
    public String sendClientFile(@RequestBody SendFileVO sendFileVO){
        ChannelHandlerContext clientChannel = ClientManager.getClientChannel(sendFileVO.getClientId());
        if(clientChannel == null ){
            clientChannel = ClientManager.getDefaultClientId();
        }
        if(clientChannel != null && clientChannel.channel().isActive()){
            DownloadFileService.doUploadIncrement(sendFileVO.getPaths(), clientChannel);
        }
        return "客户端已下线";
    }
}
