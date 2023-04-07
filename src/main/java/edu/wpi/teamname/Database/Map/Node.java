package edu.wpi.teamname.Database.Map;

import lombok.Getter;
import lombok.Setter;

public class Node {
  @Getter @Setter private int nodeID;
  @Getter @Setter private int xCoord;
  @Getter @Setter private int yCoord;
  @Getter @Setter private Floor floor;
  @Getter @Setter private String building;

  public Node(int nodeID, int xCoord, int yCoord, Floor floor, String building) {

    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.floor = floor;
    this.building = building;
  }

  @Override
  public String toString() {

    return "Node{"
        + "nodeID = "
        + nodeID
        + "  "
        + "coords = ("
        + xCoord
        + ", "
        + yCoord
        + ") "
        + "floor = "
        + floor
        + "  "
        + "building = "
        + building
        + "}";
  }

  public String toCSVString() {
    return nodeID
        + ","
        + xCoord
        + ","
        + yCoord
        + ","
        + floor.toString().replaceAll("[a-zA-Z]", "")
        + ","
        + building;
  }
}
