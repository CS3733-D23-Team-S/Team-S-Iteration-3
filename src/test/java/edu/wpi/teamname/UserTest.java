package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamname.DAOs.orms.Permission;
import edu.wpi.teamname.DAOs.orms.User;
import org.junit.jupiter.api.Test;

public class UserTest {

  User user = new User("GenericAdmin", "admin", "admin", Permission.ADMIN);

  @Test
  public void checkLoginTest() {
    user.checkLogin("admin");
  }

  @Test
  public void userTest() {
    assertEquals(user.getUserName(), "admin");
  }

  @Test
  public void toCSVStringTest() {
    assertEquals(user.toCSVString(), "admin,admin");
  }
}
