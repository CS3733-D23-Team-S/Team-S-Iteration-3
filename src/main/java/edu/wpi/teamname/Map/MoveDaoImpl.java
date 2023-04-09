package edu.wpi.teamname.Map;

import edu.wpi.teamname.Database.dbConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import edu.wpi.teamname.databaseredo.orms.Move;
import lombok.Getter;

public class MoveDaoImpl implements MoveDAO_I {

  private static MoveDaoImpl single_instance;
  @Getter private String name;
  dbConnection connection;

  @Getter HashMap<String, Move> moves = new HashMap<>();
  @Getter ArrayList<Move> listOfMoves = new ArrayList<>();
  @Getter HashMap<Integer, ArrayList<String>> nodeToLoc = new HashMap<>();

  private MoveDaoImpl() {
    connection = dbConnection.getInstance();
  }

  public static MoveDaoImpl getInstance() {
    if (single_instance == null) single_instance = new MoveDaoImpl();

    return single_instance;
  }

  @Override
  public List<Move> getAllMoves() {
    return moves.values().stream().toList();
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
  public boolean checkCanMove(String location, LocalDate date) {
    return moves.get(location).getDates().contains(date);
  }

  @Override
  public Move getMove(String location) {
    return moves.get(location);
  }

  public String processMoveRequest(int newLocNodeID, String location, LocalDate date)
      throws Exception {
    if (checkCanMove(location, date)) {
      throw new Exception("Moved Today already");
    } else {
      String moveResult;
      if (date.isAfter(LocalDate.now())) {
        moveResult = "Going to move " + location + " on " + date;
      } else {
        moveResult = "Moved " + location + " to its new location";
      }
      Move thisMove = new Move(newLocNodeID, location, date);
      listOfMoves.add(thisMove);
      Move target = moves.get(location);
      nodeToLoc.get(target.getNodeID()).remove(location);
      target.setNodeID(newLocNodeID);
      nodeToLoc.get(newLocNodeID).add(location);
      target.getDates().add(date);
      return moveResult;
    }
  }

  public void loadToRemote(String pathToCSV) {
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

  private void constructFromRemote() {
    if (!moves.isEmpty()) {
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
        moves.put(thisMove.getLocation(), thisMove);
        listOfMoves.add(thisMove);
        ArrayList<String> listOfLoc = new ArrayList<>();
        listOfLoc.add(thisMove.getLocation());
        nodeToLoc.put(thisMove.getNodeID(), listOfLoc);
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
          LocalDate date = DateParser.parseDate(fields[2]);
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
          moves.put(thisMove.getLocation(), thisMove);
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

  private static class DateParser {
    public static LocalDate parseDate(String dateString) {
      // Parse the input date string into a LocalDate object
      LocalDate date =
          LocalDate.parse(
              dateString, DateTimeFormatter.ofPattern("M/d/yyyy").withLocale(Locale.US));
      // Format the LocalDate object as a string in the desired output format
      String outputString =
          date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.US));
      // Parse the output string into a LocalDate object
      return LocalDate.parse(outputString);
    }
  }
}
