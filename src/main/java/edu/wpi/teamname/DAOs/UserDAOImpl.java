package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Floor;
import edu.wpi.teamname.DAOs.orms.Permission;
import edu.wpi.teamname.DAOs.orms.User;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

public class UserDAOImpl implements IDAO<User, String> {

  private final dbConnection connection;

  @Getter private HashMap<String, User> listOfUsers = new HashMap<>();
  @Getter private String name;

  public UserDAOImpl() {
    connection = dbConnection.getInstance();
  }

  /**
   * @param username the username entered by the user
   * @return true if user exists, false if otherwise
   */
  private boolean checkIfUserExists(String username) {
    return listOfUsers.get(username) != null;
  }

  /**
   * @param username the username of the new account
   * @param password the password of the new account
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
      listOfUsers.put(username, user);

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
      if (password.equals(listOfUsers.get(username).getPassword())) {
        ActiveUser.getInstance().setCurrentUser(get(username));
        return true;
      }
    }
    return false;
  }

  @Override
  public void initTable(String name) {
    this.name = name;
    try {
      Statement stmt = connection.getConnection().createStatement();
      String loginTableConstruct =
          //          "CREATE TYPE permissionLevel AS ENUM"
          //              + " ('admin','general','guest') AND
          "CREATE TABLE IF NOT EXISTS "
              + name
              + " (username varchar(100) UNIQUE PRIMARY KEY, "
              + "password varchar(100) NOT NULL, "
              + "permission int)";
      stmt.execute(loginTableConstruct);
      User admin = new User("admin", "admin", Permission.ADMIN);
      User staff = new User("staff", "staff", Permission.STAFF);
      listOfUsers.put("admin", admin);
      listOfUsers.put("staff", staff);
      ResultSet checkExists =
          connection.getConnection().createStatement().executeQuery("SELECT  * FROM " + name);
      if (checkExists.next()) return;

      String addAdmin =
          "INSERT INTO "
              + name
              + " (username, password, permission) VALUES "
              + "('admin','admin',"
              + Permission.ADMIN.ordinal()
              + ")";
      stmt.executeUpdate(addAdmin);

      String addStaff =
          "INSERT INTO "
              + name
              + " (username, password, permission) VALUES "
              + "('staff','staff',"
              + Permission.STAFF.ordinal()
              + ")";
      stmt.executeUpdate(addStaff);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void dropTable() {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String drop = "DROP TABLE IF EXISTS " + name + " CASCADE";
      stmt.executeUpdate(drop);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void loadRemote(String pathToCSV) {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String checkTable = "SELECT * FROM " + name + " LIMIT 2";
      ResultSet check = stmt.executeQuery(checkTable);
      if (check.next()) {
        System.out.println("Loading the users from the server");
        constructFromRemote();
      } else {
        System.out.println("Loading the users to the server");
        constructRemote(pathToCSV);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void importCSV(String path) {
    dropTable();
    listOfUsers.clear();
    loadRemote(path);
  }

  @Override
  public void exportCSV(String path) {
    try {
      BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path));
      fileWriter.write("username,password,permission");
      for (User user : listOfUsers.values()) {
        fileWriter.newLine();
        fileWriter.write(user.toCSVString());
      }
      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<User> getAll() {
    return listOfUsers.values().stream().toList();
  }

  @Override
  public User get(String target) {
    return listOfUsers.get(target);
  }

  @Override
  public void delete(String target) {
    listOfUsers.remove(target);
  }

  @Override
  public void add(User addition) {

    listOfUsers.put(addition.getUserName(), addition);
  }

  private void constructFromRemote() {
    if (!listOfUsers.isEmpty()) {
      System.out.println("There is already stuff in the user orm database");
      return;
    }
    try {
      Statement stmt = connection.getConnection().createStatement();
      String listOfNodes = "SELECT * FROM " + name;
      ResultSet data = stmt.executeQuery(listOfNodes);
      while (data.next()) {
        String username = data.getString("username");
        String password = data.getString("password");
        Permission permission = Permission.values()[data.getInt("permission")];
        Floor floor = Floor.values()[data.getInt("Floor")];
        String building = data.getString("Building");
        User newUser = new User(username, password, permission);
        listOfUsers.put(username, newUser);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println("Error accessing the remote and constructing user database");
    }
  }

  private void constructRemote(String csvFilePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
      try {
        PreparedStatement stmt =
            connection
                .getConnection()
                .prepareStatement(
                    "INSERT INTO "
                        + name
                        + " (username, password, permission) VALUES (? , ? , ? )");
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
          String[] fields = line.split(",");
          //          System.out.println(Arrays.toString(fields));
          stmt.setString(1, fields[0]);
          stmt.setString(2, fields[1]);
          stmt.setInt(3, Integer.parseInt(fields[2]));
          stmt.executeUpdate();
          User newUser =
              new User(fields[0], fields[1], Permission.values()[Integer.parseInt(fields[2])]);
          listOfUsers.put(fields[0], newUser);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
