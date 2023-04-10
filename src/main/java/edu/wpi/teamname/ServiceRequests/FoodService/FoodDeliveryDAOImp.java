package edu.wpi.teamname.ServiceRequests.FoodService;

import edu.wpi.teamname.databaseredo.IDAO;
import edu.wpi.teamname.databaseredo.dbConnection;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FoodDeliveryDAOImp implements IDAO<FoodDelivery,FoodDelivery> {
  protected static final String schemaName = "hospitaldb";
  protected static final String foodRequestsTable = schemaName + "." + "foodRequests";
  private List<FoodDelivery> requests = new LinkedList<>();
  private dbConnection connection = dbConnection.getInstance();


  private FoodDeliveryDAOImp() {}


  public void addRequest(FoodDelivery request) {
    requests.put(request.getDeliveryID(), request);
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + foodRequestsTable
                      + " (Cart, orderDate, orderTime, location, orderer, assignedTo, status, cost, notes) "
                      + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

      preparedStatement.setString(1, request.getCart());
      preparedStatement.setDate(2, request.getDate());
      preparedStatement.setTime(3, request.getTime());
      preparedStatement.setString(4, request.getLocation().getLongName());
      preparedStatement.setString(5, request.getOrderer());
      preparedStatement.setString(6, request.getAssignedTo());
      preparedStatement.setString(7, request.getOrderStatus());
      preparedStatement.setDouble(8, request.getCost());
      preparedStatement.setString(9, request.getNotes());

      preparedStatement.executeUpdate();

      requests.add(request);

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  @Override
  public List<FoodDelivery> getAll() {
    return requests;
  }


  public FoodDelivery getRequest(String orderedBy, Date orderDate) {
    return null;
  }


  public void deleteRequest(String orderedBy, Date orderDate) {
    try {
      PreparedStatement deleteFood =
          connection
              .getConnection()
              .prepareStatement("DELETE FROM " + foodRequestsTable + " WHERE orderer = ? AND  orderDate = ?");

      deleteFood.setString(1, orderedBy);
      deleteFood.setDate(2, (java.sql.Date) orderDate);
      deleteFood.execute();

      // remove from local Hashmap


      System.out.println("FoodRequest deleted");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  @Override
  public void initTable(String tableName) {
    try {
      Statement st = connection.getConnection().createStatement();
      String dropFoodRequestsTable = "DROP TABLE IF EXISTS " + foodRequestsTable + " CASCADE";

      String foodRequestsTableConstruct =
          "CREATE TABLE IF NOT EXISTS "
              + foodRequestsTable
              + " "
              + "(deliveryid SERIAL PRIMARY KEY,"
              + "cart Varchar(100),"
              + "orderdate Date,"
              + "ordertime time,"
              + "location Varchar(100),"
              + "orderer Varchar(100),"
              + "assignedto Varchar(100),"
              + "Status Varchar(100),"
              + "cost int,"
              + "notes Varchar(255),"
              + "foreign key (location) references "
              + "hospitaldb.locations(longname) ON DELETE CASCADE)";

      st.execute(dropFoodRequestsTable);
      st.execute(foodRequestsTableConstruct);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("Database creation error");
      e.printStackTrace();
    }
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
  public void delete(FoodDelivery target) {

  }


}
