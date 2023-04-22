package edu.wpi.teamname.ServiceRequests.flowers;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

/** Cart class Cart is represented as a HashMap with Integer: id and Flower: flower */
public class Cart {
  @Getter @Setter private int cartID;
  // @Getter @Setter private HashMap<Integer, Flower> cartItems;
  @Getter @Setter private ArrayList<Flower> cartItems;

  public Cart(int cartID) {
    this.cartID = cartID;
    // cartItems = new HashMap<Integer, Flower>();
    cartItems = new ArrayList<Flower>();
  }

  public double getTotalPrice() {
    double totalPrice = 0;
    for (Flower flower : cartItems) {
      totalPrice += flower.getPrice() * flower.getQuantity();
    }

    return totalPrice;
  }

  public void addFlowerItem(Flower flower) {
    cartItems.add(flower);
  }

  public void removeFlowerItem(Flower flower) {
    cartItems.remove(flower);
  }

  public void removeAll() {
    cartItems.clear();
  }

  @Override
  public String toString() {
    StringBuilder finale = new StringBuilder();
    boolean comma = false;

    for (Flower flower : cartItems) {
      if (!comma) {
        finale.append(flower.toString());
        comma = true;
      } else {
        finale.append(", ").append(flower.toString());
      }
    }

    return finale.toString();
  }
}
