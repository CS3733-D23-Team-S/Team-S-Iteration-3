package edu.wpi.teamname.DAOs.orms;

import edu.wpi.teamname.DAOs.IDataPack;
import lombok.Getter;
import lombok.Setter;

public class Signage implements IDataPack {
  @Getter @Setter Location kioskLocation;
  @Getter @Setter Direction direction;
  @Getter @Setter String surroundingLocation;

  public enum Direction {
    up,
    down,
    left,
    right,
    stop
  }

  public Signage(Location kioskLocation, Direction direction, String surroundingLocation) {
    this.direction = direction;
    this.kioskLocation = kioskLocation;
    this.surroundingLocation = surroundingLocation;
  }

  @Override
  public String toCSVString() {
    return kioskLocation + "," + direction.name() + "," + surroundingLocation;
  }
}
