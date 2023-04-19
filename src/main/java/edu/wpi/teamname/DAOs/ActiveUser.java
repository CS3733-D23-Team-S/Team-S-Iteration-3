package edu.wpi.teamname.DAOs;

import edu.wpi.teamname.DAOs.orms.Permission;
import edu.wpi.teamname.DAOs.orms.User;
import lombok.Getter;
import lombok.Setter;

public class ActiveUser {

  @Getter User currentUser;

  @Getter Permission permission;

  @Getter @Setter private boolean loggedIn = false;

  // public SRCart cart;

  private static ActiveUser single_instance = null;

  private ActiveUser() {}

  public void setCurrentUser(User user) {
    this.currentUser = user;
    this.permission = user.getPermission();
  }

  public static synchronized ActiveUser getInstance() {
    if (single_instance == null) single_instance = new ActiveUser();
    return single_instance;
  }

  /*public void addToCart(SRItem item, int quantity) {
    cart.addItem(item, quantity);
  }

  public void removeFromCart(IDataPack item) {
    cart.deleteItem(item);
  }*/
}
