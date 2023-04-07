package edu.wpi.teamname.Database.Login;

import lombok.Getter;
import lombok.Setter;

public class LoginInfo {
  @Setter @Getter private String username;
  @Setter @Getter private String password;
  @Setter @Getter private Permission permission;

  public LoginInfo(String username, String password, Permission permission) {
    this.username = username;
    this.password = password;
    this.permission = permission;
  }
}
