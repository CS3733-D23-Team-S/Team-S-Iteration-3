package edu.wpi.teamname.ServiceRequests.flowers;
import edu.wpi.teamname.Database.dbConnection;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.Status;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDelivery;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDeliveryDAOImp;

import java.sql.*;
import java.util.ArrayList;
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
                    + " "
                    + "(deliveryID int UNIQUE PRIMARY KEY,"
                    + "cart Varchar(100),"
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

    /**
     * Returns list of all FlowerDeliveries within the deliveries HashMap
     * @return List of all FlowerDeliveries on success
     */
    @Override
    public List<FlowerDelivery> getAllRequests() {
        List<FlowerDelivery> FDList = new ArrayList<>();

        try {
            String query = (
                "SELECT * FROM "
                    + flowerRequestsTable);

            Statement stmt = connection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {

                FDList.add(getRequest(rs.getInt(1)));;
            }

            return FDList;
        }

        catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }




        return FDList;
    }

    /**
     * Gets FlowerDelivery
     * @param ID
     * @return request (FlowerDelivery) on success, otherwise returns null or exception
     */
    @Override
    public FlowerDelivery getRequest(int ID) {
        FlowerDelivery request;
        try {
            String query = (
                    "SELECT * FROM "
                        + flowerRequestsTable
                        + " WHERE deliveryID = "
                        + ID);

            Statement stmt = connection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int flowerID = rs.getInt(1);
                //int cart = rs.getString(2);
                Cart cart = null;
                Date date = rs.getDate(3);
                Time time = rs.getTime(4);
                int room = rs.getInt(5);
                String orderedBy = rs.getString(6);
                String assignedTo = rs.getString(7);
                String orderStatus = rs.getString(8);
                double cost = rs.getDouble(9);

                request = new FlowerDelivery(flowerID, cart, date, time, room, orderedBy, assignedTo, orderStatus, cost);
                return request;
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
        }

        return null;
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
                        + " (deliveryID, cart, orderDate, orderTime, room, orderedBy, assignedTo, orderStatus, cost) "
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, request.getID());
            preparedStatement.setString(1, request.getCart());
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
