package edu.wpi.teamname.Database;

import edu.wpi.teamname.Map.*;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import edu.wpi.teamname.User.UserDAOImpl;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class LoaderDAO implements LoaderDAOI {

  static LoaderDAO single_instance = null;
  final String schemaName = "hospitaldb";
  final String nodeTable = schemaName + "." + "nodes";
  final String edgesTable = schemaName + "." + "edges";
  final String locationTable = schemaName + "." + "locations";
  final String moveTable = schemaName + "." + "moves";
  final String roomrequestsTable = schemaName + "." + "roomReservations";

  final String loginTable = schemaName + "." + "login";
  final String defaultNodePath = "src/main/java/edu/wpi/teamname/defaultCSV/Node.csv";
  final String defaultEdgePath = "src/main/java/edu/wpi/teamname/defaultCSV/Edge.csv";
  final String defaultLocationPath = "src/main/java/edu/wpi/teamname/defaultCSV/LocationName.csv";
  final String defaultMovePath = "src/main/java/edu/wpi/teamname/defaultCSV/Move.csv";
  dbConnection connection;

  private LoaderDAO() {}

  public static LoaderDAO getInstance() {
    if (single_instance == null) single_instance = new LoaderDAO();
    return single_instance;
  }

  @Override
  public void load() throws SQLException {
    establishConnection();
    initTables();
    loadDefaultDataIfEmpty();
  }

  public void establishConnection() {
    connection = dbConnection.getInstance();
  }

  private void initTables() {
    String createSchema = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
    try {
      Statement stmt = connection.getConnection().createStatement();
      stmt.execute(createSchema);
      System.out.println("Created the schema");
      NodeDaoImpl.getInstance().initTable(nodeTable);
      EdgeDaoImpl.getInstance().initTable(edgesTable);
      LocationDoaImpl.getInstance().initTable(locationTable);
      MoveDaoImpl.getInstance().initTable(moveTable);
      UserDAOImpl.getInstance().initTables(loginTable);
      RoomRequestDAO.getInstance().initTable();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("Database update/creation error");
    }
  }

  private void loadDefaultDataIfEmpty() {
    NodeDaoImpl.getInstance().loadToRemote(defaultNodePath);
    EdgeDaoImpl.getInstance().loadToRemote(defaultEdgePath);
    LocationDoaImpl.getInstance().loadToRemote(defaultLocationPath);
    MoveDaoImpl.getInstance().loadToRemote(defaultMovePath);
  }

  @Override
  public void resetData() {
    String resetCommand = "DROP SCHEMA IF EXISTS " + schemaName + " CASCADE";
    try {
      Statement stmt = connection.getConnection().createStatement();
      stmt.executeUpdate(resetCommand);
      System.out.println("Deleted the database");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      System.out.println(e.getSQLState());
      System.out.println("Could not reset the database");
    }
  }

  // Add more imports as needed
  @Override
  public void loadCSV(String importPath, char dataType) {
    try {
      PreparedStatement stmt =
          connection.getConnection().prepareStatement("DROP TABLE IF EXISTS ? CASCADE");
      switch (dataType) {
        case 'n':
          stmt.setString(1, NodeDaoImpl.getInstance().getName());
          stmt.executeUpdate();
          NodeDaoImpl.getInstance().loadToRemote(importPath);
          break;
        case 'e':
          stmt.setString(1, EdgeDaoImpl.getInstance().getName());
          stmt.executeUpdate();
          EdgeDaoImpl.getInstance().loadToRemote(importPath);
          break;
        case 'l':
          stmt.setString(1, LocationDoaImpl.getInstance().getName());
          stmt.executeUpdate();
          LocationDoaImpl.getInstance().loadToRemote(importPath);
          break;
        case 'm':
          stmt.setString(1, LocationDoaImpl.getInstance().getName());
          stmt.executeUpdate();
          MoveDaoImpl.getInstance().loadToRemote(importPath);
          break;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void exportCSVs(String exportPath) {
    handleExport(exportPath, 'n');
    handleExport(exportPath, 'e');
    handleExport(exportPath, 'l');
    handleExport(exportPath, 'm');
  }

  // Add more exports as needed
  private void handleExport(String exportPath, char dataType) {
    BufferedWriter fileWriter;
    try {
      switch (dataType) {
        case 'n':
          exportPath += "node.csv";
          fileWriter = new BufferedWriter(new FileWriter(exportPath));
          fileWriter.write("nodeID,xcoord,ycoord,floor,building");
          for (Node node : NodeDaoImpl.getInstance().getNodes().values()) {
            fileWriter.newLine();
            fileWriter.write(node.toCSVString());
          }
          break;
        case 'e':
          exportPath += "edge.csv";
          fileWriter = new BufferedWriter(new FileWriter(exportPath));
          fileWriter.write("startNode,endNode");
          for (Edge edge : EdgeDaoImpl.getInstance().getAllEdges()) {
            fileWriter.newLine();
            fileWriter.write(edge.toCSVString());
          }
          break;
        case 'l':
          exportPath += "location.csv";
          fileWriter = new BufferedWriter(new FileWriter(exportPath));
          fileWriter.write("longName,shortName,nodeType");
          for (Location location : LocationDoaImpl.getInstance().getAllLocations()) {
            fileWriter.newLine();
            fileWriter.write(location.toCSVString());
          }
          break;
        case 'm':
          exportPath += "move.csv";
          fileWriter = new BufferedWriter(new FileWriter(exportPath));
          fileWriter.write("nodeID,longName,date");
          for (Move move : MoveDaoImpl.getInstance().getAllMoves()) {
            fileWriter.newLine();
            fileWriter.write(move.toCSVString());
          }
          break;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
