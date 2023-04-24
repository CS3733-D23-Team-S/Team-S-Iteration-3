package edu.wpi.teamname;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.dbConnection;

public class Main {
  static ConnectionThread thread;

  public static void main(String[] args) throws Exception {
    DataBaseRepository database = DataBaseRepository.getInstance();
    database.load();
    //    FoodDAOImpl foodDAO = database.getFoodDAO();
    //    List<String> features = new ArrayList<>();
    //    features.add("vegetarian");
    //    features.add("glutenfree");
    //    System.out.println(foodDAO.queriedFoods(features));
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
