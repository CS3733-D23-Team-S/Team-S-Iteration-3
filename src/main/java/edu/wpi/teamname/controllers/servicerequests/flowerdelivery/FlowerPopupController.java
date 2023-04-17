package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import edu.wpi.teamname.controllers.PopUpController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class FlowerPopupController extends PopUpController {
  @FXML MFXButton addcartbutton;
  @FXML MFXButton decrementbutton;
  @FXML ImageView flowerimage;
  @FXML MFXButton incrementbutton;
  @FXML Label quantitylabel;
}
