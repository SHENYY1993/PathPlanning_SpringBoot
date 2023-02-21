package com.shenyy.pretendto.core.biz;

public interface RedisBiz {
    public void set(String key, Object value);

    public Object get(String key);

    public void delete(String key);
}
