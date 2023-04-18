package edu.wpi.teamname.DAOs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import lombok.Getter;

public class dbConnection {
  private static dbConnection single_instance;

  Connection c;
  final String schemaName = "hospitaldb";
  @Getter final String nodeTable = schemaName + "." + "nodes";
  @Getter final String edgesTable = schemaName + "." + "edges";
  @Getter final String moveTable = schemaName + "." + "moves";
  @Getter final String locationTable = schemaName + "." + "locations";
  @Getter final String loginTable = schemaName + "." + "login";

  @Getter final String flowerDeliveryTable = schemaName + "." + "flowerRequests";
  @Getter final String flowerTable = schemaName + "." + "flowers";

  @Getter final String foodTable = schemaName + "." + "foods";
  @Getter final String foodRequestsTable = schemaName + "." + "foodRequests";
  @Getter final String roomReservationsTable = schemaName + "." + "roomReservations";
  @Getter final String conferenceRoomTables = schemaName + "." + "confRooms";
  @Getter final String officesuppliesTable = schemaName + "." + "officeSupplies";
  @Getter final String oSuppliesRequestsTable = schemaName + "." + "officeSuppliesRequests";

  private dbConnection() {
    try {
      Class.forName("org.postgresql.Driver");
      String url = "jdbc:postgresql://database.cs.wpi.edu:5432/teamsdb";
      String user = "teams";
      String password = "teams160";
      c = DriverManager.getConnection(url, user, password);
      PreparedStatement stmt = c.prepareStatement("CREATE SCHEMA IF NOT EXISTS " + schemaName);
      stmt.execute();
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
    System.out.println("Opened database successfully");
  }

  // Static method
  // Static method to create instance of Singleton class
  public static synchronized dbConnection getInstance() {
    if (single_instance == null) single_instance = new dbConnection();
    return single_instance;
  }

  public Connection getConnection() {
    return c;
  }
}
