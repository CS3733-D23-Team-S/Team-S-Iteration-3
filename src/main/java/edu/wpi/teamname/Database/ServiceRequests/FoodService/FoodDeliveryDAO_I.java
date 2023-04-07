package edu.wpi.teamname.Database.ServiceRequests.FoodService;

import java.util.List;

public interface FoodDeliveryDAO_I {
  public List<FoodDelivery> getAllRequests();

  public FoodDelivery getRequest(int target);

  public void addRequest(FoodDelivery request);

  public void deleteRequest(int target);
}
