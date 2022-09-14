package cn.bugstack.middleware.test;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author 徐明龙 XuMingLong 2022-02-17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedissonLockTests {

    @Resource
    private RedissonClient redissonClient;
    @Test
    public void testLock1(){
        String key = "testLock1";
        RLock lock =redissonClient.getLock(key);
        try {
            System.out.println(Thread.currentThread().getName()+" "+ "获得锁的结果"+ lock.tryLock(1,30, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLock2(){
        String key = "testLock2";
        RLock lock =redissonClient.getLock(key);
        try {
            IntStream.range(1,5).parallel().forEach(r->{
                try {
                    System.out.println(Thread.currentThread().getName()+" "+ r+ "获得锁的结果"+ lock.tryLock(1,3, TimeUnit.SECONDS));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }finally {
            lock.unlock();
        }
    }

    @Test
    public void testLockAsync1(){
        String key = "testLockAsync1";
        RLock lock =redissonClient.getLock(key);
        List<Future<Boolean>> lockList = IntStream.range(1,5).parallel().mapToObj(r->{
            return lock.tryLockAsync(5, 10, TimeUnit.SECONDS);
        }).collect(Collectors.toList());

        lockList.stream().parallel().forEach(r->{
            try {
                System.out.println(Thread.currentThread().getName()+" "+  "获得锁的结果"+ r.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

    }


    @Test
    public void testSemaphore1(){
        String key = "testSemaphore1";
        RSemaphore semaphore =redissonClient.getSemaphore(key);
        //设置10个信号量
        semaphore.trySetPermits(5);
        //启动10个线程
        IntStream.range(1,11).parallel().forEach(r->{
            try {
                Thread.sleep(1000);//模拟车辆在排队
                System.out.println("===="+r+"来到停车场");
                if(semaphore.availablePermits()==0){
                    System.out.println("车位不足，请耐心等待");
                }
                semaphore.acquire();//获取令牌尝试进入停车场
                System.out.println(r+"成功进入停车场");
                Thread.sleep(new Random().nextInt(10000));//模拟车辆在停车场停留的时间
                System.out.println(r+"驶出停车场");
                semaphore.release();//释放令牌，腾出停车场车位
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    @After
    public void afterAll(){
        redissonClient.shutdown();
        System.out.println("关闭连接！");
    }
}
