package edu.wpi.teamname.pathfinding;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

public class PathfindingEntity {

  @Getter @Setter String startingLocation;
  @Getter @Setter String destination;

  @Getter @Setter AStar aStar;
  @Getter @Setter ArrayList<PathEntity> pathEntities;

  public PathfindingEntity(String startingLocation, String destination) {
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
    for (int i = 0; i < this.aStar.findPath(this.startingLocation, this.destination).size(); i++) {
      this.pathEntities.add(
          new PathEntity(this.aStar.findPath(this.startingLocation, this.destination).get(i)));
      // this.pathEntities.add(new PathEntity(i));
    }
  }
}
