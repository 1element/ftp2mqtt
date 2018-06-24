package com.github._1element.ftp2mqtt.ftp;

import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FtpUser implements User {

  private final String username;
  private final String password;
  private final String homeDirectory;
  private final ArrayList<Authority> authorities = new ArrayList<>();

  public FtpUser(String username, String password, String homeDirectory) {
    this.username = username;
    this.password = password;
    this.homeDirectory = homeDirectory;

    authorities.add(new WritePermission());
  }

  @Override
  public String getName() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public List<? extends Authority> getAuthorities() {
    return authorities;
  }

  @Override
  public List<? extends Authority> getAuthorities(Class<? extends Authority> clazz) {
    return authorities.stream()
      .filter(authority -> authority.getClass().equals(clazz))
      .collect(toList());
  }

  @Override
  public AuthorizationRequest authorize(AuthorizationRequest request) {
    return request;
  }

  @Override
  public int getMaxIdleTime() {
    return 0;
  }

  @Override
  public boolean getEnabled() {
    return true;
  }

  @Override
  public String getHomeDirectory() {
    return homeDirectory;
  }

}
