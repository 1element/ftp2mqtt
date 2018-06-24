package com.github._1element.ftp2mqtt.ftp;

import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class FtpConfiguration {

  @Bean
  @Scope("prototype")
  public FtpServerFactory ftpServerFactory() {
    return new FtpServerFactory();
  }

  @Bean
  @Scope("prototype")
  public ListenerFactory listenerFactory() {
    return new ListenerFactory();
  }

}
