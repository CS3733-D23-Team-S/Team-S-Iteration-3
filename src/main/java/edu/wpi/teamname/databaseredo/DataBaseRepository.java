package edu.wpi.teamname.databaseredo;

import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDAOImpl;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDelivery;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDeliveryDAOImp;
import edu.wpi.teamname.databaseredo.orms.Edge;
import edu.wpi.teamname.databaseredo.orms.Location;
import edu.wpi.teamname.databaseredo.orms.Move;
import edu.wpi.teamname.databaseredo.orms.Node;
import edu.wpi.teamname.pathfinding.AStar;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;

public class DataBaseRepository {

  private static DataBaseRepository single_instance = null;
  private dbConnection connection;
  AStar pathFinder;
  @Getter IDAO<Node, Integer> nodeDAO;
  @Getter MoveDAOImpl moveDAO;
  @Getter IDAO<Location, String> locationDAO;
  @Getter IDAO<Edge, Edge> edgeDAO;
  @Getter IDAO<Food, Integer> foodDAO;
  @Getter IDAO<FoodDelivery, Integer> foodDeliveryDAO;

  private DataBaseRepository() {
    nodeDAO = new NodeDAOImpl();
    moveDAO = new MoveDAOImpl();
    locationDAO = new LocationDAOImpl();
    edgeDAO = new EdgeDAOImpl();

    // ServiceRequests
    foodDAO = new FoodDAOImpl();
    foodDeliveryDAO = new FoodDeliveryDAOImp();
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

    // ServiceRequests
    foodDAO.initTable(connection.getFoodTable());
    foodDAO.loadRemote("src/main/java/edu/wpi/teamname/defaultCSV/Foods.csv");
    foodDeliveryDAO.initTable(connection.getFoodRequestsTable());
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
}
