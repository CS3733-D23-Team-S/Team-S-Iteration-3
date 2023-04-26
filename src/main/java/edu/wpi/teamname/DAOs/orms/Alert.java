package edu.wpi.teamname.DAOs.orms;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

public class Alert {
  @Getter @Setter String heading;
  @Getter @Setter String message;
  @Getter @Setter LocalDate dateOfAlert;
  @Getter @Setter LocalTime timeOfAlert;
  @Getter @Setter User user;

  public Alert(String heading, String message, User user) {
    this.heading = heading;
    this.message = message;
    this.dateOfAlert = LocalDate.now();
    this.timeOfAlert = LocalTime.now();
    this.user = user;
  }
}
