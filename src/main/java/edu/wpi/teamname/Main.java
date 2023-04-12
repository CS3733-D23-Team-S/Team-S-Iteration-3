package edu.wpi.teamname;

import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.dbConnection;
import edu.wpi.teamname.databaseredo.orms.*;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {
    DataBaseRepository database = DataBaseRepository.getInstance();
    database.load();
    App.launch(App.class, args);
    dbConnection.getInstance().getConnection().close();
    System.out.println("Loaded everything");
    //    for (int key : NodeDaoImpl.getInstance().getNodes().keySet())
    //      System.out.println(NodeDaoImpl.getInstance().getNodes().get(key).toString());
    //    for (String key : MoveDaoImpl.getInstance().getMoves().keySet())
    //      System.out.println(MoveDaoImpl.getInstance().getMoves().get(key).toString());
    //    for (int key : EdgeDaoImpl.getInstance().getNeighbors().keySet()) {
    //      System.out.print(key);
    //      System.out.print("\t Neighbors:\t");
    //      System.out.println(EdgeDaoImpl.getInstance().getNeighbors().get(key).toString());
    //    }

  }
}
