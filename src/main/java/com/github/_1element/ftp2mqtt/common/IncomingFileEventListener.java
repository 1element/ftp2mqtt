package com.github._1element.ftp2mqtt.common;

import com.github._1element.ftp2mqtt.mqtt.MqttPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IncomingFileEventListener {

  private final MqttPublisher mqttPublisher;

  @Async
  @EventListener
  public void handleIncomingFileEvent(final IncomingFileEvent incomingFileEvent) {
    mqttPublisher.publish(incomingFileEvent.getSourceName(), incomingFileEvent.getFileData());
  }

}
