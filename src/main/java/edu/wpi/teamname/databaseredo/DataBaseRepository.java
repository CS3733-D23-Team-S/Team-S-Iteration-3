package edu.wpi.teamname.databaseredo;

import edu.wpi.teamname.Database.dbConnection;
import edu.wpi.teamname.databaseredo.orms.Edge;
import edu.wpi.teamname.databaseredo.orms.Location;
import edu.wpi.teamname.databaseredo.orms.Move;
import edu.wpi.teamname.databaseredo.orms.Node;
import edu.wpi.teamname.pathfinding.AStar;

import javax.xml.crypto.Data;

public class DataBaseRepository {

	private static DataBaseRepository single_instance = null;
	private dbConnection connection;
	AStar pathFinder = new AStar();
	IDAO<Node> nodeDAO;
	IDAO<Move> moveDAO;
	IDAO<Location> locationDAO;
	IDAO<Edge> edgeDAO;


	private DataBaseRepository() {
		connection = dbConnection.getInstance();
		nodeDAO = new NodeDAOImpl();
		moveDAO = new MoveDAOImpl();
		locationDAO = new LocationDAOImpl();
		edgeDAO = new EdgeDAOImpl();
	}

	public static synchronized DataBaseRepository getInstance() {
		if (single_instance == null) single_instance = new DataBaseRepository();
		return single_instance;
	}





}
