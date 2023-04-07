package edu.wpi.teamname;

import lombok.Getter;
import lombok.Setter;

public class PathEntity {

  @Getter @Setter int nodePassed;

  public PathEntity(int nodePassed) {
    this.nodePassed = nodePassed;
  }
}
