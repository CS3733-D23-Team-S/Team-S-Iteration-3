package edu.wpi.teamname.ServiceRequests;

import edu.wpi.teamname.databaseredo.IDAO;

import java.util.List;

public interface IServiceRequestDAO extends IDAO {
    public List<String> getListOfEligibleRooms();

}
