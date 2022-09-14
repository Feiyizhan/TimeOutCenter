package cn.bugstack.middleware.test;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.*;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.codec.SerializationCodec;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Redisson 测试类
 * @author 徐明龙 XuMingLong 2022-02-16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedissonTests {

    @Resource
    private RedissonClient redissonClient;


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
        voRBucket.set(new Vo(1,"测试1"));
        System.out.println(voRBucket.get());

        //指定编码器的Bucket
        RBucket<Vo> jsonRBucket = redissonClient.getBucket("testJsonObjVo", JsonJacksonCodec.INSTANCE);
        jsonRBucket.set(new Vo(1,"测试1"));
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
        jsonMap.put("1",new Vo(1,"测试1"));
        jsonMap.put("2",new Vo(2,"测试2"));
        jsonMap.put("3",new Vo(3,"测试3"));
        System.out.println(jsonMap.readAllMap());


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
        jsonList.add(new Vo(1,"测试1"));
        jsonList.add(new Vo(2,"测试2"));
        jsonList.add(new Vo(3,"测试3"));
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
        jsonSet.add(new Vo(1,"测试1"));
        jsonSet.add(new Vo(1,"测试1"));
        jsonSet.add(new Vo(3,"测试3"));
        jsonSet.add(new Vo(2,"测试2"));
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
        jsonSet.add(new Vo(1,"测试1"));
        jsonSet.add(new Vo(1,"测试1"));
        jsonSet.add(new Vo(3,"测试3"));
        jsonSet.add(new Vo(2,"测试2"));
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
        Vo vo1 = new Vo(1,"测试1");
        Vo vo2 = new Vo(2,"测试1");
        Comparator<Vo> comparator = Comparator.nullsFirst((Comparator<Vo> & Serializable)(x,y)->Integer.compare(x.id,y.id));
        System.out.println(comparator.compare(vo1,vo2));
        RBucket<Comparator<Vo>> comparatorInstance = redissonClient.getBucket("testSerializable", new SerializationCodec());
        comparatorInstance.set(comparator);

        Comparator<Vo> comparator2 = comparatorInstance.get();
        System.out.println(comparator2.compare(vo1,vo2));

    }



    @After
    public void afterAll(){
        redissonClient.shutdown();
        System.out.println("关闭连接！");
    }




}
