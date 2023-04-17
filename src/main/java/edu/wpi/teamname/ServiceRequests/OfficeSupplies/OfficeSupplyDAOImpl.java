package edu.wpi.teamname.ServiceRequests.OfficeSupplies;

import edu.wpi.teamname.DAOs.IDAO;
import edu.wpi.teamname.DAOs.dbConnection;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.ISRDAO;
import lombok.Getter;
import java.io.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                        + "isSoldOut boolean,"
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
    public void exportCSV(String path) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path));
        fileWriter.write("officesupplyid,name,price,description,quantity,issoldout,image");

        for (OfficeSupply os : supplies.values()) {
            fileWriter.newLine();
            fileWriter.write(os.toCSVString());
        }
        fileWriter.close();
    }

    @Override
    public ArrayList<OfficeSupply> getAll() {
        ArrayList<OfficeSupply> list = new ArrayList<>();

        for (OfficeSupply os : supplies.values()) {
                list.add(os);
        }
        return list;
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
                    connection.getConnection().prepareStatement("DELETE FROM " + name + " WHERE officesupplyid = ?");

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
            preparedStatement.setInt(1, addition.getOfficesupplyid());
            preparedStatement.setString(2, addition.getName());
            preparedStatement.setDouble(3, addition.getPrice());
            preparedStatement.setString(4, addition.getDescription());
            preparedStatement.setInt(5, addition.getQuantity());
            preparedStatement.setBoolean(6, addition.isSoldOut());
            preparedStatement.setString(7, addition.getImage());

            preparedStatement.executeUpdate();

            // add to local Hashmap
            supplies.put(addition.getOfficesupplyid(), addition);

            System.out.println("OfficeSupply Added");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
    }

    public void constructFromRemote()
    {
        try {
            Statement st = connection.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + name);

            while (rs.next()) {
                int id = rs.getInt("officesupplyid");
                String oname = rs.getString("name");
                double price = rs.getDouble("price");
                String desciption = rs.getString("description");
                int quantity = rs.getInt("quantity");
                boolean issoldout = rs.getBoolean("issoldout");
                String image = rs.getString("image");

                OfficeSupply ooo =
                        new OfficeSupply(
                                id,
                                oname,
                                price,
                                desciption,
                                quantity,
                                issoldout,
                                image);

                supplies.put(id, ooo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Error accessing the remote and constructing the list of foods");
        }
    }

  private void constructRemote(String csvFilePath) {
      try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
          String headerLine = reader.readLine();
          String line;
          while ((line = reader.readLine()) != null) {
              String[] fields = line.split(",");

              OfficeSupply thisos =
                      new OfficeSupply(
                              Integer.parseInt(fields[0]),
                              (fields[1]),
                              Double.parseDouble(fields[2]),
                              (fields[3]),
                              Integer.parseInt(fields[4]),
                              Boolean.parseBoolean(fields[5]),
                              fields[6]);
              add(thisos);
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}
