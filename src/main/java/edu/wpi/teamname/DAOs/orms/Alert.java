package edu.wpi.teamname.DAOs.orms;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class Alert {
    @Getter @Setter String heading;
    @Getter @Setter String message;
    @Getter @Setter LocalDate dateOfAlert;

    public Alert(String heading, String message){
        this.heading = heading;
        this.message = message;
        this.dateOfAlert = LocalDate.now();

    }




}
