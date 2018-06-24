package com.github._1element.ftp2mqtt.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncomingFileEvent {

  private final String sourceName;
  private final byte[] fileData;

}
