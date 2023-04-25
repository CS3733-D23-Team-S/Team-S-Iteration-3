package edu.wpi.teamname;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.dbConnection;

public class Main {
//  static ConnectionThread thread;

  public static void main(String[] args) throws Exception {
    DataBaseRepository database = DataBaseRepository.getInstance();
    database.load();
    //    MoveDAOImpl moveDAO = database.getMoveDAO();
    //    //    thread = new ConnectionThread();
    //    //    thread.start();
    //    moveDAO.constructForGivenDate(LocalDate.now());
    //    moveDAO.getFutureMoves();
    thread = new ConnectionThread();
    thread.start();
    App.launch(App.class, args);
    // dbConnection.getInstance().getConnection().close();
    System.out.println("Loaded everything");
    dbConnection.getInstance().getConnection().close();
  }
}
