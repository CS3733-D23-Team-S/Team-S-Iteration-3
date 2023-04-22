package edu.wpi.teamname.ServiceRequests.FoodService;

import java.util.HashMap;
import lombok.Getter;

/* TODO:
       add Java Docs
       StringBuilder for finale in toString()?
*/
public class OrderItem {
  // This is the cart class
  @Getter private final int cartID;
  private final HashMap<Integer, Food> theCart;

  public OrderItem(int cartID) {
    this.cartID = cartID;
    theCart = new HashMap<Integer, Food>();
  }

  public HashMap<Integer, Food> getTheCart() {
    return theCart;
  }

  public double getTotalPrice() {
    double totalprice = 0.0;
    for (Food aFood : theCart.values()) {
      totalprice += aFood.getFoodPrice() * aFood.getQuantity();
    }

    return totalprice;
  }

  @Override
  public String toString() {
    String finale = "";
    for (Food aFood : theCart.values()) {
      finale += aFood.toString() + ", ";
    }

    return finale;
  }

  public void addFoodItem(Food f) {
    theCart.put(f.getFoodID(), f);
  }
}
