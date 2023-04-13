package edu.wpi.teamname;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.NodeType;
import org.junit.jupiter.api.Test;

public class LocationTest {

  Location location = new Location("Long Location", "Short Location", NodeType.CONF);

  @Test
  public void LocationLongNameTest() {
    assertEquals(location.getLongName(), "Long Location");
  }

  @Test
  public void toStringTest() {
    assertEquals(
        location.toString(),
        "Location{notetype = CONF, longname = Long Location, shortname = Short Location}");
  }

  @Test
  public void toCSVStringTest() {
    assertEquals(location.toCSVString(), "Long Location,Short Location,CONF");
  }
}
