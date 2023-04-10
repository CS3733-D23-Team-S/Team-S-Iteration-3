package edu.wpi.teamname;

import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.dbConnection;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {
    DataBaseRepository database = DataBaseRepository.getInstance();
    database.load();
    App.launch(App.class, args);
    dbConnection.getInstance().getConnection().close();
    System.out.println("Loaded everything");
  }
}
