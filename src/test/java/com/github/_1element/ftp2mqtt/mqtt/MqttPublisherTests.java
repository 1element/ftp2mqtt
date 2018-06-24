package com.github._1element.ftp2mqtt.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class MqttPublisherTests {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private MqttClientFactory clientFactory;

  @Mock
  private MqttProperties properties;

  @InjectMocks
  private MqttPublisher mqttPublisher;

  @Test
  public void shouldPublishMessage() throws Exception {
    // given
    final MqttClient mqttClient = mock(MqttClient.class);
    final String serverUri = "ssl://localhost:1883";
    final String topicPrefix = "topic-prefix/";
    final String topicSuffix = "topic-suffix";
    given(clientFactory.getClientInstance(serverUri)).willReturn(mqttClient);
    given(properties.getBrokerConnection()).willReturn(serverUri);
    given(properties.getTopicPrefix()).willReturn(topicPrefix);
    given(properties.getPassword()).willReturn("password");
    final MqttMessage expectedMessage = new MqttMessage(aByteArray());

    // when
    mqttPublisher.publish(topicSuffix, aByteArray());

    // then
    then(mqttClient).should().publish(eq(topicPrefix + topicSuffix), refEq(expectedMessage));
  }

  @Test
  public void shouldNotPublishMessageIfConnectionFails() throws Exception {
    // given
    final MqttClient mqttClient = mock(MqttClient.class);
    given(properties.getTopicPrefix()).willReturn("topic-prefix/");
    given(properties.getPassword()).willReturn("password");
    given(clientFactory.getClientInstance(any())).willReturn(mqttClient);
    doThrow(MqttException.class).when(mqttClient).connect(any());

    // when
    mqttPublisher.publish("topic-suffix", aByteArray());

    // then
    then(mqttClient).should().connect(any());
    then(mqttClient).shouldHaveNoMoreInteractions();
  }

  private byte[] aByteArray() {
    return "test-data".getBytes();
  }

}
