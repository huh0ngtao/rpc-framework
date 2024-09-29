package com.code.rpc.registry;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.zookeeper.CreateMode;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-26 13:49
 */
@Slf4j
public class ZookeeperRegistry<T> extends AbstractRegistry {

    String root = "rpc";

    String address = "127.0.0.1:2181";

    private ServiceDiscovery<ServiceInfo> serviceDiscovery;

    private ServiceCache<ServiceInfo> serviceCache;

    public ZookeeperRegistry() {
        CuratorFramework client = CuratorFrameworkFactory.newClient(address, new ExponentialBackoffRetry(1000, 3));
        client.start();

        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class).client(client).basePath(root).build();
        try {
            serviceDiscovery.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        serviceCache = serviceDiscovery.serviceCacheBuilder()
                .name("/helloService")
                .build();
        try {
            serviceCache.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(ServiceInstance serviceInstance) {
        try {
            serviceDiscovery.registerService(serviceInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unregister(ServiceInstance serviceInstance) {
        try {
            serviceDiscovery.unregisterService(serviceInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ServiceInstance> lookup(String name) {
        System.out.println("xx");
        List<ServiceInstance<ServiceInfo>> collect = serviceCache.getInstances().stream().collect(Collectors.toList());
        System.out.println(collect.toString());
        log.info("{}", collect.toString());
        return serviceCache.getInstances().stream()
                .filter(s -> s.getName().equals(name))
                .collect(Collectors.toList());
    }
}
