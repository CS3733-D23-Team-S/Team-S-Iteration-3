package edu.wpi.teamname;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.dbConnection;
import edu.wpi.teamname.DAOs.orms.Permission;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserDAOImplTest {
  static DataBaseRepository dbr = DataBaseRepository.getInstance();

  @BeforeAll
  public static void start() {
    // Init database shit fuck
    dbr.getUserDAO().loadRemote("k");
  }

  @Test
  public void loginTest() throws Exception {
    assertTrue(dbr.getUserDAO().login("admin", "admin"));
  }

  // Current Active User
  @Test
  public void testNewUserCreationAndLoginWithActiveUser() throws Exception {

    dbr.getUserDAO().createLoginInfo("test", "test", Permission.STAFF);
    dbr.getUserDAO().login("test", "test");
    assertEquals(ActiveUser.getInstance().getCurrentUser().getPermission().toString(), "STAFF");
    assertEquals(ActiveUser.getInstance().getCurrentUser().getUserName(), "test");
    assertEquals(ActiveUser.getInstance().getCurrentUser().getPassword(), "test");
    dbr.getUserDAO().delete("test");
  }

  @Test
  public void testForNonexistantUser() {
    Exception exception =
        assertThrows(
            Exception.class,
            () -> {
              dbr.login("aaa", "bbb");
            });

    String expectedMessage = "User does not exist";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void testForAdminWithinDatabase() throws Exception {
    assertTrue(dbr.login("admin", "admin"));
  }

  @Test
  public void testForStaffWithinDatabase() throws Exception {
    assertTrue(dbr.login("admin", "admin"));
  }

  // FIX
  /*@Test
  public void testForAll() throws Exception {
    System.out.println(userDAOImpl.getAll());
  }*/

  @AfterAll
  public static void closeDB() throws SQLException {
    dbConnection.getInstance().getConnection().close();
  }
}
