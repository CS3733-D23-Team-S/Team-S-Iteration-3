package edu.wpi.teamname.controllers.mainpages;

import edu.wpi.teamname.controllers.PopUpController;
import java.awt.*;
import java.io.FileNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class NewUserController extends PopUpController {

  @FXML Label errorMessage;
  String error;
  @FXML BackgroundImage backgroundImage;
  @FXML TextField unField;
  @FXML PasswordField psField1;
  @FXML PasswordField psField2;
  @FXML Button submit;

  public void initialize() throws FileNotFoundException {
    exitButton.setOnMouseClicked(event -> stage.close());

    BackgroundSize size = new BackgroundSize(200, 200, false, false, false, false);

    BackgroundImage background =
        new BackgroundImage(
            new Image("edu/wpi/teamname/images/bwlogo.png"),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            size);

    submit.setOnMouseClicked(event -> {});
  }

  private boolean isfieldFilled() {
    boolean isFilled = true;
    if (unField.getText().isEmpty()) {
      isFilled = false;
      error = "Username is empty!";
    }

    if (psField1.getText().isEmpty() || psField2.getText().isEmpty()) {
      isFilled = false;
      if (error.isEmpty()) {
        error = "Password is empty!";
      } else {
        error += "\nPassword is empty!";
      }
    }
    errorMessage.setText(error);
    return isFilled;
  }
}

/*
-fx-background-image: url(edu/wpi/teamname/images/bwlogo.png); -fx-background-position: center center; -fx-background-repeat: no-repeat; -fx-background-size: 500 500;"
 */
