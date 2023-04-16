package edu.wpi.teamname.pathfinding;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class PathfindingEntity {

  @Getter @Setter String startingLocation;
  @Getter @Setter String destination;

  @Setter IPathFinder pathFinder;
  @Getter @Setter ArrayList<PathEntity> pathEntities;

  public PathfindingEntity(String startingLocation, String destination, IPathFinder pathFinder) {
    this.startingLocation = startingLocation;
    this.destination = destination;
    this.pathFinder = pathFinder;
    this.pathEntities = new ArrayList<>();
  }

  // goes through the list of node IDs returned by the findPath algorithm from index 0 to the list
  // size
  // for each index, this.pathEntities adds a PathEntity with a value of 'astarlist.get(i)'
  // thus adds node IDs to this.pathEntities
  public void generatePath() {
    List<Integer> passedNodeIDs = new ArrayList<>(this.pathFinder.findPath(
            Integer.parseInt(this.startingLocation), Integer.parseInt(this.destination)));
    /*
    for (int i = 0;
        i
            < this.aStar
                .findPath(
                    Integer.parseInt(this.startingLocation), Integer.parseInt(this.destination))
                .size();
        i++) {
      this.pathEntities.add(
          new PathEntity(
              this.aStar
                  .findPath(
                      Integer.parseInt(this.startingLocation), Integer.parseInt(this.destination))
                  .get(i)));
      // this.pathEntities.add(new PathEntity(i));
      */
    for (Integer passedNodeID : passedNodeIDs) {
      this.pathEntities.add(new PathEntity(passedNodeID));
    }
  }
}
