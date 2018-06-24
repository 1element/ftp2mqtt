package com.github._1element.ftp2mqtt.ftp;

import com.github._1element.ftp2mqtt.common.FilesFacade;
import lombok.RequiredArgsConstructor;
import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FtpUserManager implements UserManager {

  private final FilesFacade filesFacade;
  private final FtpProperties properties;

  private static final String UNSUPPORTED_OPERATION = "Unsupported operation.";
  private static final String AUTHENTICATION_FAILED = "Authentication failed.";

  @Override
  public User getUserByName(String username) {
    try {
      return new FtpUser(username, properties.getPassword(), createTemporaryHomeDirectory());
    } catch (IOException exception) {
      throw new IllegalStateException("Could not create temporary directory for incoming files.", exception);
    }
  }

  @Override
  public String[] getAllUserNames() throws FtpException {
    throw new FtpException(UNSUPPORTED_OPERATION);
  }

  @Override
  public void delete(String username) throws FtpException {
    throw new FtpException(UNSUPPORTED_OPERATION);
  }

  @Override
  public void save(User user) throws FtpException {
    throw new FtpException(UNSUPPORTED_OPERATION);
  }

  @Override
  public boolean doesExist(String username) throws FtpException {
    return true;
  }

  @Override
  public User authenticate(Authentication authentication) throws AuthenticationFailedException {
    if (authentication instanceof UsernamePasswordAuthentication) {
      final UsernamePasswordAuthentication usernamePasswordAuthentication = (UsernamePasswordAuthentication) authentication;
      return authenticateBy(usernamePasswordAuthentication.getUsername(), usernamePasswordAuthentication.getPassword());
    }

    throw new AuthenticationFailedException(AUTHENTICATION_FAILED);
  }

  @Override
  public String getAdminName() throws FtpException {
    throw new FtpException(UNSUPPORTED_OPERATION);
  }

  @Override
  public boolean isAdmin(String username) throws FtpException {
    return false;
  }

  private String createTemporaryHomeDirectory() throws IOException {
    return filesFacade.createTempDirectory().toString();
  }

  private User authenticateBy(String username, String password) throws AuthenticationFailedException {
    if (username == null) {
      throw new AuthenticationFailedException(AUTHENTICATION_FAILED);
    }

    if (!properties.getPassword().equals(password)) {
      throw new AuthenticationFailedException(AUTHENTICATION_FAILED);
    }

    return getUserByName(username);
  }

}
