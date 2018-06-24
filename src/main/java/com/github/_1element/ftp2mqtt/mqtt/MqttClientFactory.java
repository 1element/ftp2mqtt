package com.github._1element.ftp2mqtt.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

@Component
public class MqttClientFactory {

  public MqttClient getClientInstance(final String serverUri) throws MqttException {
    return new MqttClient(serverUri, MqttClient.generateClientId());
  }

}
