package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class RoomBookingTest {
  RoomRequestDAO roomRequestDAO = RoomRequestDAO.getInstance();
  int id = 18;
  ConfRoomRequest res1 =
      new ConfRoomRequest(
          LocalDate.of(12, 12, 4),
          LocalTime.of(6, 0, 0, 0),
          LocalTime.of(8, 0, 0, 0),
          "Conf",
          "Sarah Kogan",
          "Checking for update",
          "description description description");

  @Test
  public void addtoDaoTest() {

    roomRequestDAO.addRequest(res1);
    assertEquals(roomRequestDAO.getRequest(id), res1);
  }

  @Test
  public void deleteTest() {
    roomRequestDAO.deleteRequest(id);
    assertNull(roomRequestDAO.getRequest(id));
  }
}
