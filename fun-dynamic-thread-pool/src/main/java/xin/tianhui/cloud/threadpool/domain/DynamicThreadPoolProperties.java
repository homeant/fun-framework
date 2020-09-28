package xin.tianhui.cloud.threadpool.domain;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "fun.thread-pool")
public class DynamicThreadPoolProperties {
	private String owner;

	private List<ThreadPoolProperties> executors = new ArrayList<>();
}
