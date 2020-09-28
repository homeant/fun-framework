package xin.tianhui.cloud.threadpool.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xin.tianhui.cloud.threadpool.DynamicThreadPoolManager;
import xin.tianhui.cloud.threadpool.domain.DynamicThreadPoolProperties;

@Configuration
@EnableConfigurationProperties({DynamicThreadPoolProperties.class})
public class ThreadPoolAutoConfiguration {

	private final DynamicThreadPoolProperties properties;

	public ThreadPoolAutoConfiguration(DynamicThreadPoolProperties properties){
		this.properties = properties;
	}

	@Bean
	public DynamicThreadPoolManager dynamicThreadPoolManager(){
		return new DynamicThreadPoolManager(properties);
	}


}
