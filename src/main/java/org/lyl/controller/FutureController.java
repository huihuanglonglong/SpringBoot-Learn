package org.lyl.controller;

import lombok.extern.slf4j.Slf4j;
import org.lyl.service.FutureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/future")
public class FutureController {

    @Resource
    private FutureService futureService;

    @PostMapping("/testFuture")
    public Object testFuture() throws Exception {
        log.info("enter testFuture method.....");
        Object result = futureService.testFuture();
        log.info("end testFuture method.....");
        return result;
    }
}
