package edu.wpi.teamname.controllers;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.Getter;
import lombok.Setter;

public class RoomBookingDetailsController {

  @FXML MFXButton submitDetailsButton;
  @FXML MFXTextField roomLocationText;
  @FXML MFXTextField startTimeText;
  @FXML MFXTextField endTimeText;
  @FXML MFXTextField eventTitleText;
  @FXML MFXTextField eventDescriptionText;
  @FXML MFXTextField staffMemberText;
  @FXML MFXButton backButton;
  @FXML MFXButton clearButton;

  @Setter @Getter String roomLocation;
  @Setter @Getter String startTime;
  @Setter @Getter String endTime;
  @Setter @Getter String eventTitle;
  @Setter @Getter String eventDescription;
  @Setter @Getter String staffMember;

  RoomBookingController rbc = new RoomBookingController();

  @FXML
  public void initialize() {
    submitDetailsButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    clearButton.setOnMouseClicked(event -> clearFields());
  }

  @FXML
  public void submitDetails(ActionEvent event) throws SQLException {
    roomLocation = roomLocationText.getText();
    startTime = startTimeText.getText();
    endTime = endTimeText.getText();
    eventTitle = eventTitleText.getText();
    eventDescription = eventDescriptionText.getText();
    staffMember = staffMemberText.getText();
    System.out.println("Took in inputs from RBD Controller");
    rbc.addNewRequest(roomLocation, startTime, endTime, eventTitle, eventDescription, staffMember);
    // rbc.addToUI(roomLocation, startTime, endTime, eventTitle, eventDescription, staffMember);
    clearFields();
  }

  public void clearFields() {
    roomLocationText.clear();
    startTimeText.clear();
    endTimeText.clear();
    eventTitleText.clear();
    eventDescriptionText.clear();
  }
}
