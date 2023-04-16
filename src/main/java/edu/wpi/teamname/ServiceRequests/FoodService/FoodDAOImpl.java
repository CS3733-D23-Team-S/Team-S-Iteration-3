package edu.wpi.teamname.ServiceRequests.FoodService;

import edu.wpi.teamname.DAOs.IDAO;
import edu.wpi.teamname.DAOs.dbConnection;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;

public class FoodDAOImpl implements IDAO<Food, Integer> {
  @Getter private String name;
  private final dbConnection connection;
  @Getter private HashMap<Integer, Food> foods = new HashMap<>();

  public FoodDAOImpl() {
    connection = dbConnection.getInstance();
  }

  public void initTable(String name) {
    this.name = name;
    String foodTable =
        "CREATE TABLE IF NOT EXISTS "
            + name
            + " "
            + "(FoodID int UNIQUE PRIMARY KEY,"
            + "Name Varchar(100),"
            + "Type Varchar(100),"
            + "PrepTime int,"
            + "Cuisine Varchar(100),"
            + "Price double precision,"
            + "Description Varchar(100),"
            + "Quantity int,"
            + "SoldOut boolean,"
            + "Image Varchar(100),"
            + "Calories int,"
            + "Note Varchar(100),"
            + "Italian boolean,"
            + "American boolean,"
            + "Indian boolean,"
            + "Mexican boolean,"
            + "Vegetarian boolean,"
            + "Halal boolean,"
            + "Vegan boolean,"
            + "GlutenFree boolean,"
            + "Kosher boolean)";
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

  public void add(Food thisFood) {
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + name
                      + " (FoodID, Name, Type, PrepTime, Cuisine, Price, Description, Quantity, SoldOut, Image, "
                      + "Calories, note, Italian, American, Indian, Mexican, Vegetarian, Halal, Vegan, GlutenFree, Kosher) "
                      + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      preparedStatement.setInt(1, thisFood.getFoodID());
      preparedStatement.setString(2, thisFood.getFoodName());
      preparedStatement.setString(3, thisFood.getFoodType());
      preparedStatement.setInt(4, thisFood.getFoodPrepTime());
      preparedStatement.setString(5, thisFood.getFoodCuisine());
      preparedStatement.setDouble(6, thisFood.getFoodPrice());
      preparedStatement.setString(7, thisFood.getFoodDescription());
      preparedStatement.setInt(8, thisFood.getQuantity());
      preparedStatement.setBoolean(9, thisFood.isSoldOut());
      preparedStatement.setString(10, thisFood.getImage());
      preparedStatement.setInt(11, thisFood.getCalories());
      preparedStatement.setString(12, thisFood.getNote());
      preparedStatement.setBoolean(13, thisFood.isItalian());
      preparedStatement.setBoolean(14, thisFood.isAmerican());
      preparedStatement.setBoolean(15, thisFood.isIndian());
      preparedStatement.setBoolean(16, thisFood.isMexican());
      preparedStatement.setBoolean(17, thisFood.isVegetarian());
      preparedStatement.setBoolean(18, thisFood.isHalal());
      preparedStatement.setBoolean(19, thisFood.isVegan());
      preparedStatement.setBoolean(20, thisFood.isGlutFree());
      preparedStatement.setBoolean(21, thisFood.isKosher());

      preparedStatement.executeUpdate();

      // add to local Hashmap
      foods.put(thisFood.getFoodID(), thisFood);

      System.out.println("Food added");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  public void delete(Integer target) {
    try {
      PreparedStatement deleteFood =
          connection.getConnection().prepareStatement("DELETE FROM " + name + " WHERE FoodID = ?");

      deleteFood.setInt(1, target);
      deleteFood.execute();

      // remove from local Hashmap
      foods.remove(target);

      System.out.println("Food deleted");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  public void updateFood(int updateNeededFood) {

    Food thisFood = foods.get(updateNeededFood);

    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "Update "
                      + name
                      + " SET Name = ? ,Type = ?, PrepTime = ? , Cuisine = ?, Price = ?, Description = ?, Quantity = ?"
                      + ", SoldOut = ?, "
                      + "Image = ?, Calories = ?, Note = ?, Italian = ?, American = ?, Indian = ?, Mexican = ?, Vegetarian = ?, "
                      + "Halal = ?, Vegan = ?, GlutenFree = ?, Kosher = ?"
                      + " WHERE FoodID = ?");

      preparedStatement.setString(1, thisFood.getFoodName());
      preparedStatement.setString(2, thisFood.getFoodType());
      preparedStatement.setInt(3, thisFood.getFoodPrepTime());
      preparedStatement.setString(4, thisFood.getFoodCuisine());
      preparedStatement.setDouble(5, thisFood.getFoodPrice());
      preparedStatement.setString(6, thisFood.getFoodDescription());
      preparedStatement.setInt(7, thisFood.getQuantity());
      preparedStatement.setBoolean(8, thisFood.isSoldOut());
      preparedStatement.setString(9, thisFood.getImage());
      preparedStatement.setInt(10, thisFood.getCalories());
      preparedStatement.setString(11, thisFood.getNote());
      preparedStatement.setBoolean(12, thisFood.isItalian());
      preparedStatement.setBoolean(13, thisFood.isAmerican());
      preparedStatement.setBoolean(14, thisFood.isIndian());
      preparedStatement.setBoolean(15, thisFood.isMexican());
      preparedStatement.setBoolean(16, thisFood.isVegetarian());
      preparedStatement.setBoolean(17, thisFood.isHalal());
      preparedStatement.setBoolean(18, thisFood.isVegan());
      preparedStatement.setBoolean(19, thisFood.isGlutFree());
      preparedStatement.setBoolean(20, thisFood.isKosher());
      preparedStatement.setInt(21, thisFood.getFoodID());
      preparedStatement.executeUpdate();

      System.out.println("Food updated");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  public Food get(Integer target) {
    if (foods.get(target) == null) {
      System.out.println("This food is not in the database, so its row cannot be printed");
      return null;
    }
    return foods.get(target);
  }

  @Override
  public ArrayList<Food> getAll() {
    ArrayList<Food> list = new ArrayList<Food>();

    for (Food aFood : foods.values()) {
      if (aFood.isWalletFriendly()) {
        list.add(aFood);
      }
    }
    return list;
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
    foods.clear();
    loadRemote(path);
  }

  @Override
  public void exportCSV(String path) throws IOException {
    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path));
    fileWriter.write(
        "foodID,name,type,prepTime,cuisine,price,description,quantity,isSoldOut,image,calories,"
            + "notes,isAmerican,isItalian,isMexican,isIndian,isVegetarian,isVegan,isHalal,isGlutenFree,isKosher");

    for (Food food : foods.values()) {
      fileWriter.newLine();
      fileWriter.write(food.toCSVString());
    }
    fileWriter.close();
  }

  private void constructFromRemote() {
    try {
      Statement st = connection.getConnection().createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM " + name);

      while (rs.next()) {
        int foodid = rs.getInt("foodid");
        String foodname = rs.getString("name");
        String foodtype = rs.getString("type");
        int foodpreptime = rs.getInt("preptime");
        String foodcuisine = rs.getString("cuisine");
        double foodprice = rs.getDouble("price");
        String fooddesciption = rs.getString("description");
        int quantity = rs.getInt("quantity");
        boolean issoldout = rs.getBoolean("soldout");
        String image = rs.getString("image");
        int cal = rs.getInt("calories");
        String not = rs.getString("note");
        boolean isit = rs.getBoolean("italian");
        boolean isam = rs.getBoolean("american");
        boolean isin = rs.getBoolean("indian");
        boolean isme = rs.getBoolean("mexican");
        boolean isveg = rs.getBoolean("vegetarian");
        boolean isha = rs.getBoolean("halal");
        boolean isve = rs.getBoolean("vegan");
        boolean isgl = rs.getBoolean("glutenfree");
        boolean isko = rs.getBoolean("kosher");

        Food f =
            new Food(
                foodid,
                foodname,
                foodtype,
                foodpreptime,
                foodcuisine,
                foodprice,
                fooddesciption,
                quantity,
                issoldout,
                image,
                cal,
                not,
                isit,
                isam,
                isin,
                isme,
                isveg,
                isha,
                isve,
                isgl,
                isko);

        foods.put(foodid, f);
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

        Food thisFood =
            new Food(
                Integer.parseInt(fields[0]),
                (fields[1]),
                (fields[2]),
                Integer.parseInt(fields[3]),
                fields[4],
                Double.parseDouble(fields[5]),
                fields[6],
                Integer.parseInt(fields[7]),
                Boolean.parseBoolean(fields[8]),
                fields[9],
                Integer.parseInt(fields[10]),
                fields[11],
                Boolean.parseBoolean(fields[12]),
                Boolean.parseBoolean(fields[13]),
                Boolean.parseBoolean(fields[14]),
                Boolean.parseBoolean(fields[15]),
                Boolean.parseBoolean(fields[16]),
                Boolean.parseBoolean(fields[17]),
                Boolean.parseBoolean(fields[18]),
                Boolean.parseBoolean(fields[19]),
                Boolean.parseBoolean(fields[20]));

        add(thisFood);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
