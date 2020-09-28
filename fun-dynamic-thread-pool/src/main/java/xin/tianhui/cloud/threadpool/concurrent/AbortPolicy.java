package xin.tianhui.cloud.threadpool.concurrent;

import xin.tianhui.cloud.threadpool.DynamicThreadPoolManager;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

public class AbortPolicy implements RejectedExecutionHandler {

	private String threadPoolName;

	/**
	 * Creates an {@code AbortPolicy}.
	 */
	public AbortPolicy() {
	}

	public AbortPolicy(String threadPoolName) {
		this.threadPoolName = threadPoolName;
	}

	/**
	 * Always throws RejectedExecutionException.
	 *
	 * @param r the runnable task requested to be executed
	 * @param e the executor attempting to execute this task
	 * @throws RejectedExecutionException always
	 */
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
		AtomicLong atomicLong = DynamicThreadPoolManager.threadPoolExecutorRejectCountMap.putIfAbsent(threadPoolName, new AtomicLong(1));
		if (atomicLong != null) {
			atomicLong.incrementAndGet();
		}
		throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString());
	}
}
