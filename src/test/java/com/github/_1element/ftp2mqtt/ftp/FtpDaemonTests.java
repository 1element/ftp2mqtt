package com.github._1element.ftp2mqtt.ftp;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class FtpDaemonTests {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private FtpUserManager ftpUserManager;

  @Mock
  private FtpActions ftpActions;

  @Mock
  private FtpServerFactory ftpServerFactory;

  @Mock
  private ListenerFactory listenerFactory;

  @Mock
  private FtpProperties properties;

  @Mock
  private FtpServer ftpServer;

  @InjectMocks
  private FtpDaemon ftpDaemon;

  @Test
  public void shouldStartFtpServer() throws Exception {
    // given
    given(ftpServerFactory.createServer()).willReturn(ftpServer);

    // when
    ftpDaemon.start();

    // then
    then(ftpServer).should().start();
  }

  @Test
  public void shouldStopFtpServer() throws Exception {
    // given
    given(ftpServerFactory.createServer()).willReturn(ftpServer);
    ftpDaemon.start();

    // when
    ftpDaemon.stop();

    // then
    then(ftpServer).should().stop();
  }

  @Test
  public void shouldNotStopFtpServerWhenNotStarted() {
    // given, when
    ftpDaemon.stop();

    // then
    then(ftpServer).shouldHaveZeroInteractions();
  }

}
