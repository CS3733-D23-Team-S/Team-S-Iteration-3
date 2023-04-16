package edu.wpi.teamname.pathfinding;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.EdgeDAOImpl;
import edu.wpi.teamname.DAOs.MoveDAOImpl;
import edu.wpi.teamname.DAOs.NodeDAOImpl;
import edu.wpi.teamname.DAOs.orms.Node;

import java.util.*;

public class DFS implements IPathFinder {

	NodeDAOImpl nodeDAO;
	EdgeDAOImpl edgeDAO;
	MoveDAOImpl moveDAO;

	public DFS() {
		this.nodeDAO = DataBaseRepository.getInstance().getNodeDAO();
		this.edgeDAO = DataBaseRepository.getInstance().getEdgeDAO();
		this.moveDAO = DataBaseRepository.getInstance().getMoveDAO();
	}

	@Override
	public ArrayList<Integer> findPath(int s, int e) {
		Node start, end;
		start = this.nodeDAO.get(s);
		end = this.nodeDAO.get(s);

		Queue<Node> queue = new LinkedList<Node>();

		HashSet<Node> visitedNodes = new HashSet<>();
		Map<Node, Node> path = new HashMap<>();
		Node currentNeighbor;
		queue.add(start);

		Node currentNode = null;
		do {
			currentNode = queue.poll();
			visitedNodes.add(currentNode);
			for (int nodeID : this.edgeDAO.getNeighbors().get(currentNode.getNodeID())) {
				currentNeighbor = this.nodeDAO.getNodes().get(nodeID);
				if (!visitedNodes.contains(currentNeighbor)) {
					queue.add(currentNeighbor);
					path.put(currentNeighbor, currentNode);
				}
			}
		} while (!queue.isEmpty());
		return constructShortestPath(currentNode, path);
	}


	private ArrayList<Integer> constructShortestPath(Node currentNode, Map<Node, Node> gotHereFrom) {
		final ArrayList<Integer> pathTaken = new ArrayList<>();
		while (gotHereFrom.get(currentNode) != null) {
			pathTaken.add(currentNode.getNodeID());
			currentNode = gotHereFrom.get(currentNode);
		}
		pathTaken.add(currentNode.getNodeID());
		Collections.reverse(pathTaken);
		return pathTaken;
	}


}
