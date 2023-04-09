package edu.wpi.teamname.ServiceRequests.FoodService;

import edu.wpi.teamname.Database.dbConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;

public class FoodDAOImpl implements FoodDAO_I {
  private static FoodDAOImpl single_instance;
  @Getter private HashMap<Integer, Food> foods = new HashMap<>();
  private dbConnection connection = dbConnection.getInstance();
  protected static final String foodsTable = "hospitaldb" + "." + "foods";

  private FoodDAOImpl() {}

  public static synchronized FoodDAOImpl getInstance() {
    if (single_instance == null) single_instance = new FoodDAOImpl();

    return single_instance;
  }

  public void addFood(Food thisFood) {
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + foodsTable
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

      foods.put(thisFood.getFoodID(), thisFood);

      System.out.println("Food added");

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
    }
  }

  public void deleteFood(int target) throws SQLException {
    PreparedStatement deleteFood =
        connection
            .getConnection()
            .prepareStatement("DELETE FROM " + foodsTable + " WHERE FoodID = ?");
    try {
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
                      + foodsTable
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

  public Food retrieveFood(int target) {
    if (foods.get(target) == null) {
      System.out.println("This node is not in the database, so its row cannot be printed");
      return null;
    }
    return foods.get(target);
  }

  public ArrayList<Food> getWalletFriendlyFood() {
    ArrayList<Food> wFriendlyFoods = new ArrayList<>();

    for (Food aFood : foods.values()) {
      if (aFood.isWalletFriendly()) {
        wFriendlyFoods.add(aFood);
      }
    }

    return wFriendlyFoods;
  }

  public ArrayList<Food> getVegetarian() {
    ArrayList<Food> vegetarianFoods = new ArrayList<>();
    for (Food aFood : foods.values()) {
      if (aFood.checkVegetarian()) {
        vegetarianFoods.add(aFood);
      }
    }
    return vegetarianFoods;
  }

  public ArrayList<Food> getVegan() {
    ArrayList<Food> veganFoods = new ArrayList<>();
    for (Food aFood : foods.values()) {
      if (aFood.checkVegan()) {
        veganFoods.add(aFood);
      }
    }
    return veganFoods;
  }

  public ArrayList<Food> getHalal() {
    ArrayList<Food> halalFoods = new ArrayList<>();
    for (Food aFood : foods.values()) {
      if (aFood.checkHalal()) {
        halalFoods.add(aFood);
      }
    }
    return halalFoods;
  }

  public ArrayList<Food> getKosher() {
    ArrayList<Food> kosherFoods = new ArrayList<>();
    for (Food aFood : foods.values()) {
      if (aFood.checkKosher()) {
        kosherFoods.add(aFood);
      }
    }
    return kosherFoods;
  }

  public ArrayList<Food> getGlutenFree() {
    ArrayList<Food> glutenFreeFoods = new ArrayList<>();
    for (Food aFood : foods.values()) {
      if (aFood.checkGlutenFree()) {
        glutenFreeFoods.add(aFood);
      }
    }
    return glutenFreeFoods;
  }

  public ArrayList<Food> getQuick() {
    ArrayList<Food> quickFood = new ArrayList<>();

    for (Food aFood : foods.values()) {
      if (aFood.isQuickDelivery()) {
        quickFood.add(aFood);
      }
    }

    return quickFood;
  }

  public void loadToRemote() {

    try {
      Statement st = connection.getConnection().createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM " + foodsTable);

      while (rs.next()) {
        Integer foodid = rs.getInt("foodid");
        String foodname = rs.getString("name");
        String foodtype = rs.getString("type");
        Integer foodpreptime = rs.getInt("preptime");
        String foodcuisine = rs.getString("cuisine");
        Double foodprice = rs.getDouble("price");
        String fooddesciption = rs.getString("description");
        Integer quantity = rs.getInt("quantity");
        Boolean issoldout = rs.getBoolean("soldout");
        String image = rs.getString("image");
        Integer cal = rs.getInt("calories");
        String not = rs.getString("note");
        Boolean isit = rs.getBoolean("italian");
        Boolean isam = rs.getBoolean("american");
        Boolean isin = rs.getBoolean("indian");
        Boolean isme = rs.getBoolean("mexican");
        Boolean isveg = rs.getBoolean("vegetarian");
        Boolean isha = rs.getBoolean("halal");
        Boolean isve = rs.getBoolean("vegan");
        Boolean isgl = rs.getBoolean("glutenfree");
        Boolean isko = rs.getBoolean("kosher");

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

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void initFood() {
    try {
      Statement st = connection.getConnection().createStatement();
      String dropFoodTable = "DROP TABLE IF EXISTS " + foodsTable + " CASCADE";

      String foodTableConstruct =
          "CREATE TABLE IF NOT EXISTS "
              + foodsTable
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

      st.execute(dropFoodTable);
      st.execute(foodTableConstruct);

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("Database creation error");
      e.printStackTrace();
    }
  }

  public void csvToFood(String csvFilePath) {

    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
      String headerLine = reader.readLine();
      String line;
      while ((line = reader.readLine()) != null) {
        String[] fields = line.split(",");
        // int fid, String fn, String ft, int fpt, String fc, double fp, String fd, int q
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
        foods.put(Integer.valueOf(fields[0]), thisFood);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
