package cn.bugstack.middleware.test;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author 徐明龙 XuMingLong 2022-02-17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedissonCacheTests {

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testRedissonCache(){
        LocalCachedMapOptions<String,String> cachedMapOptions = LocalCachedMapOptions.defaults();
        RLocalCachedMap<String, String> voCache = redissonClient.getLocalCachedMap("cities",
            cachedMapOptions
        );
        voCache.put("1","1");
        voCache.put("2","2");
        for(int i=0;i<10;i++){
            System.out.println(voCache.get("1"));
            System.out.println(voCache.containsKey("2"));
            try {
                //通过其他方式修改Redis的值
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //需要重新获取CachedMap才能获取最新的Redis的值
            voCache = redissonClient.getLocalCachedMap("cities",
                cachedMapOptions
            );
        }
    }

    @After
    public void afterAll(){
        redissonClient.shutdown();
        System.out.println("关闭连接！");
    }
}
