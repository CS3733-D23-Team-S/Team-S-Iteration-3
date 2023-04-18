package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Edge;
import edu.wpi.teamname.DAOs.orms.Node;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;

public class EdgeDAOImpl implements IDAO<Edge, Edge> {

  @Getter private String name;
  @Getter private final String CSVheader = "startNode,endNode";
  private final dbConnection connection;
  List<Edge> edges = new ArrayList<>();
  @Getter HashMap<Integer, HashSet<Integer>> neighbors = new HashMap<>();

  public EdgeDAOImpl() {
    connection = dbConnection.getInstance();
  }

  @Override
  public void initTable(String name) {
    this.name = name;
    String edgeTable =
        "CREATE TABLE IF NOT EXISTS "
            + name
            + " (startNode int FOREIGN KEY, endNode int FOREIGN KEY)";
    try {
      Statement stmt = connection.getConnection().createStatement();
      stmt.execute(edgeTable);
      System.out.println("Created the edge table");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error with creating the edge table");
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
      e.printStackTrace();
    }
  }

  @Override
  public void importCSV(String path) {
    dropTable();
    edges.clear();
    neighbors.clear();
    initTable(name);
    loadRemote(path);
  }

  @Override
  public void exportCSV(String path) throws IOException {
    path += "Edge.csv";
    BufferedWriter fileWriter;
    fileWriter = new BufferedWriter(new FileWriter(path));
    fileWriter.write("startNode,endNode");
    for (Edge edge : edges) {
      fileWriter.newLine();
      fileWriter.write(edge.toCSVString());
    }
  }

  @Override
  public List<Edge> getAll() {
    return edges;
  }

  @Override
  public Edge get(Edge target) {
    return null;
  }

  /**
   * This is not finished since this is going to take a lot of manipulation
   *
   * @param target the edge to delete
   */
  @Override
  public void delete(Edge target) {

    edges.remove(target);
  }

  /**
   * This is not finished since this is going to take a lot of manipulation of edges
   *
   * @param addition the edge to add
   */
  @Override
  public void add(Edge addition) {
    edges.add(addition);
  }

  public void add(Node stN, Node enN) {
    Edge addition = new Edge(stN, enN);
    add(addition);
  }

  public void add(Node emptyEdge) {
    HashSet<Integer> neighs = new HashSet<>();
    neighbors.put(emptyEdge.getNodeID(), neighs);
  }

  private void constructFromRemote() {
    try {
      PreparedStatement getNeighbors =
          connection.getConnection().prepareStatement("SELECT * FROM " + name);
      ResultSet data = getNeighbors.executeQuery();

      while (data.next()) {
        int startNodeID = data.getInt("startNode");
        int endNodeID = data.getInt("endNode");
        if (!neighbors.containsKey(startNodeID)) {
          HashSet<Integer> edges = new HashSet<>();
          neighbors.put(startNodeID, edges);
        }
        if (!neighbors.containsKey(endNodeID)) {
          HashSet<Integer> edges = new HashSet<>();
          neighbors.put(endNodeID, edges);
        }
        neighbors.get(startNodeID).add(endNodeID);
        neighbors.get(endNodeID).add(startNodeID);
      }
      // Removes the current node's ID from its list of neighbors so that it doesn't cause a loop
      for (int currentID : neighbors.keySet()) {
        neighbors.get(currentID).remove(currentID);
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
          PreparedStatement stmt =
              connection
                  .getConnection()
                  .prepareStatement("INSERT INTO " + name + " (startNode, endNode) VALUES (?,?)");
          stmt.setInt(1, Integer.parseInt(fields[0]));
          stmt.setInt(2, Integer.parseInt(fields[1]));
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
