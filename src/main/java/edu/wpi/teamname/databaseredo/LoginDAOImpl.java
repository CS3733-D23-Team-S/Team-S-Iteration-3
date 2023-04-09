package edu.wpi.teamname.databaseredo;

import edu.wpi.teamname.databaseredo.orms.User;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

public class LoginDAOImpl implements IDAO<User> {

  private dbConnection connection;

  @Getter private HashMap<String, User> loginInfo = new HashMap<>();
  private String name;

  public LoginDAOImpl() {
    connection = dbConnection.getInstance();
  }

  /**
   * @param username
   * @return true if user exists, false if otherwise
   */
  private boolean checkIfUserExists(String username) {
    return loginInfo.get(username) != null;
  }

  /**
   * @param username
   * @param password
   * @return false if user already exists, true if user is made successfully
   */
  public boolean createLoginInfo(String username, String password) {
    // Check if username already exists
    boolean doesUserExist = checkIfUserExists(username);
    if (doesUserExist) return false;

    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + name
                      + " (username, password, permission) VALUES "
                      + "(?, ?, ?)");
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);
      preparedStatement.setInt(3, User.Permission.WORKER.ordinal());
      User user = new User(username, password, User.Permission.WORKER);
      loginInfo.put(username, user);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  /**
   * @param username
   * @param password
   * @return false if username/password does not exist, true if login is successful
   */
  public boolean login(String username, String password) throws Exception {
    if (!checkIfUserExists(username)) {
      throw new Exception("User does not exist");
    } else {
      return password.equals(loginInfo.get(username).getPassword());
    }
  }

  @Override
  public void initTable(String name) {
    this.name = name;
    try {
      Statement stmt = connection.getConnection().createStatement();
      String loginTableConstruct =
          "CREATE TABLE IF NOT EXISTS "
              + name
              + " (username varchar(100) UNIQUE PRIMARY KEY, "
              + "password varchar(100) NOT NULL, "
              + "permission int)";
      stmt.execute(loginTableConstruct);
      User admin = new User("admin", "admin", User.Permission.ADMIN);
      loginInfo.put("admin", admin);
      ResultSet checkExists =
          connection.getConnection().createStatement().executeQuery("SELECT  * FROM " + name);
      if (checkExists.next()) return;

      String addAdmin =
          "INSERT INTO "
              + name
              + " (username, password, permission) VALUES "
              + "('admin','admin',"
              + User.Permission.ADMIN.ordinal()
              + ")";
      stmt.executeUpdate(addAdmin);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void dropTable() {}

  @Override
  public void loadRemote(String pathToCSV) {}

  @Override
  public void importCSV(String path) {}

  @Override
  public void exportCSV(String path) {}

  @Override
  public List<User> getAll() {
    return null;
  }

  @Override
  public void delete(User target) {}

  @Override
  public void add(User addition) {}
}
