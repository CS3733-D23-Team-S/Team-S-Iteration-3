package edu.wpi.teamname.databaseredo.orms;

import edu.wpi.teamname.databaseredo.IDataPack;
import lombok.Getter;
import lombok.Setter;

public class Location implements IDataPack {
  @Getter @Setter private NodeType nodeType;
  @Getter @Setter private String longName;
  @Getter @Setter private String shortName;

  @Getter @Setter private Node node;

  public Location(String longName, String shortName, NodeType nodeType) {
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
  }

  @Override
  public String toString() {
    return "Location{notetype = "
        + this.nodeType.toString()
        + ", longname =  "
        + this.longName
        + ", shortname = "
        + this.shortName
        + "}";
  }

  @Override
  public String toCSVString() {
    return longName + "," + shortName + "," + nodeType.toString();
  }
}
