package cn.bugstack.middleware.test.service.impl;

import cn.bugstack.middleware.test.Vo;
import cn.bugstack.middleware.test.service.ITestService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author 徐明龙 XuMingLong 2022-02-15
 */
@Service
//@CacheConfig(cacheNames = "testCache")
public class TestServiceImpl implements ITestService {


    @Cacheable(cacheNames = "getValue",key = "#id")
    @Override public Vo getValue(Integer id) {
        System.out.println("getValue方法被调用");
        return id==1?new Vo(1,"测试1"):new Vo(id,"不存在");
    }


    @CacheEvict(cacheNames = "getValue",key = "#id")
    @Override public void updateValue(Integer id) {
        System.out.println("updateValue方法被调用");
    }
}
