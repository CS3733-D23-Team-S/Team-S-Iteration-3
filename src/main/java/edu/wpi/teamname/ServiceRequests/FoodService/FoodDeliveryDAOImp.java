package edu.wpi.teamname.ServiceRequests.FoodService;

import edu.wpi.teamname.Database.dbConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

/* TODO:
       change Id to ID in deleteRequest
*/
public class FoodDeliveryDAOImp implements FoodDeliveryDAO_I {
  protected static final String schemaName = "hospitaldb";
  protected static final String foodRequestsTable = schemaName + "." + "foodRequests";
  private HashMap<Integer, FoodDelivery> requests = new HashMap<>();
  private dbConnection connection = dbConnection.getInstance();
  static FoodDeliveryDAOImp single_instance = null;

  private FoodDeliveryDAOImp() {}

  public static synchronized FoodDeliveryDAOImp getInstance() {

    if (single_instance == null) single_instance = new FoodDeliveryDAOImp();

    return single_instance;
  }

  public void addRequest(FoodDelivery request) {
    requests.put(request.getDeliveryID(), request);
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + foodRequestsTable
                      + " (deliveryID, Cart, orderDate, orderTime, room, orderer, assignedTo, status, cost, notes) "
                      + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      preparedStatement.setInt(1, request.getDeliveryID());
      preparedStatement.setString(2, request.getCart());
      preparedStatement.setDate(3, request.getDate());
      preparedStatement.setTime(4, request.getTime());
      preparedStatement.setInt(5, request.getRoom());
      preparedStatement.setString(6, request.getOrderer());
      preparedStatement.setString(7, request.getAssignedTo());
      preparedStatement.setString(8, request.getOrderStatus());
      preparedStatement.setDouble(9, request.getCost());
      preparedStatement.setString(10, request.getNotes());

      preparedStatement.executeUpdate();

      requests.put(request.getDeliveryID(), request);

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  @Override
  public List<FoodDelivery> getAllRequests() {
    return null;
  }

  @Override
  public FoodDelivery getRequest(int target) {
    if (requests.get(target) == null) {
      System.out.println("This node is not in the database, so its row cannot be printed");
      return null;
    }
    return requests.get(target);
  }

  @Override
  public void deleteRequest(int target) {
    try {
      PreparedStatement deleteFood =
          connection
              .getConnection()
              .prepareStatement(
                  "DELETE FROM " + foodRequestsTable + " WHERE deliveryId = ?"); // HERE Id

      deleteFood.setInt(1, target);
      deleteFood.execute();

      // remove from local Hashmap
      requests.remove(target);

      System.out.println("FoodRequest deleted");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  public void initFoodRequests() {
    try {
      Statement st = connection.getConnection().createStatement();
      String dropFoodRequestsTable = "DROP TABLE IF EXISTS " + foodRequestsTable + " CASCADE";

      String foodRequestsTableConstruct =
          "CREATE TABLE IF NOT EXISTS "
              + foodRequestsTable
              + " "
              + "(deliveryID int UNIQUE PRIMARY KEY,"
              + "cartID Varchar(100),"
              + "orderDate Date,"
              + "orderTime time,"
              + "room int,"
              + "orderedBy Varchar(100),"
              + "assignedTo Varchar(100),"
              + "Status Varchar(100),"
              + "cost int,"
              + "notes Varchar(255))";

      st.execute(dropFoodRequestsTable);
      st.execute(foodRequestsTableConstruct);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("Database creation error");
      e.printStackTrace();
    }
  }
}
