package edu.wpi.teamname;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.dbConnection;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

  public static void main(String[] args) throws Exception {
    DataBaseRepository database = DataBaseRepository.getInstance();
    database.load();

    RoomRequestDAO roomRequestDAO = database.getRoomRequestDAO();
    ConfRoomRequest confRoomRequest =
        new ConfRoomRequest(
            LocalDate.of(2023, 4, 17),
            LocalTime.of(14, 45),
            LocalTime.of(15, 10),
            "BTM Conference Center",
            "staff",
            "nothing",
            "Yes",
            "staff",
            true);
    roomRequestDAO.add(confRoomRequest);

    System.out.println(
        roomRequestDAO.hasConflicts(
            "BTM Conference Center",
            LocalDate.of(2023, 4, 17),
            LocalTime.of(14, 45),
            LocalTime.of(15, 10)));

    // App.launch(App.class, args);
    // dbConnection.getInstance().getConnection().close();
    System.out.println("Loaded everything");
    dbConnection.getInstance().getConnection().close();
    //    for (int key : NodeDaoImpl.getInstance().getNodes().keySet())
    //      System.out.println(NodeDaoImpl.getInstance().getNodes().get(key).toString());
    //    for (String key : MoveDaoImpl.getInstance().getMoves().keySet())
    //      System.out.println(MoveDaoImpl.getInstance().getMoves().get(key).toString());

  }
}
