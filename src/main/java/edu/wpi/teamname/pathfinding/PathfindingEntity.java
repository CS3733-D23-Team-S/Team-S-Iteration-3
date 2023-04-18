package edu.wpi.teamname.pathfinding;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class PathfindingEntity {

  @Getter @Setter int startingLocation;
  @Getter @Setter int destination;
  @Getter @Setter String alg;
  @Getter @Setter AStar aStar;
  @Getter @Setter ArrayList<PathEntity> pathEntities;

  public PathfindingEntity(int startingLocation, int destination) {
    this.startingLocation = startingLocation;
    this.destination = destination;
    this.aStar = new AStar();
    this.pathEntities = new ArrayList<>();
  }

  // goes through the list of node IDs returned by the findPath algorithm from index 0 to the list
  // size
  // for each index, this.pathEntities adds a PathEntity with a value of 'astarlist.get(i)'
  // thus adds node IDs to this.pathEntities
  public void generatePath() {
    List<Integer> passedNodeIDs = new ArrayList<>();
    if (this.alg.equals("AStar")) {
      passedNodeIDs.addAll(this.aStar.findPath(this.startingLocation, this.destination));
    } else if (this.alg.equals("Breadth-first search")) {
      // breadth first search it
    } else if (this.alg.equals("Depth-first search")) {
      // depth first search it
    }
    for (int i = 0; i < passedNodeIDs.size(); i++) {
      this.pathEntities.add(new PathEntity(passedNodeIDs.get(i)));
    }
  }
}
