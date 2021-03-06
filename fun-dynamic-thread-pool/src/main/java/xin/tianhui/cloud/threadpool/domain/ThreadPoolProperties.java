package xin.tianhui.cloud.threadpool.domain;

import lombok.Data;
import xin.tianhui.cloud.threadpool.enums.QueueTypeEnum;
import xin.tianhui.cloud.threadpool.enums.RejectedExecutionHandlerEnum;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Data
public class ThreadPoolProperties implements Serializable {
	/**
	 * 线程池名称
	 */
	private String threadPoolName = "defaultThreadPool";

	/**
	 * 核心线程数
	 */
	private int corePoolSize = 1;

	/**
	 * 最大线程数, 默认值为CPU核心数量
	 */
	private int maximumPoolSize = Runtime.getRuntime().availableProcessors();

	/**
	 * 队列最大数量
	 */
	private int queueCapacity = Integer.MAX_VALUE;

	/**
	 * 队列类型
	 * @see QueueTypeEnum
	 */
	private String queueType = QueueTypeEnum.LINKED_BLOCKING_QUEUE.getType();

	/**
	 * SynchronousQueue 是否公平策略
	 */
	private boolean fair;

	/**
	 * 拒绝策略
	 * @see RejectedExecutionHandlerEnum
	 */
	private String rejectedExecutionType = RejectedExecutionHandlerEnum.ABORT_POLICY.getType();

	/**
	 * 空闲线程存活时间
	 */
	private long keepAliveTime;

	/**
	 * 空闲线程存活时间单位
	 */
	private TimeUnit unit = TimeUnit.MILLISECONDS;

	/**
	 * 队列容量阀值，超过此值告警
	 */
	private int queueCapacityThreshold = queueCapacity;
}
