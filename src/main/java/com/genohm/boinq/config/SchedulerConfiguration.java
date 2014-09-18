package com.genohm.boinq.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerConfiguration {

	@Bean
	public SchedulerFactoryBean schedulerFactory() {
		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		Properties quartzProperties = new Properties();
		quartzProperties.setProperty("org.quartz.threadPool.threadCount", "1");
		schedulerFactory.setQuartzProperties(quartzProperties);
		schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
		return schedulerFactory;
	}
	
}


