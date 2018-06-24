package com.github._1element.ftp2mqtt.ftp;

import lombok.RequiredArgsConstructor;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.listener.Listener;
import org.apache.ftpserver.listener.ListenerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class FtpDaemon {

  private final FtpUserManager ftpUserManager;
  private final FtpActions ftpActions;
  private final FtpServerFactory ftpServerFactory;
  private final ListenerFactory listenerFactory;
  private final FtpProperties properties;

  private FtpServer ftpServer;

  @PostConstruct
  public void start() throws FtpException {
    ftpServer = createFtpServer();
    ftpServer.start();
  }

  @PreDestroy
  public void stop() {
    if (ftpServer != null) {
      ftpServer.stop();
    }
  }

  private FtpServer createFtpServer() {
    ftpServerFactory.addListener("default", createListener());
    ftpServerFactory.setUserManager(ftpUserManager);
    ftpServerFactory.setFtplets(createFtplets());

    return ftpServerFactory.createServer();
  }

  private Listener createListener() {
    listenerFactory.setPort(properties.getPort());

    return listenerFactory.createListener();
  }

  private Map<String, Ftplet> createFtplets() {
    final Map<String, Ftplet> ftpletMap = new HashMap<>();
    ftpletMap.put("ftpActions", ftpActions);

    return ftpletMap;
  }

}
