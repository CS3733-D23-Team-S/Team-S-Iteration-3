package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Alert;
import edu.wpi.teamname.DAOs.orms.User;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class AlertDAO implements IDAO<Alert, String> {
  private final dbConnection connection;

  @Getter public ArrayList<Alert> listOfAlerts;
  @Getter private String name = "hospitaldb.alerts";

  public AlertDAO() {
    connection = dbConnection.getInstance();
    listOfAlerts = new ArrayList<>();
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
                      + " (heading varchar(100), message varchar(200), username varchar(200), date date, time time)");

      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error with creating the alert table");
    }
  }

  @Override
  public void add(Alert addition) {
    connection.reinitConnection();
    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + name
                      + " (heading, message, username, date, time) "
                      + "VALUES (?, ?, ?, ?, ?)");
      preparedStatement.setString(1, addition.getHeading());
      preparedStatement.setString(2, addition.getMessage());
      preparedStatement.setString(3, addition.getUser().getUserName());
      preparedStatement.setDate(4, Date.valueOf(addition.getDateOfAlert()));
      preparedStatement.setTime(5, Time.valueOf(addition.getTimeOfAlert()));

      preparedStatement.executeUpdate();

      listOfAlerts.add(addition);

    } catch (SQLException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  @Override
  public void dropTable() {}

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
      UserDAOImpl userDAO = DataBaseRepository.getInstance().getUserDAO();
      Statement stmt = connection.getConnection().createStatement();
      String alerts = "SELECT * FROM " + name + " order by date desc ";
      ResultSet rs = stmt.executeQuery(alerts);
      while (rs.next()) {
        String heading = rs.getString("heading");
        String message = rs.getString("message");
        LocalDate date = rs.getDate("date").toLocalDate();
        LocalTime time = rs.getTime("time").toLocalTime();
        User thisUser = userDAO.get(rs.getString("username"));
        Alert thisAlert = new Alert(heading, message, thisUser);
        thisAlert.setDateOfAlert(date);
        thisAlert.setTimeOfAlert(time);
        listOfAlerts.add(thisAlert);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println("Error accessing the remote and constructing the list of nodes");
    }
  }

  @Override
  public void importCSV(String path) {}

  @Override
  public void exportCSV(String path) throws IOException {}

  @Override
  public List<Alert> getAll() {
    return listOfAlerts;
  }

  @Override
  public Alert get(String target) {
    return null;
  }

  @Override
  public void delete(String target) {}
}
