package edu.wpi.teamname.ServiceRequests.flowers;

import io.github.palexdev.materialfx.controls.MFXTextField;
import lombok.Getter;
import lombok.Setter;

public class FlowerDeliverySubmission {
  @Getter @Setter private String flower;
  @Getter @Setter private MFXTextField room;

  public FlowerDeliverySubmission(String flower, MFXTextField room) {
    this.flower = flower;
    this.room = room;
  }

  public String getFlower() {
    return flower;
  }
}
