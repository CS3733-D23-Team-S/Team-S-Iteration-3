package edu.wpi.teamname.DAOs.orms;

import edu.wpi.teamname.DAOs.IDataPack;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class User implements IDataPack {

  @Getter private String name;
  @Getter private String userName;
  @Getter private String password;
  @Getter @Setter private Permission permission;
  @Getter @Setter private LocalDate DOB;
  @Getter @Setter private String title;
  @Getter @Setter private List<SRItem> cart;

  public User(String person, String name, String pass, Permission perm) {
    this.name = person;
    this.userName = name;
    this.password = pass;
    this.permission = perm;
  }

  public boolean checkLogin(String password) {
    return this.password.equals(password);
  }

  @Override
  public String toString() {
    return "User= { Username: "
        + userName
        + ", Password: "
        + password
        + ", AccountType: "
        + permission.ordinal()
        + "}";
  }

  @Override
  public String toCSVString() {

    return userName + "," + password + "," + permission.ordinal();
  }
}
