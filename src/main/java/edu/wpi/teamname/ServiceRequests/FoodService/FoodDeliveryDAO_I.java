package edu.wpi.teamname.ServiceRequests.FoodService;

import java.util.HashMap;

public interface FoodDeliveryDAO_I {
  public HashMap<Integer, FoodDelivery> getAllRequests();

  public FoodDelivery getRequest(int target);

  public void addRequest(FoodDelivery request);

  public void deleteRequest(int target);
}
