package edu.wpi.teamname.ServiceRequests.FoodService;

import java.util.List;

public interface FoodDeliveryDAO_I {
  List<FoodDelivery> getAllRequests();

  FoodDelivery getRequest(int target);

  void addRequest(FoodDelivery request);

  void deleteRequest(int target);
}
