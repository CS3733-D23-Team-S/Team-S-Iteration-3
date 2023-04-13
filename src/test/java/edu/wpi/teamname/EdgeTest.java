package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamname.DAOs.orms.Edge;
import org.junit.jupiter.api.Test;

public class EdgeTest {

    Edge e1 = new Edge(2955, 280);

    @Test
    public void startNodeTest() {
        assertEquals(e1.getStartNodeID(), 2955);
    }

    @Test
    public void endNodeTest() {
        assertEquals(e1.getEndNodeID(), 280);
    }

    @Test
    public void updateEdgeTest() {
        e1.updateEdge(1105, 1165);
        assertEquals(e1.getStartNodeID(), 1105);
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
