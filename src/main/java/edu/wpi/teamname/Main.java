package edu.wpi.teamname;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.dbConnection;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {
    DataBaseRepository database = DataBaseRepository.getInstance();
    database.load();
    //    EdgeDAOImpl edgeDAO = database.getEdgeDAO();
    //    System.out.println(edgeDAO.getNeighbors());
    //    AStar astar = new AStar();
    //    astar.findPath(870, 850);
    //    for (int key : DataBaseRepository.getInstance().getEdgeDAO().getNeighbors().keySet()) {
    //      System.out.print(key);
    //      System.out.print("\t Neighbors:\t");
    //      System.out.println(
    //          DataBaseRepository.getInstance().getEdgeDAO().getNeighbors().get(key).toString());
    //    }

    App.launch(App.class, args);
    // dbConnection.getInstance().getConnection().close();
    System.out.println("Loaded everything");
    dbConnection.getInstance().getConnection().close();
    //    for (int key : NodeDaoImpl.getInstance().getNodes().keySet())
    //      System.out.println(NodeDaoImpl.getInstance().getNodes().get(key).toString());
    //    for (String key : MoveDaoImpl.getInstance().getMoves().keySet())
    //      System.out.println(MoveDaoImpl.getInstance().getMoves().get(key).toString());

  }
}
