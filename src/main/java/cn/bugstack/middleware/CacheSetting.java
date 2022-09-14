package cn.bugstack.middleware;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 徐明龙 XuMingLong 2022-02-15
 */
@Configuration
@EnableCaching
public class CacheSetting {
//
//
//    @Resource
//    private RedissonClient redissonClient;

    @Autowired
    private RedisProperties redisProperties;

    @Bean(destroyMethod="shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisUrl = String.format("redis://%s:%s", redisProperties.getHost() + "", redisProperties.getPort() + "");
        config.useSingleServer().setAddress(redisUrl).setPassword(redisProperties.getPassword());
        config.useSingleServer().setDatabase(redisProperties.getDatabase());
        config.setCodec(new JsonJacksonCodec(JsonJacksonCodec.INSTANCE.getObjectMapper()));
        return Redisson.create(config);
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();
//        config.put("testCache", new CacheConfig(30*60*1000, 15*60*1000));
        return new RedissonSpringCacheManager(redissonClient, config, JsonJacksonCodec.INSTANCE);
    }

//    @Bean
//    CacheManager cacheManager(RedissonClient redissonClient) {
//        Map<String, CacheConfig> config = new HashMap<String, CacheConfig>();
//
//        LocalCachedMapOptions options = LocalCachedMapOptions.defaults()
//            .evictionPolicy(LocalCachedMapOptions.EvictionPolicy.LFU)
//            .cacheSize(1000);
//        // 创建一个名称为"testMap"的缓存，过期时间ttl为24分钟，同时最长空闲时maxIdleTime为12分钟。
//        config.put("testMap", new LocalCachedCacheConfig(24*60*1000, 12*60*1000, options));
//        return new RedissonSpringLocalCachedCacheManager(redissonClient, config);
//    }
}
