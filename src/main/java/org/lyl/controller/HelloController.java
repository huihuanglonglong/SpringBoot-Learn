package org.lyl.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;


@RestController
@RequestMapping("/hello")
public class HelloController {

    /**
     * 我们的生后
     * @return
     */
    @PostMapping("/sayHello")
    public Object sayHello() {
        Map<String, String> dataMap = Maps.newHashMap();
        List<String> sources =  Lists.newArrayList("Aa", "BB", "C#", "king", "peter");
        sources.stream().forEach(x -> dataMap.put(x, x.hashCode()+""));
        return dataMap;
    }
}
