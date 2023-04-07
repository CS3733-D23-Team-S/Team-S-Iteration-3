package edu.wpi.teamname.navigation;

import edu.wpi.teamname.Database.ServiceRequests.FoodService.FoodDAOImpl;
import java.util.ArrayList;

public class MealDelivery1 {
  private FoodDAOImpl foodDAO;
  private ArrayList<String> foodP = new ArrayList<String>();

  public MealDelivery1() {
    foodDAO = FoodDAOImpl.getInstance();
  }
}
