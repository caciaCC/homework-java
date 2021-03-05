package com.db.homework.service;

import java.util.Set;

public interface RedisService {
    // 设置带过期时间的缓存
    public void set(String key, Object value, long time);
    // 设置缓存
    public void set(String key, Object value);
    // 根据 key 获得缓存
    public Object get(String key);
    // 根据 key 删除缓存
    public Boolean delete(String key);
    // 根据 keys 集合批量删除缓存
    public Long delete(Set<String> keys);
    // 根据正则表达式匹配 keys 获取缓存
    public Set<String> getKeysByPattern(String pattern);
}
