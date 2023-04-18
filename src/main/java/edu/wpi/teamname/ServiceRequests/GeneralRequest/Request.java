package edu.wpi.teamname.ServiceRequests.GeneralRequest;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

public class Request {
  @Getter @Setter String requestType;
  @Getter @Setter LocalTime deliveryTime;
  @Getter @Setter String orderStatus;
  @Getter @Setter String location;
  @Getter @Setter String assignedTo;
  @Getter @Setter String orderedBy;

  public Request(
      String requestType,
      LocalTime deliveryTime,
      String orderStatus,
      String location,
      String assignedTo,
      String orderedBy) {
    this.deliveryTime = deliveryTime;
    this.requestType = requestType;
    this.orderStatus = orderStatus;
    this.location = location;
    this.assignedTo = assignedTo;
    this.orderedBy = orderedBy;
  }
}
