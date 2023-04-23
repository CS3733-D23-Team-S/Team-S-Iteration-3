package edu.wpi.teamname.DAOs.orms;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

public class Signage {
    @Getter @Setter
    Location kioskLocation;
    @Getter @Setter Direction direction;
    @Getter @Setter String surroundingLocation;



    public enum Direction{
        up,
        down,
        left,
        right
    }

    public Signage(Location kioskLocation, Direction direction, String surroundingLocation){
        this.direction = direction;
        this.kioskLocation = kioskLocation;
        this.surroundingLocation = surroundingLocation;
    }



}


