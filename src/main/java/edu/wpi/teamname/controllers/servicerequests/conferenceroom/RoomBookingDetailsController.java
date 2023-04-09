package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import edu.wpi.teamname.Map.Location;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.Getter;
import lombok.Setter;

public class RoomBookingDetailsController {

  @FXML MFXButton submitDetailsButton;
  @FXML MFXTextField startTimeText;
  @FXML MFXTextField endTimeText;
  @FXML MFXTextField eventTitleText;
  @FXML MFXTextField eventDescriptionText;
  @FXML MFXComboBox roomComboBox;
  @FXML MFXButton backButton;
  @FXML MFXButton clearButton;

  @Setter @Getter String roomLocation;
  @Setter @Getter String startTime;
  @Setter @Getter String endTime;
  @Setter @Getter String eventTitle;
  @Setter @Getter String eventDescription;

  RoomBookingController rbc = new RoomBookingController();

  ArrayList<Location> rbcRoomList = rbc.roomList;

  @FXML
  public void initialize() {
    submitDetailsButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    clearButton.setOnMouseClicked(event -> clearFields());

    initializeRoomComboBox();
  }

  public void initializeRoomComboBox() {
    roomComboBox.getItems().add("BTM Conference Center");
    roomComboBox.getItems().add("Duncan Reid Conference Room");
    roomComboBox.getItems().add("Anesthesia Conf Floor L1");
    roomComboBox.getItems().add("Medical Records Conference Room Floor L1");
    roomComboBox.getItems().add("Abrams Conference Room");
    roomComboBox.getItems().add("Carrie M. Hall Conference Center Floor 2");
    roomComboBox.getItems().add("Shapiro Board Room MapNode 20 Floor 1");
  }

  @FXML
  public void submitDetails(ActionEvent event) throws SQLException {
    roomLocation = roomComboBox.getValue().toString();
    startTime = startTimeText.getText();
    endTime = endTimeText.getText();
    eventTitle = eventTitleText.getText();
    eventDescription = eventDescriptionText.getText();
    System.out.println("Took in inputs from RBD Controller");
    rbc.addNewRequest(roomLocation, startTime, endTime, eventTitle, eventDescription);
    // rbc.addToUI(roomLocation, startTime, endTime, eventTitle, eventDescription, staffMember);
    clearFields();
  }

  public void clearFields() {
    startTimeText.clear();
    endTimeText.clear();
    eventTitleText.clear();
    eventDescriptionText.clear();
  }
}
