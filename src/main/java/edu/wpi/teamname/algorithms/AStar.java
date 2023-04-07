package edu.wpi.teamname.algorithms;

import edu.wpi.teamname.Database.Map.*;
import java.util.*;

public class AStar {

  private NodeDaoImpl nodeDao;
  private EdgeDaoImpl edgeDao;
  private LocationDoaImpl locationDoa;
  private MoveDaoImpl moveDao;

  public AStar() {
    nodeDao = NodeDaoImpl.getInstance();
    edgeDao = EdgeDaoImpl.getInstance();
    locationDoa = LocationDoaImpl.getInstance();
    moveDao = MoveDaoImpl.getInstance();
  }

  /**
   * A* Using strings that represents either the long name of the nodes or the node ids
   *
   * @param s
   * @param e
   * @return
   */
  public ArrayList<Integer> findPath(String s, String e) {
    Node start, end;
    if (s.replaceAll("[a-zA-Z]+/g", "").isEmpty()) start = nodeDao.getNode(Integer.parseInt(s));
    else start = nodeDao.getNode(moveDao.getMoves().get(s).getNodeID());
    if (e.replaceAll("[a-zA-Z]+/g", "").isEmpty()) end = nodeDao.getNode(Integer.parseInt(e));
    else end = nodeDao.getNode(moveDao.getMoves().get(e).getNodeID());

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
      for (int nodeToSearchID : edgeDao.getNeighbors().get(currentNode.node.getNodeID())) {
        //        System.out.print(nodeToSearchID + "\t");
        Node nodeToSearch = nodeDao.getNodes().get(nodeToSearchID);
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
