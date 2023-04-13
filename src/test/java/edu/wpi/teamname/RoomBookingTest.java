package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.NodeType;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomDAO;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomLocation;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class RoomBookingTest {
  Location l1 = new Location("Unity Hall", "UH", NodeType.CONF);
  ConfRoomLocation roomLocation = new ConfRoomLocation(l1, 30, "AirConditioner");
  ConfRoomDAO confRoomDAO = new ConfRoomDAO();

  RoomRequestDAO roomRequestDAO = new RoomRequestDAO();

  ConfRoomRequest confRoomRequest = null;

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
            "staff");
    roomRequestDAO.add(confRoomRequest);
    assertEquals(1, roomRequestDAO.getRequests().size());
  }

  @Test
  public void initializingLocationsTest() {
    confRoomDAO.initializeLocations();
    assertEquals(7, confRoomDAO.conferenceRooms.size());
  }

  @Test
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
            "staff");
    roomRequestDAO.add(confRoomRequest);
    roomRequestDAO.delete(confRoomRequest);
    assertFalse(roomRequestDAO.getRequests().contains(confRoomRequest));
  }
}
