package com.github._1element.ftp2mqtt.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MqttPublisherIntegrationTests {

  @Autowired
  private MqttPublisher mqttPublisher;

  @Autowired
  private MqttProperties properties;

  @MockBean
  private MqttClientFactory mqttClientFactory;

  @Mock
  private MqttClient mqttClient;

  @Test
  public void shouldPublishMessage() throws Exception {
    // given
    given(mqttClientFactory.getClientInstance(properties.getBrokerConnection())).willReturn(mqttClient);
    final MqttMessage expectedMessage = new MqttMessage(aByteArray());
    final String topicSuffix = "topic-suffix";

    // when
    mqttPublisher.publish(topicSuffix, aByteArray());

    // then
    then(mqttClient).should().publish(eq(properties.getTopicPrefix() + topicSuffix), refEq(expectedMessage));
  }

  private byte[] aByteArray() {
    return "test-data".getBytes();
  }

}
