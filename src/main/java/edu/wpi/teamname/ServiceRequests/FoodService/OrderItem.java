package edu.wpi.teamname.ServiceRequests.FoodService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

/* TODO:
       add Java Docs
       StringBuilder for finale in toString()?
*/
public class OrderItem {
  // This is the cart class
  @Getter private int cartID;
  // private HashMap<Integer, Food> theCart;
  @Getter @Setter ObservableList<Food> cartItems;

  public OrderItem(int cartID) {
    this.cartID = cartID;
    // theCart = new HashMap<Integer, Food>();
    cartItems = FXCollections.observableArrayList();
  }

  public double getTotalPrice() {
    double totalprice = 0.0;
    for (Food aFood : cartItems) {
      totalprice += aFood.getFoodPrice() * aFood.getQuantity();
    }

    return totalprice;
  }

  @Override
  public String toString() {
    String finale = "";
    for (Food aFood : cartItems) {
      finale += aFood.toString() + ", ";
    }

    return finale;
  }

  public void addFoodItem(Food f) {
    cartItems.add(f);
  }

  public void removeFoodItem(Food f) {
    cartItems.remove(f);
  }

  public void removeAll() {
    cartItems.clear();
  }
}
