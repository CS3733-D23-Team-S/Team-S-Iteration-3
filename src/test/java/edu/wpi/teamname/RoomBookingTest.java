package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.NodeType;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomLocation;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class RoomBookingTest {
  Location l1 = new Location("Unity Hall", "UH", NodeType.CONF);
  ConfRoomLocation roomLocation = new ConfRoomLocation(l1, 30, "AirConditioner");
  static DataBaseRepository dbr = DataBaseRepository.getInstance();

  ConfRoomRequest confRoomRequest = null;
  ConfRoomRequest confRoomRequest2 = null;

  @Test
  public void addRequestTest() {
    confRoomRequest =
        new ConfRoomRequest(
            LocalDate.of(2022, 5, 13),
            LocalTime.of(14, 45),
            LocalTime.of(15, 10),
            "BTM Conference Center",
            "staff",
            "nothing",
            "Yes",
            "staff",
            true);
    dbr.getRoomRequestDAO().add(confRoomRequest);
    assertEquals(1, dbr.getRoomRequestDAO().getRequests().size());
  }

  @Test public void requestErrorsTest() {
    confRoomRequest =
            new ConfRoomRequest(
                    LocalDate.of(2022, 5, 13),
                    LocalTime.of(14, 45),
                    LocalTime.of(15, 10),
                    "BTM Conference Center",
                    "staff",
                    "nothing",
                    "Yes",
                    "staff",
                    true);
    dbr.getRoomRequestDAO().add(confRoomRequest);
    assertEquals(1, dbr.getRoomRequestDAO().getRequests().size());
    confRoomRequest2 = new ConfRoomRequest(LocalDate.of(2022, 5, 13), LocalTime.of(14, 45, ))
  }


  @Test public void initializingLocationsTest() {
    // confRoomDAO.initializeLocations();
    // assertEquals(7, confRoomDAO.conferenceRooms.size());
  }

  @org.junit.Test
  public void deleteRequestTest() {
    confRoomRequest =
        new ConfRoomRequest(
            LocalDate.of(2022, 5, 13),
            LocalTime.of(14, 45),
            LocalTime.of(15, 10),
            "BTM Conference Center",
            "staff",
            "nothing",
            "Yes",
            "staff",
            true);
    dbr.getRoomRequestDAO().add(confRoomRequest);
    dbr.getRoomRequestDAO().delete(confRoomRequest.getRoom());
    assertFalse(dbr.getRoomRequestDAO().getRequests().contains(confRoomRequest));
  }
}
