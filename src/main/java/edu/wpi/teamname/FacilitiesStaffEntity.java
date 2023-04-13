package edu.wpi.teamname;

import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class FacilitiesStaffEntity {

  @Setter @Getter ArrayList<ConfRoomRequest> listOfRoomBookings;

  public FacilitiesStaffEntity(ArrayList<ConfRoomRequest> listOfRoomBookings) {
    this.listOfRoomBookings = listOfRoomBookings;
  }
}
