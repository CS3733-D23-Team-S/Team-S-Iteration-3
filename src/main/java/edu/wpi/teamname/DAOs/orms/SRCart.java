package edu.wpi.teamname.DAOs.orms;

import edu.wpi.teamname.DAOs.IDataPack;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

public class SRCart implements SRItem {

  // @Getter @Setter private HashMap<Integer, Flower> cartItems;
  @Getter @Setter private HashMap<SRItem, Integer> cartItems;

  public SRCart() {

    // cartItems = new HashMap<Integer, Flower>();
    cartItems = new HashMap<>();
  }

  public double getTotalPrice() {
    double totalPrice = 0;
    for (SRItem item : cartItems.keySet()) {
      totalPrice += item.getPrice() * cartItems.get(item);
    }

    return totalPrice;
  }

  public void addItem(SRItem dataItem, int quantity) {
    cartItems.put(dataItem, quantity);
  }

  public void deleteItem(IDataPack dataItem) {
    cartItems.remove(dataItem);
  }

  @Override
  public String toString() {
    return "SRCart{" + "cartItems=" + cartItems + '}';
  }

  @Override
  public double getPrice() {
    return 0;
  }
}
