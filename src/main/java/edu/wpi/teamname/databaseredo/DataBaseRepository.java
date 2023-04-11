package edu.wpi.teamname.databaseredo;

import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomDAO;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import edu.wpi.teamname.databaseredo.orms.Move;
import edu.wpi.teamname.pathfinding.AStar;
import java.io.*;
import java.time.LocalDate;
import lombok.Getter;

public class DataBaseRepository {

  private static DataBaseRepository single_instance = null;
  private dbConnection connection;
  AStar pathFinder;
  @Getter NodeDAOImpl nodeDAO;
  @Getter MoveDAOImpl moveDAO;
  @Getter LocationDAOImpl locationDAO;
  @Getter EdgeDAOImpl edgeDAO;
  @Getter RoomRequestDAO roomRequestDAO;
  @Getter ConfRoomDAO confRoomDAO;

  private DataBaseRepository() {
    nodeDAO = new NodeDAOImpl();
    moveDAO = new MoveDAOImpl();
    locationDAO = new LocationDAOImpl();
    edgeDAO = new EdgeDAOImpl();
    roomRequestDAO = new RoomRequestDAO();
    confRoomDAO = new ConfRoomDAO();
  }

  public static synchronized DataBaseRepository getInstance() {
    if (single_instance == null) single_instance = new DataBaseRepository();
    return single_instance;
  }

  public void load() {
    connection = dbConnection.getInstance();
    pathFinder = new AStar();
    nodeDAO.initTable(connection.getNodeTable());
    edgeDAO.initTable(connection.getEdgesTable());
    locationDAO.initTable(connection.getLocationTable());
    moveDAO.initTable(connection.getMoveTable());
    roomRequestDAO.initTable(connection.getRoomReservationsTable());
    confRoomDAO.initTable(connection.getConferenceRoomTables());

    nodeDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Node.csv");
    edgeDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Edge.csv");
    locationDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/LocationName.csv");
    moveDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Move.csv");
  }

  public boolean login(String text, String text1) {
    return true;
  }

  public String processMoveRequest(int newLocNodeID, String location, LocalDate date)
      throws Exception {
    if (checkCanMove(location, date)) {
      throw new Exception("Moved that day already");
    } else {
      String moveResult;
      if (date.isAfter(LocalDate.now())) {
        moveResult = "Going to move " + location + " on " + date;
      } else {
        moveResult = "Moved " + location + " to its new location";
      }
      Move thisMove = new Move(newLocNodeID, location, date);
      moveDAO.add(thisMove);
      return moveResult;
    }
  }

  private boolean checkCanMove(String location, LocalDate date) {
    return moveDAO.getMoveHistory().get(location).contains(date);
  }

  public void importCSV(String inputPath) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(inputPath));
    String check = reader.readLine();

    if (check.equals(nodeDAO.getCSVheader())) nodeDAO.importCSV(inputPath);
    else if (check.equals(edgeDAO.getCSVheader())) edgeDAO.importCSV(inputPath);
    else if (check.equals(moveDAO.getCSVheader())) {
      moveDAO.importCSV(inputPath);
    } else if (check.equals(locationDAO.getCSVheader())) {
      locationDAO.importCSV(inputPath);
    } else throw new RuntimeException();
  }

  public void exportCSV(String outputPath) throws IOException {
    nodeDAO.exportCSV(outputPath);
    edgeDAO.exportCSV(outputPath);
    moveDAO.exportCSV(outputPath);
    locationDAO.exportCSV(outputPath);
  }

  public void addRoomRequest(ConfRoomRequest confRoomRequest) throws Exception {

    if (!roomRequestDAO.hasConflicts(
        confRoomRequest.getRoom(),
        confRoomRequest.getEventDate(),
        confRoomRequest.getStartTime(),
        confRoomRequest.getEndTime())) {
      roomRequestDAO.add(confRoomRequest);

    } else {
      throw new Exception();
    }
  }
}
