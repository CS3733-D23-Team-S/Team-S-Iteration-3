package edu.wpi.teamname;

import static org.junit.Assert.assertEquals;

import edu.wpi.teamname.DAOs.*;
import edu.wpi.teamname.pathfinding.AStar;
import edu.wpi.teamname.pathfinding.BFS;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PathFindingTest {
  static DataBaseRepository dbr = DataBaseRepository.getInstance();
  BFS bfs = new BFS();
  AStar astar = new AStar();

  @BeforeAll
  public static void start() {
    // Init database shit fuck
    dbr.load();
  }
  // BFS
  @Test
  public void bfs1() throws SQLException {

    List<Integer> expectedPath = new ArrayList<>();
    expectedPath.add(130);
    expectedPath.add(110);
    expectedPath.add(2825);
    // assertTrue(expectedPath);
    assertEquals(bfs.findPath(130, 2825), expectedPath);
    dbConnection.getInstance().getConnection().close();
  }

  @Test
  public void bfslong() throws SQLException {

    List<Integer> expectedPath = new ArrayList<>();
    expectedPath.add(480);
    expectedPath.add(485);
    expectedPath.add(495);
    expectedPath.add(505);
    expectedPath.add(510);
    expectedPath.add(515);
    expectedPath.add(520);
    expectedPath.add(525);
    expectedPath.add(530);

    // assertTrue(expectedPath);
    BFS bfs = new BFS();
    Assertions.assertEquals(bfs.findPath(480, 530), expectedPath);
    dbConnection.getInstance().getConnection().close();
  }

  @Test
  public void AStar() throws SQLException {

    List<Integer> expectedPath = new ArrayList<>();
    expectedPath.add(130);
    expectedPath.add(110);
    expectedPath.add(2825);
    // assertTrue(expectedPath);
    Assertions.assertEquals(astar.findPath(130, 2825), expectedPath);
    dbConnection.getInstance().getConnection().close();
  }

  @Test
  public void AStarLong() throws SQLException {

    List<Integer> expectedPath = new ArrayList<>();
    expectedPath.add(480);
    expectedPath.add(485);
    expectedPath.add(495);
    expectedPath.add(505);
    expectedPath.add(510);
    expectedPath.add(515);
    expectedPath.add(520);
    expectedPath.add(525);
    expectedPath.add(530);

    // assertTrue(expectedPath);
    AStar astar = new AStar();
    Assertions.assertEquals(astar.findPath(480, 530), expectedPath);
    dbConnection.getInstance().getConnection().close();
  }

  @AfterAll
  public static void closeDB() throws SQLException {
    dbConnection.getInstance().getConnection().close();
  }
}
