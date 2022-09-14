package cn.bugstack.middleware.test;

import cn.bugstack.middleware.test.service.ITestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author 徐明龙 XuMingLong 2022-02-15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableCaching
public class SpringCacheTests {


    @Resource
    private ITestService testService;


    @Test
    public void testCache(){
        System.out.println(testService.getValue(1));
        System.out.println(testService.getValue(2));
        System.out.println(testService.getValue(1));
    }


    @Test
    public void testCache2(){
        System.out.println(testService.getValue(1));
        testService.updateValue(1);
        System.out.println(testService.getValue(1));
    }
}
