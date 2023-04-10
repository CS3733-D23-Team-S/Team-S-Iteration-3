/*-------------------------*/
/* DO NOT DELETE THIS TEST */
/*-------------------------*/

package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.orms.Location;
import edu.wpi.teamname.databaseredo.orms.NodeType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DefaultTest {
  DataBaseRepository repo;

  /*@BeforeAll
  public static void init() {
    DataBaseRepository repo = DataBaseRepository.getInstance();
    repo.load();
  }*/

  @Test
  public List<String> getListOfEligibleRooms() {
    DataBaseRepository repo = DataBaseRepository.getInstance();
    repo.load();

    List<String> listOfEligibleRooms = new ArrayList<>();
    List<Location> locationList = repo.getLocationDAO().getAll();

    NodeType[] nodeTypes = new NodeType[6];
    nodeTypes[0] = NodeType.ELEV;
    nodeTypes[1] = NodeType.EXIT;
    nodeTypes[2] = NodeType.HALL;
    nodeTypes[3] = NodeType.REST;
    nodeTypes[4] = NodeType.STAI;
    nodeTypes[5] = NodeType.BATH;

    boolean isFound;
    for (Location loc : locationList) {
      isFound = false;
      for (NodeType nt : nodeTypes) {
        if (loc.getNodeType() == nt) {
          isFound = true;
          break;
        }
      }
      if (!isFound) listOfEligibleRooms.add(loc.getLongName());
    }
    Collections.sort(listOfEligibleRooms);

    return listOfEligibleRooms;
  }

  public void setup() throws SQLException {
    DataBaseRepository database = DataBaseRepository.getInstance();
    database.load();
  }

  /*@Test
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
  }*/

  @Test
  public void testLogin() throws Exception {
    //setup();
    //getListOfEligibleRooms().forEach(System.out::println);
    /*LoginDAOImpl LDaoI = LoginDAOImpl.getInstance();
    Exception exception = assertThrows(Exception.class, () -> LDaoI.login("aaaa", "bbbb"));
    assertEquals("User does not exist", exception.getMessage());
    assertTrue(LDaoI.login("admin", "admin"));*/
  }
}
