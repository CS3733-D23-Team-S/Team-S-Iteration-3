package edu.wpi.teamname.Database.Map;

import edu.wpi.teamname.Database.dbConnection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

public class NodeDaoImpl implements NodeDOA_I {
  private static NodeDaoImpl single_instance;
  @Getter private String name;
  dbConnection connection;

  @Getter private HashMap<Integer, Node> nodes = new HashMap<>();

  private NodeDaoImpl() {
    connection = dbConnection.getInstance();
  }

  public static NodeDaoImpl getInstance() {
    if (single_instance == null) single_instance = new NodeDaoImpl();
    return single_instance;
  }

  @Override
  public List<Node> getAllNodes() {
    return new ArrayList<>(this.nodes.values());
  }

  @Override
  public void initTable(String name) {
    this.name = name;
    String nodeTable =
        "CREATE TABLE IF NOT EXISTS "
            + name
            + " (nodeID int UNIQUE PRIMARY KEY,"
            + "xcoord int,"
            + "ycoord int,"
            + "floor int,"
            + "building varchar(100))";
    try {
      Statement stmt = connection.getConnection().createStatement();
      stmt.execute(nodeTable);
      System.out.println("Created the node table");
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
      System.out.println("Error with creating the node table");
    }
  }

  @Override
  public Node getNode(int nodeID) {
    return nodes.get(nodeID);
  }

  @Override
  public void updateNode(Node node) {
    nodes.put(node.getNodeID(), node);
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement(
                  "UPDATE "
                      + name
                      + " SET xCoord = ?,"
                      + "yCoord = ?,"
                      + "floor = ?,"
                      + "building = ? WHERE nodeID = ?");
      stmt.setInt(1, node.getXCoord());
      stmt.setInt(2, node.getYCoord());
      stmt.setInt(3, node.getFloor().ordinal());
      stmt.setString(4, node.getBuilding());
      stmt.setInt(5, node.getNodeID());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
  }

  @Override
  public void deleteNode(Node node) {
    nodes.remove(node.getNodeID());
  }

  @Override
  public void loadToRemote(String pathToCSV) {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String checkTable = "SELECT * FROM " + name;
      ResultSet check = stmt.executeQuery(checkTable);
      if (check.next()) {
        System.out.println("Loading the nodes from the server");
        constructFromRemote();
      } else {
        System.out.println("Loading the nodes to the server");
        constructRemote(pathToCSV);
      }
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
  }

  @Override
  public void addNode(Node thisNode) {
    try {
      PreparedStatement stmt =
          connection
              .getConnection()
              .prepareStatement(
                  "INSERT INTO "
                      + name
                      + " (nodeID, xCoord, yCoord, floor, building) VALUES (?,?,?,?,?)");
      stmt.setInt(1, thisNode.getNodeID());
      stmt.setInt(2, thisNode.getXCoord());
      stmt.setInt(3, thisNode.getYCoord());
      stmt.setInt(4, thisNode.getFloor().ordinal());
      stmt.setString(5, thisNode.getBuilding());
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
  }

  private void constructFromRemote() {
    if (!nodes.isEmpty()) {
      System.out.println("There is already stuff in the orm database");
      return;
    }
    try {
      Statement stmt = connection.getConnection().createStatement();
      String listOfNodes = "SELECT * FROM " + name;
      ResultSet data = stmt.executeQuery(listOfNodes);
      while (data.next()) {
        int nodeID = data.getInt("nodeID");
        int xCoord = data.getInt("xCoord");
        int yCoord = data.getInt("yCoord");
        Floor floor = Floor.values()[data.getInt("Floor")];
        String building = data.getString("Building");
        Node floorNode = new Node(nodeID, xCoord, yCoord, floor, building);
        nodes.put(nodeID, floorNode);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println("Error accessing the remote and constructing the list of nodes");
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
                        + " (nodeID, xCoord, yCoord, floor, building) VALUES (? , ? , ? , ? , ?)");
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
          String[] fields = line.split(",");
          //          System.out.println(Arrays.toString(fields));
          stmt.setInt(1, Integer.parseInt(fields[0]));
          stmt.setInt(2, Integer.parseInt(fields[1]));
          stmt.setInt(3, Integer.parseInt(fields[2]));
          stmt.setInt(4, Floor.valueOf("Floor" + fields[3].trim()).ordinal());
          stmt.setString(5, fields[4]);
          stmt.executeUpdate();
          Node thisNode =
              new Node(
                  Integer.parseInt(fields[0]),
                  Integer.parseInt(fields[1]),
                  Integer.parseInt(fields[2]),
                  Floor.valueOf("Floor" + fields[3].trim()),
                  fields[4]);
          nodes.put(Integer.parseInt(fields[0]), thisNode);
        }
      } catch (SQLException e) {
        e.getMessage();
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
