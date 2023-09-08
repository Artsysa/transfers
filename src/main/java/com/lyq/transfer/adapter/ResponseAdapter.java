package com.lyq.transfer.adapter;

import com.alibaba.fastjson.JSON;
import com.lyq.transfer.constant.CommandConsts;
import com.lyq.transfer.pojo.Response;

import java.util.Objects;

public class ResponseAdapter {

    public static Response success(Object object){
        return buildResponse(CommandConsts.COMMAND_RESPONSE_SUCCESS, object);
    }

    public static Response fail(Object object){
        return buildResponse(CommandConsts.COMMAND_RESPONSE_ERROR, object);
    }

    public static Response otherFail(CommandConsts commandConsts, Object object){
        return buildResponse(commandConsts, object);
    }

    private static Response buildResponse(CommandConsts commandConsts, Object object){
        Response response = new Response();
        response.setCode(commandConsts.getCode());
        if(Objects.nonNull(object)){
            response.setBody(JSON.toJSONBytes(object));
        }
        return response;
    }
}
