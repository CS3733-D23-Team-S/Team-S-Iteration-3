package edu.wpi.teamname.ServiceRequests.ConferenceRoom;

import edu.wpi.teamname.Database.dbConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class RoomRequestDAO implements RoomRequest_I {
  protected static final String schemaName = "hospitaldb";
  protected final String roomReservationsTable = schemaName + "." + "roomReservations";
  HashMap<Integer, ConfRoomRequest> requests = new HashMap<Integer, ConfRoomRequest>();
  dbConnection connection = dbConnection.getInstance();
  static RoomRequestDAO single_instance = null;

  private RoomRequestDAO() {}

  public static synchronized RoomRequestDAO getInstance() {

    if (single_instance == null) single_instance = new RoomRequestDAO();

    return single_instance;
  }

  public void initTable() throws SQLException {
    Statement stmt = connection.getConnection().createStatement();
    String roomReservationsTableConstruct =
        "CREATE TABLE IF NOT EXISTS "
            + roomReservationsTable
            + " "
            + "(reservationID int,"
            + "dateordered Date,"
            + "startTime Time,"
            + "endTime Time,"
            + "room Varchar(100),"
            + "reservedBy Varchar(100),"
            + "eventName Varchar(100),"
            + "eventDescription Varchar(100),"
            + "assignedTo Varchar(100),"
            + "orderStatus Varchar(100),"
            + "notes Varchar(500))";
    try {
      stmt.execute(roomReservationsTableConstruct);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("Room reservation creation error");
    }
    System.out.println("Room reservations table created");
  }

  @Override
  public List<ConfRoomRequest> getAllRequests() {
    return this.requests.values().stream().toList();
  }

  @Override
  public ConfRoomRequest getRequest(int requestID) {
    return requests.get(requestID);
  }

  @Override
  public void addRequest(ConfRoomRequest request) {

    int resID = 0;

    requests.put(request.getReservationID(), request);

    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + roomReservationsTable
                      + " (reservationID, dateordered, startTime, endTime, room, reservedBy, eventName, eventDescription, assignedTo, orderStatus, notes) "
                      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      preparedStatement.setInt(1, resID);
      preparedStatement.setDate(2, null);
      preparedStatement.setTime(3, null);
      preparedStatement.setTime(4, null);
      preparedStatement.setString(5, "ROOM"); // TODO add room id
      preparedStatement.setString(6, request.getReservedBy());
      preparedStatement.setString(7, request.getEventName());
      preparedStatement.setString(8, request.getEventDescription());
      preparedStatement.setString(9, request.getAssignedTo());
      preparedStatement.setString(10, request.getOrderStatus().name()); // TODO fix
      preparedStatement.setString(11, request.getNotes());
      preparedStatement.executeUpdate();

      resID++;
      System.out.println("New Reservation ID: " + resID);

      //      preparedStatement.executeUpdate();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void deleteRequest(int requestID) {
    requests.remove(requestID);
  }
}
