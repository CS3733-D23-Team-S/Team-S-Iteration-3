package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamname.DAOs.*;
import edu.wpi.teamname.DAOs.orms.Floor;
import edu.wpi.teamname.DAOs.orms.Node;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DAOFacadeTest {
  static DataBaseRepository DBR = DataBaseRepository.getInstance();
  Node n = new Node(100, 2265, 904, Floor.FloorL1, "45 Francis");

  @BeforeAll
  public static void before() {
    DBR.load();
  }

  @Test
  public void getNodeRowTest() {
    Node test = DBR.getNodeDAO().get(100);

    assertEquals(test.getNodeID(), n.getNodeID());
  }

  @Test
  public void getNodebyXYTest() {
    Node test = DBR.getNodebyXY(2265, 904);

    assertEquals(test.getNodeID(), n.getNodeID());
  }

  @Test
  public void addNodeTest() {
    Node test = new Node(55555, 245, 245, Floor.FloorL1, "Tower");
    DBR.getNodeDAO().add(test);

    boolean found = false;
    for (Node aNode : DBR.getNodeDAO().getNodes().values()) {
      if (aNode.getNodeID() == test.getNodeID()) {
        found = true;
        break;
      }
    }
    assertTrue(found);
    DBR.getNodeDAO().delete(55555);
  }

  @Test
  public void removeNodeTest() {
    Node test = new Node(55555, 245, 245, Floor.FloorL1, "Tower");
    DBR.getNodeDAO().add(test);

    DBR.getNodeDAO().delete(55555);

    boolean found = false;
    for (Node aNode : DBR.getNodeDAO().getNodes().values()) {
      if (aNode.getNodeID() == test.getNodeID()) {
        found = true;
        break;
      }
    }
    assertFalse(found);
  }

  @Test
  public void addNodeWithAtsTest() {
    DBR.getNodeDAO().add(55555, 245, 245, Floor.FloorL1, "Tower");

    boolean found = false;
    for (Node aNode : DBR.getNodeDAO().getNodes().values()) {
      if (aNode.getNodeID() == 55555) {
        found = true;
        break;
      }
    }
    assertTrue(found);
    DBR.getNodeDAO().delete(55555);
  }

  @Test
  public void NodetoStringTest() {
    String nodee =
        "Node{"
            + "nodeID = "
            + n.getNodeID()
            + "  "
            + "coords = ("
            + n.getXCoord()
            + ", "
            + n.getYCoord()
            + ") "
            + "floor = "
            + n.getFloor()
            + "  "
            + "building = "
            + n.getBuilding()
            + "}";

    assertEquals(nodee, n.toString());
  }
}
