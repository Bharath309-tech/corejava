package com.itpeople.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Custom Fixed Thread pool implementation
 * @author Bharath
 *
 */
public class SimpleThreadpool {
    private static AtomicInteger poolCount = new AtomicInteger(0);
    private ConcurrentLinkedQueue<Runnable> runnables;
    private AtomicBoolean execute;
    private List<SimpleThreadpoolThread> threads;

    /**
     * Inner Thread class which represents the threads in the pool
     */
    private class SimpleThreadpoolThread extends Thread {
        private AtomicBoolean execute;
        private ConcurrentLinkedQueue<Runnable> runnables;

        public SimpleThreadpoolThread(String name, AtomicBoolean execute, ConcurrentLinkedQueue<Runnable> runnables) {
            super(name);
            this.execute = execute;
            this.runnables = runnables;
        }

        @Override
        public void run() {
            try {
                while (execute.get() || !runnables.isEmpty()) {
                    Runnable runnable;
                    while ((runnable = runnables.poll()) != null) {
                        runnable.run();
                    }
                    Thread.sleep(1);
                }
            } catch (RuntimeException | InterruptedException e) {
                throw new ThreadpoolException(e);
            }
        }
    }
    
    private SimpleThreadpool(int threadCount) {
        poolCount.incrementAndGet();
        this.runnables = new ConcurrentLinkedQueue<>();
        this.execute = new AtomicBoolean(true);
        this.threads = new ArrayList<>();
        for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
            SimpleThreadpoolThread thread = new SimpleThreadpoolThread("SimpleThreadpool" + poolCount.get() + "Thread" + threadIndex, this.execute, this.runnables);
            thread.start();
            this.threads.add(thread);
        }
    }

    public static SimpleThreadpool getInstance(int threadCount) {
        return new SimpleThreadpool(threadCount);
    }

    public void execute(Runnable runnable) {
        if (this.execute.get()) {
            runnables.add(runnable);
        } else {
            throw new IllegalStateException("Threadpool terminating, unable to execute runnable");
        }
    }

    public void shutdown() {
        runnables.clear();
        stop();
    }

    public void stop() {
        execute.set(false);
    }
    
    public static void main(String a[]) {
    	   int threadPoolCount = 6;
           SimpleThreadpool threadpool = SimpleThreadpool.getInstance(threadPoolCount);
           
           Runnable r1 = new WorkerThread("" + 1);  
           Runnable r2 = new WorkerThread("" + 2);  
           Runnable r3 = new WorkerThread("" + 3);  
           Runnable r4 = new WorkerThread("" + 4);  
           Runnable r5 = new WorkerThread("" + 5);  
           Runnable r6 = new WorkerThread("" + 6); 
           Runnable r7 = new WorkerThread("" + 7);  
           Runnable r8 = new WorkerThread("" + 8);  
           Runnable r9 = new WorkerThread("" + 9);  
           Runnable r10 = new WorkerThread("" + 10);  
           Runnable r11 = new WorkerThread("" + 11);  
           Runnable r12 = new WorkerThread("" + 12); 
           
           threadpool.execute(r1);
           threadpool.execute(r2);
           threadpool.execute(r3);
           threadpool.execute(r4);
           threadpool.execute(r5);
           threadpool.execute(r6);
           threadpool.execute(r7);
           threadpool.execute(r8);
           threadpool.execute(r9);
           threadpool.execute(r10);
           threadpool.execute(r11);
           threadpool.execute(r12);
           
           //Wait till execution of all threads for specified amout of time
           try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           threadpool.shutdown();
          
	
    }
}
