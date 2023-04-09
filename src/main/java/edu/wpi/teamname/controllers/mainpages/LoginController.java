package edu.wpi.teamname.controllers.mainpages;

import static edu.wpi.teamname.navigation.Screen.HOME;

import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
  DataBaseRepository loginManager;
  @FXML private MFXButton backButton;

  @FXML private Label errormessageLabel;

  @FXML private MFXButton loginbutton;

  @FXML private PasswordField pfPassword;

  @FXML private TextField tfUsername;

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
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.WELCOME_PAGE));

    loginbutton.setOnMouseClicked(
        event -> {
          errorMessage = "";
          if (isfieldFilled() && isValid()) {
            Navigation.navigate(HOME);
          }
        });
  }
}
