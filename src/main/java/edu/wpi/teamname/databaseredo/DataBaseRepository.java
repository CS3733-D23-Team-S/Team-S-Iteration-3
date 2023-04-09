package edu.wpi.teamname.databaseredo;

import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import edu.wpi.teamname.databaseredo.orms.Edge;
import edu.wpi.teamname.databaseredo.orms.Location;
import edu.wpi.teamname.databaseredo.orms.Move;
import edu.wpi.teamname.databaseredo.orms.Node;
import edu.wpi.teamname.pathfinding.AStar;
import lombok.Getter;

public class DataBaseRepository {

  private static DataBaseRepository single_instance = null;
  private dbConnection connection;
  AStar pathFinder;
  @Getter IDAO<Node> nodeDAO;
  @Getter IDAO<Move> moveDAO;
  @Getter IDAO<Location> locationDAO;
  @Getter IDAO<Edge> edgeDAO;
  @Getter IDAO<ConfRoomRequest> roomRequestDAO;

  private DataBaseRepository() {
    nodeDAO = new NodeDAOImpl();
    moveDAO = new MoveDAOImpl();
    locationDAO = new LocationDAOImpl();
    edgeDAO = new EdgeDAOImpl();
    roomRequestDAO = new RoomRequestDAO();
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
    nodeDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Node.csv");
    edgeDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Edge.csv");
    locationDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/LocationName.csv");
    moveDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Move.csv");
  }

  public boolean login(String text, String text1) {
    return true;
  }

  /*
  public boolean checkCanMove(String location, LocalDate date) {
      return moves.get(location).getDates().contains(date);
    }
    public String processMoveRequest(int newLocNodeID, String location, LocalDate date)

            throws Exception {
      if (checkCanMove(location, date)) {
        throw new Exception("Moved Today already");
      } else {
        String moveResult;
        if (date.isAfter(LocalDate.now())) {
          moveResult = "Going to move " + location + " on " + date;
        } else {
          moveResult = "Moved " + location + " to its new location";
        }
        Move thisMove = new Move(newLocNodeID, location, date);
        listOfMoves.add(thisMove);
        Move target = moves.get(location);
        nodeToLoc.get(target.getNodeID()).remove(location);
        target.setNodeID(newLocNodeID);
        nodeToLoc.get(newLocNodeID).add(location);
        target.getDates().add(date);
        return moveResult;
      }
    }*/
}
