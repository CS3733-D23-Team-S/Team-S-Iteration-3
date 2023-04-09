package edu.wpi.teamname.ServiceRequests.ConferenceRoom;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

public class ConfRoomRequest {

  @Getter @Setter LocalDate orderDate;
  @Getter @Setter LocalDate eventDate;
  @Getter @Setter LocalTime startTime;
  @Getter @Setter LocalTime endTime;
  @Getter @Setter String room;
  @Getter @Setter String reservedBy;
  @Getter @Setter String eventName;
  @Getter @Setter String eventDescription;
  @Getter @Setter String assignedTo = "NA";
  @Getter @Setter Status orderStatus;
  @Getter @Setter String notes = "";

  public ConfRoomRequest(
      LocalDate eventDate,
      LocalTime startTime,
      LocalTime endTime,
      String room,
      String eventName,
      String eventDescription,
      String assignedTo) {
    this.orderDate = LocalDate.now();
    this.eventDate = eventDate;
    this.startTime = startTime;
    this.endTime = endTime;
    this.room = room;
    this.reservedBy = "staff";
    this.eventName = eventName;
    this.eventDescription = eventDescription;
    this.assignedTo = assignedTo;
    this.orderStatus = Status.Received;
  }
}
