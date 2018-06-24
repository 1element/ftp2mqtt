package com.github._1element.ftp2mqtt.ftp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("ftp")
public class FtpProperties {

  private int port;
  private String password;

}
