package edu.wpi.teamname.pathfinding;

import edu.wpi.teamname.DAOs.DataBaseRepository;

public class MapEditorEntity {

  DataBaseRepository dataBase;

  public MapEditorEntity() {
    dataBase = DataBaseRepository.getInstance();
  }
}
