package edu.wpi.teamname.ServiceRequests.ConferenceRoom;

import edu.wpi.teamname.databaseredo.orms.Location;
import lombok.Getter;
import lombok.Setter;

public class ConfRoomLocation {
  @Getter @Setter Location location;
  @Getter @Setter int capacity;
  @Getter @Setter String features;

  public ConfRoomLocation(Location location, int capacity, String features) {
    this.location = location;
    this.capacity = capacity;
    this.features = features;
  }
}
