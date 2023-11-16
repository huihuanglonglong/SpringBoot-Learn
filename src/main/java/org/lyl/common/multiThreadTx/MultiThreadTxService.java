package org.lyl.common.multiThreadTx;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public interface MultiThreadTxService<T> {


    CompletableFuture<Void> runAsync(MultiThreadContext threadContext, Consumer<T> consumer, ExecutorService executorService);

    


}
