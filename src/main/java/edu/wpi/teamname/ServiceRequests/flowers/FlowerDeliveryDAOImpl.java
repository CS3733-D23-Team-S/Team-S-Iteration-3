package edu.wpi.teamname.ServiceRequests.flowers;
import edu.wpi.teamname.Database.dbConnection;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.Status;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDelivery;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDeliveryDAOImp;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class FlowerDeliveryDAOImpl implements FlowerDeliveryDAO_I {
    protected static final String schemaName = "hospitaldb";
    protected static final String flowerRequestsTable = schemaName + "." + "flowerRequests";
    private HashMap<Integer, FlowerDelivery> requests = new HashMap<>();
    private dbConnection connection = dbConnection.getInstance();
    static FlowerDeliveryDAOImpl single_instance = null;

    private FlowerDeliveryDAOImpl() {}

    public static synchronized FlowerDeliveryDAOImpl getInstance() {

        if (single_instance == null) single_instance = new FlowerDeliveryDAOImpl();

        return single_instance;
    }

    public void initFlowerRequests() {
        try {
            Statement st = connection.getConnection().createStatement();
            String dropFlowerRequestsTable = "DROP TABLE IF EXISTS " + flowerRequestsTable + " CASCADE";

            String flowerRequestsTableConstruct =
                "CREATE TABLE IF NOT EXISTS "
                    + flowerRequestsTable
                    +" "
                    + "(deliveryID int UNIQUE PRIMARY KEY,"
                    + "cartID Varchar(100),"
                    + "orderDate Date,"
                    + "orderTime time,"
                    + "room int,"
                    + "orderedBy Varchar(100),"
                    + "assignedTo Varchar(100),"
                    + "orderStatus Varchar(100),"
                    + "cost int,";

        st.execute(dropFlowerRequestsTable);
        st.execute(flowerRequestsTableConstruct);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            System.out.println("Could not create flowerRequest");
            e.printStackTrace();
        }
    }
    @Override
    public List<FlowerDelivery> getAllRequests() {
        return null;
    }

    @Override
    public FlowerDelivery getRequest(int ID) {
        if (requests.get(ID) == null) {
            throw new NullPointerException("Request not in database");
        }
        return requests.get(ID);
    }

    @Override
    public void addRequest(FlowerDelivery request) {
        requests.put(request.getID(), request);
        try {
            PreparedStatement preparedStatement = connection
                .getConnection()
                .prepareStatement(
                    "INSERT INTO "
                        + flowerRequestsTable
                        + " (deliveryID, cartID, orderDate, orderTime, room, orderedBy, assignedTo, orderStatus, cost) "
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, request.getID());
            preparedStatement.setInt(1, request.getCartID());
            preparedStatement.setDate(1, request.getDate());
            preparedStatement.setTime(1, request.getTime());
            preparedStatement.setInt(1, request.getRoom());
            preparedStatement.setString(1, request.getOrderedBy());
            preparedStatement.setString(1, request.getAssignedTo());
            preparedStatement.setString(1, request.getOrderStatus());
            preparedStatement.setDouble(1, request.getCost());

            preparedStatement.executeUpdate();

            requests.put(request.getID(), request);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
    }

    @Override
    public void deleteRequest(int ID) {
        try {
            PreparedStatement deleteFlower =
                connection
                    .getConnection()
                    .prepareStatement("DELETE FROM " + flowerRequestsTable + " WHERE deliveryID = ?");

            deleteFlower.setInt(1, ID);
            deleteFlower.execute();

            requests.remove(ID);

            System.out.println("FlowerRequest deleted");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }
    }
}
