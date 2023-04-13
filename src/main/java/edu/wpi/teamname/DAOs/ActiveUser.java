package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.User;
import lombok.Getter;
import lombok.Setter;

public class ActiveUser {

  @Getter @Setter User currentUser;
  private dbConnection connection;

  private static ActiveUser single_instance = null;

  private ActiveUser() {}

  public static synchronized ActiveUser getInstance() {
    if (single_instance == null) single_instance = new ActiveUser();
    return single_instance;
  }
}
