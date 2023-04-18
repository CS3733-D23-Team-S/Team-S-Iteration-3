package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.teamname.DAOs.UserDAOImpl;
import edu.wpi.teamname.DAOs.orms.Permission;
import org.junit.jupiter.api.Test;

public class UserDAOImplTest {

  UserDAOImpl userDAOImpl = new UserDAOImpl();

  @Test
  public void loginTest() throws Exception {
    userDAOImpl.createLoginInfo("admin", "admin", Permission.ADMIN);
    assertTrue(userDAOImpl.login("admin", "admin"));
  }

  @Test
  public void loginTest2() throws Exception {
    assertFalse(userDAOImpl.login("nothing", "nothing"));
  }
}
