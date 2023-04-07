package edu.wpi.teamname;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class FacilitiesStaffEntity {

  @Setter @Getter ArrayList<RoomBooking> listOfRoomBookings;

  public FacilitiesStaffEntity(ArrayList<RoomBooking> listOfRoomBookings) {
    this.listOfRoomBookings = listOfRoomBookings;
  }
}
