package org.lyl.common.util;

import lombok.extern.slf4j.Slf4j;
import org.lyl.common.multiThreadTx.MultiThreadContext;
import org.lyl.common.util.ApplicationContextUtil;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class MultiThreadTxUtils {

    private static final PlatformTransactionManager TX_MANAGER = ApplicationContextUtil.getBean(PlatformTransactionManager.class) ;


    private static final TransactionDefinition TX_DEFINITION = ApplicationContextUtil.getBean(TransactionDefinition.class);

    private static final AsyncTaskExecutor EXECUTOR = ApplicationContextUtil.getBean("asyncInvokeExecutor", AsyncTaskExecutor.class);


    /**
     * 多线程执行 增，删，改
     *
     * @param threadTxContext
     * @param consumer
     * @param <T>
     */
    public <T> void runAsync(MultiThreadContext threadTxContext, Consumer<T> consumer) {
        CompletableFuture.runAsync(() -> executeThreadTx(threadTxContext, consumer), EXECUTOR);
    }

    /**
     * 多表查询，需要返回字段的操作，
     *
     * @param threadTxContext
     * @param function
     * @param <I>
     * @param <R>
     * @return
     */
    public <I, R> CompletableFuture<R> runAsyncFunction(MultiThreadContext threadTxContext, Function<I, R> function) {
        return CompletableFuture.supplyAsync(() -> supplyAsyncThreadTx(threadTxContext, function), EXECUTOR);
    }


    private <T> void executeThreadTx(MultiThreadContext threadTxContext, Consumer<T> consumer) {
        TransactionStatus txStatus = TX_MANAGER.getTransaction(TX_DEFINITION);
        AtomicBoolean isRollBack = threadTxContext.getIsRollBack();
        CountDownLatch downLatch = threadTxContext.getDownLatch();
        try {
            consumer.accept(null);
            downLatch.countDown();
            downLatch.await(threadTxContext.getTimeoutSec(), TimeUnit.SECONDS);
        } catch (Exception e) {
            isRollBack.set(Boolean.TRUE);
            downLatch.countDown();
        } finally {
            if (isRollBack.get()) {
                TX_MANAGER.rollback(txStatus);
            } else {
                TX_MANAGER.commit(txStatus);
            }
        }
    }

    private <I, R> R supplyAsyncThreadTx(MultiThreadContext threadTxContext, Function<I, R> function) {
        TransactionStatus txStatus = TX_MANAGER.getTransaction(TX_DEFINITION);
        AtomicBoolean isRollBack = threadTxContext.getIsRollBack();
        CountDownLatch downLatch = threadTxContext.getDownLatch();

        R result = null;
        try {
            result = function.apply(null);
            downLatch.countDown();
            downLatch.await(threadTxContext.getTimeoutSec(), TimeUnit.SECONDS);
        } catch (Exception e) {
            isRollBack.set(Boolean.TRUE);
            downLatch.countDown();
        } finally {
            if (isRollBack.get()) {
                TX_MANAGER.rollback(txStatus);
            } else {
                TX_MANAGER.commit(txStatus);
            }
        }
        return result;
    }


}
