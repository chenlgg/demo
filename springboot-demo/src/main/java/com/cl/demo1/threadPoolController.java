package com.cl.demo1;

import java.util.concurrent.*;

/**
 * @author: chenl
 * @since: 2025/9/3 20:50
 * @description:
 */
public class threadPoolController {

   //写一个自定义线程池案例
    public static void main(String[] args) {
        //ThreadPoolExecutorTask();
        //ExecutorTask();
        ExecutorTask4();
    }

    public static void ThreadPoolExecutorTask() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, //核心线程数
                10, //最大线程数
                1000, //线程空闲时间
                TimeUnit.MILLISECONDS, //时间单位
                new ArrayBlockingQueue<>(10), //任务队列
                new ThreadPoolExecutor.DiscardOldestPolicy() //拒绝策略
        );
        //耗时
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "正在执行任务");
            });
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
        threadPoolExecutor.shutdown();
    }

    //Executor 创建线程池
    public static void ExecutorTask() {
        Executor executor = Executors.newFixedThreadPool(5);
        //耗时
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "正在执行任务");
            });
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
    }

    //Executor 创建线程池
    public static void ExecutorTask2() {
        Executor executor = Executors.newSingleThreadExecutor();
        //耗时
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "正在执行任务");
            });
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
    }

    public static void ExecutorTask3() {
        Executor executor = Executors.newCachedThreadPool();
        //耗时
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "正在执行任务");
            });
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
    }

    public static void ExecutorTask4() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

        // 耗时统计
        long start = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            final int taskId = i;
            executor.execute(() -> {
                System.out.println("任务 " + taskId + " 由 " + Thread.currentThread().getName() + " 正在执行");
            });
        }

        long end = System.currentTimeMillis();
        System.out.println("ScheduledThreadPool提交100个任务耗时：" + (end - start) + "毫秒");

        // 关闭线程池
        executor.shutdown();

        // 等待所有任务完成
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.out.println("线程池任务未在60秒内完成，强制关闭");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("所有任务执行完成");
    }










}
