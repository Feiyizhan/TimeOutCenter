package cn.bugstack.middleware.test.service;

import cn.bugstack.middleware.test.Vo;
import org.springframework.cache.annotation.CacheEvict;

/**
 * @author 徐明龙 XuMingLong 2022-02-15
 */
public interface ITestService {

    Vo getValue(Integer id);

    @CacheEvict(cacheNames = "getValue",key = "#id") void updateValue(Integer id);
}
