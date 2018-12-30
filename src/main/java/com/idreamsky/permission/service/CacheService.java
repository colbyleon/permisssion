package com.idreamsky.permission.service;

import com.google.common.base.Joiner;
import com.idreamsky.permission.beans.CacheKeyConstants;
import com.idreamsky.permission.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * @Author: colby
 * @Date: 2018/12/30 10:46
 */
@Service
@Slf4j
public class CacheService {
    @Resource
    private RedisPool redisPool;

    public void saveCache(String toSaveValue, int timeoutSecond, CacheKeyConstants prefix) {
        saveCache(toSaveValue, timeoutSecond, prefix, (String) null);
    }

    public void saveCache(String toSaveValue, int timeoutSecond, CacheKeyConstants prefix, String... keys) {
        if (toSaveValue == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateCacheKey(prefix, keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey, timeoutSecond, toSaveValue);
        } catch (Exception e) {
            log.error("save cache exception, prefix {}, keys{}", prefix, keys);
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    public String getFromCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix, keys);
        try{
            shardedJedis = redisPool.instance();
            return shardedJedis.get(cacheKey);
        }catch(Exception e){
            log.error("get from cache exception, prefix:{}, keys:{}",prefix,keys);
        }finally {
            redisPool.safeClose(shardedJedis);
        }
        return null;
    }

    public void removeCache(CacheKeyConstants prefix, String keys) {
        ShardedJedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix, keys);
        try{
            shardedJedis = redisPool.instance();
            shardedJedis.del(cacheKey);
        }catch(Exception e){
            log.error("del cache exception, prefix:{}, keys:{}",prefix,keys);
        }finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (key != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
