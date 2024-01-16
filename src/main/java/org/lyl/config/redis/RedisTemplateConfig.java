package org.lyl.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisTemplateConfig extends CachingConfigurerSupport {

    @Autowired
    private ObjectMapper objectMapper;


    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory(@Autowired JedisPoolConfig jedisPool,
                                                         @Autowired RedisClusterConfiguration jedisConfig) {
        JedisConnectionFactory factory = new JedisConnectionFactory(jedisConfig, jedisPool);
        factory.afterPropertiesSet();
        return factory;
    }


    @Primary
    @Bean("redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(@Autowired RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // 自定义缓存序列化操作
        Jackson2JsonRedisSerializer<Object> JacksonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        JacksonSerializer.setObjectMapper(objectMapper);
        redisTemplate.setDefaultSerializer(JacksonSerializer);
        redisTemplate.setEnableDefaultSerializer(Boolean.TRUE);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
