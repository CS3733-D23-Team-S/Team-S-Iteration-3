package edu.wpi.teamname;

import edu.wpi.teamname.Database.LoaderDAO;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {
    LoaderDAO loader = LoaderDAO.getInstance();
    //    loader.establishConnection();
    //    loader.resetData();
    loader.load();
    App.launch(App.class, args);
    // Debugging stuff in order to check everything looks about right
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
