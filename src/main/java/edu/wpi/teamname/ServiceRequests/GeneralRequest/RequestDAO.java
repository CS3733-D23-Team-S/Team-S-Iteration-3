package edu.wpi.teamname.ServiceRequests.GeneralRequest;

import edu.wpi.teamname.DAOs.dbConnection;
import edu.wpi.teamname.DAOs.orms.User;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class RequestDAO {
  dbConnection connection = dbConnection.getInstance();
  @Getter ArrayList<Request> requests;
  public static final String allRequestTable = "hospitaldb." + "allRequests";

  public RequestDAO() {
    requests = new ArrayList<>();
  }

  public void initTable(String name) {
    String allRequestsRoomTableConstruct =
        "CREATE TABLE IF NOT EXISTS "
            + allRequestTable
            + " "
            + "(RequestID SERIAL primary key, "
            + "RequestType varchar(100),"
            + "deliveryLocation Varchar(200),"
            + "requestTime time,"
            + "orderedBy varchar(100),"
            + " assignedTo varchar(100),"
            + " orderStatus varchar(100))";
    try {
      Statement stmt = connection.getConnection().createStatement();
      stmt.execute(allRequestsRoomTableConstruct);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("All request creation error");
    }
    System.out.println("Created the universal requests table");
  }

  public void loadFromRemote() {
    ArrayList<Request> curr = new ArrayList<>();

    try {
      dbConnection connection = dbConnection.getInstance();
      Statement st = connection.getConnection().createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM " + allRequestTable);

      while (rs.next()) {
        String requestType = rs.getString("requestType");
        String location = rs.getString("deliveryLocation");
        String orderStatus = rs.getString("orderStatus");
        LocalTime ordertime = rs.getTime("requestTime").toLocalTime();
        String orderedBy = rs.getString("orderedBy");
        String assignedTo = rs.getString("assignedTo");
        Request newRequest =
            new Request(requestType, ordertime, orderStatus, location, assignedTo, orderedBy);

        curr.add(newRequest);
      }
      requests = curr;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void updateRequest(
      String orderStatus, String orderedBy, LocalTime orderTime, String orderType) {
    PreparedStatement preparedStatement2;

    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "UPDATE hospitaldb.allrequests SET orderStatus = ? WHERE orderedby = ? AND requesttime = ? ");

      preparedStatement.setString(1, orderStatus);
      preparedStatement.setString(2, orderedBy);
      preparedStatement.setTime(3, Time.valueOf(orderTime));
      preparedStatement.executeUpdate();

      if (orderType.equals("Food")) {
        connection
            .getConnection()
            .prepareStatement(
                "UPDATE hospitaldb.foodrequests SET status = ? WHERE orderer = ? AND ordertime = ? ");

      } else if (orderType.equals("Flower")) {
        connection
            .getConnection()
            .prepareStatement(
                "UPDATE hospitaldb.flowerrequests SET orderstatus = ? WHERE orderedby = ? AND ordertime = ? ");
      } else if (orderType.equals("Room")) {
        connection
            .getConnection()
            .prepareStatement(
                "UPDATE hospitaldb.roomreservations SET orderstatus = ? WHERE reservedby = ? AND starttime = ? ");

      } else if (orderType.equals("Office")) {
        connection
            .getConnection()
            .prepareStatement(
                "UPDATE hospitaldb.officesuppliesrequests SET orderstatus  = ? WHERE orderedby = ? AND ordertime = ? ");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<Request> getRequestsForUser(User thisUser) {
    List<Request> requestList = new ArrayList<>();
    for (Request request : getRequests()) {
      if (request.assignedTo.equals(thisUser.getUserName())) requestList.add(request);
    }

    return requestList;
  }
}
