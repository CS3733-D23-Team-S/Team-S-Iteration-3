package edu.wpi.teamname.DAOs.orms;

import edu.wpi.teamname.DAOs.IDataPack;
import java.sql.Date;
import lombok.Getter;
import lombok.Setter;

public class Signage implements IDataPack {
  @Getter @Setter int kioskLocation;
  @Getter @Setter Direction direction;
  @Getter @Setter Location surroundingLocation;
  @Getter @Setter Date thedate;

  public enum Direction {
    up,
    down,
    left,
    right,
    stop
  }

  public Signage(int kioskLocation, Direction direction, Location surroundingLocation, Date d) {
    this.direction = direction;
    this.kioskLocation = kioskLocation;
    this.surroundingLocation = surroundingLocation;
    this.thedate = d;
  }

  @Override
  public String toCSVString() {
    return kioskLocation
        + ","
        + direction.name()
        + ","
        + surroundingLocation
        + ","
        + thedate.toString();
  }
}
