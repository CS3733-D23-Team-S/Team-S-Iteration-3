package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomLocation;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

public class RoomBookingDetailsController {

  @FXML MFXButton submitDetailsButton;
  @FXML MFXTextField eventTitleText;
  @FXML MFXTextField eventDescriptionText;
  @FXML MFXComboBox roomComboBox;
  @FXML MFXButton backButton;
  @FXML MFXButton clearButton;
  @FXML MFXDatePicker roomBookingDate;
  @FXML MFXTextField startTimeField;
  @FXML MFXTextField endTimeField;
  @FXML Pane startTimePane;

  @Setter @Getter String roomLocation;
  @Setter @Getter LocalDate eventDate;
  @Setter @Getter String startTime;
  @Setter @Getter String endTime;
  @Setter @Getter String eventTitle;
  @Setter @Getter String eventDescription;

  RoomBookingController rbc = new RoomBookingController();

  ArrayList<ConfRoomLocation> rbcRoomList = rbc.roomList;

  @FXML
  public void initialize() throws ParseException {
    submitDetailsButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    clearButton.setOnMouseClicked(event -> clearFields());

    initializeRoomComboBox();
  }

  // hard code items into room combo box (TODO read from list later)
  public void initializeRoomComboBox() {
    for (String location :
        DataBaseRepository.getInstance().getConfRoomDAO().getLocationsAlphabetically())
      roomComboBox.getItems().add(location);
  }

  // submit details from controller
  @FXML
  public void submitDetails(ActionEvent event) throws SQLException {
    roomLocation = roomComboBox.getValue().toString().replaceAll(" ", "");
    eventDate = roomBookingDate.getValue();
    startTime = startTimeField.getText();
    endTime = endTimeField.getText();
    eventTitle = eventTitleText.getText();
    eventDescription = eventDescriptionText.getText();
    System.out.println("Took in inputs from RBD Controller");
    rbc.addNewRequest(roomLocation, eventDate, startTime, endTime, eventTitle, eventDescription);

    // clearFields();
  }

  // clear text fields
  public void clearFields() {
    roomComboBox.setValue("");
    // startTimeText.clear();
    // endTimeText.clear();
    eventTitleText.clear();
    eventDescriptionText.clear();
  }
}
