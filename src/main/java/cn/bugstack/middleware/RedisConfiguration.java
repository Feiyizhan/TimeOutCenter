package cn.bugstack.middleware;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * @author 徐明龙 XuMingLong 2020-06-12
 */

@Configuration
public class RedisConfiguration {

    /**
     * 默认的 Jackson Object Mapper对象
     * @author 徐明龙 XuMingLong 2020-04-30
     */
    public static final ObjectMapper REDIS_OM = new ObjectMapper();

    private static final ZoneId UTC_8 = ZoneId.of("UTC+8");

    private static final DateTimeFormatter FORMAT_TIMESTAMP = new DateTimeFormatterBuilder()
        .appendPattern("yyyy/MM/dd HH:mm:ss.SSS")
        .toFormatter()
        .withZone(UTC_8);


    static {

        //设置支持下划线转驼峰
        //        OM.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        SimpleModule bean = new SimpleModule();
        //LocalDateTime的反系列化使用自定义的处理器，用于适应多种自定义日期格式的转换处理
        bean.addDeserializer(LocalDateTime.class, MyLocalDateTimeDeserializer.INSTANCE);
        //String的反系列化使用自定义的处理器，用于XSS攻击
        //        bean.addDeserializer(String.class, StringXssDeserializer.INSTANCE);
        //LocalDateTime的系列化替换默认的ISO日期格式为自定义的日期格式
        bean.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(FORMAT_TIMESTAMP));

        REDIS_OM.registerModule(bean);
        REDIS_OM.activateDefaultTyping(REDIS_OM.getPolymorphicTypeValidator(),ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        REDIS_OM.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }



    @Bean(name = "jackson2JsonRedisSerializer")
    public RedisSerializer<Object> jackson2JsonRedisSerializer() {
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        ObjectMapper redisOm = new ObjectMapper();
        redisOm.registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return new GenericJackson2JsonRedisSerializer(redisOm);
    }

    @Bean
    public StringRedisSerializer getStringRedisSerializer() {
        return new StringRedisSerializer();
    }


    @Bean
    public RedisTemplate<Object, Object> configRedisTemplate(RedisTemplate redisTemplate){
        redisTemplate.setKeySerializer(getStringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(getStringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer());
        System.out.println("自定义RedisTemplate加载成功");
        return redisTemplate;
    }

}
