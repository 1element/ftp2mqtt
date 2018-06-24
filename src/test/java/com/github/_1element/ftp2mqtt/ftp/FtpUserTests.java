package com.github._1element.ftp2mqtt.ftp;

import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.mock;

public class FtpUserTests {

  @Test
  public void shouldReturnName() {
    // given
    final FtpUser ftpUser = aFtpUser();

    // when
    final String result = ftpUser.getName();

    // then
    then(result).isEqualTo("username");
  }

  @Test
  public void shouldReturnPassword() {
    // given
    final FtpUser ftpUser = aFtpUser();

    // when
    final String result = ftpUser.getPassword();

    // then
    then(result).isEqualTo("password");
  }

  @Test
  public void shouldReturnAuthorities() {
    // given
    final FtpUser ftpUser = aFtpUser();

    // when
    final List<? extends Authority> result = ftpUser.getAuthorities();

    // then
    then(result).hasSize(1).extracting(element -> then(element).isInstanceOf(WritePermission.class));
  }

  @Test
  public void shouldReturnAuthoritiesForClassWritePermission() {
    // given
    final FtpUser ftpUser = aFtpUser();

    // when
    final List<? extends Authority> result = ftpUser.getAuthorities(WritePermission.class);

    // then
    then(result).hasSize(1).extracting(element -> then(element).isInstanceOf(WritePermission.class));
  }

  @Test
  public void shouldNotReturnAuthoritiesForUnknownClass() {
    // given
    final FtpUser ftpUser = aFtpUser();

    // when
    final List<? extends Authority> result = ftpUser.getAuthorities(TransferRatePermission.class);

    // then
    then(result).isEmpty();
  }

  @Test
  public void shouldAuthorize() {
    // given
    final FtpUser ftpUser = aFtpUser();
    final AuthorizationRequest authorizationRequest = mock(AuthorizationRequest.class);

    // when
    final AuthorizationRequest result = ftpUser.authorize(authorizationRequest);

    // then
    then(result).isEqualTo(authorizationRequest);
  }

  @Test
  public void shouldReturnMaxIdleTime() {
    // given
    final FtpUser ftpUser = aFtpUser();

    // when
    final int result = ftpUser.getMaxIdleTime();

    // then
    then(result).isEqualTo(0);
  }

  @Test
  public void shouldReturnEnabled() {
    // given
    final FtpUser ftpUser = aFtpUser();

    // when
    final boolean result = ftpUser.getEnabled();

    // then
    then(result).isTrue();
  }

  @Test
  public void shouldReturnHomeDirectory() {
    // given
    final FtpUser ftpUser = aFtpUser();

    // when
    final String result = ftpUser.getHomeDirectory();

    // then
    then(result).isEqualTo("/home/directory");
  }

  private FtpUser aFtpUser() {
    return new FtpUser("username", "password", "/home/directory");
  }

}
