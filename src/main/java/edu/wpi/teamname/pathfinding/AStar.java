package edu.wpi.teamname.pathfinding;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.EdgeDAOImpl;
import edu.wpi.teamname.DAOs.MoveDAOImpl;
import edu.wpi.teamname.DAOs.NodeDAOImpl;
import edu.wpi.teamname.DAOs.orms.Node;
import java.util.*;

public class AStar implements IPathFinder {
  NodeDAOImpl nodeDAO;
  EdgeDAOImpl edgeDAO;
  MoveDAOImpl moveDAO;

  public AStar() {
    this.nodeDAO = DataBaseRepository.getInstance().getNodeDAO();
    this.edgeDAO = DataBaseRepository.getInstance().getEdgeDAO();
    this.moveDAO = DataBaseRepository.getInstance().getMoveDAO();
  }

  /**
   * A* Using strings that represents either the long name of the nodes or the node ids
   *
   * @param s
   * @param e
   * @return
   */
  @Override
  public ArrayList<Integer> findPath(int s, int e) {
    Node start, end;
    start = this.nodeDAO.getNodes().get(s);
    end = this.nodeDAO.getNodes().get(e);
    final PriorityQueue<HeuristicNode> nodesYetToSearch =
        new PriorityQueue<>(10, new HeuristicNode(null, Double.MAX_VALUE));
    final HashSet<Node> visitedNodes = new HashSet<>();
    final Map<Node, Node> gotHereFrom = new HashMap<>();

    HeuristicNode startHNode = new HeuristicNode(start, calculateWeight(start, end));
    //    System.out.println(startHNode.node + "\t" + startHNode.weight);
    nodesYetToSearch.add(startHNode);
    HeuristicNode currentNode;

    while (nodesYetToSearch.size() != 0) {
      currentNode = nodesYetToSearch.poll();
      if (currentNode.node.getNodeID() == (end.getNodeID())) {
        return constructShortestPath(currentNode.node, gotHereFrom);
      }
      //      dbManager.printLocalDatabases();
      //      System.out.print(currentNode.node.getNodeID());
      for (int nodeToSearchID : this.edgeDAO.getNeighbors().get(currentNode.node.getNodeID())) {
        //        System.out.print(nodeToSearchID + "\t");
        Node nodeToSearch = this.nodeDAO.getNodes().get(nodeToSearchID);
        if (!visitedNodes.contains(nodeToSearch)) {
          double weight = calculateWeight(nodeToSearch, end);
          nodesYetToSearch.add(new HeuristicNode(nodeToSearch, weight));
          gotHereFrom.put(nodeToSearch, currentNode.node);
        }
      }
      visitedNodes.add(currentNode.node);
    }
    return new ArrayList<>();
  }

  private double calculateWeight(Node start, Node target) {
    return Math.sqrt(
        Math.pow((start.getXCoord() - target.getXCoord()), 2)
            + Math.pow((start.getYCoord() - target.getYCoord()), 2));
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

  static class HeuristicNode implements Comparator<HeuristicNode> {
    Node node;
    double weight;

    public HeuristicNode(Node node, double weight) {
      this.node = node;
      this.weight = weight;
    }

    @Override
    public int compare(HeuristicNode o1, HeuristicNode o2) {
      return Double.compare(o1.weight, o2.weight);
    }
  }
}
