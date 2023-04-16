package edu.wpi.teamname;

import edu.wpi.teamname.DAOs.UserDAOImpl;
import org.junit.jupiter.api.BeforeAll;

public class PathFindingTest {

  @BeforeAll
  public static void init() {
    UserDAOImpl.DataBaseRepository.getInstance().load();
  }

  //	@Test
  //	public static void

}
