package com.github._1element.ftp2mqtt.mqtt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MqttPublisher {

  private final MqttClientFactory clientFactory;
  private final MqttProperties properties;

  public void publish(final String topicSuffix, final byte[] payload) {
    final String topic = properties.getTopicPrefix() + topicSuffix;
    try {
      final MqttClient client = clientFactory.getClientInstance(properties.getBrokerConnection());
      client.connect(createConnectOptions());
      client.publish(topic, new MqttMessage(payload));
      client.disconnect();
      log.debug("Published message to topic '{}'", topic);
    } catch (final MqttException exception) {
      log.error("Error publishing to MQTT broker '{}'.", properties.getBrokerConnection(), exception);
    }
  }

  private MqttConnectOptions createConnectOptions() {
    final MqttConnectOptions connectOptions = new MqttConnectOptions();
    connectOptions.setUserName(properties.getUsername());
    connectOptions.setPassword(properties.getPassword().toCharArray());

    return connectOptions;
  }

}
