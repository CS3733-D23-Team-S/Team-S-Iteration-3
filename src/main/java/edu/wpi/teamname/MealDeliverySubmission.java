package edu.wpi.teamname;

import io.github.palexdev.materialfx.controls.MFXTextField;

public class MealDeliverySubmission {
  static String food;
  static MFXTextField room;

  public static String getFood() {
    return food;
  }

  public static MFXTextField getRoom() {
    return room;
  }

  public static void setFood(String food) {
    MealDeliverySubmission.food = food;
  }

  public static void setRoom(MFXTextField room) {
    MealDeliverySubmission.room = room;
  }
}
