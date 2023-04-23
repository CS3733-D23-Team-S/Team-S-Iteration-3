package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Alert;
import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.Signage;
import lombok.Getter;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SignageDAOImpl implements IDAO<Signage, String> {
    private final dbConnection connection;


    @Getter
    private ArrayList<Signage> signages;
    @Getter private String name = "hospitaldb.signage";

    public SignageDAOImpl() {
        connection = dbConnection.getInstance();
        signages = new ArrayList<>();
    }

    @Override
    public void initTable(String name) {
        name = this.name;
        try {
            PreparedStatement stmt =
                    connection
                            .getConnection()
                            .prepareStatement(
                                    "CREATE TABLE IF NOT EXISTS "
                                            + name
                                            + " (kiosklocation varchar(200), direction varchar(20), referredLocation varchar(200)");

            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error with creating the signage table");
        }
    }
    @Override
    public void add(Signage addition) {
        connection.reinitConnection();
        try {
            PreparedStatement preparedStatement =
                    connection
                            .getConnection()
                            .prepareStatement(
                                    "INSERT INTO "
                                            + name
                                            + " (heading, message, alertDate) "
                                            + "VALUES (?, ?, ?)");
            preparedStatement.setString(1, addition.getKioskLocation().getLongName());
            preparedStatement.setString(2, String.valueOf(addition.getDirection()));
            preparedStatement.setString(3, addition.getSurroundingLocation());
            signages.add(addition);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }



    }
    @Override
    public void dropTable() {

    }

    @Override
    public void loadRemote(String pathToCSV) {
        try {
            Statement stmt = connection.getConnection().createStatement();
            String checkTable = "SELECT * FROM " + name + " LIMIT 2";
            ResultSet check = stmt.executeQuery(checkTable);
            if (check.next()) {
                System.out.println("Loading the alerts from the server");
                constructFromRemote();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void constructFromRemote() {
        try {
            LocationDAOImpl locationDAO = DataBaseRepository.getInstance().getLocationDAO();
            Statement stmt = connection.getConnection().createStatement();
            String alerts = "SELECT * FROM " + name;
            ResultSet rs = stmt.executeQuery(alerts);
            while (rs.next()) {
                Location kioskLocation = locationDAO.get(rs.getString("kioskLocation"));
                String direction = rs.getString("direction");
                String referredLocation = rs.getString("referredLocation");
                Signage thisSign = new Signage(kioskLocation, Signage.Direction.valueOf(direction), referredLocation);
                signages.add(thisSign);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getSQLState());
            System.out.println("Error accessing the remote and constructing the list of nodes");
        }
    }

    @Override
    public void importCSV(String path) {

    }

    @Override
    public void exportCSV(String path) throws IOException {

    }

    @Override
    public List<Signage> getAll() {
        return signages;
    }

    @Override
    public Signage get(String target) {
        return null;
    }

    @Override
    public void delete(String target) {

    }


}
