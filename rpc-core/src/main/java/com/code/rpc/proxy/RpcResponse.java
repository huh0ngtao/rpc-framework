package com.code.rpc.proxy;

import java.io.Serializable;

/**
 * @description: TODO
 * @author: huhongtao
 * @date: 2024-09-26 14:53
 */
public class RpcResponse<T> implements Serializable {

    private Long requestId;

    private Integer code;

    private String message;

    private T data;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "requestId=" + requestId +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
