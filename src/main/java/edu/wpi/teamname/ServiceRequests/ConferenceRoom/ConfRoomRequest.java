package edu.wpi.teamname.ServiceRequests.ConferenceRoom;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

public class ConfRoomRequest {
  static int reservationIDUpdater;
  @Getter @Setter int reservationID;
  @Getter @Setter LocalDate date;
  @Getter @Setter LocalTime startTime;
  @Getter @Setter LocalTime endTime;
  @Getter @Setter Room room;
  @Getter @Setter String reservedBy;
  @Getter @Setter String eventName;
  @Getter @Setter String eventDescription;
  @Getter @Setter String assignedTo;
  @Getter @Setter Status orderStatus;
  @Getter @Setter String notes = "";

  public ConfRoomRequest(
      LocalDate date,
      LocalTime startTime,
      LocalTime endTime,
      Room room,
      String reservedBy,
      String eventName,
      String eventDescription,
      String assignedTo,
      Status orderStatus,
      String notes) {
    reservationID = createReservationID();
    this.date = date;
    this.startTime = startTime;
    this.endTime = endTime;
    this.room = room;
    this.reservedBy = reservedBy;
    this.eventName = eventName;
    this.eventDescription = eventDescription;
    this.assignedTo = assignedTo;
    this.orderStatus = orderStatus;
    this.notes = notes;
  }

  public int createReservationID() {
    ++reservationIDUpdater;
    this.reservationID = reservationIDUpdater;
    return reservationID;
  }

  public String getRoomId() {
    return room.getId();
  }
}
