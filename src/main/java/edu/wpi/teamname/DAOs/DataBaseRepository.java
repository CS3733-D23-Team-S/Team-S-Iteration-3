package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.Move;
import edu.wpi.teamname.DAOs.orms.NodeType;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomDAO;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import edu.wpi.teamname.ServiceRequests.flowers.*;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDAOImpl;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDelivery;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDeliveryDAOImp;
import edu.wpi.teamname.pathfinding.AStar;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
  @Getter RoomRequestDAO roomRequestDAO;
  @Getter ConfRoomDAO confRoomDAO;

  @Getter IDAO<Food, Integer> foodDAO;

  @Getter IDAO<FoodDelivery, Integer> foodDeliveryDAO;

  @Getter IDAO<Flower, Integer> flowerDAO;
  @Getter IDAO<FlowerDelivery, Integer> flowerDeliveryDAO;

  private DataBaseRepository() {
    nodeDAO = new NodeDAOImpl();
    moveDAO = new MoveDAOImpl();
    locationDAO = new LocationDAOImpl();
    edgeDAO = new EdgeDAOImpl();
    roomRequestDAO = new RoomRequestDAO();
    confRoomDAO = new ConfRoomDAO();
    foodDAO = new FoodDAOImpl();
    foodDeliveryDAO = new FoodDeliveryDAOImp();

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
    roomRequestDAO.initTable(connection.getRoomReservationsTable());
    confRoomDAO.initTable(connection.getConferenceRoomTables());
    foodDAO.initTable(connection.getFoodTable());
    foodDeliveryDAO.initTable(connection.getFoodRequestsTable());

    nodeDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Node.csv");
    edgeDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Edge.csv");
    locationDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/LocationName.csv");
    moveDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Move.csv");

    flowerDAO.initTable(connection.getFlowerTable());
    flowerDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Flower.csv");
    flowerDeliveryDAO.initTable(connection.getFlowerDeliveryTable());
    flowerDeliveryDAO.loadRemote("flowersssssss!?");
    foodDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Foods.csv");
    foodDeliveryDAO.loadRemote("This means nothing");
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

  public void addRoomRequest(ConfRoomRequest confRoomRequest) throws Exception {

    if (!roomRequestDAO.hasConflicts(
        confRoomRequest.getRoom(),
        confRoomRequest.getEventDate(),
        confRoomRequest.getStartTime(),
        confRoomRequest.getEndTime())) {
      roomRequestDAO.add(confRoomRequest);

    } else {
      throw new Exception();
    }
  }

  public Location getLocation(String longname) {
    return locationDAO.getRow(longname);
  }

  // Service Request stuff
  public ArrayList<Food> getWalletFriendlyFood() {
    ArrayList<Food> wFriendlyFoods = new ArrayList<>();

    for (Food aFood : foodDAO.getAll()) {
      if (aFood.isWalletFriendly()) {
        wFriendlyFoods.add(aFood);
      }
    }
    return wFriendlyFoods;
  }

  public ArrayList<Food> getQuick() {
    ArrayList<Food> quickFood = new ArrayList<>();

    for (Food aFood : foodDAO.getAll()) {
      if (aFood.isQuickDelivery()) {
        quickFood.add(aFood);
      }
    }

    return quickFood;
  }

  public ArrayList<Food> getVegetarian() {
    ArrayList<Food> vegetarianFoods = new ArrayList<>();
    for (Food aFood : foodDAO.getAll()) {
      if (aFood.isVegetarian()) {
        vegetarianFoods.add(aFood);
      }
    }
    return vegetarianFoods;
  }

  public ArrayList<Food> getVegan() {
    ArrayList<Food> veganFoods = new ArrayList<>();
    for (Food aFood : foodDAO.getAll()) {
      if (aFood.isVegan()) {
        veganFoods.add(aFood);
      }
    }
    return veganFoods;
  }

  public ArrayList<Food> getHalal() {
    ArrayList<Food> halalFoods = new ArrayList<>();
    for (Food aFood : foodDAO.getAll()) {
      if (aFood.isHalal()) {
        halalFoods.add(aFood);
      }
    }
    return halalFoods;
  }

  public ArrayList<Food> getKosher() {
    ArrayList<Food> kosherFoods = new ArrayList<>();
    for (Food aFood : foodDAO.getAll()) {
      if (aFood.isKosher()) {
        kosherFoods.add(aFood);
      }
    }
    return kosherFoods;
  }

  public ArrayList<Food> getGlutenFree() {
    ArrayList<Food> glutenFreeFoods = new ArrayList<>();
    for (Food aFood : foodDAO.getAll()) {
      if (aFood.isGlutFree()) {
        glutenFreeFoods.add(aFood);
      }
    }
    return glutenFreeFoods;
  }

  public ArrayList<Food> getAmerican() {
    ArrayList<Food> americanFood = new ArrayList<>();

    for (Food aFood : foodDAO.getAll()) {
      if (aFood.isAmerican()) {
        americanFood.add(aFood);
      }
    }

    return americanFood;
  }

  public ArrayList<Food> getItalian() {
    ArrayList<Food> italianFood = new ArrayList<>();

    for (Food aFood : foodDAO.getAll()) {
      if (aFood.isItalian()) {
        italianFood.add(aFood);
      }
    }

    return italianFood;
  }

  public ArrayList<Food> getMexican() {
    ArrayList<Food> mexicanFood = new ArrayList<>();

    for (Food aFood : foodDAO.getAll()) {
      if (aFood.isMexican()) {
        mexicanFood.add(aFood);
      }
    }

    return mexicanFood;
  }

  public ArrayList<Food> getIndian() {
    ArrayList<Food> indianFood = new ArrayList<>();

    for (Food aFood : foodDAO.getAll()) {
      if (aFood.isIndian()) {
        indianFood.add(aFood);
      }
    }

    return indianFood;
  }

  public Food retrieveFood(int target) {
    return foodDAO.getRow(target);
  }

  public void addFoodRequest(FoodDelivery foodev) {
    foodDeliveryDAO.add(foodev);
  }

  public int getLastFoodDevID() {
    int lastIndex = foodDeliveryDAO.getAll().size() - 1;

    if (lastIndex == -1) return 0;
    else return foodDeliveryDAO.getAll().get(lastIndex).getDeliveryID() + 1;
  }

  public List<String> getListOfEligibleRooms() {

    List<String> listOfEligibleRooms = new ArrayList<>();
    List<Location> locationList = locationDAO.getAll();

    NodeType[] nodeTypes = new NodeType[6];
    nodeTypes[0] = NodeType.ELEV;
    nodeTypes[1] = NodeType.EXIT;
    nodeTypes[2] = NodeType.HALL;
    nodeTypes[3] = NodeType.REST;
    nodeTypes[4] = NodeType.STAI;
    nodeTypes[5] = NodeType.BATH;

    boolean isFound;
    for (Location loc : locationList) { // hashmap
      isFound = false;
      for (NodeType nt : nodeTypes) {
        if (loc.getNodeType() == nt) {
          isFound = true;
          break;
        }
      }
      if (!isFound) listOfEligibleRooms.add(loc.getLongName());
    }
    Collections.sort(listOfEligibleRooms);

    return listOfEligibleRooms;
  }

  public void flowerAdd(Flower flower) {
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

  /*public void addFlowers() {
    Flower flowerf1 =
        new Flower(
            flowerGetNewID(),
            "flower1",
            Size.SMALL,
            50,
            1,
            "message",
            false,
            "description1",
            "image1");
    flowerDAO.add(flowerf1);
    Flower flowerf2 =
        new Flower(
            flowerGetNewID(),
            "flower2",
            Size.MEDIUM,
            100,
            1,
            "message",
            false,
            "description2",
            "image2");
    flowerDAO.add(flowerf2);
    Flower flowerf3 =
        new Flower(
            flowerGetNewID(),
            "flower3",
            Size.SMALL,
            50,
            1,
            "message",
            false,
            "description3",
            "image3");
    flowerDAO.add(flowerf3);
    Flower flowerf4 =
        new Flower(
            flowerGetNewID(),
            "flower4",
            Size.LARGE,
            200,
            1,
            "message",
            false,
            "description4",
            "image4");
    flowerDAO.add(flowerf4);
    Flower flowerf5 =
        new Flower(
            flowerGetNewID(),
            "flower5",
            Size.SMALL,
            50,
            1,
            "message",
            false,
            "description5",
            "image5");
    flowerDAO.add(flowerf5);
  }*/

  public Flower flowerRetrieve(int target) {
    return flowerDAO.getRow(target);
  }

  public void flowerDeliveryAdd(FlowerDelivery fd) {
    flowerDeliveryDAO.add(fd);
  }

  public List<FlowerDelivery> flowerDeliveryGetAll() {
    return flowerDeliveryDAO.getAll();
  }

  public List<String> getListOfEligibleRooms() {
    DataBaseRepository repo = DataBaseRepository.getInstance();

    List<String> listOfEligibleRooms = new ArrayList<>();
    List<Location> locationList = repo.getLocationDAO().getAll();

    NodeType[] nodeTypes = new NodeType[6];
    nodeTypes[0] = NodeType.ELEV;
    nodeTypes[1] = NodeType.EXIT;
    nodeTypes[2] = NodeType.HALL;
    nodeTypes[3] = NodeType.REST;
    nodeTypes[4] = NodeType.STAI;
    nodeTypes[5] = NodeType.BATH;

    boolean isFound;
    for (Location loc : locationList) { // hashmap
      isFound = false;
      for (NodeType nt : nodeTypes) {
        if (loc.getNodeType() == nt) {
          isFound = true;
          break;
        }
      }
      if (!isFound) listOfEligibleRooms.add(loc.getLongName());
    }
    Collections.sort(listOfEligibleRooms);

    return listOfEligibleRooms;
  }

  public int flowerGetNewID() {
    return flowerDAO.getAll().size();
  }

  public int flowerGetNewDeliveryID() {
    return flowerDeliveryDAO.getAll().size();
  }
}
