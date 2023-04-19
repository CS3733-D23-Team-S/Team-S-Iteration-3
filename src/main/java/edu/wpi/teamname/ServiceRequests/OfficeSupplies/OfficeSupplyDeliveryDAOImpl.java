package edu.wpi.teamname.ServiceRequests.OfficeSupplies;

import edu.wpi.teamname.DAOs.dbConnection;
import edu.wpi.teamname.ServiceRequests.ISRDAO;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;

import static edu.wpi.teamname.ServiceRequests.GeneralRequest.RequestDAO.allRequestTable;

public class OfficeSupplyDeliveryDAOImpl implements ISRDAO<OfficeSupplyDelivery, Integer> {

  @Getter HashMap<Integer, OfficeSupplyDelivery> requests = new HashMap<>();
  private final dbConnection connection;
  @Getter String name;

  public OfficeSupplyDeliveryDAOImpl() {
    connection = dbConnection.getInstance();
  }

  public void initTable(String name) {
    this.name = name;
    try {
      Statement st = connection.getConnection().createStatement();

      String officeSupplyRequestsTableConstruct =
          "CREATE TABLE IF NOT EXISTS "
              + name
              + " "
              + "(deliveryID int UNIQUE PRIMARY KEY,"
              + "cart Varchar(400),"
              + "orderDate Date,"
              + "orderTime Time,"
              + "room Varchar(400),"
              + "orderedBy Varchar(400),"
              + "assignedTo Varchar(400),"
              + "orderStatus Varchar(1000),"
              + "cost DOUBLE PRECISION,"
              + "notes Varchar(100),"
              + " requestType varchar(100))";

      st.execute(officeSupplyRequestsTableConstruct);
      System.out.println("Created the office supplies");

      // Move to hashmap requests
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("Could not create officesupply");
      e.printStackTrace();
    }
  }

  @Override
  public void dropTable() {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String drop = "DROP TABLE IF EXISTS " + name + " CASCADE";
      stmt.executeUpdate(drop);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void constructFromRemote() {
    try {
      dbConnection connection = dbConnection.getInstance();
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
        double cost = data.getDouble("cost");
        String notes = data.getString("notes");

        OfficeSupplyDelivery od =
            new OfficeSupplyDelivery(
                ID, cart, date, time, room, orderedBy, assignedTo, orderStatus, cost, notes);
        requests.put(ID, od);
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
    dbConnection connection = dbConnection.getInstance();
    try {
      Statement stmt = connection.getConnection().createStatement();
      String checkTable = "SELECT * FROM " + name;
      ResultSet check = stmt.executeQuery(checkTable);
      if (check.next()) {
        System.out.println("Loading the flowerDeliveries from the server");
        constructFromRemote();
      } else {
        System.out.println("flowerDelivery table is empty");
      }
    } catch (SQLException e) {
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
                        + " (deliveryID, cart, orderDate, orderTime, room, orderedBy, assignedTo, orderStatus, cost) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

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
          stmt.setString(7, fields[6]);
          stmt.setString(8, fields[7]);
          stmt.setDouble(9, Double.parseDouble(fields[8]));
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

  /**
   * Returns list of all FlowerDeliveries within the deliveries HashMap
   *
   * @return List of all FlowerDeliveries on success
   */
  @Override
  public List<OfficeSupplyDelivery> getAll() {
    return requests.values().stream().toList();
  }

  @Override
  public OfficeSupplyDelivery getRow(Integer target) {
    return null;
  }

  /**
   * Gets FlowerDelivery
   *
   * @param ID: ID of FlowerDelivery wanted
   * @return request (FlowerDelivery) on success, otherwise returns null or exception
   */
  // TODO: Change to hashmap
  public OfficeSupplyDelivery get(int ID) {
    return requests.get(ID);
  }

  @Override
  public void delete(Integer ID) {
    try {
      PreparedStatement deleteFlower =
          connection
              .getConnection()
              .prepareStatement("DELETE FROM " + name + " WHERE deliveryID = ?");

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
  public void add(OfficeSupplyDelivery request) {
    dbConnection connection = dbConnection.getInstance();

    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + name
                      + " (deliveryID, cart, orderDate, orderTime, room, orderedBy, assignedTo, orderStatus, cost, notes, requestType)"
                      + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

      preparedStatement.setInt(1, request.getDeliveryid());
      preparedStatement.setString(2, request.getCart());
      preparedStatement.setDate(3, request.getDate());
      preparedStatement.setTime(4, request.getTime());
      preparedStatement.setString(5, request.getRoom());
      preparedStatement.setString(6, request.getOrderedBy());
      preparedStatement.setString(7, request.getAssignedTo());
      preparedStatement.setString(8, request.getOrderStatus());
      preparedStatement.setDouble(9, request.getCost());
      preparedStatement.setString(10, request.getNotes());
      preparedStatement.setString(11, "Office");
      requests.put(request.getDeliveryid(), request);

      PreparedStatement preparedStatement2 =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + allRequestTable
                      + " (requestType, deliveryLocation, requestTime, assignedto, orderedBy, orderstatus) VALUES"
                      + " (?, ?, ?, ?, ?, ?)");
      preparedStatement2.setString(1, "Office");
      preparedStatement2.setString(2, request.getRoom());
      preparedStatement2.setTime(3, Time.valueOf((request.getTime()).toLocalTime()));
      preparedStatement2.setString(4, request.getAssignedTo());
      preparedStatement2.setString(5, request.getOrderedBy());
      preparedStatement2.setString(6, "Received");

      preparedStatement.executeUpdate();
      preparedStatement2.executeUpdate();

      requests.put(request.getDeliveryid(), request);

    } catch (SQLException e) {
      System.out.println("Exception:");
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }
}
