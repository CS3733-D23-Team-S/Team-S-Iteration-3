package edu.wpi.teamname.ServiceRequests.flowers;

import edu.wpi.teamname.Database.dbConnection;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDAOImpl;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class FlowerDAOImpl {
    private static FlowerDAOImpl single_instance;
    @Getter
    private HashMap<Integer, Flower> flowers = new HashMap<>();
    private dbConnection connection = dbConnection.getInstance();
    protected static final String flowersTable = "hospitaldb" + "." + "flowers";

    private FlowerDAOImpl() {}

    public static synchronized FlowerDAOImpl getInstance() {
        if (single_instance == null) single_instance = new FlowerDAOImpl();

        return single_instance;
    }

    public void init() {
        try {
            Statement st = connection.getConnection().createStatement();
            String dropFlowerTable = "DROP TABLE IF EXISTS " + flowersTable + " CASCADE";

            String flowerTableConstruct =
                    "CREATE TABLE IF NOT EXISTS "
                            + flowersTable
                            + " "
                            + "(ID int UNIQUE PRIMARY KEY,"
                            + "Name Varchar(100),"
                            + "Size Varchar(100),"
                            + "Price double precision,"
                            + "Quantity int,"
                            + "SoldOut boolean,"
                            + "Description Varchar(100),"
                            + "Image Varchar(100),";

            st.execute(dropFlowerTable);
            st.execute(flowerTableConstruct);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            System.out.println("Database creation error");
            e.printStackTrace();
        }
    }

    /**
     * Adds flower to database
     * @param thisFlower
     */
    public void addFlower(Flower thisFlower) {
        try {
            PreparedStatement preparedStatement =
                connection
                    .getConnection()
                    .prepareStatement(
                        "INSERT INTO "
                            + flowersTable
                            + " (FlowerID, Name, size, price, quantity, isSoldOut, description, image) "
                            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, thisFlower.getID());
            preparedStatement.setString(1, thisFlower.getName());
            preparedStatement.setString(1, thisFlower.getSize());
            preparedStatement.setDouble(1, thisFlower.getPrice());
            preparedStatement.setInt(1, thisFlower.getQuantity());
            preparedStatement.setBoolean(1, thisFlower.getIsSoldOut());
            preparedStatement.setString(1, thisFlower.getDescription());
            preparedStatement.setString(1, thisFlower.getImage());

            preparedStatement.executeUpdate();

            flowers.put(thisFlower.getID(), thisFlower);

            System.out.println("Flower added");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.print(e.getSQLState());
        }
    }

    /**
     * Removes flower from database
     * @param ID: flower.ID
     * @throws SQLException
     */
    public void deleteFlower(int ID) throws SQLException {
        PreparedStatement deleteFlower =
            connection
                .getConnection()
                .prepareStatement("DELETE FROM " + flowersTable + " WHERE FlowerID = ?");

        try {
            deleteFlower.setInt(1, ID);
            deleteFlower.execute();

            // remove from local Hashmap
            flowers.remove(ID);

            System.out.println("Flower deleted");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
    }

    public void updateFlower(int ID){}

    public Flower retrieveFlower(int ID) {
        if (flowers.get(ID) == null) {
            throw new NullPointerException("Flower not in database\n");
        } else {
            return flowers.get(ID);
        }
    }

    public void loadToRemote() {

        try {
            Statement st = connection.getConnection().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM " + flowersTable);

            while (rs.next()) {
                Integer ID = rs.getInt("ID");
                String name = rs.getString("name");
                String size = rs.getString("size");
                Double price = rs.getDouble("price");
                Integer quantity = rs.getInt("quantity");
                Boolean isSoldOut = rs.getBoolean("isSoldOut");
                String description = rs.getString("description");
                String image = rs.getString("image");

                Flower flower = new Flower(ID, name, size, price, quantity, isSoldOut, description, image);
                flowers.put(ID, flower);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param csvFilePath
     */

    public void csvToFlower(String csvFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String headerLine = reader.readLine();
            String line;
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
                flowers.put(Integer.valueOf(fields[0]), flower);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }*/
}
