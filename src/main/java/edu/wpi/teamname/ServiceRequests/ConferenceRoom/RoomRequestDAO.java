package edu.wpi.teamname.ServiceRequests.ConferenceRoom;

import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.IDAO;
import edu.wpi.teamname.databaseredo.dbConnection;
import edu.wpi.teamname.databaseredo.orms.Location;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoomRequestDAO implements IDAO<ConfRoomRequest, String> {
  protected static final String schemaName = "hospitaldb";
  protected final String roomReservationsTable = schemaName + "." + "roomReservations";
  LinkedList<ConfRoomRequest> requests = new LinkedList<>();
  dbConnection connection = dbConnection.getInstance();
  static RoomRequestDAO single_instance = null;
  private Statement statement;

  public RoomRequestDAO() {
  }



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
                    + "notes Varchar(500))";
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

    requests.add(request);

    try {
      PreparedStatement preparedStatement =
              connection
                      .getConnection()
                      .prepareStatement(
                              "INSERT INTO "
                                      + roomReservationsTable
                                      + " (dateOrdered, eventDate, startTime, endTime, room, reservedBy, eventName, eventDescription, assignedTo, orderStatus, notes) "
                                      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
      preparedStatement.executeUpdate();

      //      preparedStatement.executeUpdate();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  public LinkedList<String> getConfRoomLocationsAlphabetically() {
    LinkedList<String> locations = new LinkedList<>();
    for (Location thisLocation : DataBaseRepository.getInstance().getLocationDAO().getAll()) {

      Pattern pattern = Pattern.compile("Conf", Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(thisLocation.getLongName());
      boolean matchFound = matcher.find();
      if (matchFound) {
        locations.add(thisLocation.getLongName());
      }
    }
    ;
    Collections.sort(locations);
    return locations;
  }

  public boolean hasConflicts(
          String location, LocalDate eventDate, LocalTime startTime, LocalTime endTime)
          throws Exception {

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
                        eventName,
                        eventDescription,
                        assignedTo);
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
                              "DELETE FROM " + roomReservationsTable + " WHERE orderer = ? AND  orderDate = ?");

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
  public ResultSet query(String[] columns, String whereClause, String[] whereArgs, String orderBy) {

    StringBuilder queryString = new StringBuilder();
    queryString.append("SELECT ");
    if(columns != null) {
      for (int i = 0; i < columns.length; i++) {
        queryString.append(columns[i]);
        if (i != (columns.length - 1)) {
          queryString.append(",");
        }
      }
    } else {
      queryString.append("*");
    }

    queryString.append(" FROM ");
    queryString.append(roomReservationsTable);

    if(whereClause != null) {
      queryString.append(" WHERE ");

      if(whereClause.contains("?") && whereArgs != null && whereArgs.length > 0){
        for (String whereArg : whereArgs) {
          whereClause = whereClause.replaceFirst("\\?", "'" + whereArg + "'");
        }
      }

      queryString.append(whereClause);
    }

    if(orderBy != null){
      queryString.append(" ORDER BY ");
      queryString.append(orderBy);
    }

    try {
      return statement.executeQuery(queryString.toString());
    } catch (SQLException e){
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void dropTable() {
  }

  @Override
  public void loadRemote(String pathToCSV) {
  }

  @Override
  public void importCSV(String path) {
  }

  @Override
  public void exportCSV(String path) throws IOException {
  }

  @Override
  public List<ConfRoomRequest> getAll() {

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
        String eventName = rs.getString("EventName");
        String eventDescription = rs.getString("EventDescription");
        String assignedTo = rs.getString("AssignedTo");

        ConfRoomRequest thisRequest =
                new ConfRoomRequest(
                        thisDate,
                        thisStartTime,
                        thisEndTime,
                        room,
                        eventName,
                        eventDescription,
                        assignedTo);
        requestList.add(thisRequest);
      }
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
    return requestList;
  }

  @Override
  public void delete(String target) {

  }

}
