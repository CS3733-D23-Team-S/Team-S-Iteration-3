package edu.wpi.teamname.databaseredo;

import edu.wpi.teamname.databaseredo.orms.Location;
import edu.wpi.teamname.databaseredo.orms.NodeType;
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
  private dbConnection connection;

  @Getter private HashMap<String, Location> locations = new HashMap<>();

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
            + "(longname varchar(100),"
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
    locations.clear();
    initTable(name);
    loadRemote(path);
  }

  @Override
  public void exportCSV(String path) throws IOException {
    path += "LocationName.csv";
    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path));
    fileWriter.write("longName,shortName,nodeType");
    for (Location location : locations.values()) {
      fileWriter.newLine();
      fileWriter.write(location.toCSVString());
    }
    fileWriter.close();
  }

  @Override
  public List<Location> getAll() {
    return locations.values().stream().toList();
  }

  @Override
  public void delete(String target) {
    locations.remove(target);
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
      locations.put(addition.getLongName(), addition);
      stmt.execute();
    } catch (SQLException e) {
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
          this.locations.put(location.getLongName(),location);
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
