package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.orms.User;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.time.LocalTime;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.tableview2.cell.TextField2TableCell;

public class UserProfileController {

  @FXML ImageView profileImage;
  @FXML MFXButton saveButton;
  @FXML MFXButton clearButton;
  @FXML MFXButton updatePassField;
  @FXML MFXButton changePhotoButton;
  @FXML MFXTextField nameField;
  @FXML MFXTextField positionField;
  @FXML MFXTextField emailField;
  @FXML MFXTextField passwordField;
  @FXML TextField2TableCell nameCell;
  @FXML TextField2TableCell permissionsCell;
  @FXML TextField2TableCell roleCell;
  @FXML TextField2TableCell emailCell;
  @FXML Label welcomeText;

  // TODO connect to database
  ActiveUser activeUser = ActiveUser.getInstance();

  User user =
      activeUser.getCurrentUser(); // new User("Michael O'Connor", "password", Permission.ADMIN);
  // String name = user.getUserName();
  String name = user.getFirstName() + " " + user.getLastName();
  String position = user.getTitle();
  String permissions = user.getPermission().toString();
  String email = user.getEmail();
  String password = user.getPassword();
  String timeString = "";

  @FXML
  public void initialize() {

    saveButton.setOnMouseClicked(event -> saveChanges());
    clearButton.setOnMouseClicked(event -> clearFields());
    updatePassField.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.USER_PROFILE_POPUP));

    nameCell.setText(name);
    permissionsCell.setText(permissions);
    roleCell.setText(position);
    emailCell.setText(email);

    int currentHour = LocalTime.now().getHour();
    if (currentHour >= 5 && currentHour <= 11) {
      timeString = "Good morning";
    } else if (currentHour > 11 && currentHour <= 17) {
      timeString = "Good afternoon";
    } else {
      timeString = "Good evening";
    }

    System.out.println(name);
    welcomeText.setText(timeString + ", " + name + "!");

    // ROUND IMAGE
    Rectangle circle = new Rectangle(300, 300);
    circle.setArcHeight(300);
    circle.setArcWidth(300);
    profileImage.setClip(circle);
  }

  public void saveChanges() {
    name = nameField.getText();
    position = positionField.getText();
    email = emailField.getText();
    password = passwordField.getText();

    clearFields();

    // TODO connect to database
    // TODO handle empty fields, invalid inputs, etc

    nameCell.setText(name);
    String permissionstring =
        permissions.substring(0, 1).toUpperCase() + permissions.substring(1).toLowerCase();
    permissionsCell.setText(permissionstring);

    roleCell.setText(position);
    emailCell.setText(email);
    welcomeText.setText(timeString + ", " + name + "!");
  }

  public void clearFields() {
    nameField.clear();
    positionField.clear();
    emailField.clear();
  }
}
