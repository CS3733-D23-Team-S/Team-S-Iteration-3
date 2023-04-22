package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.NodeType;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

public class LocationDAOImpl implements IDAO<Location, String> {

  @Getter private String name;
  @Getter private final String CSVheader = "longName,shortName,nodeType";
  private final dbConnection connection;

  @Getter private HashMap<String, Location> locationMap = new HashMap<>();

  public LocationDAOImpl() {
    connection = dbConnection.getInstance();
  }

  @Override
  public void initTable(String name) {
    this.name = name;
    String locationTable =
        "CREATE TABLE IF NOT EXISTS "
            + name
            + " "
            + "(longname varchar(100) UNIQUE PRIMARY KEY,"
            + "shortname varchar(100),"
            + "nodetype int)";
    System.out.println("Created the location table");
    try {
      Statement stmt = connection.getConnection().createStatement();
      stmt.execute(locationTable);
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error with creating the location table");
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
      e.printStackTrace();
    }
  }

  @Override
  public void importCSV(String path) {
    dropTable();
    locationMap.clear();
    initTable(name);
    loadRemote(path);
  }

  @Override
  public void exportCSV(String path) throws IOException {
    path += "LocationName.csv";
    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path));
    fileWriter.write("longName,shortName,nodeType");
    for (Location location : locationMap.values()) {
      fileWriter.newLine();
      fileWriter.write(location.toCSVString());
    }
    fileWriter.close();
  }

  @Override
  public List<Location> getAll() {
    return locationMap.values().stream().toList();
  }

  @Override
  public Location get(String target) {
    if (locationMap.get(target) == null) {
      System.out.println(
          "This location("
              + target
              + ") is not in the database, so its contents cannot be printed");
      return null;
    }
    return locationMap.get(target);
  }

  @Override
  public void delete(String target) {
    locationMap.remove(target);
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement("DELETE FROM " + name + " WHERE longName = ?");
      stmt.setString(1, target);
      stmt.execute();
      DataBaseRepository.getInstance().moveDAO.constructFromRemote();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void add(Location addition) {
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO " + name + " (longName, shortName, nodetype) VALUES (?,?,?)");
      stmt.setString(1, addition.getLongName());
      stmt.setString(2, addition.getShortName());
      stmt.setInt(3, addition.getNodeType().ordinal());

      this.locationMap.put(addition.getLongName(), addition);
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void add(String longName, String shortName, NodeType type) {
    Location addition = new Location(longName, shortName, type);
    this.add(addition);
  }

  /** Constructs from the remote */
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
        locationMap.put(longName, location);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println("Error accessing the remote and constructing the list of locations");
    }
  }

  /**
   * Constructs remote and database
   *
   * @param csvFilePath
   */
  private void constructRemote(String csvFilePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
      reader.readLine();
      String line;
      try {
        while ((line = reader.readLine()) != null) {
          String[] fields = line.split(",");
          NodeType value = NodeType.valueOf(fields[2]);
          Location location = new Location(fields[0], fields[1], value);
          this.add(location);
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
          locationMap.put(location.getLongName(), location);
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
