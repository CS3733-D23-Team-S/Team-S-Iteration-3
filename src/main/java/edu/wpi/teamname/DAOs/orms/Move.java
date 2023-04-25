package edu.wpi.teamname.DAOs.orms;

import edu.wpi.teamname.DAOs.IDataPack;
import java.time.LocalDate;
import lombok.Getter;

public class Move implements IDataPack {
  @Getter private final Node node;
  @Getter private final Location location;
  @Getter private final LocalDate date;

  public Move(Node node, Location location, LocalDate date) {
    this.node = node;
    this.date = date;
    this.location = location;
  }

  @Override
  public String toString() {
    return "Move{"
        + "nodeID = "
        + node.getNodeID()
        + ", location = '"
        + location.getLongName()
        + ", date = "
        + date.toString()
        + '}';
  }

  @Override
  public String toCSVString() {
    return node.getNodeID() + "," + location.getLongName() + "," + date;
  }

  public int getNodeID() {
    return node.getNodeID();
  }

  public String getLocationName() {
    return location.getLongName();
  }
}
