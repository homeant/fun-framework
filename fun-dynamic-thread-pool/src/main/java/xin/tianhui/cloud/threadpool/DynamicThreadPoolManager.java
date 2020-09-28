package xin.tianhui.cloud.threadpool;

import xin.tianhui.cloud.threadpool.concurrent.AbortPolicy;
import xin.tianhui.cloud.threadpool.concurrent.ResizableCapacityLinkedBlockIngQueue;
import xin.tianhui.cloud.threadpool.concurrent.ThreadFactory;
import xin.tianhui.cloud.threadpool.domain.DynamicThreadPoolProperties;
import xin.tianhui.cloud.threadpool.enums.QueueTypeEnum;
import xin.tianhui.cloud.threadpool.enums.RejectedExecutionHandlerEnum;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class DynamicThreadPoolManager {
	private final DynamicThreadPoolProperties properties;

	/**
	 * 存储线程池对象，Key:名称 Value:对象
	 */
	private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap = new HashMap<>();

	/**
	 * 存储线程池拒绝次数，Key:名称 Value:次数
	 */
	public static Map<String, AtomicLong> threadPoolExecutorRejectCountMap = new ConcurrentHashMap<>();

	public DynamicThreadPoolManager(DynamicThreadPoolProperties properties) {
		this.properties = properties;
	}


	@PostConstruct
	public void init() {
		createThreadPoolExecutor(properties);
	}

	/**
	 * 创建线程池
	 *
	 * @param threadPoolProperties
	 */
	public void createThreadPoolExecutor(DynamicThreadPoolProperties threadPoolProperties) {
		threadPoolProperties.getExecutors().forEach(executor -> {
			if (!threadPoolExecutorMap.containsKey(executor.getThreadPoolName())) {
				ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
					executor.getCorePoolSize(),
					executor.getMaximumPoolSize(),
					executor.getKeepAliveTime(),
					executor.getUnit(),
					getBlockingQueue(executor.getQueueType(), executor.getQueueCapacity(), executor.isFair()),
					new ThreadFactory(executor.getThreadPoolName()),
					getRejectedExecutionHandler(executor.getRejectedExecutionType(), executor.getThreadPoolName()), executor.getThreadPoolName());

				threadPoolExecutorMap.put(executor.getThreadPoolName(), threadPoolExecutor);
			}
		});
	}

	/**
	 * 获取拒绝策略
	 * @param rejectedExecutionType
	 * @param threadPoolName
	 * @return
	 */
	private RejectedExecutionHandler getRejectedExecutionHandler(String rejectedExecutionType, String threadPoolName) {
		if (RejectedExecutionHandlerEnum.CALLER_RUNS_POLICY.getType().equals(rejectedExecutionType)) {
			return new ThreadPoolExecutor.CallerRunsPolicy();
		}
		if (RejectedExecutionHandlerEnum.DISCARD_OLDEST_POLICY.getType().equals(rejectedExecutionType)) {
			return new ThreadPoolExecutor.DiscardOldestPolicy();
		}
		if (RejectedExecutionHandlerEnum.DISCARD_POLICY.getType().equals(rejectedExecutionType)) {
			return new ThreadPoolExecutor.DiscardPolicy();
		}
		ServiceLoader<RejectedExecutionHandler> serviceLoader = ServiceLoader.load(RejectedExecutionHandler.class);
		Iterator<RejectedExecutionHandler> iterator = serviceLoader.iterator();
		while (iterator.hasNext()) {
			RejectedExecutionHandler rejectedExecutionHandler = iterator.next();
			String rejectedExecutionHandlerName = rejectedExecutionHandler.getClass().getSimpleName();
			if (rejectedExecutionType.equals(rejectedExecutionHandlerName)) {
				return rejectedExecutionHandler;
			}
		}
		return new AbortPolicy(threadPoolName);
	}

	/**
	 * 获取阻塞队列
	 * @param queueType
	 * @param queueCapacity
	 * @param fair
	 * @return
	 */
	private BlockingQueue getBlockingQueue(String queueType, int queueCapacity, boolean fair) {
		if (!QueueTypeEnum.exists(queueType)) {
			throw new RuntimeException("队列不存在 " + queueType);
		}
		if (QueueTypeEnum.ARRAY_BLOCKING_QUEUE.getType().equals(queueType)) {
			return new ArrayBlockingQueue(queueCapacity);
		}
		if (QueueTypeEnum.SYNCHRONOUS_QUEUE.getType().equals(queueType)) {
			return new SynchronousQueue(fair);
		}
		if (QueueTypeEnum.PRIORITY_BLOCKING_QUEUE.getType().equals(queueType)) {
			return new PriorityBlockingQueue(queueCapacity);
		}
		if (QueueTypeEnum.DELAY_QUEUE.getType().equals(queueType)) {
			return new DelayQueue();
		}
		if (QueueTypeEnum.LINKED_BLOCKING_DEQUE.getType().equals(queueType)) {
			return new LinkedBlockingDeque(queueCapacity);
		}
		if (QueueTypeEnum.LINKED_TRANSFER_DEQUE.getType().equals(queueType)) {
			return new LinkedTransferQueue();
		}
		return new ResizableCapacityLinkedBlockIngQueue(queueCapacity);
	}
}
