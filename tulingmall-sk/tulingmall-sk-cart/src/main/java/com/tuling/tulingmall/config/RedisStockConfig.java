package com.tuling.tulingmall.config;

import cn.hutool.core.convert.Convert;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuling.tulingmall.rediscomm.util.RedisOpsExtUtil;
import com.tuling.tulingmall.util.RedisStockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @description:
 **/
@Slf4j
@Configuration
public class RedisStockConfig {

    @Value("${spring.redisStock.host}")
    private String host;
    @Value("${spring.redisStock.port}")
    private String port;
    @Value("${spring.redisStock.lettuce.pool.max-active}")
    private String max_active;
    @Value("${spring.redisStock.lettuce.pool.max-idle}")
    private String max_idle;
    @Value("${spring.redisStock.lettuce.pool.max-wait}")
    private String max_wait;
    @Value("${spring.redisStock.lettuce.pool.min-idle}")
    private String min_idle;

    @Bean
    public GenericObjectPoolConfig redisPoolStock() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(Convert.toInt(min_idle));
        config.setMaxIdle(Convert.toInt(max_idle));
        config.setMaxTotal(Convert.toInt(max_active));
        config.setMaxWaitMillis(Convert.toInt(max_wait));
        return config;
    }

    @Bean
    public RedisStandaloneConfiguration redisConfigStock() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(host,Convert.toInt(port));
        return redisConfig;
    }

    @Bean("redisFactoryStock")
    public LettuceConnectionFactory redisFactoryStock(
            @Qualifier("redisPoolStock") GenericObjectPoolConfig config,
            @Qualifier("redisConfigStock") RedisStandaloneConfiguration redisConfig) {
        //注意传入的对象名和类型RedisStandaloneConfiguration
        LettuceClientConfiguration clientConfiguration =
                LettucePoolingClientConfiguration.builder().poolConfig(config).build();
        return new LettuceConnectionFactory(redisConfig, clientConfiguration);
    }

    /*单实例redis数据源*/
    @Bean("redisTemplateStock")
    public RedisTemplate<String, Object> redisTemplateSingle(
            @Qualifier("redisFactoryStock")
            LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String,Object> template = new RedisTemplate();
        template.setConnectionFactory(connectionFactory);
        // 序列化工具
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer
                = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);


        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisStockUtil redisStockUtil(){
        return new RedisStockUtil();
    }

}
