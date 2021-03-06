package com.satansk.concurrency.thread;

/**
 * Author:  satansk
 * Email:   satansk@hotmail.com
 * Date:    17/6/14
 */
public class SimpleThreads {
    public static void main(String[] args) throws InterruptedException {
        // 中断 MessageLoop 线程之前等待的时间，默认 5s
        long patience = 10 * 1000;

        // 若有命令行参数，则使用，且以 s 为单位
        if (args.length > 0) {
            patience = Long.parseLong(args[0]) * 1000;
        }

        // 开始
        threadMessage("Starting MessageLoop thread");

        long startTime = System.currentTimeMillis();

        Thread t = new Thread(new MessageLoop());
        t.start();

        threadMessage("Waiting for MessageLoop thread to finish");

        while (t.isAlive()) {
            threadMessage("Still waiting...");

            // 暂停主线程的执行，1s 后检查等待时间
            t.join(1000);

            long currentTime = System.currentTimeMillis();
            // 等待时间超过 patience 则中断 MessageLoop 线程
            if ((currentTime - startTime) > patience && t.isAlive()) {
                threadMessage("Tired of waiting!");

                t.interrupt();

                t.join();
            }
        }

        // 结束
        threadMessage("Finally!");
    }

    private static class MessageLoop implements Runnable {
        public void run() {
            String[] infos = {
                    "Mares eat oats",
                    "Does eat oats",
                    "Little lambs eat ivy",
                    "A kid will eat ivy too"
            };

            for (String message : infos) {
                try {
                    Thread.sleep(4000);
                    threadMessage(message);
                } catch (InterruptedException e) {
                    // 被中断后直接返回
                    threadMessage("I wasn't done!");
                }
            }
        }
    }

    static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }
}
