package edu.wpi.teamname.databaseredo.orms;

import edu.wpi.teamname.databaseredo.IDataPack;
import lombok.Getter;
import lombok.Setter;

public class Edge implements IDataPack {
  @Getter @Setter private int startNodeID;
  @Getter @Setter private int endNodeID;

  public Edge(int sN, int eN) {
    startNodeID = sN;
    endNodeID = eN;
  }

  public void updateEdge(int sN, int eN) {
    startNodeID = sN;
    endNodeID = eN;
  }

  @Override
  public String toString() {
    return "Edge{"
        + "startNode = "
        + startNodeID.getNodeID()
        + ", endNode = "
        + endNodeID.getNodeID()
        + '}';
  }

  @Override
  public String toCSVString() {
    return startNodeID.getNodeID() + "," + endNodeID.getNodeID();
  }
}
