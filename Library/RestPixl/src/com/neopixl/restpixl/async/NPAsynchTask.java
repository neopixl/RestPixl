package com.neopixl.restpixl.async;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class NPAsynchTask<Params, Progress, Result> extends
		NPNewAsyncTask<Params, Progress, Result>  implements Comparable<NPAsynchTask<Params, Progress, Result>> {
	
	static final BlockingQueue<Runnable> sWorkQueue = new PriorityBlockingQueue<Runnable>(
			20);

	private static final int CORE_POOL_SIZE = 1;
	private static final int MAXIMUM_POOL_SIZE = 1;
	private static final int KEEP_ALIVE = 0; // keep indefinitely

	private NPRequestPriority priority = NPRequestPriority.REQ_INTERACTIVE_PRIORITY;
	
	public static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
			CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
			sWorkQueue);

	public NPRequestPriority getPriority() {
		return priority;
	}

	public void setPriority(NPRequestPriority priority) {
		this.priority = priority;
	}
	
	@Override
	public int compareTo(NPAsynchTask<Params, Progress, Result> another) {
		int order = this.priority.compareTo(another.getPriority());
		if (order == 0) {
			if (this.getTaskId() < another.getTaskId()) {
				return -1; 
			} else if (this.getTaskId() > another.getTaskId()) {
				return 1;
			}
		} 
		return order;
	}
	
}
