package com.kemzeb.planetviewer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorConfig {

  /**
   * The TaskExecutor bean. Currently it is just used to fire up tasks in {@link
   * com.kemzeb.planetviewer.db.DatabaseLoader}.
   */
  @Bean
  public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    taskExecutor.setCorePoolSize(4);
    taskExecutor.setMaxPoolSize(16);
    taskExecutor.setQueueCapacity(32);
    return taskExecutor;
  }
}
