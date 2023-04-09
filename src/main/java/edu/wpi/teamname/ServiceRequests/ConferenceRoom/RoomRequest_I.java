package edu.wpi.teamname.ServiceRequests.ConferenceRoom;

import java.sql.SQLException;
import java.util.List;

public interface RoomRequest_I {
  List<ConfRoomRequest> getAllRequests();

  ConfRoomRequest getRequest(int requestID);

  void addRequest(ConfRoomRequest request) throws SQLException;

  void deleteRequest(int requestID);
}
