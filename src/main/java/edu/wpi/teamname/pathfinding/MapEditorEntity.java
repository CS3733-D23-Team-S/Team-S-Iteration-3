package edu.wpi.teamname.pathfinding;

import edu.wpi.teamname.databaseredo.DataBaseRepository;

public class MapEditorEntity {

  DataBaseRepository dataBase;

  public MapEditorEntity() {
    dataBase = DataBaseRepository.getInstance();
  }
}
