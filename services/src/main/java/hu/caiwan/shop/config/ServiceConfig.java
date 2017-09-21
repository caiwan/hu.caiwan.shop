package hu.caiwan.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ComponentScan(basePackages = { "hu.caiwan.shop.service", "hu.caiwan.shop.listeners", "hu.caiwan.shop.tasks" })
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class ServiceConfig {

	@Bean
	TaskExecutor taskExecutor() {
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(25);
		executor.setQueueCapacity(250);
		return executor;
	}

}
