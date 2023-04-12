package edu.wpi.teamname.databaseredo;

import edu.wpi.teamname.ServiceRequests.flowers.*;
import edu.wpi.teamname.databaseredo.orms.Move;
import edu.wpi.teamname.pathfinding.AStar;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class DataBaseRepository {

  private static DataBaseRepository single_instance = null;
  private dbConnection connection;
  AStar pathFinder;
  @Getter NodeDAOImpl nodeDAO;
  @Getter MoveDAOImpl moveDAO;
  @Getter LocationDAOImpl locationDAO;
  @Getter EdgeDAOImpl edgeDAO;

  @Getter IDAO<Flower, Integer> flowerDAO;
  @Getter IDAO<FlowerDelivery, Integer> flowerDeliveryDAO;

  private DataBaseRepository() {
    nodeDAO = new NodeDAOImpl();
    moveDAO = new MoveDAOImpl();
    locationDAO = new LocationDAOImpl();
    edgeDAO = new EdgeDAOImpl();

    flowerDAO = new FlowerDAOImpl();
    flowerDeliveryDAO = new FlowerDeliveryDAOImpl();
  }

  public static synchronized DataBaseRepository getInstance() {
    if (single_instance == null) single_instance = new DataBaseRepository();
    return single_instance;
  }

  public void load() {
    connection = dbConnection.getInstance();
    pathFinder = new AStar();
    nodeDAO.initTable(connection.getNodeTable());
    edgeDAO.initTable(connection.getEdgesTable());
    locationDAO.initTable(connection.getLocationTable());
    moveDAO.initTable(connection.getMoveTable());
    nodeDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Node.csv");
    edgeDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Edge.csv");
    locationDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/LocationName.csv");
    moveDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Move.csv");

    flowerDAO.initTable(connection.getFlowerTable());
    flowerDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Flower.csv");
    flowerDeliveryDAO.initTable(connection.getFlowerDeliveryTable());
    flowerDeliveryDAO.loadRemote("flowersssssss!?");
  }

  public boolean login(String text, String text1) {
    return true;
  }

  public String processMoveRequest(int newLocNodeID, String location, LocalDate date)
      throws Exception {
    if (checkCanMove(location, date)) {
      throw new Exception("Moved that day already");
    } else {
      String moveResult;
      if (date.isAfter(LocalDate.now())) {
        moveResult = "Going to move " + location + " on " + date;
      } else {
        moveResult = "Moved " + location + " to its new location";
      }
      Move thisMove = new Move(newLocNodeID, location, date);
      moveDAO.add(thisMove);
      return moveResult;
    }
  }

  private boolean checkCanMove(String location, LocalDate date) {
    return moveDAO.getMoveHistory().get(location).contains(date);
  }

  public void importCSV(String inputPath) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(inputPath));
    String check = reader.readLine();

    if (check.equals(nodeDAO.getCSVheader())) nodeDAO.importCSV(inputPath);
    else if (check.equals(edgeDAO.getCSVheader())) edgeDAO.importCSV(inputPath);
    else if (check.equals(moveDAO.getCSVheader())) {
      moveDAO.importCSV(inputPath);
    } else if (check.equals(locationDAO.getCSVheader())) {
      locationDAO.importCSV(inputPath);
    } else throw new RuntimeException();
  }

  public void exportCSV(String outputPath) throws IOException {
    nodeDAO.exportCSV(outputPath);
    edgeDAO.exportCSV(outputPath);
    moveDAO.exportCSV(outputPath);
    locationDAO.exportCSV(outputPath);
  }

  public void addFlower(Flower flower) {
    flowerDAO.add(flower);
  }

  public List<Flower> getListOfSize(String size) {
    List<Flower> flowers = flowerDAO.getAll();
    List<Flower> sizedFlowers = new ArrayList<>();

    for (Flower flower : flowers) {
      if (flower.getSize().toString().equalsIgnoreCase(size)) {
        sizedFlowers.add(flower);
      }
    }

    return sizedFlowers;
  }

  public List getListOfFlowers() {
    List<Flower> flowers = new ArrayList<>();
    Flower flowerf1 = new Flower(1, "bouquet1", Size.SMALL, 50, 1, "message",false, "description1", "image1");
    Flower flowerf2 =
        new Flower(2, "bouquet2", Size.MEDIUM, 100, 1, "message",false, "description2", "image2");
    Flower flowerf3 = new Flower(3, "bouquet3", Size.SMALL, 50, 1, "message",false, "description3", "image3");
    Flower flowerf4 =
        new Flower(4, "bouquet4", Size.LARGE, 200, 1, "message",false, "description4", "image4");
    Flower flowerf5 = new Flower(5, "bouquet5", Size.SMALL, 50, 1, "message",false, "description5", "image5");

    flowers.add(flowerf1);
    flowers.add(flowerf2);
    flowers.add(flowerf3);
    flowers.add(flowerf4);
    flowers.add(flowerf5);

    return flowers;
  }

  public Flower retrieveFlower(int target) {
    return flowerDAO.getRow(target);
  }
}
