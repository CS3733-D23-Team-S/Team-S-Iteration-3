package edu.wpi.teamname;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.LocationDAOImpl;
import edu.wpi.teamname.DAOs.MoveDAOImpl;
import edu.wpi.teamname.DAOs.dbConnection;
import edu.wpi.teamname.DAOs.orms.Move;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {
  static ConnectionThread thread;

  public static void main(String[] args) throws Exception {
    DataBaseRepository database = DataBaseRepository.getInstance();
    database.load();
    MoveDAOImpl moveDAO = database.getMoveDAO();
    LocationDAOImpl locationDAO = database.getLocationDAO();
    //    thread = new ConnectionThread();
    //    thread.start();
    ArrayList<Move> moves =
        new ArrayList<>(moveDAO.constructForGivenDate(LocalDate.of(2023, 4, 27)));

    ArrayList<Move> moves1 =
        new ArrayList<>(moveDAO.constructForGivenDate(LocalDate.of(2023, 4, 25)));

    for (Move thisMove : moves) {
      if (thisMove.getLocationName().equals("Cafe")) System.out.println(thisMove);
    }
    for (Move thisMove : moves1) {
      if (thisMove.getLocationName().equals("Cafe")) System.out.println(thisMove);
    }

    //    moveDAO.getFutureMoves();
    thread = new ConnectionThread();
    thread.start();
    App.launch(App.class, args);
    dbConnection.getInstance().getConnection().close();
    System.out.println("Loaded everything");
    dbConnection.getInstance().getConnection().close();
  }
}
