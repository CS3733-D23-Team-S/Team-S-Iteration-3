package edu.wpi.teamname.DAOs.orms;

import edu.wpi.teamname.DAOs.IDataPack;
import java.time.LocalDate;
import lombok.Getter;

public class Move implements IDataPack {
  @Getter private int nodeID;
  @Getter private String location;
  @Getter private LocalDate date;

  public Move(int nodeID, String location, LocalDate date) {
    this.nodeID = nodeID;
    this.date = date;
    this.location = location;
  }

  @Override
  public String toString() {
    return "Move{"
        + "nodeID = "
        + nodeID
        + ", location = '"
        + location
        + ", date = "
        + date.toString()
        + '}';
  }

  @Override
  public String toCSVString() {
    return nodeID + "," + location + "," + date;
  }
}
