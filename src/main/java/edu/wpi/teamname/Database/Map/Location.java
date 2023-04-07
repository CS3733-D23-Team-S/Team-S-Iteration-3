package edu.wpi.teamname.Database.Map;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

public class Location {
  @Getter @Setter private NodeType nodeType;
  @Getter @Setter private String longName;
  @Getter @Setter private String shortName;
  @Getter @Setter private LocalDate mostRecentMove = null;

  @Getter @Setter private Node node;

  public Location(String longName, String shortName, NodeType nodeType) {
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
  }

  public void updateDate(LocalDate date) {
    this.mostRecentMove = date;
  }

  public boolean checkDateEquals(LocalDate date) {
    return this.mostRecentMove.equals(date);
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

  public String toCSVString() {
    return longName + "," + shortName + "," + nodeType.toString();
  }
}
