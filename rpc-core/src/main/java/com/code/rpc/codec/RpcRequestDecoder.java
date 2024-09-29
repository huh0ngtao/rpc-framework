package com.code.rpc.codec;

import com.code.rpc.proxy.RpcRequest;
import com.code.rpc.proxy.RpcResponse;
import com.code.rpc.seialization.HessianSerialization;
import com.code.rpc.seialization.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-27 13:39
 */
public class RpcRequestDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("request decode");
        int i = byteBuf.readableBytes();
        byte[] bs = new byte[i];
        byteBuf.readBytes(bs);

        Serialization serialization = new HessianSerialization();
        RpcRequest rpcRequest = serialization.deserialize(bs, RpcRequest.class);
        list.add(rpcRequest);
    }
}
