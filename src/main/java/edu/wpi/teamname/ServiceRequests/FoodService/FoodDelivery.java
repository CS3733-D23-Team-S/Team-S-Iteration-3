package edu.wpi.teamname.ServiceRequests.FoodService;

import edu.wpi.teamname.databaseredo.IDataPack;
import edu.wpi.teamname.databaseredo.orms.Location;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("ALL")
public class FoodDelivery implements IDataPack {

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
      int id,
      OrderItem cart,
      Location location,
      String orderedBy,
      String assignedTo,
      String notes) {

    this.deliveryID = id;
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

  @Override
  public String toString() {
    String finale =
        "FoodDelivery{cart = "
            + cart
            + ", date = "
            + date.toString()
            + ", time = "
            + time.toString()
            + ", location = "
            + location.toString()
            + ", orderer = "
            + orderer
            + ", assignedTo = "
            + assignedTo
            + ", status = "
            + orderStatus
            + ", cost = "
            + cost
            + ", notes = "
            + notes
            + "}";

    return finale;
  }

  @Override
  public String toCSVString() {
    String finale =
        cart
            + ","
            + date.toString()
            + ","
            + time.toString()
            + ","
            + location.toString()
            + ","
            + orderer
            + ","
            + assignedTo
            + ","
            + orderStatus
            + ","
            + cost
            + ","
            + notes;

    return finale;
  }
}
