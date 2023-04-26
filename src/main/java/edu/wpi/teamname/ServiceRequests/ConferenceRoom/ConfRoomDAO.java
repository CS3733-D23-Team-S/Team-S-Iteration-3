package edu.wpi.teamname.ServiceRequests.ConferenceRoom;

import static edu.wpi.teamname.DAOs.orms.NodeType.CONF;
import static edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO.schemaName;

import edu.wpi.teamname.DAOs.IDAO;
import edu.wpi.teamname.DAOs.dbConnection;
import edu.wpi.teamname.DAOs.orms.Location;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ConfRoomDAO implements IDAO<ConfRoomLocation, String> {
  dbConnection connection = dbConnection.getInstance();
  public HashMap<String, ConfRoomLocation> conferenceRooms = new HashMap<>();
  protected final String confRoomTable = schemaName + "." + "confRooms";

  public ConfRoomDAO() {
    initializeLocations();
  }

  @Override
  public void initTable(String name) {
    String confRoomTableConstruct =
        "CREATE TABLE IF NOT EXISTS "
            + confRoomTable
            + " "
            + "(Location Varchar(200),"
            + "capacity int,"
            + "features varchar(100))";
    try {
      Statement stmt = connection.getConnection().createStatement();
      stmt.execute(confRoomTableConstruct);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("Room reservation creation error");
    }
    System.out.println("Created the conference rooms table");
  }

  @Override
  public List<ConfRoomLocation> getAll() {
    return conferenceRooms.values().stream().toList();
  }

  @Override
  public ConfRoomLocation get(String target) {
    return null;
  }

  public LinkedList<String> getLocationsAlphabetically() {
    LinkedList<String> locations = new LinkedList<>(conferenceRooms.keySet().stream().toList());
    Collections.sort(locations);
    return locations;
  }

  @Override
  public void delete(String target) {}

  @Override
  public void add(ConfRoomLocation addition) {
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + confRoomTable
                      + " (Location, capacity, features) "
                      + "VALUES (?, ?, ?)");
      preparedStatement.setString(1, addition.getLocation().getLongName());
      preparedStatement.setInt(2, addition.getCapacity());
      preparedStatement.setString(3, addition.getFeatures());
      conferenceRooms.put(addition.location.getLongName(), addition);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void initializeLocations() {
    HashMap<String, ConfRoomLocation> temp = new HashMap<>();
    Location l1 = new Location("Abrams Conference Room", "Conf C003L1", CONF);
    Location l2 = new Location("Anesthesia Conf Floor L1", "Conf C001L1", CONF);
    Location l3 = new Location("BTM Conference Center", "BTM Conference", CONF);
    Location l4 =
        new Location("Carrie M. Hall Conference Center Floor 2", "Conference Center", CONF);
    Location l5 = new Location("Duncan Reid Conference Room", "Conf B0102", CONF);
    Location l6 = new Location("Medical Records Conference Room Floor L1", "Conf C002L1", CONF);
    Location l7 = new Location("Shapiro Board Room MapNode 20 Floor 1", "Shapiro Board Room", CONF);

    temp.put(l1.getLongName(), new ConfRoomLocation(l1, 50, ""));
    temp.put(l2.getLongName(), new ConfRoomLocation(l2, 30, "Projector,Whiteboard"));
    temp.put(l3.getLongName(), new ConfRoomLocation(l3, 10, "Projector,DocCamera,Whiteboard"));
    temp.put(l4.getLongName(), new ConfRoomLocation(l4, 30, "Projector"));
    temp.put(l5.getLongName(), new ConfRoomLocation(l5, 70, "DocCamera,Whiteboard"));
    temp.put(l6.getLongName(), new ConfRoomLocation(l6, 100, "Projector,DocCamera,Blackboard"));
    temp.put(l7.getLongName(), new ConfRoomLocation(l7, 60, "DocCamera,Whiteboard"));
    conferenceRooms = temp;
  }

  @Override
  public void dropTable() {}

  @Override
  public void loadRemote(String pathToCSV) {}

  @Override
  public void importCSV(String path) {
    dropTable();
    conferenceRooms.clear();
    loadRemote(path);
  }

  @Override
  public void exportCSV(String path) throws IOException {
    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path));
    fileWriter.write(
        "orderDate,eventDate,startTime,endTime,room,reservedBy,"
            + "eventName,eventDescription,assignedTo,orderStatus,notes,isPrivate;");

    for (ConfRoomLocation loc : conferenceRooms.values()) {
      fileWriter.newLine();
      fileWriter.write(loc.toCSVString());
    }
    fileWriter.close();
  }
}
