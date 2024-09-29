package com.code.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-27 14:19
 */
public interface ServiceProvider<T> {

    void addService(String serviceName, Class clazz);

    Object getService(String serviceName);

    void publicService(String name, Class clazz, InetSocketAddress address);

}
