package com.github._1element.ftp2mqtt.ftp;

import com.github._1element.ftp2mqtt.common.FilesFacade;
import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

public class FtpUserManagerTests {

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private FilesFacade filesFacade;

  @Mock
  private FtpProperties properties;

  @InjectMocks
  private FtpUserManager ftpUserManager;

  @Test
  public void shouldReturnCorrectUsernameForUserByName() throws Exception {
    // given
    given(filesFacade.createTempDirectory()).willReturn(Paths.get("/home/directory"));

    // when
    final User result = ftpUserManager.getUserByName("username");

    // then
    then(result.getName()).isEqualTo("username");
  }

  @Test
  public void shouldReturnCorrectPasswordForUserByName() throws Exception {
    // given
    given(filesFacade.createTempDirectory()).willReturn(Paths.get("/home/directory"));
    given(properties.getPassword()).willReturn("password");

    // when
    final User result = ftpUserManager.getUserByName("username");

    // then
    then(result.getPassword()).isEqualTo("password");
  }

  @Test
  public void shouldThrowExceptionIfTemporaryDirectoryCouldNotBeCreated() throws Exception {
    // given
    given(filesFacade.createTempDirectory()).willThrow(IOException.class);

    // when
    final Throwable thrown = catchThrowable(() -> ftpUserManager.getUserByName("username"));

    // then
    then(thrown).hasMessage("Could not create temporary directory for incoming files.").isInstanceOf(IllegalStateException.class);
  }

  @Test
  public void shouldAuthenticateWithValidUsernameAndPassword() throws Exception {
    // given
    final Authentication authentication = new UsernamePasswordAuthentication("username", "password");
    given(properties.getPassword()).willReturn("password");
    given(filesFacade.createTempDirectory()).willReturn(Paths.get("/home/directory"));

    // when
    final User result = ftpUserManager.authenticate(authentication);

    // then
    then(result.getName()).isEqualTo("username");
  }

  @Test
  public void shouldNotAuthenticateWithInvalidPassword() {
    // given
    final Authentication authentication = new UsernamePasswordAuthentication("username", "invalid-password");
    given(properties.getPassword()).willReturn("password");

    // when
    final Throwable thrown = catchThrowable(() -> ftpUserManager.authenticate(authentication));

    // then
    then(thrown).hasMessage("Authentication failed.").isInstanceOf(AuthenticationFailedException.class);
  }

  @Test
  public void shouldNotAuthenticateWithoutUsername() {
    // given
    final Authentication authentication = new UsernamePasswordAuthentication(null, "invalid-password");
    // given(properties.getPassword()).willReturn("password");

    // when
    final Throwable thrown = catchThrowable(() -> ftpUserManager.authenticate(authentication));

    // then
    then(thrown).hasMessage("Authentication failed.").isInstanceOf(AuthenticationFailedException.class);
  }

  @Test
  public void shouldNotAuthenticateWithInvalidAuthentication() {
    // given, when
    final Throwable thrown = catchThrowable(() -> ftpUserManager.authenticate(null));

    // then
    then(thrown).hasMessage("Authentication failed.").isInstanceOf(AuthenticationFailedException.class);
  }

  @Test
  public void shouldReturnUnsupportedOperationForAllUserNames() {
    // given, when
    final Throwable thrown = catchThrowable(() -> ftpUserManager.getAllUserNames());

    // then
    then(thrown).hasMessage("Unsupported operation.").isInstanceOf(FtpException.class);
  }

  @Test
  public void shouldReturnUnsupportedOperationForDelete() {
    // given, when
    final Throwable thrown = catchThrowable(() -> ftpUserManager.delete("username"));

    // then
    then(thrown).hasMessage("Unsupported operation.").isInstanceOf(FtpException.class);
  }

  @Test
  public void shouldReturnUnsupportedOperationForSave() {
    // given, when
    final Throwable thrown = catchThrowable(() -> ftpUserManager.save(aFtpUser()));

    // then
    then(thrown).hasMessage("Unsupported operation.").isInstanceOf(FtpException.class);
  }

  @Test
  public void shouldReturnUnsupportedOperationForAdminName() {
    // given, when
    final Throwable thrown = catchThrowable(() -> ftpUserManager.getAdminName());

    // then
    then(thrown).hasMessage("Unsupported operation.").isInstanceOf(FtpException.class);
  }

  @Test
  public void shouldAffirmDoesExist() throws Exception {
    // given, when
    final boolean result = ftpUserManager.doesExist("username");

    // then
    then(result).isTrue();
  }

  @Test
  public void shouldNegateIsAdmin() throws Exception {
    // given, when
    final boolean result = ftpUserManager.isAdmin("username");

    // then
    then(result).isFalse();
  }

  private FtpUser aFtpUser() {
    return new FtpUser("username", "password", "/home/directory");
  }

}
