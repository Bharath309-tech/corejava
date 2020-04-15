package com.itpeople.threadpool;

public class WorkerThread implements Runnable {
	private String message;

	public WorkerThread(String s) {
		this.message = s;
	}

	public void run() {
		System.out.println(Thread.currentThread().getName() + " Printing = " + message);
		processmessage();
		System.out.println(Thread.currentThread().getName() + " (End)");
	}

	private void processmessage() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
