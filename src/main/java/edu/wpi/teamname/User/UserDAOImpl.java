package edu.wpi.teamname.User;

import edu.wpi.teamname.Database.dbConnection;
import java.sql.*;
import java.util.HashMap;
import lombok.Getter;

public class UserDAOImpl implements UserDAOI {
  private static UserDAOImpl single_instance = null;
  private dbConnection connection;

  @Getter private HashMap<String, User> loginInfo = new HashMap<>();
  private String name;

  public static UserDAOImpl getInstance() {
    if (single_instance == null) single_instance = new UserDAOImpl();
    return single_instance;
  }

  private UserDAOImpl() {
    connection = dbConnection.getInstance();
  }

  public void initTables(String loginTableName) throws SQLException {
    this.name = loginTableName;
    try {
      Statement stmt = connection.getConnection().createStatement();
      String loginTableConstruct =
          "CREATE TABLE IF NOT EXISTS "
              + loginTableName
              + " (username varchar(100) UNIQUE PRIMARY KEY, "
              + "password varchar(100) NOT NULL, "
              + "permission int)";
      stmt.execute(loginTableConstruct);
      User admin = new User("admin", "admin", Permission.ADMIN);
      loginInfo.put("admin", admin);
      ResultSet checkExists =
          connection.getConnection().createStatement().executeQuery("SELECT  * FROM " + name);
      if (checkExists.next()) return;

      String addAdmin =
          "INSERT INTO "
              + loginTableName
              + " (username, password, permission) VALUES "
              + "('admin','admin',"
              + Permission.ADMIN.ordinal()
              + ")";
      stmt.executeUpdate(addAdmin);
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
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
      preparedStatement.setInt(3, Permission.STAFF.ordinal());
      User user = new User(username, password, Permission.STAFF);
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

  // public void setGlobalPermission(){}

  // public boolean checkEntry(String str){}

}
