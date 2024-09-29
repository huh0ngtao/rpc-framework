package com.code.rpc.transport;

import com.code.rpc.factory.BeanManager;
import com.code.rpc.proxy.RpcRequest;
import com.code.rpc.proxy.RpcResponse;
import com.code.rpc.registry.ServiceProvider;
import com.code.rpc.registry.ZkServiceProviderImpl;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-27 14:03
 */
@Slf4j
public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg.toString());
        super.channelRead(ctx, msg);
        RpcRequest rpcRequest = (RpcRequest) msg;
        Object result;
        try {
            // 查找注册服务
            Object service = BeanManager.getBean(rpcRequest.getInterfaceName());
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());

            RpcResponse<Object> response = new RpcResponse<>();
            response.setRequestId(rpcRequest.getRequestId());
            response.setData(result);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage(), e);
        }
    }
}
