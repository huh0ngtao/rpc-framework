package com.code.rpc.registry;

import java.io.Serializable;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-26 14:29
 */
public class ServiceInfo implements Serializable {

    private String host;

    private Integer port;

    public ServiceInfo() {
    }

    public ServiceInfo(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
