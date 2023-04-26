package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Floor;
import edu.wpi.teamname.DAOs.orms.Node;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

public class NodeDAOImpl implements IDAO<Node, Integer> {

	@Getter
	private String name;
	@Getter
	private final String CSVheader = "nodeID,xcoord,ycoord,floor,building";
	private final dbConnection connection;

	@Getter
	private final HashMap<Integer, Node> nodes = new HashMap<>();

	public NodeDAOImpl() {
		connection = dbConnection.getInstance();
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
			e.printStackTrace();
		}
	}

	@Override
	public void importCSV(String path) {
		dropTable();
		nodes.clear();
		initTable(name);
		loadRemote(path);
	}

	@Override
	public void exportCSV(String path) throws IOException {
		path += "Node.csv";
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path));
		fileWriter.write("nodeID,xcoord,ycoord,floor,building");
		for (Node node : nodes.values()) {
			fileWriter.newLine();
			fileWriter.write(node.toCSVString());
		}
		fileWriter.close();
	}

	@Override
	public List<Node> getAll() {
		return nodes.values().stream().toList();
	}

  public List<Integer> getAllNodeId() {
    List<Integer> listOfEligibleNodes = new ArrayList<>();
    for (int i = 0; i < getAll().size(); i++) {
      listOfEligibleNodes.add(getAll().get(i).getNodeID());
    }
    return listOfEligibleNodes;
  }
	@Override
	public Node get(Integer target) {
		return nodes.get(target);
	}

	@Override
	public void delete(Integer target) {
		nodes.remove(target);
		try {
			PreparedStatement stmt =
					connection.getConnection().prepareStatement("DELETE FROM " + name + " WHERE nodeID=?");
			stmt.setInt(1, target);
			stmt.execute();
			System.out.println("Node deleted from database");
			DataBaseRepository.getInstance().getMoveDAO().constructFromRemote();
			DataBaseRepository.getInstance().getEdgeDAO().constructFromRemote();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delete(Node node) {
		delete(node.getNodeID());
	}

	@Override
	public void add(Node addition) {
		if (nodes.containsKey(addition.getNodeID())) {
			nodes.put(addition.getNodeID(), addition);
			updateNodeLocation(addition);
		} else {
			nodes.put(addition.getNodeID(), addition);
			System.out.println("Node added to Local");
			addToRemote(addition);
		}
	}

	public void add(int nodeID, int xCoord, int yCoord, Floor floor, String building) {
		Node newNode = new Node(nodeID, xCoord, yCoord, floor, building);
		this.add(newNode);
	}

	public void updateNodeID(int oldID, int newID) {
		try {
			PreparedStatement statement =
					connection
							.getConnection()
							.prepareStatement("UPDATE " + name + " SET nodeID=? WHERE nodeID=?");
			statement.setInt(1, newID);
			statement.setInt(2, oldID);
			statement.executeUpdate();
			Node temp = nodes.get(oldID);
			nodes.remove(oldID);
			nodes.put(newID, temp);
			DataBaseRepository.getInstance().forceUpdate();
			System.out.println("successfully updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateNodeLocation(Node node) {
		try {
			PreparedStatement statement =
					connection
							.getConnection()
							.prepareStatement("UPDATE " + name + " SET xCoord=?, yCoord=? WHERE nodeID=?");
			statement.setInt(1, node.getXCoord());
			statement.setInt(2, node.getYCoord());
			statement.setInt(3, node.getNodeID());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void addToRemote(Node addition) {
		try {
			PreparedStatement stmt =
					connection
							.getConnection()
							.prepareStatement(
									"INSERT INTO "
									+ name
									+ " (nodeID, xCoord, yCoord, floor, building) VALUES (?,?,?,?,?)");
			stmt.setInt(1, addition.getNodeID());
			stmt.setInt(2, addition.getXCoord());
			stmt.setInt(3, addition.getYCoord());
			stmt.setInt(4, addition.getFloor().ordinal());
			stmt.setString(5, addition.getBuilding());
			stmt.executeUpdate();
			System.out.println("Node added to Database");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void constructFromRemote() {
		nodes.clear();
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
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
