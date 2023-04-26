package edu.wpi.teamname.DAOs.orms;

import edu.wpi.teamname.DAOs.IDataPack;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class User implements IDataPack {

  @Getter @Setter private String userName;

  @Getter @Setter private String password;
  @Getter @Setter private String firstName;
  @Getter @Setter private String lastName;

  @Getter @Setter private String sessionID;
  @Getter @Setter private Permission permission;
  @Getter @Setter private String email;
  @Getter @Setter private LocalDate DOB;
  @Getter @Setter private String title;
  @Getter @Setter private List<SRItem> cart;

  public User(
      String name,
      String pass,
      String first,
      String last,
      Permission perm,
      String e,
      String title) {
    this.userName = name;
    this.password = pass;
    this.firstName = first;
    this.lastName = last;
    this.email = e;
    this.permission = perm;
    this.title = title;
  }

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
    return "User= { Username: "
        + userName
        + ", Password: "
        + password
        + ", AccountType: "
        + permission
        + "}";
  }

  @Override
  public String toCSVString() {

    return userName + "," + password;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }
}
