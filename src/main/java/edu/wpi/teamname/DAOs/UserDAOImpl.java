package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Permission;
import edu.wpi.teamname.DAOs.orms.User;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

public class UserDAOImpl implements IDAO<User, String> {

  private final dbConnection connection;

  @Getter private HashMap<String, User> listOfUsers;
  @Getter private String name;

  public UserDAOImpl() {
    connection = dbConnection.getInstance();
    listOfUsers = new HashMap<>();
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
   */
  public void createLoginInfo(String username, String password, Permission permission) {
    // Check if username already exists
    boolean doesUserExist = checkIfUserExists(username);
    if (doesUserExist) return;

    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO hospitaldb.login"
                      + " (username, password, permission) VALUES "
                      + "(?, ?, ?)");
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);
      preparedStatement.setInt(3, permission.ordinal());
      preparedStatement.executeUpdate();
      User user = new User(username, password, permission);
      listOfUsers.put(username, user);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
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
        ActiveUser.getInstance().setCurrentUser(listOfUsers.get(username));
        ActiveUser.getInstance().setLoggedIn(true);
        System.out.println(ActiveUser.getInstance().getCurrentUser().getFirstName());
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
              + "permission varchar(100),"
              + " firstName varchar(100), "
              + " lastName varchar(100), "
              + "DOB date, "
              + "email varchar(100), "
              + "jobTitle varchar(100))";
      stmt.execute(loginTableConstruct);
      System.out.println("Created the Users Table");

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
      String checkTable = "SELECT username, password, permission FROM hospitaldb.login ";
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

  public void updateUserInfo(String username, String firstName, String lastName, String email) {
    User thisUser = listOfUsers.get(username);
    thisUser.setEmail(email);
    thisUser.setLastName(lastName);
    thisUser.setFirstName(firstName);

    try {
      PreparedStatement preparedStatement =
          connection
              .getConnection()
              .prepareStatement(
                  "UPDATE hospitaldb.login SET firstname = ?, lastname = ?, email = ? WHERE username = ? ");

      preparedStatement.setString(1, firstName);
      preparedStatement.setString(2, lastName);
      preparedStatement.setString(3, email);
      preparedStatement.setString(4, username);
      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void initUsers() {
    createLoginInfo("admin", "admin", Permission.ADMIN);
    createLoginInfo("staff", "staff", Permission.STAFF);
    createLoginInfo("jolsen", "12346", Permission.STAFF);
    createLoginInfo("ksingh", "12346", Permission.ADMIN);
    createLoginInfo("atitcombe", "12346", Permission.ADMIN);
    createLoginInfo("skogan", "12346", Permission.ADMIN);
    createLoginInfo("nwalling", "12346", Permission.ADMIN);
    createLoginInfo("nho", "12346", Permission.STAFF);
    createLoginInfo("nruben", "12346", Permission.STAFF);
    createLoginInfo("tbrown", "12346", Permission.STAFF);
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

  public List<String> getAssignableUsers() {
    List<String> temp = new ArrayList<>();
    for (User thisUser : listOfUsers.values()) {
      if (thisUser.getPermission().equals(Permission.STAFF)) temp.add(thisUser.getUserName());
    }
    Collections.sort(temp);

    return temp;
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
  public void add(User addition) {}

  private void constructFromRemote() {
    if (!listOfUsers.isEmpty()) {
      System.out.println("There is already stuff in the user orm database");
      return;
    }
    try {
      Statement stmt = connection.getConnection().createStatement();
      String listOfNodes =
          "SELECT username, password, permission, firstname, lastname, email, jobtitle FROM hospitaldb.login";
      ResultSet data = stmt.executeQuery(listOfNodes);
      while (data.next()) {
        String username = data.getString("username");
        String password = data.getString("password");
        Permission permission = Permission.values()[data.getInt("permission")];
        String f = data.getString("firstname");
        String l = data.getString("lastname");
        String e = data.getString("email");
        String t = data.getString("jobtitle");

        User newUser = new User(username, password, f, l, permission, e, t);
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
