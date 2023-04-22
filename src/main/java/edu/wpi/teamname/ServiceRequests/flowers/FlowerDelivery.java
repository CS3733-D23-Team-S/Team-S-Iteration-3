package edu.wpi.teamname.ServiceRequests.flowers;

import edu.wpi.teamname.DAOs.IDataPack;
import java.sql.*;
import lombok.Getter;
import lombok.Setter;

public class FlowerDelivery implements IDataPack {
  @Getter @Setter private int ID;
  @Getter @Setter private String cart;
  @Getter @Setter private Date date;
  @Getter @Setter private Time time;
  @Getter @Setter private String room;
  @Getter @Setter private String orderedBy;
  @Getter @Setter private String assignedTo;
  @Getter @Setter private String orderStatus;
  @Getter @Setter private double cost;
  @Getter @Setter private String notes = "";

  public FlowerDelivery(
      int ID,
      String cart,
      Date date,
      Time time,
      String room,
      String orderedBy,
      String assignedTo,
      String orderStatus,
      double cost,
      String n) {
    this.ID = ID;
    this.cart = cart;
    this.date = date;
    this.time = time;
    this.room = room;
    this.orderedBy = orderedBy;
    this.assignedTo = assignedTo;
    this.orderStatus = orderStatus;
    this.cost = cost;
    this.notes = n;
  }

  @Override
  public String toString() {
    return ID
        + " "
        + cart
        + " "
        + date
        + " "
        + time
        + " "
        + room
        + " "
        + orderedBy
        + " "
        + assignedTo
        + " "
        + orderStatus
        + " "
        + cost;
  }

  public String toCSVString() {
    return ID
        + ","
        + cart
        + ","
        + date
        + ","
        + time
        + ","
        + room
        + ","
        + orderedBy
        + ","
        + assignedTo
        + ","
        + orderStatus
        + ","
        + cost;
  }

  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }
}
