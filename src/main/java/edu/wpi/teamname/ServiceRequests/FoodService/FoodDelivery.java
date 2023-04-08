package edu.wpi.teamname.ServiceRequests.FoodService;

import edu.wpi.teamname.Map.Location;
import java.sql.*;
import lombok.Getter;
import lombok.Setter;

public class FoodDelivery {

  @Getter private int deliveryID;
  @Getter private String cart;
  @Getter @Setter private Date date;
  @Getter @Setter private Time time;
  @Getter private Location location;
  @Getter @Setter private String orderer;
  @Getter @Setter private String assignedTo;
  @Getter @Setter private String orderStatus;
  @Getter @Setter private double cost;
  @Getter @Setter private String notes = "";

  public FoodDelivery(
      int deliveryID,
      OrderItem cart,
      Date date,
      Time time,
      Location location,
      String orderedBy,
      String assignedTo,
      String orderStatus,
      String notes) {
    this.deliveryID = deliveryID;
    this.cart = cart.toString();
    this.date = date;
    this.time = time;
    this.location = location;
    this.orderer = orderedBy;
    this.assignedTo = assignedTo;
    this.orderStatus = orderStatus;
    this.cost = cart.getTotalPrice();
    this.notes = notes;
  }
}
