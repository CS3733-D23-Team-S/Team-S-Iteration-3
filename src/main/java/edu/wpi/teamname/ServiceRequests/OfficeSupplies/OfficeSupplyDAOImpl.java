package edu.wpi.teamname.ServiceRequests.OfficeSupplies;

import edu.wpi.teamname.DAOs.IDAO;
import edu.wpi.teamname.DAOs.dbConnection;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

public class OfficeSupplyDAOImpl implements IDAO<OfficeSupply, Integer> {
  @Getter private String name;
  private final dbConnection connection;
  @Getter private HashMap<Integer, OfficeSupply> supplies = new HashMap<>();

  public OfficeSupplyDAOImpl() {
    connection = dbConnection.getInstance();
  }

  @Override
  public void initTable(String name) {
    this.name = name;

    String foodTable =
        "CREATE TABLE IF NOT EXISTS "
            + name
            + " "
            + "(officesupplyid int UNIQUE PRIMARY KEY,"
            + "name Varchar(100),"
            + "Price double precision,"
            + "Description Varchar(100),"
            + "Quantity int,"
            + "SoldOut boolean,"
            + "Image Varchar(100),";
    try {
      Statement stmt = connection.getConnection().createStatement();
      stmt.execute(foodTable);
      System.out.println("Created the foods table");

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
  public void loadRemote(String pathToCSV) {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String checkTable = "SELECT * FROM " + name;
      ResultSet check = stmt.executeQuery(checkTable);
      if (check.next()) {
        System.out.println("Loading the foods from the server");
        constructFromRemote();
      } else {
        System.out.println("Loading the foods to the server");
        constructRemote(pathToCSV);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void importCSV(String path) {
    dropTable();
    supplies.clear();
    loadRemote(path);
  }

  @Override
  public void exportCSV(String path) throws IOException {}

  @Override
  public List<OfficeSupply> getAll() {
    return null;
  }

  @Override
  public OfficeSupply get(Integer target) {
    if (supplies.get(target) == null) {
      System.out.println("This office supply is not in the database, so its row cannot be printed");
      return null;
    }
    return supplies.get(target);
  }

  @Override
  public void delete(Integer target) {
    try {
      PreparedStatement deleteSupply =
          connection
              .getConnection()
              .prepareStatement("DELETE FROM " + name + " WHERE officesupplyid = ?");

      deleteSupply.setInt(1, target);
      deleteSupply.execute();

      // remove from local Hashmap
      supplies.remove(target);

      System.out.println("Office Supply deleted");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  @Override
  public void add(OfficeSupply addition) {
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + name
                      + " (officesupplyid, name, price, description, quantity, issoldout, image)"
                      + " VALUES (?, ?, ?, ?, ?, ?, ?)");
      preparedStatement.setInt(1, addition.getId());
      preparedStatement.setString(2, addition.getName());
      preparedStatement.setDouble(3, addition.getPrice());
      preparedStatement.setString(4, addition.getDescription());
      preparedStatement.setInt(5, addition.getQuantity());
      preparedStatement.setBoolean(6, addition.isSoldOut());
      preparedStatement.setString(7, addition.getImage());

      preparedStatement.executeUpdate();

      // add to local Hashmap
      supplies.put(addition.getId(), addition);

      System.out.println("OfficeSupply Added");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  public void constructFromRemote() {}

  private void constructRemote(String pathToCSV) {}
}
