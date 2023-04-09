package edu.wpi.teamname.ServiceRequests.FoodService;

import java.util.HashMap;

public interface FoodDeliveryDAO_I {
  public HashMap<Integer, FoodDelivery> getAllRequests();

  FoodDelivery getRequest(int target);

  void addRequest(FoodDelivery request);

  void deleteRequest(int target);
}
