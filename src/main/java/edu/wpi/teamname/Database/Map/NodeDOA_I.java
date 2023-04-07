package edu.wpi.teamname.Database.Map;

import java.util.List;

public interface NodeDOA_I {
  List<Node> getAllNodes();

  void initTable(String name);

  Node getNode(int nodeID);

  void updateNode(Node node);

  void deleteNode(Node node);

  void loadToRemote(String pathToCSV);

  void addNode(Node thisNode);
}
