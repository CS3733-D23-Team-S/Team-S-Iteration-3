package edu.wpi.teamname.Login;

import edu.wpi.teamname.databaseredo.orms.User;
import lombok.Getter;
import lombok.Setter;

public class LoginInfo {
  @Setter @Getter private String username;
  @Setter @Getter private String password;
  @Setter @Getter private User.Permission permission;

  public LoginInfo(String username, String password, User.Permission permission) {
    this.username = username;
    this.password = password;
    this.permission = permission;
  }
}
