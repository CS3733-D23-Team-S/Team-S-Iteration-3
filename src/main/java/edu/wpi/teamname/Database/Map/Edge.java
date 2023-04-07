package edu.wpi.teamname.Database.Map;

import lombok.Getter;
import lombok.Setter;

public class Edge {
  @Getter @Setter private Node startNode;
  @Getter @Setter private Node endNode;

  public Edge(Node sN, Node eN) {
    startNode = sN;
    endNode = eN;
  }

  public void updateEdge(Node sN, Node eN) {
    startNode = sN;
    endNode = eN;
  }

  @Override
  public String toString() {
    return "Edge{"
        + "startNode = "
        + startNode.getNodeID()
        + ", endNode = "
        + endNode.getNodeID()
        + '}';
  }

  public String toCSVString() {
    return startNode.getNodeID() + "," + endNode.getNodeID();
  }
}
