package com.code.rpc.seialization;

public interface Serialization {

    <T> byte[] serialize(T data) throws Exception;

    <T> T deserialize(byte[] data, Class<T> clazz) throws Exception;

}
