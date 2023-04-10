package edu.wpi.teamname;

import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import edu.wpi.teamname.databaseredo.DataBaseRepository;
import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

  public static void main(String[] args) throws Exception {
    DataBaseRepository database = DataBaseRepository.getInstance();
    database.load();

    LocalDate startDate = LocalDate.of(2022, 5, 13);
    LocalTime startTime = LocalTime.of(14, 50);
    LocalTime endTime = LocalTime.of(15, 35);

    ConfRoomRequest confRoomRequest =
        new ConfRoomRequest(
            LocalDate.of(2022, 5, 13),
            LocalTime.of(14, 45),
            LocalTime.of(15, 10),
            "Unity",
            "Hello",
            "nothing",
            "staff");
    database.getRoomRequestDAO().add(confRoomRequest);
    RoomRequestDAO roomRequestDAO = (RoomRequestDAO) database.getRoomRequestDAO();
    System.out.println(roomRequestDAO.hasConflicts("Unity", startDate, startTime, endTime));
    System.out.println(roomRequestDAO.getConfRoomLocationsAlphabetically().toString());
    System.out.println(roomRequestDAO.allPastRequestsbyUser("staff"));

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
