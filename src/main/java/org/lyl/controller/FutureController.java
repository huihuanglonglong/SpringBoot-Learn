package org.lyl.controller;

import org.lyl.service.FutureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/future")
public class FutureController {

    @Autowired
    private FutureService futureService;

    @PostMapping("/testFuture")
    public Object testFuture() throws Exception {
        return futureService.testFuture();
    }
}
