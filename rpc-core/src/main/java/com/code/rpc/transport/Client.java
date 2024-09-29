package com.code.rpc.transport;

import com.code.rpc.codec.RpcRequestEncoder;
import com.code.rpc.codec.RpcResponseDecoder;
import com.code.rpc.proxy.RpcRequest;
import com.code.rpc.registry.ServiceInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-27 11:13
 */
@Slf4j
public class Client {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    private Map<Long, CompletableFuture> map = new ConcurrentHashMap<>();


    public Client() {
        // initialize resources such as EventLoopGroup, Bootstrap
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                //  The timeout period of the connection.
                //  If this time is exceeded or the connection cannot be established, the connection fails.
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // If no data is sent to the server within 15 seconds, a heartbeat request is sent
                        p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        p.addLast(new RpcResponseDecoder());
                        p.addLast(new RpcRequestEncoder());
                        p.addLast(new RpcClientHandler(map));
                    }
                });
    }

    public Object send(ServiceInfo serviceInfo, RpcRequest rpcRequest) {
        Channel channel = getChannel(serviceInfo);
        if (channel == null) {
            log.info("channel is null");
        }
        CompletableFuture completableFuture = new CompletableFuture<>();
        Object data = null;
        try {
            map.put(rpcRequest.getRequestId(), completableFuture);
            // 发送命令
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) channelFuture -> {
                // 处理发送失败的情况
                if (!channelFuture.isSuccess()) {
                    completableFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
            System.out.println("send end");
            data = completableFuture.get();
        } catch (Throwable t) {
            // 处理发送异常
            completableFuture.completeExceptionally(t);
        }
        return data;
    }

    private Channel getChannel(ServiceInfo serviceInfo) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(serviceInfo.getHost(),serviceInfo.getPort());
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("The client has connected [{}] successful!", inetSocketAddress.toString());
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        Channel channel = null;
        try {
            channel = completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return channel;
    }

}
