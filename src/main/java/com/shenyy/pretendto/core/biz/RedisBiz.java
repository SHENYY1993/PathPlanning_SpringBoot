package com.shenyy.pretendto.core.biz;

public interface RedisBiz {
    Object executeRedisCliCommand(String command, byte[]... var2);

    void set(String key, Object value);

    void setIfAbsent(String key, Object value);

    void setIfPresent(String key, Object value);

    Object get(String key);

    void delete(String key);
}
