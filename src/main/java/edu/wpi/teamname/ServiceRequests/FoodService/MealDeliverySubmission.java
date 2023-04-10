package edu.wpi.teamname.ServiceRequests.FoodService;

import io.github.palexdev.materialfx.controls.MFXTextField;

/* TODO:
       add @Setter @Getter
       remove static
*/
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
