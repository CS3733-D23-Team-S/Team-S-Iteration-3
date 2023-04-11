package edu.wpi.teamname.ServiceRequests.flowers;

import edu.wpi.teamname.databaseredo.IDAO;
import edu.wpi.teamname.databaseredo.dbConnection;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/* TODO:
       make sure updateQuantity() works
*/
public class FlowerDAOImpl implements IDAO<Flower, Integer> {
  @Getter private String name;
  private dbConnection connection;
  @Getter private HashMap<Integer, Flower> flowers = new HashMap<>();

  public FlowerDAOImpl() {}

  public void initTable(String name) {
    this.name = name;
    String flowerTableConstruct =
        "CREATE TABLE IF NOT EXISTS "
            + name
            + " "
            + "(ID int UNIQUE PRIMARY KEY,"
            + "flowerName Varchar(100),"
            + "Size Varchar(100),"
            + "Price double precision,"
            + "Quantity int,"
            + "SoldOut boolean,"
            + "Description Varchar(100),"
            + "Image Varchar(100))";

    try {
      Statement st = connection.getConnection().createStatement();
      st.execute(flowerTableConstruct);
      System.out.println("Created the flowers table");

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
        System.out.println("Loading the flowers from the server");
        constructFromRemote();
      } else {
        System.out.println("Loading the flowers to the server");
        constructRemote(pathToCSV);
      }
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
  }

  @Override
  public void importCSV(String path) {
    dropTable();
    flowers.clear();
    loadRemote(path);
  }

  @Override
  public void exportCSV(String path) throws IOException {
    BufferedWriter fileWriter;
    fileWriter = new BufferedWriter(new FileWriter(path));
    fileWriter.write("ID,name,size,price,quantity,isSoldOut,description,image");
    for (Flower flower : flowers.values()) {
      fileWriter.newLine();
      fileWriter.write(flower.toCSVString());
    }
  }

  public Flower get(int ID) {
    return flowers.get(ID);
  }

  @Override
  public List<Flower> getAll() {
    return flowers.values().stream().toList();
  }

  public void delete(Integer ID) {
    try {
      PreparedStatement deleteFlower =
          connection.getConnection().prepareStatement("DELETE FROM " + name + " WHERE ID = ?");

      deleteFlower.setInt(1, ID);
      deleteFlower.execute();

      // remove from local Hashmap
      flowers.remove(ID);

      System.out.println("Flower deleted");

    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  public void add(Flower thisFlower) {
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + name
                      + " (FlowerID, flowerName, size, price, quantity, isSoldOut, description, image) "
                      + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
      preparedStatement.setInt(1, thisFlower.getID());
      preparedStatement.setString(1, thisFlower.getName());
      preparedStatement.setString(1, thisFlower.getSize());
      preparedStatement.setDouble(1, thisFlower.getPrice());
      preparedStatement.setInt(1, thisFlower.getQuantity());
      preparedStatement.setBoolean(1, thisFlower.getIsSoldOut());
      preparedStatement.setString(1, thisFlower.getDescription());
      preparedStatement.setString(1, thisFlower.getImage());

      preparedStatement.execute();

      flowers.put(thisFlower.getID(), thisFlower);

      System.out.println("Flower added");

    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
      System.out.print(e.getSQLState());
    }
  }

  private void constructFromRemote() {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String listOfFlowers = "SELECT * FROM " + name;
      ResultSet rs = stmt.executeQuery(listOfFlowers);
      while (rs.next()) {
        Integer ID = rs.getInt("ID");
        String name = rs.getString("flowerName");
        String size = rs.getString("size");
        Double price = rs.getDouble("price");
        Integer quantity = rs.getInt("quantity");
        Boolean isSoldOut = rs.getBoolean("isSoldOut");
        String description = rs.getString("description");
        String image = rs.getString("image");

        Flower flower = new Flower(ID, name, size, price, quantity, isSoldOut, description, image);

        flowers.put(ID, flower);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println("Error accessing the remote and constructing the list of flowers");
    }
  }

  private void constructRemote(String csvFilePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
      reader.readLine();
      String line;
      try {
        while ((line = reader.readLine()) != null) {
          String[] fields = line.split(",");
          Flower flower =
              new Flower(
                  Integer.parseInt(fields[0]),
                  fields[1],
                  fields[2],
                  Double.parseDouble(fields[3]),
                  Integer.parseInt(fields[4]),
                  Boolean.parseBoolean(fields[5]),
                  fields[6],
                  fields[7]);
          this.add(flower);

          PreparedStatement stmt =
              connection
                  .getConnection()
                  .prepareStatement(
                      "INSERT INTO "
                          + name
                          + " (FlowerID, flowerName, size, price, quantity, isSoldOut, description, image) "
                          + "VALUES (?,?,?,?,?,?,?,?)");

          stmt.setInt(1, Integer.valueOf(fields[0]));
          stmt.setString(2, fields[1]);
          stmt.setString(3, fields[2]);
          stmt.setDouble(4, Double.valueOf(fields[3]));
          stmt.setInt(5, Integer.valueOf(fields[4]));
          stmt.setBoolean(6, Boolean.valueOf(fields[5]));
          stmt.setString(7, fields[6]);
          stmt.setString(8, fields[7]);
          this.flowers.put(flower.getID(), flower);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void updateQuantity(Flower target) {
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement("Update " + name + " SET Quantity = ?, " + "WHERE FlowerID = ?");

      preparedStatement.setInt(1, target.getQuantity());
      preparedStatement.setInt(2, target.getID());

      System.out.println("Flower quantity updated");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  public Flower retrieveFlower(Integer ID) {
    if (flowers.get(ID) == null) {
      throw new NullPointerException("Flower not in database\n");
    } else {
      return flowers.get(ID);
    }
  }

  public List<Flower> getListOfSize(String size) {
    List<Flower> flowers = getAll();
    List<Flower> sizedFlowers = new ArrayList<>();

    for (Flower flower: flowers) {
      if (flower.getSize().equals(size)) {
        sizedFlowers.add(flower);
      }
    }

    return sizedFlowers;

  }
}
