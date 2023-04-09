package edu.wpi.teamname.databaseredo;

import edu.wpi.teamname.Database.dbConnection;
import edu.wpi.teamname.databaseredo.orms.Edge;
import lombok.Getter;
import lombok.Setter;

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

public class EdgeDAOImpl implements IDAO<Edge> {


	@Setter
	@Getter
	private String name;
	private dbConnection connection;
	List<Edge> edges = new ArrayList<>();
	@Getter
	HashMap<Integer, HashSet<Integer>> neighbors = new HashMap<>();

	public EdgeDAOImpl() {
		connection = dbConnection.getInstance();
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
	public void dropTable(String name) {

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
			e.getMessage();
			e.printStackTrace();
		}
	}

	@Override
	public void importCSV(String path) {

	}

	@Override
	public void exportCSV(String path) {

	}

	@Override
	public List<Edge> getAll() {
		return null;
	}

	@Override
	public void delete(Edge target) {

	}

	@Override
	public void add(Edge addition) {

	}


	private void constructFromRemote() {
		try {
			Statement stmt = connection.getConnection().createStatement();
			String getNodes = "SELECT DISTINCT nodeID FROM " + name;
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
									edgeList.getInt("startNode"),
									edgeList.getInt("endNode"));
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
									Integer.parseInt(fields[0]),
									Integer.parseInt(fields[1]));
					edges.add(thisEdge);
					PreparedStatement stmt =
							connection
									.getConnection()
									.prepareStatement("INSERT INTO " + name + " (startNode, endNode) VALUES (?,?)");
					stmt.setInt(1, thisEdge.getStartNodeID());
					stmt.setInt(2, thisEdge.getEndNodeID());
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