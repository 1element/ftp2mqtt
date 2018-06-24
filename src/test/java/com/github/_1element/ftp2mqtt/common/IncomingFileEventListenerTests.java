package com.github._1element.ftp2mqtt.common;

import com.github._1element.ftp2mqtt.mqtt.MqttPublisher;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.BDDMockito.then;

public class IncomingFileEventListenerTests {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private MqttPublisher mqttPublisher;

  @InjectMocks
  private IncomingFileEventListener incomingFileEventListener;

  @Test
  public void shouldDelegateToMqttPublisher() {
    // given
    final IncomingFileEvent incomingFileEvent = anIncomingFileEvent();

    // when
    incomingFileEventListener.handleIncomingFileEvent(incomingFileEvent);

    // then
    then(mqttPublisher).should().publish("source-name", "file-data".getBytes());
  }

  private IncomingFileEvent anIncomingFileEvent() {
    return new IncomingFileEvent("source-name", "file-data".getBytes());
  }

}
