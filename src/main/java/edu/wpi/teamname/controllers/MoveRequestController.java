package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.control.SearchableComboBox;

public class MoveRequestController extends PopUpController {

  @FXML SearchableComboBox originalRoomComboBox;
  @FXML SearchableComboBox newRoomComboBox;
  @FXML MFXDatePicker moveDateBox;
  @FXML MFXButton submitButton;
  @FXML MFXButton clearButton;
  @FXML Label errorMessage;

  LocalDate moveDate;
  String startRoom;
  String destinationRoom;

  DataBaseRepository dbr = DataBaseRepository.getInstance();

  @FXML
  public void initialize() {

    submitButton.setOnMouseClicked(event -> submitDetails());
    clearButton.setOnMouseClicked(event -> clearFields());

    initializeRoomBoxes();
  }

  public void submitDetails() {

    moveDate = moveDateBox.getValue();
    startRoom = originalRoomComboBox.getValue().toString();
    destinationRoom = newRoomComboBox.getValue().toString();

    if (moveDate.equals(null)) {
      errorMessage.setText("Please enter a date for the move.");
      return;
    }

    if (startRoom.equals(null)) {
      errorMessage.setText("Please enter a starting room.");
      return;
    }

    if (destinationRoom.equals(null)) {
      errorMessage.setText("Please enter a destination room.");
      return;
    }

    // TODO add moverequest to database

    stage.close();
  }

  public void clearFields() {
    originalRoomComboBox.setValue(null);
    newRoomComboBox.setValue(null);
    moveDateBox.clear();
  }

  public void initializeRoomBoxes() {
    for (String room : dbr.getListOfEligibleRooms()) {
      originalRoomComboBox.getItems().add(room);
      newRoomComboBox.getItems().add(room);
    }
  }
}
