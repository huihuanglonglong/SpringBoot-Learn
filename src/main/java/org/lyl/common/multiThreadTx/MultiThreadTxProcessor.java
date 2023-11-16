package org.lyl.common.multiThreadTx;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.lyl.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

@Component
@Slf4j
public class MultiThreadTxProcessor implements MultiThreadTxService<IService> {

    @Autowired
    private PlatformTransactionManager txManager;

    @Autowired
    private TransactionDefinition txDefinition;



    @Override
    public CompletableFuture runAsync(MultiThreadContext threadContext, Consumer<IService> consumer, ExecutorService executorService) {
        return CompletableFuture.runAsync(() -> executeThreadTx(threadContext, consumer), executorService);
    }

    private void executeThreadTx(MultiThreadContext threadTxContext, Consumer<IService> consumer) {
        List<Integer> dataList = Lists.newArrayList();
        dataList.forEach(data -> System.out.println(data));
    }
}
