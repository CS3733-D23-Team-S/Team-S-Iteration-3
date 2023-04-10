package edu.wpi.teamname.ServiceRequests.flowers;

import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.IDAO;
import edu.wpi.teamname.databaseredo.LocationDAOImpl;
import edu.wpi.teamname.databaseredo.dbConnection;
import edu.wpi.teamname.databaseredo.orms.Location;
import edu.wpi.teamname.databaseredo.orms.NodeType;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FlowerDeliveryDAOImpl implements IDAO<FlowerDelivery> {
  protected static final String schemaName = "hospitaldb";
  protected static final String flowerRequestsTable = schemaName + "." + "flowerRequests";
  private HashMap<Integer, FlowerDelivery> requests = new HashMap<>();
  private dbConnection connection = dbConnection.getInstance();
  private String name;

  public FlowerDeliveryDAOImpl() {}

  public void initTable(String name) {
    this.name = name;
    try {
      Statement st = connection.getConnection().createStatement();
      String dropFlowerRequestsTable = "DROP TABLE IF EXISTS " + flowerRequestsTable + " CASCADE";

      String flowerRequestsTableConstruct =
          "CREATE TABLE IF NOT EXISTS "
              + name
              + " "
              + "(deliveryID int UNIQUE PRIMARY KEY,"
              + "cart Varchar(100),"
              + "orderDate Date,"
              + "orderTime time,"
              + "room Varchar(100),"
              + "orderedBy Varchar(100),"
              + "assignedTo Varchar(100),"
              + "orderStatus Varchar(100))";
      // + "cost int,";

      st.execute(dropFlowerRequestsTable);
      st.execute(flowerRequestsTableConstruct);

      // Move to hashmap requests

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("Could not create flowerRequest");
      e.printStackTrace();
    }
  }

  @Override
  public void dropTable() {}

  private void constructFromRemote() {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String listOfFlowerDeliveries = "SELECT * FROM " + name;
      ResultSet data = stmt.executeQuery(listOfFlowerDeliveries);
      while (data.next()) {
        int ID = data.getInt("deliveryID");
        String cart = data.getString("cart");
        Date date = data.getDate("orderDate");
        Time time = data.getTime("orderTime");
        String room = data.getString("room");
        String orderedBy = data.getString("orderedBy");
        String assignedTo = data.getString("assignedTo");
        String orderStatus = data.getString("orderStatus");
        FlowerDelivery fd =
            new FlowerDelivery(ID, null, date, time, room, orderedBy, assignedTo, orderStatus);
        requests.put(ID, fd);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println(
          "Error accessing the remote and constructing the list of FlowerDeliveries");
    }
  }

  @Override
  public void loadRemote(String pathToCSV) {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String checkTable = "SELECT * FROM " + name;
      ResultSet check = stmt.executeQuery(checkTable);
      if (check.next()) {
        System.out.println("Loading the locations from the server");
        constructFromRemote();
      } else {
        System.out.println("Loading the locations to the server");
        constructRemote(pathToCSV);
      }
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
  }

  private void constructRemote(String csvFilePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
      try {
        PreparedStatement stmt =
              connection
                  .getConnection()
                  .prepareStatement(
                      "INSERT INTO "
                          + name
                          + " (deliveryID, cart, orderDate, orderTime, room, orderedBye, assignedTo, orderStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
          String[] fields = line.split(",");
          stmt.setInt(1, Integer.parseInt(fields[0]));
          stmt.setString(2, fields[1]);
          stmt.setDate(3, Date.valueOf(fields[2]));
          stmt.setTime(4, Time.valueOf(fields[3]));
          stmt.setString(5, fields[4]);
          stmt.setString(6, fields[5]);
          stmt.setString( 7, fields[6]);
          stmt.setString(8, fields[7]);
        }
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println(e.getSQLState());
        System.out.println(
            "Error accessing the remote and constructing list of requests in remote");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void importCSV(String path) {}

  @Override
  public void exportCSV(String path) throws IOException {
    BufferedWriter fileWriter;
    fileWriter = new BufferedWriter(new FileWriter(path));
    fileWriter.write("deliveryID,cart,orderDate,orderTime,room,orderedBye,assignedTo,orderStatus)");
    for (FlowerDelivery flowerDelivery : requests.values()) {
      fileWriter.newLine();
      fileWriter.write(flowerDelivery.toCSVString());
    }
  }

  /**
   * Returns list of all FlowerDeliveries within the deliveries HashMap
   *
   * @return List of all FlowerDeliveries on success
   */
  @Override
  public List<FlowerDelivery> getAll() {
    return requests.values().stream().toList();
  }

  /**
   * Gets FlowerDelivery
   *
   * @param target
   * @return request (FlowerDelivery) on success, otherwise returns null or exception
   */
  // TODO: Change to hashmap
  public FlowerDelivery get(FlowerDelivery target) {
    int ID = target.getID();
    FlowerDelivery request;
    try {
      String query = ("SELECT * FROM " + flowerRequestsTable + " WHERE deliveryID = " + ID);

      Statement stmt = connection.getConnection().createStatement();
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        int flowerID = rs.getInt(1);
        // int cart = rs.getString(2);
        Cart cart = null;
        Date date = rs.getDate(3);
        Time time = rs.getTime(4);
        String room = rs.getString(5);
        String orderedBy = rs.getString(6);
        String assignedTo = rs.getString(7);
        String orderStatus = rs.getString(8);
        // double cost = rs.getDouble(9);

        request =
            new FlowerDelivery(
                flowerID, cart, date, time, room, orderedBy, assignedTo, orderStatus);
        return request;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }

    return null;
  }

  @Override
  public void delete(FlowerDelivery target) {
    int ID = target.getID();
    try {
      PreparedStatement deleteFlower =
          connection
              .getConnection()
              .prepareStatement("DELETE FROM " + flowerRequestsTable + " WHERE deliveryID = ?");

      deleteFlower.setInt(1, ID);
      deleteFlower.execute();

      requests.remove(ID);

      System.out.println("FlowerRequest deleted");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  @Override
  public void add(FlowerDelivery request) {
    requests.put(request.getID(), request);
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + flowerRequestsTable
                      + " (deliveryID, cart, orderDate, orderTime, room, orderedBy, assignedTo, orderStatus, cost) "
                      + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
      preparedStatement.setInt(1, request.getID());
      preparedStatement.setString(1, request.getCart());
      preparedStatement.setDate(1, request.getDate());
      preparedStatement.setTime(1, request.getTime());
      preparedStatement.setString(1, request.getRoom());
      preparedStatement.setString(1, request.getOrderedBy());
      preparedStatement.setString(1, request.getAssignedTo());
      preparedStatement.setString(1, request.getOrderStatus());
      preparedStatement.setDouble(1, request.getCost());

      preparedStatement.executeUpdate();

      requests.put(request.getID(), request);

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  public List<String> getListOfEligibleRooms() {
    DataBaseRepository repo = DataBaseRepository.getInstance();
    repo.load();

    List<String> listOfEligibleRooms = new ArrayList<>();
    List<Location> locationList = repo.getLocationDAO().getAll();

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

}
