package cn.bugstack.middleware;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.stereotype.Component;

/**
 * 自定义的Redisson的配置类
 * @author 徐明龙 XuMingLong 2022-09-19
 */
@Component
public class MyRedissonAutoConfigurationCustomizer implements RedissonAutoConfigurationCustomizer {
    /**
     * Customize the RedissonClient configuration.
     *
     * @param configuration the {@link Config} to customize
     */
    @Override public void customize(Config configuration) {
        System.out.println("设置默认的Redisson Codec 为 JsonJacksonCodec");
        JsonJacksonCodec jacksonCodec = new JsonJacksonCodec();
        //支持Java 8 新的日期类型序列化和反序列化
        jacksonCodec.getObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configuration.setCodec(jacksonCodec);
    }
}
