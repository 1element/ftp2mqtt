package com.github._1element.ftp2mqtt.mqtt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("mqtt")
public class MqttProperties {

  private String brokerConnection;
  private String username;
  private String password;
  private String topicPrefix;

}
