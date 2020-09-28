package xin.tianhui.cloud.threadpool.enums;

public enum RejectedExecutionHandlerEnum {
	CALLER_RUNS_POLICY("CallerRunsPolicy"),
	ABORT_POLICY("AbortPolicy"),
	DISCARD_POLICY("DiscardPolicy"),
	DISCARD_OLDEST_POLICY("DiscardOldestPolicy");

	RejectedExecutionHandlerEnum(String type) {
		this.type = type;
	}

	;

	private String type;

	public String getType() {
		return type;
	}

	public static boolean exists(String type) {
		for (RejectedExecutionHandlerEnum typeEnum : RejectedExecutionHandlerEnum.values()) {
			if (typeEnum.getType().equals(type)) {
				return true;
			}
		}
		return false;
	}
}
