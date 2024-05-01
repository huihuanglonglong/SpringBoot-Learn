package org.lyl.common.multiThreadTx;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Data
@Accessors(chain = true)
public class MultiThreadContext {

    private Integer timeoutSec = 5;

    private CountDownLatch downLatch;

    private AtomicBoolean isRollBack = new AtomicBoolean(Boolean.FALSE);


}
