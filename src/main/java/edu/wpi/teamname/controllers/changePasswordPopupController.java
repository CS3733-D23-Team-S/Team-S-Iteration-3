package edu.wpi.teamname.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javax.swing.*;

public class changePasswordPopupController extends PopUpController {

  @FXML MFXPasswordField currentPassField;
  @FXML MFXPasswordField newPassField;
  @FXML MFXPasswordField confirmPassField;
  @FXML MFXButton saveButton;
  @FXML MFXButton clearButton;
  @FXML Label errorMessage;

  String currPass = "", newPass = "", confirmPass = "", password = "";

  @FXML
  public void initialize() {
    errorMessage.setText("");
    saveButton.setOnMouseClicked(event -> submitNewPassword());
    clearButton.setOnMouseClicked(event -> clearFields());
  }

  public void submitNewPassword() {

    currentPassField.setStyle("-fx-border-color: #FFFFFF00");
    currentPassField.setStyle("-fx-border-color: #FFFFFF00");
    currentPassField.setStyle("-fx-border-color: #FFFFFF00");

    currPass = currentPassField.getText();
    newPass = newPassField.getText();
    confirmPass = confirmPassField.getText();
    errorMessage.setText("");

    // check for null
    if (currPass.equals("")) {
      currentPassField.setStyle("-fx-border-color: RED");
      errorMessage.setText("Error: Please enter your current password.");
      return;
    }

    if (newPass.equals("")) {
      newPassField.setStyle("-fx-border-color: RED");
      errorMessage.setText("Error: Please enter a new password.");
      return;
    }

    if (confirmPass.equals("")) {
      confirmPassField.setStyle("-fx-border-color: RED");
      errorMessage.setText("Error: Please confirm your password.");
      return;
    }

    // TODO: If currPass != userPass: Current password is incorrect.

    if (newPass.equals(currPass)) {
      newPassField.setStyle("-fx-border-color: RED");
      currentPassField.setStyle("-fx-border-color: RED");
      errorMessage.setText("Error: The new password is the same as the previous one.");
      return;
    }

    if (!newPass.equals(confirmPass)) {
      confirmPassField.setStyle("-fx-border-color: RED");
      errorMessage.setText("Error: Your password confirmation does not match.");
      return;
    }

    this.password = newPass;
    stage.close();
  }

  public void clearFields() {
    currentPassField.clear();
    newPassField.clear();
  }
}
