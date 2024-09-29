package com.code.rpc.proxy;

import com.code.rpc.registry.Registry;
import com.code.rpc.registry.ServiceInfo;
import com.code.rpc.transport.Client;
import org.apache.curator.x.discovery.ServiceInstance;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-26 14:45
 */
public class RpcProxy implements InvocationHandler {

    private Registry<ServiceInfo> registry;

    private String serviceName;

    private Client client;

    public RpcProxy(Registry<ServiceInfo> registry, String serviceName, Client client) {
        this.registry = registry;
        this.serviceName = serviceName;
        this.client = client;
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 注册中心查找服务
        List<ServiceInstance> serviceInstances = registry.lookup(serviceName);
        ServiceInstance<ServiceInfo> serviceInstance =
                serviceInstances.get(ThreadLocalRandom.current().nextInt(serviceInstances.size()));
        System.out.println("call");
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParamTypes(method.getParameterTypes());
        rpcRequest.setParameters(args);
        Object result = call(serviceInstance.getPayload(), rpcRequest);
        return result;
    }

    private Object call(ServiceInfo serviceInfo, RpcRequest request) {
        // 发送请求
        Object data = client.send(serviceInfo, request);
        return data;
    }
}
