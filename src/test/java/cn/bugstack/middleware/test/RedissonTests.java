package cn.bugstack.middleware.test;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.*;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.SerializationCodec;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Redisson 测试类
 * @author 徐明龙 XuMingLong 2022-02-16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedissonTests {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedisTemplate(){
        System.out.println(redisTemplate.hasKey("testJsonString"));
        System.out.println(redisTemplate.opsForValue().get("testJsonString"));
    }

    @Test
    public void testStringRedisTemplate(){
        System.out.println(stringRedisTemplate.hasKey("testJsonString"));
        System.out.println(stringRedisTemplate.opsForValue().get("testJsonString"));
    }


    @Test
    public void testRedissonString(){
        //字符串,默认编码器
        RBucket<String> keyObj = redissonClient.getBucket("testObjString");
        keyObj.set("测试字符串1");
        keyObj.set("测试字符串2");
        keyObj.set("测试字符串3");
        System.out.println(keyObj.get());

        //指定编码器的Bucket
        RBucket<String> jsonRBucket = redissonClient.getBucket("testJsonString", JsonJacksonCodec.INSTANCE);
        jsonRBucket.set("测试字符串1");
        System.out.println(jsonRBucket.get());

    }

    @Test
    public void testRedissonObj(){

        //默认编码器（Java序列化和反序列化）Bucket
        RBucket<Vo> voRBucket = redissonClient.getBucket("testObjVo");
        voRBucket.set(new Vo(1,"测试1",LocalDateTime.now()));
        System.out.println(voRBucket.get());

        //指定编码器的Bucket
        RBucket<Vo> jsonRBucket = redissonClient.getBucket("testJsonObjVo", JsonJacksonCodec.INSTANCE);
        jsonRBucket.set(new Vo(1,"测试1",LocalDateTime.now()));
        System.out.println(jsonRBucket.get());

    }


    @Test
    public void testRedissonHash(){

        //默认编码器（Java序列化和反序列化）Bucket
        RMap<String,String> map = redissonClient.getMap("testMapString");
        map.put("id", "123");
        map.put("name", "小李");
        map.put("age", "50");
        System.out.println(map.get("id"));

        //指定编码器的Bucket
        RMap<String,Vo> jsonMap = redissonClient.getMap("testJsonObjVoMap", JsonJacksonCodec.INSTANCE);
        jsonMap.put("1",new Vo(1,"测试1",LocalDateTime.now()));
        jsonMap.put("2",new Vo(2,"测试2",LocalDateTime.now()));
        jsonMap.put("3",new Vo(3,"测试3",LocalDateTime.now()));
        System.out.println(jsonMap.readAllMap());


    }

    @Test
    public void testRedissonHash2(){

        //指定编码器的Bucket
        RMap<String,Vo> jsonMap1 = redissonClient.getMap("testJsonObjVoMap2::Map1", JsonJacksonCodec.INSTANCE);
        jsonMap1.put("1",new Vo(1,"测试1",LocalDateTime.now()));
        jsonMap1.put("2",new Vo(2,"测试2",LocalDateTime.now()));
        jsonMap1.put("3",new Vo(3,"测试3",LocalDateTime.now()));
        System.out.println(jsonMap1.readAllMap());

        //指定编码器的Bucket
        RMap<String,Vo> jsonMap2 = redissonClient.getMap("testJsonObjVoMap2::Map2", JsonJacksonCodec.INSTANCE);
        jsonMap2.put("1",new Vo(1,"测试1",LocalDateTime.now()));
        jsonMap2.put("2",new Vo(2,"测试2",LocalDateTime.now()));
        jsonMap2.put("3",new Vo(3,"测试3",LocalDateTime.now()));
        System.out.println(jsonMap2.readAllMap());


    }

    @Test
    public void testRedissonHash3(){

        //指定编码器的Bucket
        RMap<String, Map<String,Vo>> jsonMap = redissonClient.getMap("testJsonObjVoMap3", JsonJacksonCodec.INSTANCE);
        if(!jsonMap.containsKey("Map1")){
            Map<String,Vo> map1 = new LinkedHashMap<>();
            map1.put("1",new Vo(1,"测试1",LocalDateTime.now()));
            map1.put("2",new Vo(2,"测试2",LocalDateTime.now()));
            map1.put("3",new Vo(3,"测试3",LocalDateTime.now()));
            jsonMap.put("Map1",map1);
        }

        System.out.println(jsonMap.readAllMap());
        if(!jsonMap.containsKey("Map2")){
            Map<String,Vo> map2 = new LinkedHashMap<>();
            map2.put("1",new Vo(1,"测试1",LocalDateTime.now()));
            map2.put("2",new Vo(2,"测试2",LocalDateTime.now()));
            map2.put("3",new Vo(3,"测试3",LocalDateTime.now()));
            jsonMap.put("Map2",map2);
        }
        System.out.println(jsonMap.readAllMap());


    }

    @Test
    public void testRedissonHash4(){

        //指定编码器的Bucket
        RMap<String, String> jsonMap = redissonClient.getMap("testJsonObjVoMap4", JsonJacksonCodec.INSTANCE);


        System.out.println(jsonMap.fastPutIfAbsent("A","A1"));

        System.out.println(jsonMap.fastPutIfAbsent("A","A1"));


    }




    @Test
    public void testRedissonList(){

        //默认编码器（Java序列化和反序列化）Bucket
        RList<String> list = redissonClient.getList("testListString");
        list.add("测试字符串1");
        list.add("测试字符串2");
        list.add("测试字符串3");
        System.out.println(list.readAll());

        //指定编码器的Bucket
        RList<Vo> jsonList = redissonClient.getList("testJsonObjVoList", JsonJacksonCodec.INSTANCE);
        jsonList.add(new Vo(1,"测试1",LocalDateTime.now()));
        jsonList.add(new Vo(2,"测试2",LocalDateTime.now()));
        jsonList.add(new Vo(3,"测试3",LocalDateTime.now()));
        System.out.println(jsonList.readAll());

    }

    @Test
    public void testRedissonSet(){
        //默认编码器（Java序列化和反序列化）Bucket
        RSet<String> set = redissonClient.getSet("testSetString");
        set.add("测试字符串1");
        set.add("测试字符串1");
        set.add("测试字符串3");
        set.add("测试字符串2");
        System.out.println(set.readAll());

        //指定编码器的Bucket
        RSet<Vo> jsonSet = redissonClient.getSet("testJsonObjVoSet", JsonJacksonCodec.INSTANCE);
        jsonSet.add(new Vo(1,"测试1",LocalDateTime.now()));
        jsonSet.add(new Vo(1,"测试1",LocalDateTime.now()));
        jsonSet.add(new Vo(3,"测试3",LocalDateTime.now()));
        jsonSet.add(new Vo(2,"测试2",LocalDateTime.now()));
        System.out.println(jsonSet.readAll());
    }

    @Test
    public void testRedissonZSet(){
        //默认编码器（Java序列化和反序列化）Bucket
        RSortedSet<String> set = redissonClient.getSortedSet("testZSetString");
        set.add("测试字符串1");
        set.add("测试字符串1");
        set.add("测试字符串3");
        set.add("测试字符串2");
        System.out.println(set.readAll());

        //指定编码器的Bucket
        RSortedSet<Vo> jsonSet = redissonClient.getSortedSet("testJsonObjVoZSet", JsonJacksonCodec.INSTANCE);
        if(jsonSet.isEmpty()){
            //在集合没有元素的时候，设置元素比对规则
            jsonSet.trySetComparator(Comparator.nullsFirst((Comparator<Vo> & Serializable)(x,y)->Integer.compare(x.id,y.id)));
        }
        jsonSet.add(new Vo(1,"测试1",LocalDateTime.now()));
        jsonSet.add(new Vo(1,"测试1",LocalDateTime.now()));
        jsonSet.add(new Vo(3,"测试3",LocalDateTime.now()));
        jsonSet.add(new Vo(2,"测试2",LocalDateTime.now()));
        System.out.println(jsonSet.readAll());

        //        jsonSet.trySetComparator(Comparator.comparing(Vo::getId));
        //        jsonSet.trySetComparator(new VoComparator());
    }

//    public static class VoComparator implements Comparator<Vo>{
//        @Override public int compare(Vo o1, Vo o2) {
//            return o1.id.compareTo(o2.id);
//        }
//    }


    @Test
    public void testSerializationCodec(){
        Vo vo1 = new Vo(1,"测试1",LocalDateTime.now());
        Vo vo2 = new Vo(2,"测试1",LocalDateTime.now());
        Comparator<Vo> comparator = Comparator.nullsFirst((Comparator<Vo> & Serializable)(x,y)->Integer.compare(x.id,y.id));
        System.out.println(comparator.compare(vo1,vo2));
        RBucket<Comparator<Vo>> comparatorInstance = redissonClient.getBucket("testSerializable", new SerializationCodec());
        comparatorInstance.set(comparator);

        Comparator<Vo> comparator2 = comparatorInstance.get();
        System.out.println(comparator2.compare(vo1,vo2));

    }

    @Test
    public void testRedissonLocalDateTime(){
        LocalDateTime now = LocalDateTime.now();

        //默认编码器（Java序列化和反序列化）Bucket
        RBucket<LocalDateTime> voRBucket = redissonClient.getBucket("testLocalDateTime");
        voRBucket.set(now);
        System.out.println(voRBucket.get());

        //指定编码器的Bucket
        RBucket<LocalDateTime> jsonRBucket = redissonClient.getBucket("testLocalDateTime2", JsonJacksonCodec.INSTANCE);
        jsonRBucket.set(now);
        System.out.println(jsonRBucket.get());

    }


    @After
    public void afterAll(){
        redissonClient.shutdown();
        System.out.println("关闭连接！");
    }




}
