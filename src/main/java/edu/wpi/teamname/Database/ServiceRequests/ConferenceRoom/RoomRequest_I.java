package edu.wpi.teamname.Database.ServiceRequests.ConferenceRoom;

import java.sql.SQLException;
import java.util.List;

public interface RoomRequest_I {
  public List<ConfRoomRequest> getAllRequests();

  public ConfRoomRequest getRequest(int requestID);

  public void addRequest(ConfRoomRequest request) throws SQLException;

  public void deleteRequest(int requestID);
}
