package com.code.rpc.codec;

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
public class RpcResponseDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("response decode");
        int i = byteBuf.readableBytes();
        byte[] bs = new byte[i];
        byteBuf.readBytes(bs);

        Serialization serialization = new HessianSerialization();
        RpcResponse response = serialization.deserialize(bs, RpcResponse.class);
        list.add(response);
    }
}
