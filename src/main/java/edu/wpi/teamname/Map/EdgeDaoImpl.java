package edu.wpi.teamname.Map;

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
import java.util.HashSet;
import java.util.List;

import edu.wpi.teamname.databaseredo.orms.Edge;
import edu.wpi.teamname.databaseredo.orms.Node;
import lombok.Getter;

public class EdgeDaoImpl implements EdgeDoa_I {
  private static EdgeDaoImpl single_instance;
  @Getter private String name;
  dbConnection connection;
  NodeDaoImpl nodeDao = NodeDaoImpl.getInstance();
  List<Edge> edges = new ArrayList<>();
  @Getter HashMap<Integer, HashSet<Integer>> neighbors = new HashMap<>();

  private EdgeDaoImpl() {
    connection = dbConnection.getInstance();
  }

  public static synchronized EdgeDaoImpl getInstance() {
    if (single_instance == null) single_instance = new EdgeDaoImpl();

    return single_instance;
  }

  @Override
  public List<Edge> getAllEdges() {
    return edges;
  }

  @Override
  public void initTable(String name) {
    this.name = name;
    String edgeTable = "CREATE TABLE IF NOT EXISTS " + name + " (startNode int, endNode int)";
    try {
      Statement stmt = connection.getConnection().createStatement();
      stmt.execute(edgeTable);
      System.out.println("Created the edge table");
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
      System.out.println("Error with creating the edge table");
    }
  }

  @Override
  public Edge getEdge(Node startNode, Node endNode) {
    return null;
  }

  @Override
  public void deleteEdge(Node startNode, Node endNode) {}

  @Override
  public void loadToRemote(String pathToCSV) {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String checkTable = "SELECT * FROM " + name;
      ResultSet check = stmt.executeQuery(checkTable);
      if (check.next()) {
        System.out.println("Loading the edges from the server");
        constructFromRemote();
      } else {
        System.out.println("Loading the edges to the server");
        constructRemote(pathToCSV);
      }
    } catch (SQLException e) {
      e.getMessage();
      e.printStackTrace();
    }
  }

  private void constructFromRemote() {
    try {
      Statement stmt = connection.getConnection().createStatement();
      String getNodes = "SELECT nodeID FROM " + nodeDao.getName();
      PreparedStatement getNeighbors =
          connection
              .getConnection()
              .prepareStatement("SELECT * FROM " + name + " WHERE startNode=? OR endNode=?");
      try {
        ResultSet listOfNodes = stmt.executeQuery(getNodes);
        while (listOfNodes.next()) {
          int currentNodeID = listOfNodes.getInt("nodeID");
          getNeighbors.setInt(1, currentNodeID);
          getNeighbors.setInt(2, currentNodeID);
          ResultSet data = getNeighbors.executeQuery();
          HashSet<Integer> neighbors = new HashSet<>();
          while (data.next()) {
            neighbors.add(data.getInt("endNode"));
            neighbors.add(data.getInt("startNode"));
          }
          neighbors.remove(currentNodeID);
          this.neighbors.put(currentNodeID, neighbors);
        }
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println(e.getSQLState());
        System.out.println("Error accessing the remote and constructing the list of edges");
      }
      try {
        PreparedStatement getEdges =
            connection.getConnection().prepareStatement("SELECT * FROM " + name);
        ResultSet edgeList = getEdges.executeQuery();
        while (edgeList.next()) {
          Edge edge =
              new Edge(
                  nodeDao.getNode(edgeList.getInt("startNode")),
                  nodeDao.getNode(edgeList.getInt("endNode")));
          edges.add(edge);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e.getSQLState());
      System.out.println("Error accessing the remote and constructing the list of edges");
    }
  }

  private void constructRemote(String csvFilePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
      try {
        reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
          String[] fields = line.split(",");
          Edge thisEdge =
              new Edge(
                  nodeDao.getNodes().get(Integer.parseInt(fields[0])),
                  nodeDao.getNodes().get(Integer.parseInt(fields[1])));
          edges.add(thisEdge);
          PreparedStatement stmt =
              connection
                  .getConnection()
                  .prepareStatement("INSERT INTO " + name + " (startNode, endNode) VALUES (?,?)");
          stmt.setInt(1, thisEdge.getStartNode().getNodeID());
          stmt.setInt(2, thisEdge.getEndNode().getNodeID());
          stmt.execute();
        }
        System.out.println("Loaded to the remote");
        constructFromRemote();
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println(e.getSQLState());
        System.out.println("Error accessing the remote and constructing the list of edges");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
