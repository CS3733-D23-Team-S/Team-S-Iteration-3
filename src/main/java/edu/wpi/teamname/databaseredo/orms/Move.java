package edu.wpi.teamname.databaseredo.orms;

import edu.wpi.teamname.databaseredo.IDataPack;
import java.time.LocalDate;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class Move implements IDataPack {
  @Getter @Setter private int nodeID;
  @Getter @Setter private String location;
  @Getter @Setter private ArrayList<LocalDate> dates = new ArrayList<>();

  public Move(int nodeID, String location, LocalDate date) {
    this.nodeID = nodeID;
    this.dates.add(date);
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
        + dates.toString()
        + '}';
  }

  @Override
  public String toCSVString() {
    return nodeID + "," + location + "," + dates.get(0);
  }
}
