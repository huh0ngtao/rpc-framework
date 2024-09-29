package com.code.rpc.registry;

import org.apache.curator.x.discovery.ServiceInstance;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-27 14:26
 */
public class ZkServiceProviderImpl implements ServiceProvider {

    private Map<String, Object> serviceMap;

    private Registry registry;

    public ZkServiceProviderImpl() {
        this.serviceMap = new HashMap<>();
        this.registry = new ZookeeperRegistry();
    }

    @Override
    public void addService(String serviceName, Class clazz) {
        serviceMap.put(serviceName, clazz);
    }

    @Override
    public Object getService(String serviceName) {
        return serviceMap.get(serviceName);
    }

    @Override
    public void publicService(String name, Class clazz, InetSocketAddress address) {
        this.addService(name, clazz);
    }

}
