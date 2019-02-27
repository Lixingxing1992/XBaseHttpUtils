package com.app.org.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lixingxing on 2018/5/10.
 */
public class BaseThreadPoolManager {
    static {
        BaseThreadPoolManager baseThreadPoolManager = new BaseThreadPoolManager();
    }
    private static  BaseThreadPoolManager baseThreadPoolManager;

    // 线程池核心线程数
    private static int CORE_POOL_SIZE = 5;

    // 线程池最大线程数
    private static int MAX_POOL_SIZE = 10;

    // 额外线程空状态生存时间
    private static int KEEP_ALIVE_TIME = 10 * 1000;

    // 1.把调用层传入的任务放入到请求队列中
    // 数据结构的选择: 1.阻塞  2.容量无线  3.插入很多
    private static LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque();

    /**
     * 从线程池中抽取线程，执行指定的Runnable对象
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        try {
            queue.put(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 2.把队列中的任务放入线程池进行执行
    // 线程池
    private static ThreadPoolExecutor threadPoolExecutor;

    private BaseThreadPoolManager() {
        threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(CORE_POOL_SIZE) , rejectedExecutionHandler);
        threadPoolExecutor.execute(runnable);
    }

    private RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            //runnable 就是被丢出来的线程
            try {
                queue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    // 3.无线运行起来

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true){
                Runnable runnableTake = null;
                try {
                    runnableTake = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(runnableTake !=null){
                    threadPoolExecutor.execute(runnableTake);
                }
            }
        }
    };
}
