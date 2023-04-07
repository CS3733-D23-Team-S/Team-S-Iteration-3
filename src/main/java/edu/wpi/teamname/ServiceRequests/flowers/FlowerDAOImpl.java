package edu.wpi.teamname.ServiceRequests.flowers;

import edu.wpi.teamname.Database.dbConnection;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDAOImpl;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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

            System.out.println("Food deleted");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
    }




}
