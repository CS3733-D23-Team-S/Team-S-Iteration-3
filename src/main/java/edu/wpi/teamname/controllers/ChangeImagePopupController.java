package edu.wpi.teamname.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ChangeImagePopupController {

  @FXML MFXTextField urlInput;
  @FXML ImageView profilePicture;
  @FXML MFXButton submitButton;
  @FXML MFXButton clearButton;
  @FXML Label errorMessage;

  @FXML
  public void initialize() {

    errorMessage.setText("");
    submitButton.setOnMouseClicked(event -> submitURL());
    clearButton.setOnMouseClicked(event -> clearFields());
  }

  public void submitURL() {
    ImageView newProfile =
        new ImageView(
            "https://www.wpi.edu/sites/default/files/styles/1x_383x383/public/faculty-image/wwong2.jpg?itok=1k92SiB1");
    // profilePicture.setImage(newProfile);
  }

  public void clearFields() {
    urlInput.clear();
  }
}
