package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.Move;
import edu.wpi.teamname.DAOs.orms.Node;
import edu.wpi.teamname.DAOs.orms.NodeType;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomDAO;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDAOImpl;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDeliveryDAOImp;
import edu.wpi.teamname.ServiceRequests.GeneralRequest.RequestDAO;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.OfficeSupplyDAOImpl;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.OfficeSupplyDeliveryDAOImpl;
import edu.wpi.teamname.ServiceRequests.flowers.FlowerDAOImpl;
import edu.wpi.teamname.ServiceRequests.flowers.FlowerDeliveryDAOImpl;
import edu.wpi.teamname.pathfinding.AStar;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBaseRepository {

  private static DataBaseRepository single_instance = null;
  AStar pathFinder;
  @Getter NodeDAOImpl nodeDAO;
  @Getter MoveDAOImpl moveDAO;
  @Getter LocationDAOImpl locationDAO;
  @Getter EdgeDAOImpl edgeDAO;
  @Getter RoomRequestDAO roomRequestDAO;
  @Getter ConfRoomDAO confRoomDAO;
  @Getter FoodDAOImpl foodDAO;

  @Getter FoodDeliveryDAOImp foodDeliveryDAO;

  @Getter FlowerDAOImpl flowerDAO;
  @Getter FlowerDeliveryDAOImpl flowerDeliveryDAO;

  @Getter OfficeSupplyDAOImpl officeSupplyDAO;

  @Getter OfficeSupplyDeliveryDAOImpl officeSupplyDeliveryDAO;
  @Getter UserDAOImpl userDAO;
  @Getter RequestDAO requestDAO;

  private DataBaseRepository() {
    nodeDAO = new NodeDAOImpl();
    moveDAO = new MoveDAOImpl();
    locationDAO = new LocationDAOImpl();
    edgeDAO = new EdgeDAOImpl();
    userDAO = new UserDAOImpl();
    roomRequestDAO = new RoomRequestDAO();
    confRoomDAO = new ConfRoomDAO();
    foodDAO = new FoodDAOImpl();
    foodDeliveryDAO = new FoodDeliveryDAOImp();
    flowerDAO = new FlowerDAOImpl();
    flowerDeliveryDAO = new FlowerDeliveryDAOImpl();
    officeSupplyDAO = new OfficeSupplyDAOImpl();
    officeSupplyDeliveryDAO = new OfficeSupplyDeliveryDAOImpl();
    requestDAO = new RequestDAO();
  }

  public static synchronized DataBaseRepository getInstance() {
    if (single_instance == null) single_instance = new DataBaseRepository();
    return single_instance;
  }

  public void load() {
    dbConnection connection = dbConnection.getInstance();
    pathFinder = new AStar();
    // Has to be in the order of Node, Edge, Location, Move so that loading the local databases
    // works correctly
    nodeDAO.initTable(connection.getNodeTable());
    edgeDAO.initTable(connection.getEdgesTable());
    locationDAO.initTable(connection.getLocationTable());
    moveDAO.initTable(connection.getMoveTable());
    roomRequestDAO.initTable(connection.getRoomReservationsTable());
    confRoomDAO.initTable(connection.getConferenceRoomTables());
    foodDAO.initTable(connection.getFoodTable());
    foodDeliveryDAO.initTable(connection.getFoodRequestsTable());
    userDAO.initTable(connection.getLoginTable());
    System.out.println(userDAO.getListOfUsers());
    requestDAO.initTable("all Requests");

    officeSupplyDAO.initTable(connection.getOfficesuppliesTable());
    officeSupplyDeliveryDAO.initTable(connection.getOSuppliesRequestsTable());

    nodeDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Node.csv");
    edgeDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Edge.csv");
    locationDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/LocationName.csv");
    moveDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Move.csv");

    flowerDAO.initTable(connection.getFlowerTable());
    flowerDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Flower.csv");
    flowerDeliveryDAO.initTable(connection.getFlowerDeliveryTable());
    flowerDeliveryDAO.loadRemote("flowersssssss!?");
    foodDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Foods.csv");
    foodDeliveryDAO.loadRemote("This means nothing");

    officeSupplyDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/OfficeSupplies.csv");
    officeSupplyDeliveryDAO.loadRemote("This shouldnt matter");
  }

  public boolean login(String text, String text1) throws Exception {
    return userDAO.login(text, text1);
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
      Location loc = locationDAO.getLocationMap().get(location);
      Node node = nodeDAO.get(newLocNodeID);
      Move thisMove = new Move(node, loc, date);
      moveDAO.add(thisMove);
      return moveResult;
    }
  }

  private boolean checkCanMove(String location, LocalDate date) {
    for (Move move : moveDAO.locationMoveHistory.get(location))
      if (move.getDate().equals(date)) return true;
    return false;
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

  public Node getNodebyXY(int x, int y) {
    for (Node aNode : nodeDAO.getAll()) {
      if (aNode.getXCoord() == x && aNode.getYCoord() == y) {
        return aNode;
      }
    }

    return null;
  }

  public void addRoomRequest(ConfRoomRequest confRoomRequest) throws Exception {

    if (!roomRequestDAO.hasConflicts(
        confRoomRequest.getRoom(),
        confRoomRequest.getEventDate(),
        confRoomRequest.getStartTime(),
        confRoomRequest.getEndTime())) {
      roomRequestDAO.add(confRoomRequest);

    } else {
      throw new Exception("Room has conflicts");
    }
  }

  public int getLastFoodDevID() {
    int lastIndex = foodDeliveryDAO.getAll().size() - 1;

    if (lastIndex == -1) return 0;
    else return foodDeliveryDAO.getAll().get(lastIndex).getDeliveryID() + 1;
  }

  public List<String> getListOfEligibleRooms() {

    List<String> listOfEligibleRooms = new ArrayList<>();
    List<Location> locationList = locationDAO.getAll();

    NodeType[] nodeTypes = new NodeType[6];
    nodeTypes[0] = NodeType.ELEV;
    nodeTypes[1] = NodeType.EXIT;
    nodeTypes[2] = NodeType.HALL;
    nodeTypes[3] = NodeType.REST;
    nodeTypes[4] = NodeType.STAI;
    nodeTypes[5] = NodeType.BATH;

    boolean isFound;
    for (Location loc : locationList) { // hashmap
      isFound = false;
      for (NodeType nt : nodeTypes) {
        if (loc.getNodeType() == nt) {
          isFound = true;
          break;
        }
      }
      if (!isFound) listOfEligibleRooms.add(loc.getLongName());
    }
    Collections.sort(listOfEligibleRooms);
    return listOfEligibleRooms;
  }

  public int flowerGetNewDeliveryID() {
    return flowerDeliveryDAO.getAll().size();
  }
}
