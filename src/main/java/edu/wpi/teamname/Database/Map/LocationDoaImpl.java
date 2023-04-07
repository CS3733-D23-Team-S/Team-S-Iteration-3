package edu.wpi.teamname.Database.Map;

import edu.wpi.teamname.Database.dbConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

public class LocationDoaImpl implements LocationDOA_I {
  private static LocationDoaImpl single_instance;
  @Getter String name;
  @Getter private HashMap<String, Location> locations = new HashMap<>();

  dbConnection connection;

  private LocationDoaImpl() {
    connection = dbConnection.getInstance();
  }

  public static LocationDoaImpl getInstance() {
    if (single_instance == null) single_instance = new LocationDoaImpl();

    return single_instance;
  }

  @Override
  public List<Location> getAllLocations() {
    return this.locations.values().stream().toList();
  }

  @Override
  public void initTable(String name) {
    this.name = name;
    String locationTable =
        "CREATE TABLE IF NOT EXISTS "
            + name
            + " "
            + "(longname varchar(100),"
            + "shortname varchar(100),"
            + "nodetype int)";
    System.out.println("Created the location table");
    try {
      Statement stmt = connection.getConnection().createStatement();
      stmt.execute(locationTable);
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
      System.out.println("Error with creating the location table");
    }
  }

  @Override
  public Location getLocation(String longName) {
    return this.locations.get(longName);
  }

  @Override
  public void loadToRemote(String pathToCSV) {
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

  @Override
  public void addLocation(Location thisLocation) {
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO " + name + " (longName, shortName, nodetype) VALUES (?,?,?)");
      stmt.setString(1, thisLocation.getLongName());
      stmt.setString(2, thisLocation.getShortName());
      stmt.setInt(3, thisLocation.getNodeType().ordinal());
      locations.put(thisLocation.getLongName(), thisLocation);
      stmt.execute();
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
  }

  private void constructFromRemote() {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String listOfLocs = "SELECT * FROM " + name;
      ResultSet data = stmt.executeQuery(listOfLocs);
      while (data.next()) {
        String longName = data.getString("longname");
        String shortName = data.getString("shortname");
        NodeType type = NodeType.values()[data.getInt("nodetype")];
        Location location = new Location(longName, shortName, type);
        locations.put(longName, location);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println("Error accessing the remote and constructing the list of locations");
    }
  }

  private void constructRemote(String csvFilePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
      reader.readLine();
      String line;
      try {
        while ((line = reader.readLine()) != null) {
          String[] fields = line.split(",");
          NodeType value = NodeType.valueOf(fields[2]);
          Location location = new Location(fields[0], fields[1], value);
          this.addLocation(location);

          PreparedStatement stmt =
              connection
                  .getConnection()
                  .prepareStatement(
                      "INSERT INTO "
                          + name
                          + " "
                          + "(longName, shortName, nodetype) VALUES (?,?,?)");
          stmt.setString(1, fields[0]);
          stmt.setString(2, fields[1]);
          stmt.setInt(3, value.ordinal());
        }
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println(e.getSQLState());
        System.out.println(
            "Error accessing the remote and constructing the list of locations in the remote");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
