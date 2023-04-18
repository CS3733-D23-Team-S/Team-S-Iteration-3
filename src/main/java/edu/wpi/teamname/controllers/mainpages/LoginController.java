package edu.wpi.teamname.controllers.mainpages;

import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
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
  @FXML MFXButton navigation;
  // @FXML MFXButton navigationbutton;
  @FXML MFXButton toSignage;
  @FXML Label errormessageLabel;

  @FXML MFXButton loginbutton;

  @FXML PasswordField pfPassword;

  @FXML private TextField tfUsername;

  public ActiveUser activeUser = ActiveUser.getInstance();
  // @FXML private Hyperlink newUser;

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
      }
      if (!isValid) {
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
    navigation.setOnMouseClicked(event -> Navigation.navigate(PATHFINDING));
    toSignage.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));

    loginManager = DataBaseRepository.getInstance();
    // newUser.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.NEW_USER));
    //    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.WELCOME_PAGE));

    backIcon.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
    loginbutton.setOnMouseClicked(
        event -> {
          errorMessage = "";
          if (isfieldFilled() && isValid()) {
            // ActiveUser.getInstance().setCurrentUser(
            Navigation.navigate(STAFF);
          }
        });
  }
}
