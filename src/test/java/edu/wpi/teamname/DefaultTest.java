/*-------------------------*/
/* DO NOT DELETE THIS TEST */
/*-------------------------*/

package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamname.Database.LoaderDAO;
import edu.wpi.teamname.databaseredo.LoginDAOImpl;
import edu.wpi.teamname.pathfinding.AStar;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DefaultTest {

  @BeforeAll
  static void setup() throws SQLException {
    LoaderDAO loaderDAO = LoaderDAO.getInstance();
    loaderDAO.load();
  }

  @Test
  public void testAlgorithm() {
    AStar a = new AStar();
    List<Integer> res = new ArrayList<>();
    res.add(1805);
    res.add(1810);

    assertEquals(a.findPath("75 Lobby", "75 Lobby Information Desk"), res);

    List<Integer> res2 = new ArrayList<>();
    res2.add(640);
    res2.add(645);
    res2.add(650);
    res2.add(655);
    res2.add(660);

    assertEquals(a.findPath("Staircase K1 Floor 1", "Ambulance Parking Exit Floor 1"), res2);
  }

  @Test
  public void testLogin() throws Exception {
    LoginDAOImpl LDaoI = LoginDAOImpl.getInstance();
    Exception exception = assertThrows(Exception.class, () -> LDaoI.login("aaaa", "bbbb"));
    assertEquals("User does not exist", exception.getMessage());
    assertTrue(LDaoI.login("admin", "admin"));
  }
}
