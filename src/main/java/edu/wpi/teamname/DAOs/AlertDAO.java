package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Alert;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class AlertDAO implements IDAO<Alert, String> {
  @Override
  public void initTable(String name) {}

  @Override
  public void dropTable() {}

  @Override
  public void loadRemote(String pathToCSV) {}

  @Override
  public void importCSV(String path) {}

  @Override
  public void exportCSV(String path) throws IOException {}

  @Override
  public List<Alert> getAll() {
    return null;
  }

  @Override
  public Alert get(String target) {
    return null;
  }

  @Override
  public void delete(String target) {}

  @Override
  public void add(Alert addition) {}

  /*
  private final dbConnection connection;

  @Getter private ArrayList<Alert> listOfAlerts;
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
                      + " (heading varchar(100), message varchar(200), date date)");

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
                  "INSERT INTO " + name + " (heading, message, alertDate) " + "VALUES (?, ?, ?)");
      preparedStatement.setString(1, addition.getHeading());
      preparedStatement.setString(2, addition.getMessage());
      preparedStatement.setDate(3, Date.valueOf(addition.getDateOfAlert()));
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
      Statement stmt = connection.getConnection().createStatement();
      String alerts = "SELECT * FROM " + name + " order by alertDate desc ";
      ResultSet rs = stmt.executeQuery(alerts);
      while (rs.next()) {
        String heading = rs.getString("heading");
        String message = rs.getString("message");
        LocalDate date = rs.getDate("alertDate").toLocalDate();
        Alert thisAlert = new Alert(heading, message);
        thisAlert.setDateOfAlert(date);
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

   */
}
