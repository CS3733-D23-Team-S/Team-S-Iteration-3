package edu.wpi.teamname.User;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

public class User {
  @Setter @Getter private String username;
  @Setter @Getter private String password;
  @Setter @Getter private Permission permission;
  @Getter @Setter String firstName;
  @Getter @Setter String lastName;
  @Getter @Setter LocalDate DOB;

  public User(String username, String password, Permission permission) {
    this.username = username;
    this.password = password;
    this.permission = permission;
  }
}
