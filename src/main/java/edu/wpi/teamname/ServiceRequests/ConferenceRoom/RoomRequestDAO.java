package edu.wpi.teamname.ServiceRequests.ConferenceRoom;

import edu.wpi.teamname.DAOs.IDAO;
import edu.wpi.teamname.DAOs.dbConnection;
import lombok.Getter;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static edu.wpi.teamname.ServiceRequests.GeneralRequest.RequestDAO.allRequestTable;

public class RoomRequestDAO implements IDAO<ConfRoomRequest, String> {
  protected static final String schemaName = "hospitaldb";
  protected final String roomReservationsTable = schemaName + "." + "roomReservations";
  @Getter LinkedList<ConfRoomRequest> requests = new LinkedList<>();
  dbConnection connection = dbConnection.getInstance();

  private Statement statement;

  public RoomRequestDAO() {}

  public void initTable(String name) {

    String roomReservationsTableConstruct =
        "CREATE TABLE IF NOT EXISTS "
            + roomReservationsTable
            + " "
            + "(reservationID SERIAL PRIMARY KEY,"
            + "dateOrdered Date,"
            + "eventDate Date,"
            + "startTime Time,"
            + "endTime Time,"
            + "room Varchar(100),"
            + "reservedBy Varchar(100),"
            + "eventName Varchar(100),"
            + "eventDescription Varchar(100),"
            + "assignedTo Varchar(100),"
            + "orderStatus Varchar(100),"
            + "notes Varchar(500),"
            + "isPrivate bool,"
            + "requestType varchar(100))";
    try {
      Statement stmt = connection.getConnection().createStatement();
      stmt.execute(roomReservationsTableConstruct);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("Room reservation creation error");
    }
    System.out.println("Room reservations table created");
  }

  @Override
  public void add(ConfRoomRequest request) {
    dbConnection connection = dbConnection.getInstance();
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + roomReservationsTable
                      + " (dateOrdered, eventDate, startTime, endTime, room, reservedBy, eventName, eventDescription, assignedTo, orderStatus, notes, isPrivate, requestType) "
                      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
      preparedStatement.setDate(2, Date.valueOf(request.eventDate));
      preparedStatement.setTime(3, Time.valueOf(request.startTime));
      preparedStatement.setTime(4, Time.valueOf(request.endTime));
      preparedStatement.setString(5, request.getRoom()); // TODO add room id
      preparedStatement.setString(6, request.getReservedBy());
      preparedStatement.setString(7, request.getEventName());
      preparedStatement.setString(8, request.getEventDescription());
      preparedStatement.setString(9, request.getAssignedTo());
      preparedStatement.setString(10, request.getOrderStatus().name()); // TODO fix
      preparedStatement.setString(11, request.getNotes());
      preparedStatement.setBoolean(12, request.isPrivate());
      preparedStatement.setString(13, "Room");
      preparedStatement.executeUpdate();

      PreparedStatement preparedStatement2 =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + allRequestTable
                      + " (requestType, deliveryLocation, requestTime, assignedto, orderedBy, orderstatus) VALUES"
                      + " (?, ?, ?, ?, ?, ?)");
      preparedStatement2.setString(1, "Room");
      preparedStatement2.setString(2, request.getRoom());
      preparedStatement2.setTime(3, Time.valueOf((request.getStartTime())));
      preparedStatement2.setString(4, request.getAssignedTo());
      preparedStatement2.setString(5, request.getReservedBy());
      preparedStatement2.setString(6, String.valueOf(request.getOrderStatus()));
      preparedStatement2.executeUpdate();
      requests.add(request);
      //      preparedStatement.executeUpdate();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }


  public boolean hasConflicts(
      String location, LocalDate eventDate, LocalTime startTime, LocalTime endTime)
      throws Exception {
    dbConnection connection = dbConnection.getInstance();

    try {

      String checkTable =
          "SELECT * FROM " + roomReservationsTable + " WHERE room = ? AND eventDate = ?";
      PreparedStatement preparedStatement = connection.getConnection().prepareStatement(checkTable);
      preparedStatement.setString(1, location);
      preparedStatement.setDate(2, Date.valueOf(eventDate));

      ResultSet times = preparedStatement.executeQuery();
      while (times.next()) {

        LocalTime registeredStart = LocalTime.parse(times.getString("startTime"));
        LocalTime registeredEnd = LocalTime.parse(times.getString("endTime"));
        if (endTime.isBefore(startTime)) return true;
        if (startTime.equals(registeredStart) || endTime.equals(registeredEnd)) return true;
        if ((endTime.isAfter(registeredStart) && endTime.isBefore(registeredEnd))) return true;
        if ((startTime.isAfter(registeredStart)) && startTime.isBefore(registeredEnd)) return true;
      }
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
    return false;
  }

  public List<ConfRoomRequest> allPastRequestsByUser(String user) throws Exception {

    List<ConfRoomRequest> requestList = new ArrayList<>();

    try {

      String checkTable = "SELECT * FROM " + roomReservationsTable + " WHERE reservedBy = ?";
      PreparedStatement preparedStatement = connection.getConnection().prepareStatement(checkTable);
      preparedStatement.setString(1, user);

      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {

        LocalDate thisDate = rs.getDate("eventDate").toLocalDate();
        LocalTime thisStartTime = rs.getTime("startTime").toLocalTime();
        LocalTime thisEndTime = rs.getTime("endTime").toLocalTime();
        String room = rs.getString("Room");
        String eventName = rs.getString("EventName");
        String eventDescription = rs.getString("EventDescription");
        String assignedTo = rs.getString("AssignedTo");
        Boolean isPrivate = rs.getBoolean("isPrivate");

        if (thisDate.isAfter(LocalDate.now())) continue;

        if (thisDate.isEqual(LocalDate.now())) {
          if (thisEndTime.isAfter(LocalTime.now())) continue;
        }

        ConfRoomRequest thisRequest =
            new ConfRoomRequest(
                thisDate,
                thisStartTime,
                thisEndTime,
                room,
                user,
                eventName,
                eventDescription,
                assignedTo,
                isPrivate);
        requestList.add(thisRequest);
      }
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
    return requestList;
  }

  public void deleteRequest(String orderedBy, LocalDate orderDate) {
    try {
      PreparedStatement deleteFood =
          connection
              .getConnection()
              .prepareStatement(
                  "DELETE FROM "
                      + roomReservationsTable
                      + " WHERE reservedby = ? AND dateordered = ?");

      deleteFood.setString(1, orderedBy);
      deleteFood.setDate(2, Date.valueOf(orderDate));
      deleteFood.execute();

      // remove from local Hashmap

      System.out.println("FoodRequest deleted");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  @Override
  public List<ConfRoomRequest> getAll() {
    dbConnection connection = dbConnection.getInstance();

    List<ConfRoomRequest> requestList = new ArrayList<>();

    try {

      String checkTable = "SELECT * FROM " + roomReservationsTable;
      PreparedStatement preparedStatement = connection.getConnection().prepareStatement(checkTable);

      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {

        LocalDate thisDate = rs.getDate("eventDate").toLocalDate();
        LocalTime thisStartTime = rs.getTime("startTime").toLocalTime();
        LocalTime thisEndTime = rs.getTime("endTime").toLocalTime();
        String room = rs.getString("Room");
        String reservedBy = rs.getString("reservedBy");
        String eventName = rs.getString("EventName");
        String eventDescription = rs.getString("EventDescription");
        String assignedTo = rs.getString("AssignedTo");
        Boolean isPrivate = rs.getBoolean("isPrivate");

        ConfRoomRequest thisRequest =
            new ConfRoomRequest(
                thisDate,
                thisStartTime,
                thisEndTime,
                room,
                reservedBy,
                eventName,
                eventDescription,
                assignedTo,
                isPrivate);
        requestList.add(thisRequest);
      }
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
    return requestList;
  }

  @Override
  public ConfRoomRequest get(String target) {
    return null;
  }

  public ConfRoomRequest getRow(String target) {
    return null;
  }

  @Override
  public void delete(String target) {
    for (ConfRoomRequest roomRequest : getRequests()) {
      if (roomRequest.eventName.equals(target)) requests.remove(roomRequest);
    }
  }

  @Override
  public void dropTable() {}

  @Override
  public void loadRemote(String pathToCSV) {}

  @Override
  public void importCSV(String path) {}

  @Override
  public void exportCSV(String path) throws IOException {}
}
