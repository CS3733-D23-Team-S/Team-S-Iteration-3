package edu.wpi.teamname.pathfinding;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.EdgeDAOImpl;
import edu.wpi.teamname.DAOs.MoveDAOImpl;
import edu.wpi.teamname.DAOs.NodeDAOImpl;
import edu.wpi.teamname.DAOs.orms.Node;
import java.util.*;

public class BFS implements IPathFinder {

  NodeDAOImpl nodeDAO;

  EdgeDAOImpl edgeDAO;

  MoveDAOImpl moveDAO;

  public BFS() {
    this.nodeDAO = DataBaseRepository.getInstance().getNodeDAO();
    this.edgeDAO = DataBaseRepository.getInstance().getEdgeDAO();
    this.moveDAO = DataBaseRepository.getInstance().getMoveDAO();
  }

  @Override
  public ArrayList<Integer> findPath(int s, int e) {
    System.out.println("Running BFS");
    Node start, end;
    start = this.nodeDAO.getNodes().get(s);
    end = this.nodeDAO.getNodes().get(e);

    Queue<Node> queue = new LinkedList<>();
    HashSet<Node> visitedNodes = new HashSet<>();
    Map<Node, Node> path = new HashMap<>();
    Node currentNeighbor = null;
    path.put(start, null);

    Node currentNode = null;

    queue.offer(start);

    while (!queue.isEmpty()) {
      currentNode = queue.poll();
      visitedNodes.add(currentNode);
      for (int nodeID : this.edgeDAO.getNeighbors().get(currentNode.getNodeID())) {
        currentNeighbor = this.nodeDAO.getNodes().get(nodeID);
        if (!visitedNodes.contains(currentNeighbor)) {
          queue.offer(currentNeighbor);
          path.put(currentNeighbor, currentNode);
        }
        if (currentNode == end) {
          System.out.println(currentNeighbor.toString());
          return constructShortestPath(currentNode, path);
        }
      }
    }
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
    // for (int curr : pathTaken) System.out.println(curr);
    return pathTaken;
  }
}
