package cn.bugstack.middleware.test;

import com.fasterxml.jackson.core.type.TypeReference;
import jodd.util.concurrent.ThreadFactoryBuilder;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Redis Map 累加测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisMapIncrementTests {


    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 定义100个线程的线程池
     */
    private static final ExecutorService
        testExecutorService = new ThreadPoolExecutor(100,100,0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(1024),new ThreadFactoryBuilder()
        .setNameFormat("Test-pool-%d").get());


    @Test
    public void testIncrementWithRedisson1_1(){
        String key = "testIncrementWithRedisson1_1";
        String hashKey = "A1";
        IntStream.range(1,101).forEach(r->{
            System.out.println(incrementWithRedisson1(key,hashKey));
        });

        RMap<String,Long> accumulatorMap = redissonClient.getMap(key);
        System.out.println(String.format("累加的结果%d",accumulatorMap.get(hashKey)));
    }

    @Test
    public void testIncrementWithRedisson1_2(){
        String key = "testIncrementWithRedisson1_2";
        String hashKey = "A1";
        IntStream.range(1,101).forEach(r->{
            System.out.println(incrementWithRedisson1(key,hashKey));
        });

        RMap<String,Long> accumulatorMap = redissonClient.getMap(key);
        //下面这行代码会抛出异常 java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.Long
        Long value = accumulatorMap.get(hashKey);
        System.out.println(String.format("累加的结果%d",value));
    }


    private long incrementWithRedisson1(String key,String hashKey){
        RMap<String,Long> accumulatorMap = redissonClient.getMap(key);
        return accumulatorMap.addAndGet(hashKey,1L);
    }

    @Test
    public void testIncrementWithRedisson2_1(){
        String key = "testIncrementWithRedisson2_1";
        String hashKey = "A1";
        IntStream.range(1,101).forEach(r->{
            System.out.println(incrementWithRedisson2(key,hashKey));
        });
        //强制指定本Map的Codec 为LongCodec
        RMap<String,Long> accumulatorMap = redissonClient.getMap(key,LongCodec.INSTANCE);
        //下面这行代码不会抛出异常
        Long value = accumulatorMap.get(hashKey);
        System.out.println(String.format("累加的结果%d",value));
    }

    @Test
    public void testIncrementWithRedisson2_2(){
        String key = "testIncrementWithRedisson2_2";
        String hashKey = "A1";
        List<CompletableFuture> cfList = IntStream.range(1,101).mapToObj(r->{
            return CompletableFuture.runAsync(()->
                        incrementWithRedisson2(key,hashKey)
                    ,testExecutorService)
                .exceptionally(ex->{
                    ex.printStackTrace();
                    return null;
                });
        }).collect(Collectors.toList());
        // 等待所有线程结束
        CompletableFuture.allOf(cfList.toArray(new CompletableFuture[cfList.size()])).whenComplete((k,e)->{
            //所有线程都结束后，取出累加的值
            //强制指定本Map的Codec 为LongCodec
            RMap<String,Long> accumulatorMap = redissonClient.getMap(key,LongCodec.INSTANCE);
            Long value = accumulatorMap.get(hashKey);
            System.out.println(String.format("累加的结果%d",value));
        }).join();
    }

    @Test
    public void testIncrementWithRedisson2_3(){
        String key = "testIncrementWithRedisson2_3";
        String hashKey = "A1";
        IntStream.range(1,101).forEach(r->{
            System.out.println(incrementWithRedisson2(key,hashKey));
        });
        //强制指定本Map的Codec 为LongCodec
        RMap<String,Long> accumulatorMap = redissonClient.getMap(key,LongCodec.INSTANCE);
        //下面这行代码不会抛出异常
        Long value = accumulatorMap.get(hashKey);
        System.out.println(String.format("累加的结果%d",value));

        //下面这些代码都会抛出异常  java.lang.NumberFormatException: For input string: "A1"
                accumulatorMap.readAllKeySet();
        //        accumulatorMap.keySet().forEach(k->{
        //            System.out.println(k+"-"+accumulatorMap.get(k));
        //        });
        //        accumulatorMap.forEach((mapKey,mapValue)->{
        //            System.out.println(mapKey+"-"+mapValue);
        //        });
        //        Map<String,Long> allMap = accumulatorMap.readAllMap();
    }


    @Test
    public void testIncrementWithRedisson2_4(){
        String key = "testIncrementWithRedisson2_4";
        String hashKey = "A1";
        List<CompletableFuture> cfList = IntStream.range(1,201).mapToObj(r->{
            return CompletableFuture.runAsync(()->
                    incrementWithRedisson3(key,hashKey)
                ,testExecutorService)
                .exceptionally(ex->{
                    ex.printStackTrace();
                    return null;
                });
        }).collect(Collectors.toList());
        // 等待所有线程结束
        CompletableFuture.allOf(cfList.toArray(new CompletableFuture[cfList.size()])).whenComplete((k,e)->{
            //所有线程都结束后，取出累加的值
            //强制指定本Map的Codec 为jacksonCodec
            TypedJsonJacksonCodec jacksonCodec = new TypedJsonJacksonCodec(
                new TypeReference<String>() {},
                new TypeReference<Long>() {}
            );
            RMap<String,Long> accumulatorMap = redissonClient.getMap(key,jacksonCodec);
            Long value = accumulatorMap.get(hashKey);
            System.out.println(String.format("累加的结果%d",value));
            accumulatorMap.forEach((mapKey,mapValue)->{
                System.out.println(mapKey+"-"+mapValue);
            });
        }).join();
    }

    private long incrementWithRedisson2(String key,String hashKey){
        //强制指定本Map的Codec 为LongCodec
        RMap<String,Long> accumulatorMap = redissonClient.getMap(key, LongCodec.INSTANCE);
        return accumulatorMap.addAndGet(hashKey,1L);
    }

    private long incrementWithRedisson3(String key,String hashKey){
        //强制指定本Map的Codec 为TypedJsonJacksonCodec
        TypedJsonJacksonCodec jacksonCodec = new TypedJsonJacksonCodec(
            new TypeReference<String>() {},
            new TypeReference<Long>() {}
        );
        RMap<String,Long> accumulatorMap = redissonClient.getMap(key, jacksonCodec);
        return accumulatorMap.addAndGet(hashKey,1L);
    }

    @Test
    public void testIncrementWithRedisTemplate1_1(){
        String key = "testIncrementWithRedisTemplate1_1";
        String hashKey = "A1";
        IntStream.range(1,101).forEach(r->{
            System.out.println(incrementWithRedisTemplate(key,hashKey));
        });
        System.out.println(String.format("累加的结果%d",redisTemplate.opsForHash().get(key,hashKey)));
    }

    @Test
    public void testIncrementWithRedisTemplate1_2(){
        String key = "testIncrementWithRedisTemplate1_2";
        String hashKey = "A1";
        IntStream.range(1,101).forEach(r->{
            System.out.println(incrementWithRedisTemplate(key,hashKey));
        });

        //下面这行代码会抛出异常 java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.Long
        Long value = (Long)redisTemplate.opsForHash().get(key,hashKey);
        System.out.println(String.format("累加的结果%d",value));
    }

    @Test
    public void testIncrementWithRedisTemplate1_3(){
        String key = "testIncrementWithRedisTemplate1_3";
        String hashKey = "A1";
        IntStream.range(1,101).forEach(r->{
            System.out.println(incrementWithRedisTemplate(key,hashKey));
        });

        //下面这行代码不会抛出异常
        Long value = Long.valueOf(redisTemplate.opsForHash().get(key,hashKey).toString());
        System.out.println(String.format("累加的结果%d",value));
    }

    @Test
    public void testIncrementWithRedisTemplate1_4(){
        String key = "testIncrementWithRedisTemplate1_4";
        String hashKey = "A1";
        List<CompletableFuture> cfList = IntStream.range(1,101).mapToObj(r->{
            return CompletableFuture.runAsync(()->
                    incrementWithRedisTemplate(key,hashKey)
                ,testExecutorService)
                .exceptionally(ex->{
                    ex.printStackTrace();
                    return null;
                });
        }).collect(Collectors.toList());
        // 等待所有线程结束
        CompletableFuture.allOf(cfList.toArray(new CompletableFuture[cfList.size()])).whenComplete((k,e)->{
            //所有线程都结束后，取出累加的值
            Long value = Long.valueOf(redisTemplate.opsForHash().get(key,hashKey).toString());
            System.out.println(String.format("累加的结果%d",value));
        }).join();
    }

    private long incrementWithRedisTemplate(String key,String hashKey){
        return redisTemplate.opsForHash().increment(key,hashKey,1L);
    }



    @After
    public void afterAll(){
        redissonClient.shutdown();
        System.out.println("关闭连接！");
    }

}
