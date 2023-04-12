package edu.wpi.teamname.ServiceRequests.FoodService;

import edu.wpi.teamname.DAOs.IDAO;
import edu.wpi.teamname.DAOs.dbConnection;
import edu.wpi.teamname.controllers.mainpages.HomeController;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

public class FoodDeliveryDAOImp implements IDAO<FoodDelivery, Integer> {

  @Getter private String name;
  private dbConnection connection;
  @Getter private HashMap<Integer, FoodDelivery> foodRequests = new HashMap<>();

  public FoodDeliveryDAOImp() {
    connection = dbConnection.getInstance();
  }

  public void initTable(String name) {
    this.name = name;
    try {
      Statement st = connection.getConnection().createStatement();

      String foodRequestsTableConstruct =
          "CREATE TABLE IF NOT EXISTS "
              + name
              + " "
              + "(deliveryid int UNIQUE PRIMARY KEY,"
              + "cart Varchar(100),"
              + "orderdate Date,"
              + "ordertime time,"
              + "location Varchar(100),"
              + "orderer Varchar(100),"
              + "assignedto Varchar(100),"
              + "Status Varchar(100),"
              + "cost DOUBLE PRECISION,"
              + "notes Varchar(255),"
              + "foreign key (location) references "
              + "hospitaldb.locations(longname) ON DELETE CASCADE)";

      st.execute(foodRequestsTableConstruct);
      System.out.println("Created the foodRequest table");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("Database creation error");
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

  @Override
  public void add(FoodDelivery request) {

    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + name
                      + " (deliveryid, Cart, orderDate, orderTime, location, orderer, assignedTo, status, cost, notes) "
                      + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

      preparedStatement.setInt(1, request.getDeliveryID());
      preparedStatement.setString(2, request.getCart());
      preparedStatement.setDate(3, request.getDate());
      preparedStatement.setTime(4, request.getTime());
      preparedStatement.setString(5, request.getLocation());
      preparedStatement.setString(6, request.getOrderer());
      preparedStatement.setString(7, request.getAssignedTo());
      preparedStatement.setString(8, request.getOrderStatus());
      preparedStatement.setDouble(9, request.getCost());
      preparedStatement.setString(10, request.getNotes());

      preparedStatement.executeUpdate();

      foodRequests.put(request.getDeliveryID(), request);

      System.out.println("FoodDeliveryAdded");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  public void delete(Integer target) {
    try {
      PreparedStatement deleteFood =
          connection
              .getConnection()
              .prepareStatement("DELETE FROM " + name + " WHERE delivery = ?");

      deleteFood.setInt(1, target);
      deleteFood.execute();

      // remove from local Hashmap
      foodRequests.remove(target);

      System.out.println("FoodRequest deleted");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  public FoodDelivery getRow(Integer target) {
    if (foodRequests.get(target) == null) {
      System.out.println("This foodRequest is not in the database, so its row cannot be printed");
      return null;
    }
    return foodRequests.get(target);
  }

  @Override
  public List<FoodDelivery> getAll() {
    return foodRequests.values().stream().toList();
  }

  @Override
  public void loadRemote(String pathToCSV) {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String checkTable = "SELECT * FROM " + name;
      ResultSet check = stmt.executeQuery(checkTable);
      if (check.next()) {
        System.out.println("Loading the foods from the server");
        constructFromRemote();
      } else {
        HomeController.delID = 0;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void importCSV(String path) {}

  @Override
  public void exportCSV(String path) throws IOException {}

  private void constructFromRemote() {
    try {
      Statement st = connection.getConnection().createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM " + name);

      while (rs.next()) {
        int deliveryid = rs.getInt("deliveryid");
        String cart = rs.getString("cart");
        Date orderdate = rs.getDate("orderdate");
        Time ordertime = rs.getTime("ordertime");
        String location = rs.getString("location");
        String orderer = rs.getString("orderer");
        String assignedto = rs.getString("assignedTo");
        String status = rs.getString("status");
        Double cost = rs.getDouble("cost");
        String notes = rs.getString("notes");

        FoodDelivery fd =
            new FoodDelivery(
                deliveryid,
                cart,
                orderdate,
                ordertime,
                location,
                orderer,
                assignedto,
                status,
                cost,
                notes);

        foodRequests.put(deliveryid, fd);
      }

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
      System.out.println("Error accessing the remote and constructing the list of foods");
    }
  }
}
