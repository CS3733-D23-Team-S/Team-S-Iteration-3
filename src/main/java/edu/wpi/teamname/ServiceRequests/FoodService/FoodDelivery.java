package edu.wpi.teamname.ServiceRequests.FoodService;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

import edu.wpi.teamname.databaseredo.orms.Location;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("ALL")
public class FoodDelivery {


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

      OrderItem cart,
      Location location,
      String orderedBy,
      String assignedTo,
      String notes) {

    this.cart = cart.toString();
    this.date = Date.valueOf(LocalDate.now());
    this.time = Time.valueOf(LocalTime.now());
    this.location = location;
    this.orderer = orderedBy;
    this.assignedTo = assignedTo;
    this.orderStatus = "Received";
    this.cost = cart.getTotalPrice();
    this.notes = notes;
  }
}
