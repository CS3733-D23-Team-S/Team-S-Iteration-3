package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.UserDAOImpl;
import org.junit.jupiter.api.Test;

public class UserDAOImplTest {
  static DataBaseRepository dbr = DataBaseRepository.getInstance();

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

  // Current Active User
  @Test
  public void testNewUserCreationAndLoginWithActiveUser() throws Exception {
    userDAOImpl.createLoginInfo("test", "test");
    userDAOImpl.login("test", "test");
    assertEquals(ActiveUser.getInstance().getCurrentUser().getPermission().toString(), "STAFF");
    assertEquals(ActiveUser.getInstance().getCurrentUser().getUserName(), "test");
    assertEquals(ActiveUser.getInstance().getCurrentUser().getPassword(), "test");

    // Init database shit fuck
    System.out.println(dbr.getUserDAO().get("admin"));
  }
}
