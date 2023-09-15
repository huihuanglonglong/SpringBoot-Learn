package org.lyl.config.redis;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

@Configuration
public class RedisConf {

    @Value("${spring.redis.cluster.nodes}")
    private String nodes;

    @Value("${spring.redis.cluster.max-redirects:5}")
    private Integer maxRedirects;

    @Value("${spring.redis.password:123456}")
    private String password;


    @Value("${spring.redis.jedis.pool.max-active:8}")
    private Integer maxActive;

    @Value("${spring.redis.jedis.pool.max-idle:8}")
    private Integer maxIdle;

    @Value("${spring.redis.jedis.pool.max-wait:-1}")
    private Long maxWait;

    @Value("${spring.redis.jedis.pool.min-idle:0}")
    private Integer minIdle;



    @Bean
    @Primary
    public JedisPoolConfig redisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);
        return jedisPoolConfig;
    }


    @Bean
    @Primary
    public RedisClusterConfiguration redisClusterConfiguration() {
        RedisClusterConfiguration config = new RedisClusterConfiguration();
        String[] nodeStrList = nodes.split(",");
        List<RedisNode> nodeList = Lists.newArrayList();

        String[] tmp = null;
        for (String nodeStr : nodeStrList) {
            tmp = nodeStr.split(":");
            nodeList.add(new RedisNode(tmp[0], Integer.parseInt(tmp[1])));
        }
        config.setClusterNodes(nodeList);
        config.setMaxRedirects(maxRedirects);
        config.setPassword(RedisPassword.of(password));
        return config;
    }

}
