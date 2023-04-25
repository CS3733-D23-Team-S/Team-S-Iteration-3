package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.*;
import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.Getter;

public class MoveDAOImpl implements IDAO<Move, Move> {

  @Getter private String name;
  @Getter private final String CSVheader = "nodeID,longName,date";
  private final dbConnection connection;

  // List of all moves that have ever occurred
  @Getter ArrayList<Move> listOfMoves = new ArrayList<>();

  // List of moves for a location
  @Getter HashMap<String, List<Move>> locationMoveHistory = new HashMap<>();
  // Get the moves associated with the nodeID
  @Getter HashMap<Integer, List<Move>> locationsAtNodeID = new HashMap<>();

  public MoveDAOImpl() {
    connection = dbConnection.getInstance();
  }

  @Override
  public void initTable(String name) {
    this.name = name;
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement(
                  "CREATE TABLE IF NOT EXISTS "
                      + name
                      + " (nodeID int, location varchar(100), date DATE, FOREIGN KEY (nodeID) "
                      + "REFERENCES hospitaldb.nodes(nodeID) ON DELETE CASCADE ON UPDATE CASCADE)");

      stmt.execute();
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
    locationsAtNodeID.clear();
    listOfMoves.clear();
    locationMoveHistory.clear();
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
  public Move get(Move target) {
    return null;
  }

  @Override
  public void delete(Move target) {
    listOfMoves.remove(target);
    locationMoveHistory.get(target.getLocationName()).remove(target);
    locationsAtNodeID.get(target.getNodeID()).remove(target);
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement(
                  "DELETE FROM " + name + " WHERE nodeID=? AND location=? " + "AND date=?");
      stmt.setInt(1, target.getNodeID());
      stmt.setString(2, target.getLocationName());
      stmt.setDate(3, Date.valueOf(target.getDate()));
      stmt.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void add(Move addition) {
    //    listOfMoves.add(addition);
    //    ArrayList<Move> moveArrayList = new ArrayList<>();
    //    moveArrayList.add(addition);
    //    if (!locationMoveHistory.containsKey(addition.getLocationName())) {
    //      locationMoveHistory.put(addition.getLocationName(), moveArrayList);
    //    } else {
    //      locationMoveHistory.get(addition.getLocationName()).add(addition);
    //    }
    //    if (!locationsAtNodeID.containsKey(addition.getNodeID())) {
    //      locationsAtNodeID.put(addition.getNodeID(), moveArrayList);
    //    } else {
    //      locationsAtNodeID.get(addition.getNodeID()).add(addition);
    //    }
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement("INSERT INTO " + name + " (nodeID, location, date) VALUES (?,?,?)");
      stmt.setInt(1, addition.getNodeID());
      stmt.setString(2, addition.getLocationName());
      stmt.setDate(3, Date.valueOf(addition.getDate()));
      stmt.executeUpdate();
      System.out.println("Added the move to the remote somehow");
    } catch (SQLException e) {
      System.out.println("Error adding a move to the remote");
      e.printStackTrace();
    }
  }

  public void add(Node node, Location location, LocalDate date) {
    Move newMove = new Move(node, location, date);
    this.add(newMove);
  }

  public void addToJustDBandLoc(Move addition) {
    // listOfMoves.add(addition);
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement("INSERT INTO " + name + " (nodeID, location, date) VALUES (?,?,?)");
      stmt.setInt(1, addition.getNode().getNodeID());
      stmt.setString(2, addition.getLocation().getLongName());
      stmt.setDate(3, Date.valueOf(addition.getDate()));
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  void constructFromRemote() {
    listOfMoves.clear();
    locationsAtNodeID.clear();
    locationMoveHistory.clear();
    if (!listOfMoves.isEmpty()) {
      System.out.println("There is already stuff in the orm database");
      return;
    }
    NodeDAOImpl nodeDAO = DataBaseRepository.getInstance().nodeDAO;
    LocationDAOImpl locationDAO = DataBaseRepository.getInstance().locationDAO;
    try {
      Statement stmt = connection.getConnection().createStatement();
      ResultSet data = stmt.executeQuery("SELECT * FROM " + name);
      while (data.next()) {
        LocalDate date = data.getDate("date").toLocalDate();
        Node node = nodeDAO.get(data.getInt("nodeID"));
        Location loc = locationDAO.get(data.getString("location"));
        Move thisMove = new Move(node, loc, date);

        if (!locationMoveHistory.containsKey(loc.getLongName())) {
          ArrayList<Move> moveArrayList = new ArrayList<>();
          moveArrayList.add(thisMove);
          locationMoveHistory.put(loc.getLongName(), moveArrayList);
        } else {
          locationMoveHistory.get(loc.getLongName()).add(thisMove);
        }
        if (!locationsAtNodeID.containsKey(node.getNodeID())) {
          ArrayList<Move> moveArrayList = new ArrayList<>();
          moveArrayList.add(thisMove);
          locationsAtNodeID.put(node.getNodeID(), moveArrayList);
        } else {
          locationsAtNodeID.get(node.getNodeID()).add(thisMove);
        }
        listOfMoves.add(thisMove);
      }
      for (List<Move> list : locationsAtNodeID.values()) {
        list.sort(new DateComparator());
      }
      System.out.println("Successfully loaded from the moves remote");
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

          //          Move thisMove = new Move(Integer.parseInt(fields[0]), fields[1], date);
          PreparedStatement stmt =
              connection
                  .getConnection()
                  .prepareStatement(
                      "INSERT INTO " + name + " (nodeID, location, date) VALUES (?,?,?)");
          stmt.setInt(1, Integer.parseInt(fields[0]));
          stmt.setString(2, fields[1]);
          stmt.setDate(3, java.sql.Date.valueOf(date));
          stmt.executeUpdate();
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

  public List<Move> constructForGivenDate(LocalDate moveDate) {
    ArrayList<Move> datedMoves = new ArrayList<>();
    NodeDAOImpl nodeDAO = DataBaseRepository.getInstance().nodeDAO;
    LocationDAOImpl locationDAO = DataBaseRepository.getInstance().locationDAO;
    try {

      String query =
          "SELECT nodeid, location, max(date) FROM hospitaldb.moves "
              + "where date <= ? "
              + "group by location, nodeid";
      PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
      preparedStatement.setDate(1, Date.valueOf(moveDate));
      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        LocalDate date = rs.getDate("max").toLocalDate();
        Node node = nodeDAO.get(rs.getInt("nodeID"));
        Location loc = locationDAO.get(rs.getString("location"));
        Move thisMove = new Move(node, loc, date);

        datedMoves.add(thisMove);
      }

      System.out.println(datedMoves);
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println("Error accessing the remote and constructing the list of moves");
    }
    return datedMoves;
  }

  public ArrayList<futureMoves> getFutureMoves() {
    ArrayList<futureMoves> futureMoves = new ArrayList<>();
    NodeDAOImpl nodeDAO = DataBaseRepository.getInstance().nodeDAO;
    LocationDAOImpl locationDAO = DataBaseRepository.getInstance().locationDAO;
    try {

      String query =
          "select  * "
              + "from hospitaldb.moves natural join hospitaldb.nodes natural join hospitaldb.locations "
              + "where date <= current_date";

      PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query);
      ResultSet rs = preparedStatement.executeQuery();
      //      ResultSetMetaData rsdata = rs.getMetaData();
      //      System.out.println(rsdata.getColumnName(5));

      while (rs.next()) {
        LocalDate date = rs.getDate("date").toLocalDate();
        int node = rs.getInt("nodeid");
        String loc = rs.getString("location");
        NodeType nodeType = NodeType.values()[rs.getInt("nodetype")];
        int xCoord = rs.getInt("xCoord");
        int yCoord = rs.getInt("yCoord");
        String floor = String.valueOf(Floor.values()[rs.getInt("Floor")]);

        futureMoves thisMove = new futureMoves(node, loc, date, nodeType, xCoord, yCoord, floor);

        futureMoves.add(thisMove);
      }

      System.out.println(futureMoves);
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println("Could not create a view");
    }
    return futureMoves;
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

  private class DateComparator implements Comparator<Move> {
    public int compare(Move o1, Move o2) {
      return o1.getDate().compareTo(o2.getDate());
    }
  }

  public class futureMoves {
    int nodeId;
    String locName;
    LocalDate moveDate;
    NodeType nodeType;
    int xcoord;
    int ycoord;
    String floor;

    public futureMoves(
        int nodeId,
        String locName,
        LocalDate moveDate,
        NodeType nodeType,
        int xcoord,
        int ycoord,
        String floor) {
      this.nodeId = nodeId;
      this.locName = locName;
      this.moveDate = moveDate;
      this.nodeType = nodeType;
      this.xcoord = xcoord;
      this.ycoord = ycoord;
      this.floor = floor;
    }
  }
}
