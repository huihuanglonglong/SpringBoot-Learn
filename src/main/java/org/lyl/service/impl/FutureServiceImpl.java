package org.lyl.service.impl;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.lyl.entity.LayoutEntry;
import org.lyl.service.FutureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

@Slf4j
@Service
public class FutureServiceImpl implements FutureService {

    @Autowired
    @Qualifier("asyncInvokeExecutor")
    private TaskExecutor invokeExecutor;

    @Resource(name = "redisTemplate")
    private RedisTemplate<Object, Object> redisTemplate;

    private static final String FIELD_IS_FAVORITE = "isFavorite";
    private static final String FIELD_SCHEDULED = "scheduled";
    private static final String FIELD_ORDER_STATE = "orderState";

    private static final Map<String, BiConsumer<Object, LayoutEntry>> consumeFutureMap =
        new HashMap<String, BiConsumer<Object, LayoutEntry>>(){
        {
            put(FIELD_IS_FAVORITE, (obj, layoutEntry) -> layoutEntry.setIsFavorite((Boolean) obj));
            put(FIELD_SCHEDULED, (obj, layoutEntry) -> layoutEntry.setScheduled((Boolean) obj));
            put(FIELD_ORDER_STATE, (obj, layoutEntry) -> layoutEntry.setOrderState((Integer) obj));
        }
    };


    @Override
    public LayoutEntry testFuture() throws Exception {
        LayoutEntry layoutEntry = new LayoutEntry();
        Map<String, Future> futureMap = Maps.newHashMap();
        futureMap.put(FIELD_SCHEDULED, getScheduledFuture());
        futureMap.put(FIELD_IS_FAVORITE, getIsFavoriteFuture());
        futureMap.put(FIELD_ORDER_STATE, getOrderStateFuture());
        Thread.sleep(3000);
        setThirdFieldVal(layoutEntry, futureMap);
        redisTemplate.opsForValue().set("testKey", "testVal", 3, TimeUnit.HOURS);
        Object testValue = redisTemplate.opsForValue().get("testKey");
        log.info("get testValue from redis value = {}", testValue);
        return layoutEntry;
    }

    private void setThirdFieldVal(LayoutEntry layoutEntry, Map<String, Future> futureMap) {
        futureMap.forEach((fieldName, future) -> {
            try {
                Object fieldVal = future.get(300, TimeUnit.MILLISECONDS);
                Optional.ofNullable(consumeFutureMap.get(fieldName)).ifPresent(consumer -> consumer.accept(fieldVal, layoutEntry));
            } catch (Exception e) {
                log.error("set third field val have Exception------>", e);
                log.info(JSON.toJSONString(e));
            }
        });
    }

    private Future getIsFavoriteFuture(){
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            String threadName = Thread.currentThread().getName() + "---isFavorite";
            System.out.println(threadName+" have execute!!!");
            return Boolean.TRUE;
        }, invokeExecutor);
        return future;
    }

    private Future getScheduledFuture(){
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            String threadName = Thread.currentThread().getName() + "---scheduled";
            System.out.println(threadName+" have execute!!!");
            return Boolean.TRUE;
        }, invokeExecutor);
        return future;
    }

    private Future getOrderStateFuture(){
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            String threadName = Thread.currentThread().getName() + "---isFavorite";
            System.out.println(threadName+" have execute!!!");
            return 100;
        }, invokeExecutor);
        return future;
    }


    private void test(){
        CompletableFuture.supplyAsync(() -> 100)
                .thenApply(x -> x + 200).thenApply(x -> x *2)
                .whenComplete((x, e) -> log.error("test future have Exception----->", e));
        List<String> strList = new ArrayList<String>(){
            {
                add("Math");
                add("English");
            }
        };
        LayoutEntry entry = new LayoutEntry(){
            {
                setIsFavorite(Boolean.TRUE);
                setOrderState(Integer.MAX_VALUE);
            }
        };
    }



}
