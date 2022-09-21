package cn.bugstack.middleware.test;

import org.junit.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author 徐明龙 XuMingLong 2022-02-16
 */
public class Tests {

    @Test
    public void test(){
        ConcurrentSkipListMap<Integer,String> map  = new ConcurrentSkipListMap<>();
        map.put(1,"1");
        map.put(3,"3");
        map.put(6,"6");
        map.put(2,"2");
        map.put(5,"5");
        map.put(4,"4");
        System.out.println(map);
        System.out.println("=================");
        map.entrySet().forEach(System.out::println);
        System.out.println("=================");
        map.values().forEach(System.out::println);
    }

    @Test
    public void se(){
        Vo vo1 = new Vo(1,"测试1", LocalDateTime.now());
        Vo vo2 = new Vo(2,"测试1",LocalDateTime.now());
//        Comparator<Vo> comparator =  Comparator.comparing(Vo::getId);
        Comparator<Vo> comparator = Comparator.nullsFirst((Comparator<Vo> &Serializable)(x,y)->Integer.compare(x.id,y.id));
        System.out.println(comparator.compare(vo1,vo2));
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(comparator);
            outputStream.close();

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream inputStream = new ObjectInputStream(in);
            Object obj = inputStream.readObject();
            if(obj instanceof Comparator){
                Comparator<Vo> comparator2 = (Comparator<Vo>) obj;
                System.out.println(comparator2.compare(vo1,vo2));
            }
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static Comparator<Vo> createComparator(){
        return Comparator.comparing(r->r.id,(Comparator<Integer> &Serializable)(x,y)->Integer.compareUnsigned(x,y));
//        return (Comparator<Vo> &Serializable)(x,y)->Integer.compareUnsigned(x.getId(),y.id);
//        return (Comparator&Serializable)(x,y)->Integer.compareUnsigned((int)x,(int)y);
    }

    @Test
    public void testSe(){
//        (Comparator<Vo> &Serializable)(x,y)->Integer.compareUnsigned(x.getId(),y.id);
    }
}
