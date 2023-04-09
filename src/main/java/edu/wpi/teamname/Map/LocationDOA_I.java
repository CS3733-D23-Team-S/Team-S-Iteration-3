package edu.wpi.teamname.Map;

import edu.wpi.teamname.databaseredo.orms.Location;

import java.util.List;

public interface LocationDOA_I {
  public List<Location> getAllLocations();

  public void initTable(String name);

  public Location getLocation(String longName);

  void addLocation(Location thisLocation);

  void loadToRemote(String pathToCSV);
}
