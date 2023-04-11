package edu.wpi.teamname.databaseredo;

import java.util.HashSet;

import edu.wpi.teamname.databaseredo.orms.User;
import lombok.Getter;
import lombok.Setter;

public class ActiveUser {

  @Getter
  @Setter
  User currentUser;
  private dbConnection connection;

  private static ActiveUser single_instance = null;

  private ActiveUser() {}


  public static synchronized ActiveUser getInstance() {
    if (single_instance == null) single_instance = new ActiveUser();
    return single_instance;
  }
}
