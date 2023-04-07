package edu.wpi.teamname.Database.Map;

import java.time.LocalDate;
import java.util.List;

public interface MoveDAO_I {
  List<Move> getAllMoves();

  void initTable(String name);

  boolean checkCanMove(String location, LocalDate date);

  Move getMove(String location);

  String processMoveRequest(int newLocNodeID, String location, LocalDate date) throws Exception;

  void loadToRemote(String pathToCSV);
}
