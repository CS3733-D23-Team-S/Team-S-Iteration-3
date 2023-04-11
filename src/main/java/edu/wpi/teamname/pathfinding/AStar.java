package edu.wpi.teamname.pathfinding;

import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.EdgeDAOImpl;
import edu.wpi.teamname.databaseredo.MoveDAOImpl;
import edu.wpi.teamname.databaseredo.NodeDAOImpl;
import edu.wpi.teamname.databaseredo.orms.Node;
import java.util.*;

public class AStar {

  NodeDAOImpl nodeDAO;
  EdgeDAOImpl edgeDAO;
  MoveDAOImpl moveDAO;

  public AStar() {
    nodeDAO = DataBaseRepository.getInstance().getNodeDAO();
    edgeDAO = DataBaseRepository.getInstance().getEdgeDAO();
    moveDAO = DataBaseRepository.getInstance().getMoveDAO();
  }

  /**
   * A* Using strings that represents either the long name of the nodes or the node ids
   *
   * @param s
   * @param e
   * @return
   */
  public ArrayList<Integer> findPath(int s, int e) {
    Node start, end;
    // try and catch shit
    //        if (s.replaceAll("[a-zA-Z]+/g", "").isEmpty()) start =
    //     nodeDAO.getNodes().get(Integer.parseInt(s));
    //        else start = nodeDAO.getNodes().get(moveDAO.getListOfMoves().get(e).getNodeID());
    //        if (e.replaceAll("[a-zA-Z]+/g", "").isEmpty()) end =
    // nodeDAO.getNodes().get(Integer.parseInt(e));
    //        else end = nodeDAO.getNodes().get(moveDao.getMoves().get(e).getNodeID());
    start = nodeDAO.getNodes().get(s);
    end = nodeDAO.getNodes().get(e);
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
      for (int nodeToSearchID : edgeDAO.getNeighbors().get(currentNode.node.getNodeID())) {
        //        System.out.print(nodeToSearchID + "\t");
        Node nodeToSearch = nodeDAO.getNodes().get(nodeToSearchID);
        if (!visitedNodes.contains(nodeToSearch)) {
          double weight = calculateWeight(nodeToSearch, end);
          nodesYetToSearch.add(new HeuristicNode(nodeToSearch, weight));
          gotHereFrom.put(nodeToSearch, currentNode.node);
        }
      }
      visitedNodes.add(currentNode.node);
    }
    return null;
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

  //
  // pathTaken.put(currentNode.getNodeID(),moveDao.getNodeToLoc().get(currentNode.getNodeID()).get(1));
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
