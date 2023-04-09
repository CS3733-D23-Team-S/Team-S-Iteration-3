package edu.wpi.teamname.databaseredo;

import java.util.HashSet;
import lombok.Getter;

public class ActiveUser {

  @Getter private HashSet<String> activeUsers;
  private dbConnection connection;

  private static ActiveUser single_instance = null;

  private ActiveUser() {
    this.activeUsers = new HashSet<>();
  }

  public static synchronized ActiveUser getInstance() {
    if (single_instance == null) single_instance = new ActiveUser();
    return single_instance;
  }

  public boolean checkUserActive(String username) {
    return activeUsers.contains(username);
  }
}
