package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Move;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import lombok.Getter;

public class MoveDAOImpl implements IDAO<Move, Move> {

  @Getter private String name;
  @Getter private final String CSVheader = "nodeID,longName,date";
  private dbConnection connection;

  @Getter ArrayList<Move> listOfMoves = new ArrayList<>();
  @Getter HashMap<String, List<LocalDate>> moveHistory = new HashMap<>();
  @Getter HashMap<String, Integer> locToNode = new HashMap<>();
  @Getter HashMap<Integer, String> nodeToLoc = new HashMap<>();

  public MoveDAOImpl() {
    connection = dbConnection.getInstance();
  }

  @Override
  public void initTable(String name) {
    this.name = name;
    String moveTable =
        "CREATE TABLE IF NOT EXISTS "
            + name
            + " "
            + "(nodeID int, location varchar(100) UNIQUE, date DATE)";
    try {
      Statement stmt = connection.getConnection().createStatement();
      stmt.execute(moveTable);
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error with creating the node table");
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
        System.out.println("Loading the moves from the server");
        constructFromRemote();
      } else {
        System.out.println("Loading the moves to the server");
        constructRemote(pathToCSV);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void importCSV(String path) {
    dropTable();
    locToNode.clear();
    listOfMoves.clear();
    initTable(name);
    loadRemote(path);
  }

  @Override
  public void exportCSV(String path) throws IOException {
    path += "Move.csv";
    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path));
    fileWriter.write("nodeID,longName,date");
    for (Move move : listOfMoves) {
      fileWriter.newLine();
      fileWriter.write(move.toCSVString());
    }
    fileWriter.close();
  }

  @Override
  public List<Move> getAll() {
    return listOfMoves;
  }

  @Override
  public Move getRow(Move target) {
    return null;
  }

  @Override
  public void delete(Move target) {
    listOfMoves.remove(target);
    moveHistory.get(target.getLocation()).remove(target.getDate());
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement(
                  "DELETE FROM " + name + " WHERE nodeID=? AND location=?" + "AND date=?");
      stmt.setInt(1, target.getNodeID());
      stmt.setString(2, target.getLocation());
      stmt.setDate(3, Date.valueOf(target.getDate()));
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  //    public void delete(String location, LocalDate date){
  //
  //    }

  @Override
  public void add(Move addition) {
    listOfMoves.add(addition);
    if (!moveHistory.containsKey(addition.getLocation()))
      moveHistory.get(addition.getLocation()).add(addition.getDate());
    else {
      List<LocalDate> temp = new ArrayList<>();
      temp.add(addition.getDate());
      moveHistory.put(addition.getLocation(), temp);
    }
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement("INSERT INTO " + name + " (nodeID, location, date) VALUES (?,?,?)");
      stmt.setInt(1, addition.getNodeID());
      stmt.setString(2, addition.getLocation());
      stmt.setDate(3, Date.valueOf(addition.getDate()));
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void add(int nodeID, String location, LocalDate date) {
    Move newMove = new Move(nodeID, location, date);
    this.add(newMove);
  }

  private void constructFromRemote() {
    if (!listOfMoves.isEmpty()) {
      System.out.println("There is already stuff in the orm database");
      return;
    }
    try {
      Statement stmt = connection.getConnection().createStatement();
      String getData = "SELECT * FROM " + name;
      ResultSet data = stmt.executeQuery(getData);
      while (data.next()) {
        LocalDate date = data.getDate("date").toLocalDate();
        Move thisMove = new Move(data.getInt("nodeID"), data.getString("location"), date);
        listOfMoves.add(thisMove);
        locToNode.put(thisMove.getLocation(), thisMove.getNodeID());
        nodeToLoc.put(thisMove.getNodeID(), thisMove.getLocation());
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println("Error accessing the remote and constructing the list of moves");
    }
  }

  private void constructRemote(String csvFilePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
      try {
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
          String[] fields = line.split(",");
          LocalDate date = parseDate(fields[2]);
          //          System.out.println(Arrays.toString(fields));
          Move thisMove = new Move(Integer.parseInt(fields[0]), fields[1], date);
          //          System.out.println(thisMove);
          PreparedStatement stmt =
              connection
                  .getConnection()
                  .prepareStatement(
                      "INSERT INTO " + name + " (nodeID, location, date) VALUES (?,?,?)");
          stmt.setInt(1, Integer.parseInt(fields[0]));
          stmt.setString(2, fields[1]);
          stmt.setDate(3, java.sql.Date.valueOf(date));
          stmt.executeUpdate();
          listOfMoves.add(thisMove);
        }
        constructFromRemote();
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println(e.getSQLState());
        System.out.println("Error accessing the remote and constructing the list of moves");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private LocalDate parseDate(String dateString) {
    // Parse the input date string into a LocalDate object
    LocalDate date =
        LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M/d/yyyy").withLocale(Locale.US));
    // Format the LocalDate object as a string in the desired output format
    String outputString =
        date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.US));
    // Parse the output string into a LocalDate object
    return LocalDate.parse(outputString);
  }
}
