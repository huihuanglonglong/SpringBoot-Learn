package org.lyl.controller;

import com.google.common.collect.Lists;
import org.lyl.common.util.ApplicationContextUtil;
import org.lyl.entity.LayoutEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/jvmTest")
public class JvmTestController {

    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/heapOom")
    public void heapOom() throws Exception {
        List<LayoutEntry> entryList = Lists.newArrayList();
        while ( true ) {
            entryList.add(new LayoutEntry());
            Thread.currentThread().sleep(10);
        }
    }

    @PostMapping("/tedstRedisTemplate")
    public void tedstRedisTemplate() throws Exception {
        //Cursor bigKeyScan = redisTemplate.opsForHash().scan("BigKeyScan", ScanOptions.scanOptions().count(1000).build());
        redisTemplate = ApplicationContextUtil.getBean("redisTemplate", RedisTemplate.class);
    }


}
