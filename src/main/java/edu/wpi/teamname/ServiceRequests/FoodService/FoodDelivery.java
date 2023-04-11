package edu.wpi.teamname.ServiceRequests.FoodService;

import edu.wpi.teamname.databaseredo.IDataPack;
import java.sql.*;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("ALL")
public class FoodDelivery implements IDataPack {

  @Getter private int deliveryID;
  @Getter private String cart;
  @Getter @Setter private Date date;
  @Getter @Setter private Time time;
  @Getter private String location;
  @Getter @Setter private String orderer;
  @Getter @Setter private String assignedTo;
  @Getter @Setter private String orderStatus;
  @Getter @Setter private double cost;
  @Getter @Setter private String notes = "";

  public FoodDelivery(
      int id,
      String cart,
      Date d,
      Time t,
      String location,
      String orderedBy,
      String assignedTo,
      String s,
      double c,
      String notes) {

    this.deliveryID = id;
    this.cart = cart;
    this.date = d;
    this.time = t;
    this.location = location;
    this.orderer = orderedBy;
    this.assignedTo = assignedTo;
    this.orderStatus = s;
    this.cost = c;
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
            + location
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
            + location
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
