package edu.wpi.teamname.controllers.mainpages;

import static edu.wpi.teamname.navigation.Screen.HOME;
import static edu.wpi.teamname.navigation.Screen.WELCOME_PAGE;

import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.awt.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LoginController {
  DataBaseRepository loginManager;
  @FXML ImageView backIcon;
  @FXML MFXButton navigationbutton;
  @FXML MFXButton signagebutton;
  @FXML Label errormessageLabel;

  @FXML MFXButton loginbutton;

  @FXML PasswordField pfPassword;

  @FXML TextField tfUsername;

  private String errorMessage = "";

  private boolean isfieldFilled() {
    boolean isFilled = true;
    if (tfUsername.getText().isEmpty()) {
      isFilled = false;
      errorMessage = "Username is empty!";
    }

    if (pfPassword.getText().isEmpty()) {
      isFilled = false;
      if (errorMessage.isEmpty()) {
        errorMessage = "Password is empty!";
      } else {
        errorMessage += "\nPassword is empty!";
      }
    }
    errormessageLabel.setText(errorMessage);
    return isFilled;
  }

  private boolean isValid() {
    boolean isValid;
    try {
      if (loginManager.login(tfUsername.getText(), pfPassword.getText())) {
        // logged in
        isValid = true;
      } else {
        isValid = false;
        if (errorMessage.isEmpty()) {
          errorMessage = "Invalid Password!";
        } else {
          errorMessage += "\nInvalid Password!";
        }
      }
    } catch (Exception e) {
      errorMessage = "Invalid Username or Password!";
      isValid = false;
    }
    errormessageLabel.setText(errorMessage);
    return isValid;
  }

  public void initialize() {

    loginManager = DataBaseRepository.getInstance();
    backIcon.setOnMouseClicked(event -> Navigation.navigate(WELCOME_PAGE));
    loginbutton.setOnMouseClicked(
        event -> {
          errorMessage = "";
          if (isfieldFilled() && isValid()) {
            Navigation.navigate(HOME);
          }
        });
  }
}
