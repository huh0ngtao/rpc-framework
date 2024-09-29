package com.code.rpc.transport;

import com.code.rpc.proxy.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-27 14:34
 */
public class RpcClientHandler extends ChannelInboundHandlerAdapter {

    Map<Long, CompletableFuture> map;

    public RpcClientHandler(Map<Long, CompletableFuture> map) {
        this.map = map;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("read");
        System.out.println(msg.toString());
        if (msg instanceof RpcResponse) {
            try {
                RpcResponse response = (RpcResponse) msg;
                CompletableFuture future = map.get(response.getRequestId());
                future.complete(response.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
