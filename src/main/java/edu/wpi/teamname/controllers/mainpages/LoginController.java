package edu.wpi.teamname.controllers.mainpages;

import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.awt.*;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class LoginController {
  DataBaseRepository loginManager;
  @FXML ImageView backIcon;
  @FXML MFXButton navigationbutton;
  @FXML MFXButton signageButton1;
  @FXML Label errormessageLabel;

  @FXML MFXButton loginbutton;

  @FXML PasswordField pfPassword;

  @FXML private TextField tfUsername;
  @FXML private Hyperlink newUser;

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
      isValid = loginManager.login(tfUsername.getText(), pfPassword.getText());
      if (errorMessage.isEmpty() && !isValid) {
        errorMessage = "Invalid Password!";
      } else {
        errorMessage += "\nInvalid Password!";
      }
    } catch (Exception e) {
      errorMessage = "Invalid Username or Password!";
      isValid = false;
    }
    errormessageLabel.setText(errorMessage);
    return isValid;
  }

  public void initialize() {
    signageButton1.setOnMouseClicked(event -> Navigation.launchPopUp(SIGNAGE_PAGE));

    loginManager = DataBaseRepository.getInstance();
    newUser.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.NEW_USER));
    //    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.WELCOME_PAGE));

    backIcon.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
    loginbutton.setOnMouseClicked(
        event -> {
          errorMessage = "";
          if (isfieldFilled() && isValid()) {

            Navigation.navigate(HOME);
          }
        });
  }
}
