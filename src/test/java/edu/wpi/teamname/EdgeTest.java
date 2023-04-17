package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.NodeDAOImpl;
import edu.wpi.teamname.DAOs.orms.Edge;
import org.junit.jupiter.api.Test;

public class EdgeTest {
  NodeDAOImpl nodeDAO = DataBaseRepository.getInstance().getNodeDAO();
  Edge e1 = new Edge(nodeDAO.get(2955), nodeDAO.get(280));

  @Test
  public void startNodeTest() {
    assertEquals(e1.getStartNode().getNodeID(), 2955);
  }

  @Test
  public void endNodeTest() {
    assertEquals(e1.getEndNode().getNodeID(), 280);
  }

  @Test
  public void updateEdgeTest() {
    e1.updateEdge(nodeDAO.get(1105), nodeDAO.get(1165));
    assertEquals(e1.getStartNode().getNodeID(), 1105);
  }

  @Test
  public void toStringTest() {
    assertEquals(e1.toString(), "Edge{startNode = 2955, endNode = 280}");
  }

  @Test
  public void toCSVStringTest() {
    assertEquals(e1.toCSVString(), "2955,280");
  }
}
