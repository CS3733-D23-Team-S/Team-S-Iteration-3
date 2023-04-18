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
    Node start, end;
    start = this.nodeDAO.getNodes().get(s);
    end = this.nodeDAO.getNodes().get(e);

    Stack<Node> stack = new Stack<>();
    HashSet<Node> visitedNodes = new HashSet<>();
    Map<Node, Node> path = new HashMap<>();
    Node currentNeighbor;
    path.put(start, null);

    Node currentNode = null;

    stack.push(start);

    while (!stack.isEmpty()) {
      currentNode = stack.pop();
      visitedNodes.add(currentNode);
      for (int nodeID : this.edgeDAO.getNeighbors().get(currentNode.getNodeID())) {
        currentNeighbor = this.nodeDAO.getNodes().get(nodeID);
        if (!visitedNodes.contains(currentNeighbor)) {
          stack.push(currentNeighbor);
          path.put(currentNeighbor, currentNode);
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
