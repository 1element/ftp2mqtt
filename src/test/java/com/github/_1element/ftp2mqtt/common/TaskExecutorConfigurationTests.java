package com.github._1element.ftp2mqtt.common;

import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.assertj.core.api.BDDAssertions.then;

public class TaskExecutorConfigurationTests {

  @Test
  public void shouldProvideExecutorWithConfiguredQueueCapacity() {
    // given
    final TaskExecutorConfiguration taskExecutorConfiguration = new TaskExecutorConfiguration();

    // when
    final ThreadPoolTaskExecutor result = (ThreadPoolTaskExecutor) taskExecutorConfiguration.asyncExecutor();

    // then
    then(result).hasFieldOrPropertyWithValue("queueCapacity", 100);
  }

  @Test
  public void shouldProvideExecutorWithConfiguredCorePoolSize() {
    // given
    final TaskExecutorConfiguration taskExecutorConfiguration = new TaskExecutorConfiguration();

    // when
    final ThreadPoolTaskExecutor result = (ThreadPoolTaskExecutor) taskExecutorConfiguration.asyncExecutor();

    // then
    then(result.getCorePoolSize()).isEqualTo(1);
  }

  @Test
  public void shouldProvideExecutorWithConfiguredMaxPoolSize() {
    // given
    final TaskExecutorConfiguration taskExecutorConfiguration = new TaskExecutorConfiguration();

    // when
    final ThreadPoolTaskExecutor result = (ThreadPoolTaskExecutor) taskExecutorConfiguration.asyncExecutor();

    // then
    then(result.getMaxPoolSize()).isEqualTo(1);
  }

}
