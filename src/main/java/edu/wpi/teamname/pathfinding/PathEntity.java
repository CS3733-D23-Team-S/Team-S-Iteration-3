package edu.wpi.teamname.pathfinding;

import lombok.Getter;
import lombok.Setter;

public class PathEntity {

  @Getter @Setter int nodePassed;

  public PathEntity(int nodePassed) {
    this.nodePassed = nodePassed;
  }
}
