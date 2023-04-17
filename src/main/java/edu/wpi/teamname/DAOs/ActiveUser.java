package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Permission;
import edu.wpi.teamname.DAOs.orms.SRCart;
import edu.wpi.teamname.DAOs.orms.SRItem;
import edu.wpi.teamname.DAOs.orms.User;
import lombok.Getter;
import lombok.Setter;

public class ActiveUser {

  @Getter @Setter User currentUser;

  @Getter Permission permission;

  @Getter @Setter private boolean loggedIn = false;

  public SRCart cart;

  private static ActiveUser single_instance = null;

  private ActiveUser() {
    cart = new SRCart();
  }

  public static synchronized ActiveUser getInstance() {
    if (single_instance == null) single_instance = new ActiveUser();
    return single_instance;
  }

  public void addToCart(SRItem item, int quantity) {
    cart.addItem(item, quantity);
  }

  public void removeFromCart(IDataPack item) {
    cart.deleteItem(item);
  }
}
