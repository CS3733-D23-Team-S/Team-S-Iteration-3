package edu.wpi.teamname.pathfinding;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.EdgeDAOImpl;
import edu.wpi.teamname.DAOs.MoveDAOImpl;
import edu.wpi.teamname.DAOs.NodeDAOImpl;
import edu.wpi.teamname.DAOs.orms.Move;
import edu.wpi.teamname.DAOs.orms.Node;
import edu.wpi.teamname.DAOs.orms.NodeType;
import java.util.*;

public class AStar implements IPathFinder {
  NodeDAOImpl nodeDAO;
  EdgeDAOImpl edgeDAO;
  MoveDAOImpl moveDAO;
  Node start;
  Node end;
  DataBaseRepository dbr = DataBaseRepository.getInstance();

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
    System.out.println("Running AStar");
    this.start = this.nodeDAO.getNodes().get(s);
    this.end = this.nodeDAO.getNodes().get(e);
    final PriorityQueue<HeuristicNode> nodesYetToSearch =
        new PriorityQueue<>(10, new HeuristicNode(null, Double.MAX_VALUE));
    final HashSet<Node> visitedNodes = new HashSet<>();
    final Map<Node, Node> gotHereFrom = new HashMap<>();

    HeuristicNode startHNode = new HeuristicNode(start, calculateWeight(start));
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
          double weight = calculateWeight(nodeToSearch);
          nodesYetToSearch.add(new HeuristicNode(nodeToSearch, weight));
          gotHereFrom.put(nodeToSearch, currentNode.node);
        }
      }
      visitedNodes.add(currentNode.node);
    }
    return new ArrayList<>();
  }

  /**
   * Calculate Weight: Finds the weight of the current path - in this case by taking the distance
   * from start to current node and adding ot to the distance from the current node to the ending
   * (destination) node
   *
   * @param currentNode
   * @return
   */
  private double calculateWeight(Node currentNode) {
    double distance =
        Math.sqrt(
                Math.pow((start.getXCoord() - currentNode.getXCoord()), 2)
                    + Math.pow((start.getYCoord() - currentNode.getYCoord()), 2))
            + Math.sqrt(
                Math.pow((currentNode.getXCoord() - end.getXCoord()), 2)
                    + Math.pow((currentNode.getYCoord() - end.getYCoord()), 2));

    for (Move move : dbr.getMoveDAO().getAll()) {
      if (currentNode == move.getNode()) {
        if (move.getLocation().getNodeType() == NodeType.ELEV
            || move.getLocation().getNodeType() == NodeType.STAI) {
          distance += 1000000000;
          return distance;
        } else {
          return distance;
        }
      }
    }
    return distance;
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
