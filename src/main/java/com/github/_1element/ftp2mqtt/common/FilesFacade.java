package com.github._1element.ftp2mqtt.common;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FilesFacade {

  public byte[] readAllBytes(Path path) throws IOException {
    return Files.readAllBytes(path);
  }

  public void delete(Path path) throws IOException {
    Files.delete(path);
  }

  public Path createTempDirectory() throws IOException {
    return Files.createTempDirectory(null);
  }

}
