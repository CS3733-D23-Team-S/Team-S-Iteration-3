package edu.wpi.teamname.DAOs.orms;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

public class Alert {
  @Getter @Setter String heading;
  @Getter @Setter String message;
  @Getter @Setter LocalDate dateOfAlert;

  public Alert(String heading, String message) {
    this.heading = heading;
    this.message = message;
    this.dateOfAlert = LocalDate.now();
  }
}
