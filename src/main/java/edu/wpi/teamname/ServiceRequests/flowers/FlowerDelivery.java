package edu.wpi.teamname.ServiceRequests.flowers;

import java.sql.*;
import lombok.Getter;
import lombok.Setter;

public class FlowerDelivery {
  @Getter @Setter private int ID;
  @Getter @Setter private String cart;
  @Getter @Setter private Date date;
  @Getter @Setter private Time time;
  @Getter @Setter private String room;
  @Getter @Setter private String orderedBy;
  @Getter @Setter private String assignedTo;
  @Getter @Setter private String orderStatus;
  @Getter @Setter private double cost;

  public FlowerDelivery(
      int ID,
      Cart cart,
      Date date,
      Time time,
      String room,
      String orderedBy,
      String assignedTo,
      String orderStatus) {
    this.ID = ID;
    this.cart = cart.toString();
    this.date = date;
    this.time = time;
    this.room = room;
    this.orderedBy = orderedBy;
    this.assignedTo = assignedTo;
    this.orderStatus = orderStatus;
    this.cost = cart.getTotalPrice();
  }
}
