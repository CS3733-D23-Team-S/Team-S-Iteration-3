package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.Room;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.Status;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class RoomBookingTest {
  RoomRequestDAO roomRequestDAO = RoomRequestDAO.getInstance();
  int id = 18;
  ConfRoomRequest res1 =
      new ConfRoomRequest(
          LocalDate.now(),
          LocalTime.of(6, 0, 0, 0),
          LocalTime.of(8, 0, 0, 0),
          new Room(Integer.parseInt("1"), "Cafe", "Floor", 50, "F"),
          "Sarah Kogan",
          "Checking for update",
          "description description description",
          null,
          Status.Received,
          "");

  @Test
  public void addtoDaoTest() {

    res1.setReservationID(id++);
    System.out.println(res1.getReservationID());
    roomRequestDAO.addRequest(res1);
    assertEquals(roomRequestDAO.getRequest(id), res1);
  }

  @Test
  public void deleteTest() {
    roomRequestDAO.deleteRequest(id);
    assertNull(roomRequestDAO.getRequest(id));
  }
}
