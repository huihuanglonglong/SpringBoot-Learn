package org.lyl.common.thread;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ThreadTest {

    /**
     * 线程池的参数说明
     * ThreadPoolExecutor
     * (int corePoolSize,---------核心线程数，一直keepAlive
     *  int maximumPoolSize,-----非核心线程数
     *  long keepAliveTime,------控制非核心线程存活时间
     *  TimeUnit unit,-----------时间单位
     *  BlockingQueue<Runnable> workQueue)-----线程等待队列
     *
     */
    private static final ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    private static final ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
    private static final ExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(20);
    private static final ExecutorService userThreadPool =
            new ThreadPoolExecutor(10,
                    20, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(80), Executors.defaultThreadFactory());

    /**
     * 性能对比
     * 无限次创建线程是需要消耗CPU
     * 概念：
     * ---线程的上下文切换
     * 多个线程同时创建，CPU在线程与线程之间切换的时候，需要将每个线程的寄存器数据，缓存数据，保存到指定内存然后才能切换下一个线程，
     * 当CPU又切回到上一个线程的时候又得从指定内存里面获取数据继续执行下一个指定。
     * 计算机的世界里，CPU会分为若干时间片，通过各种算法分配时间片来执行任务，
     * 每个任务在指定的时间片里面，当一个任务的时间片用完，就会切换到另一个任务，
     * 因此开启多个线程会带来一下开销
     * 操作系统保存和恢复上下文的开销、线程调度器调度线程的开销和高速缓存重新加载的开销等
     * 一次类推，
     * 在高并发环境下面或者多任务并行处理时，就会造成频繁的数据上下文切换，CPU没法计算业务了，损耗计算机性能。
     * 所以高并发服务，建议扩容，对于代码层面的多任务需要注意线程数量不宜过多。
     *
     * Thread的start()方法之后做的事情：
     * 1、通过JNI方式，调用到native层。
     * 2、native层，JVM通过pthread_create方法创建一个系统内核线程，并指定内核线程的初始运行地址，即一个方法指针。
     * 3、在内核线程的初始运行方法中，利用JavaCalls模块，回调到java线程的run方法，开始java级别的线程执行。
     *
     * 开启大量线程引起的问题，总结起来。
     * 1、消耗时间。线程的创建和销毁都需要时间，当数量太大的时候，会影响效率。
     * 2、消耗内存。创建更多的线程会消耗更多的内存，这是毋庸置疑的。线程频繁创建与销毁，还有可能引起内存抖动，频繁触发GC，最直接的表现就是卡顿。
     *   长而久之，内存资源占用过多或者内存碎片过多，系统甚至会出现OOM。
     * 3、消耗CPU。在操作系统中，CPU都是遵循时间片轮转机制进行处理任务，
     *   线程数过多，必然会引起CPU频繁的进行线程上下文切换。这个代价是昂贵的，某些场景下甚至超过任务本身的消耗。
     *
     * 任务分大致分为两类：
     * CPU密集型任务和IO密集型任务
     * CPU密集型任务，比如公式计算、资源解码等。这类任务要进行大量的计算，全都依赖CPU的运算能力，持久消耗CPU资源。所以针对这类任务，
     * 其实不应该开启大量线程。因为线程越多，花在线程切换的时间就越多，CPU执行效率就越低，一般CPU密集型任务同时进行的数量等于CPU的核心数，
     * 最多再加个1。
     * IO密集型任务，比如网络读写、文件读写等。这类任务不需要消耗太多的CPU资源，绝大部分时间是在IO操作上。所以针对这类任务，
     * 可以开启大量线程去提高CPU的执行效率，一般IO密集型任务同时进行的数量等于CPU的核心数的两倍。
     *
     * 在无法避免，必须要开启大量线程的情况下，我们也可以使用线程池代替直接创建线程的做法进行优化。
     * 线程池的基本作用就是复用已有的线程，从而减少线程的创建，降低开销。在Java中，线程池的使用还是非常方便的，
     * JDK中提供了现成的ThreadPoolExecutor类，我们只需要按照自己的需求进行相应的参数配置即可
     *
     * @param args args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // 新建带有回调方法的线程
        //testThreadByFutureTask();
        // 测试线程池
        //testThreadPool();
        testThreadCommunicate();
    }


    /********************************* 测试Thread的创建 ********************************************/

    /**
     * 创建线程可以 (extends Thread), (implement Runnable)
     * 还可以将指定FutureTask任务放入Thread构建参数中创建线程，
     * 在构建FutureTask时，可以自定义回调函数
     * FutureTask implement RunnableFuture extends Runnable, Future
     * 证明FutureTask是一根Runnable 同时也是一根 Future
     *
     */
    private static void testThreadByFutureTask() throws Exception {
        FutureTask<String> futureTask = new FutureTask(new MyCallable());
        new Thread(futureTask).start();
        String callResult = futureTask.get();
        System.out.println(callResult);
    }

    public static class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            Thread.sleep(1000L);
            return "my Callable finished, 可以把老王赶出去了！！！";
        }
    }


    /********************************* 测试线程池 ********************************************/

    /**
     * 问题说明：线程池创建的线程都是用户线程，不是JVM守护线程
     * 所有当前线程池中只要有线程挂载，JVM就不会退出，
     * 除非使用线程池关闭方法executor.shutdown();
     *
     */
    public static void testThreadPool() throws InterruptedException {
        // cacheThreadPool 效率最高,为什么会在taskId=31时被拒绝呢
        IntStream.range(0, 100).forEach(x -> userThreadPool.execute(new VoidRtnTask(x)));
        Thread.sleep(1500L);
        userThreadPool.shutdown();
        System.out.println("main thread emd!!!");
    }

    public static class VoidRtnTask implements Runnable {
        private int count = 0;
        public VoidRtnTask (int i) {
            this.count = i;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName()+"---"+count);
                Thread.sleep(20L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    /********************************* 测试线程通信交替打印数据 ********************************************/
    /**
     * 核心点事需要找到唯一标识，
     * 让每个线程只识别自己的标识下才操作运算，
     * 不识别自己的标识，则要释放锁，等待其他线程，给其他线程让道
     */
    private static Integer number = 1;
    private static String code = "taskA";
    private static final Object objectLock = new Object();

    public static void testThreadCommunicate() {
        Thread threadA = new Thread(new ThreadTask(), "taskA");
        Thread threadB = new Thread(new ThreadTask(), "taskB");
        Thread threadC = new Thread(new ThreadTask(), "taskC");
        threadA.start();
        threadB.start();
        threadC.start();

    }

    //wait()方法是Object类的方法，该方法是用来将当前线程置入“预执行队列”中，
    // 并且在wait()所在的代码处停止执行，直到接到通知或被中断为止。
    // 释放锁和Cpu，等待其他线程执行objectLock.notifyAll(),
    // 收到notifyAll消息后，当前线程再次回到Synchronized行代码与其他线程竞争这个锁
    private static class ThreadTask implements Runnable {
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            System.out.println("thread = "+ name +" start run......");
            for (int m = 0; m < 100; m++) {
                synchronized (objectLock) {
                    while (!name.equals(code)) {
                        try {
                            objectLock.wait();
                        } catch (InterruptedException e) {
                            System.out.println("currentThread is "+ name + "is interrupted...." + e);
                        }
                    }
                    number++;
                    code = exchange(name);
                    objectLock.notifyAll();
                    System.out.println("currentThread is "+ name +" data = " + number);
                }
            }
        }

        private String exchange(String currentName) {
           if ("taskA".equals(currentName)) {
               return "taskB";
           }
           if ("taskB".equals(currentName)) {
               return "taskC";
           }
           if ("taskC".equals(currentName)) {
               return "taskA";
           }
           return null;
        }

    }








}
