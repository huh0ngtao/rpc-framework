package com.code.rpc.codec;

import com.code.rpc.seialization.HessianSerialization;
import com.code.rpc.seialization.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-27 11:19
 */
public class RpcRequestEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object data, ByteBuf byteBuf) throws Exception {
        System.out.println("request encode");
        Serialization serialization = new HessianSerialization();
        byte[] bytes = serialization.serialize(data);
        byteBuf.writeBytes(bytes);
    }
}
