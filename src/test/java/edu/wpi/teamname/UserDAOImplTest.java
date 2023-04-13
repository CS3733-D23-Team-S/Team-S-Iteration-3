package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamname.DAOs.UserDAOImpl;
import org.junit.jupiter.api.Test;

public class UserDAOImplTest {

  UserDAOImpl userDAOImpl = new UserDAOImpl();

  @Test
  public void loginTest() throws Exception {
    userDAOImpl.createLoginInfo("admin", "admin");
    assertTrue(userDAOImpl.login("admin", "admin"));
  }

  @Test
  public void loginTest2() throws Exception {
    assertFalse(userDAOImpl.login("nothing", "nothing"));
  }
}
