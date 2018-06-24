package com.github._1element.ftp2mqtt.ftp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.net.URLConnection;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FtpDaemonIntegrationTests {

  @Autowired
  private FtpDaemon ftpDaemon;

  @Autowired
  private FtpProperties properties;

  @Test
  public void shouldStartFtpServer() throws Exception {
    // given
    final String connection = "ftp://user:" + properties.getPassword() + "@localhost:" + properties.getPort();
    final URLConnection urlConnection = new URL(connection).openConnection();

    // when
    urlConnection.connect();

    // then
    then(urlConnection.getInputStream()).isNotNull();
  }

}
