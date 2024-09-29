package com.code.rpc.registry;

import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;

public interface Registry<T> {

    void register(ServiceInstance serviceInstance);

    void unregister(ServiceInstance serviceInstance);

    List<ServiceInstance> lookup(String name);

}
