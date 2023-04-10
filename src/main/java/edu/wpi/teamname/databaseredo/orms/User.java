package edu.wpi.teamname.databaseredo.orms;

import edu.wpi.teamname.databaseredo.IDataPack;
import lombok.Getter;
import lombok.Setter;

public class User implements IDataPack {

  @Getter private String userName;
  @Getter private String password;
  @Getter @Setter private Permission permission;

  public User(String name, String pass, Permission perm) {
    this.userName = name;
    this.password = pass;
    this.permission = perm;
  }

  public boolean checkLogin(String password) {
    return this.password.equals(password);
  }

  @Override
  public String toString() {
    return super.toString();
  }

  @Override
  public String toCSVString() {

    return userName + "," + password + "," + permission.ordinal();
  }

  public enum Permission {
    ADMIN,
    STAFF,
    GUEST,
  }
}
