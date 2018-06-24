package com.github._1element.ftp2mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@EnableAsync
public class Ftp2MqttApplication {

  @Bean
  public CountDownLatch closeLatch() {
    return new CountDownLatch(1);
  }

  public static void main(String[] args) throws InterruptedException {
    ConfigurableApplicationContext applicationContext = SpringApplication.run(Ftp2MqttApplication.class, args);

    final CountDownLatch closeLatch = applicationContext.getBean(CountDownLatch.class);
    Runtime.getRuntime().addShutdownHook(new Thread(closeLatch::countDown));
    closeLatch.await();
  }

}
