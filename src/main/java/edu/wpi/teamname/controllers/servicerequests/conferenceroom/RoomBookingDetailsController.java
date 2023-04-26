package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.UserDAOImpl;
import edu.wpi.teamname.DAOs.orms.User;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomLocation;
import edu.wpi.teamname.controllers.PopUpController;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.control.SearchableComboBox;

public class RoomBookingDetailsController extends PopUpController {

  @FXML MFXButton submitDetailsButton;
  @FXML MFXTextField eventTitleText;
  @FXML MFXTextField eventDescriptionText;
  @FXML SearchableComboBox roomComboBox;
  @FXML MFXButton clearButton;
  @FXML SearchableComboBox staffMemberComboBox;
  @FXML MFXDatePicker roomBookingDate;
  @FXML SearchableComboBox startTimeField;
  @FXML SearchableComboBox endTimeField;
  @FXML Label errorMessage;
  @FXML MFXToggleButton privateToggle;
  @Setter @Getter String roomLocation;
  @Setter @Getter LocalDate eventDate;
  @Setter @Getter LocalTime startTime;
  @Setter @Getter LocalTime endTime;
  @Setter @Getter String eventTitle;
  @Setter @Getter String eventDescription;
  @Setter @Getter boolean isPrivate;
  @FXML MFXButton navigationbutton;
  @FXML MFXButton navigationbutton1;
  @FXML MFXButton mealbutton;
  @FXML MFXButton roombutton;
  @FXML MFXButton flowerbutton;

  RoomBookingController rbc = new RoomBookingController();

  ArrayList<ConfRoomLocation> rbcRoomList = rbc.roomList;

  UserDAOImpl ud = DataBaseRepository.getInstance().getUserDAO();

  @FXML
  public void initialize() {

    submitDetailsButton.setDisable(true);

    eventDescriptionText
        .textProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitDetailsButton.setDisable(
                  eventTitleText.getText().trim().isEmpty()
                      || eventDescriptionText.getText().trim().isEmpty()
                      || roomComboBox.valueProperty().toString().length() == 0
                      || startTimeField.valueProperty().toString().length() == 0
                      || endTimeField.valueProperty().toString().length() == 0
                      || roomBookingDate.valueProperty().toString().length() == 0);
            }));

    roomBookingDate
        .textProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitDetailsButton.setDisable(
                  eventTitleText.getText().trim().isEmpty()
                      || eventDescriptionText.getText().trim().isEmpty()
                      || roomComboBox.valueProperty().toString().length() == 0
                      || startTimeField.valueProperty().toString().length() == 0
                      || endTimeField.valueProperty().toString().length() == 0
                      || roomBookingDate.valueProperty().toString().length() == 0);
            }));

    eventTitleText
        .textProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitDetailsButton.setDisable(
                  eventTitleText.getText().trim().isEmpty()
                      || eventDescriptionText.getText().trim().isEmpty()
                      || roomComboBox.getValue() == null
                      || startTimeField.valueProperty().toString().length() == 0
                      || roomBookingDate.valueProperty().toString().length() == 0
                      || endTimeField.valueProperty().toString().length() == 0);
            }));

    roomComboBox
        .valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitDetailsButton.setDisable(
                  eventTitleText.getText().trim().isEmpty()
                      || eventDescriptionText.getText().trim().isEmpty()
                      || roomComboBox.getValue() == null
                      || startTimeField.valueProperty().toString().length() == 0
                      || endTimeField.valueProperty().toString().length() == 0);
            }));

    staffMemberComboBox
        .valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitDetailsButton.setDisable(
                  eventTitleText.getText().trim().isEmpty()
                      || eventDescriptionText.getText().trim().isEmpty()
                      || roomComboBox.getValue() == null
                      || startTimeField.valueProperty().toString().length() == 0
                      || endTimeField.valueProperty().toString().length() == 0);
            }));

    submitDetailsButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    clearButton.setOnMouseClicked(event -> clearFields());
    initializeRoomComboBox();
    initializeTimeComboBoxes();
    initializeStaffList();
  }

  public void initializeRoomComboBox() {
    for (String location :
        DataBaseRepository.getInstance().getConfRoomDAO().getLocationsAlphabetically())
      roomComboBox.getItems().add(location);
  }

  public void initializeTimeComboBoxes() {
    LocalTime time = LocalTime.of(0, 0);
    for (int i = 0; i < 96; i++) {
      startTimeField.getItems().add(time);
      endTimeField.getItems().add(time);
      time = time.plusMinutes(15);
    }
  }

  public void initializeStaffList() {
    for (User u : ud.getListOfUsers().values()) {
      staffMemberComboBox.getItems().add(u.getFirstName() + " " + u.getLastName());
    }
  }

  // submit details from controller
  @FXML
  public void submitDetails(ActionEvent event) throws Exception {

    roomLocation = roomComboBox.getValue().toString().replaceAll(" ", "");
    eventDate = roomBookingDate.getValue();
    startTime = (LocalTime) startTimeField.getValue();
    endTime = (LocalTime) endTimeField.getValue();
    eventTitle = eventTitleText.getText();
    eventDescription = eventDescriptionText.getText();
    isPrivate = privateToggle.isSelected();

    System.out.println(isPrivate);

    if (!catchBookingErrors(roomLocation, eventDate, startTime, endTime)) {
      System.out.println("Room booking cancelled because of faulty inputs");
      return;
    }
    ;

    try {
      System.out.println("Is private in rb details controller: " + isPrivate);
      rbc.addNewRequest(
          roomLocation, eventDate, startTime, endTime, eventTitle, eventDescription, isPrivate);
      stage.close();
    } catch (Exception e) {
      errorMessage.setText(roomComboBox.getValue().toString() + " is not available at this time.");
      roomComboBox.setStyle("-fx-border-color: RED");
    }
  }

  public boolean catchBookingErrors(
      String roomLocation, LocalDate date, LocalTime startTime, LocalTime endTime) {

    if (startTime.isAfter(endTime)) {
      errorMessage.setText("Start time must be before end time.");
      startTimeField.setStyle("-fx-border-color: RED");
      endTimeField.setStyle("-fx-border-color: RED");
      return false;
    }

    if (startTime.equals(endTime)) {
      errorMessage.setText("Start time must be different from end time.");
      startTimeField.setStyle("-fx-border-color: RED");
      endTimeField.setStyle("-fx-border-color: RED");
      return false;
    }
    return true;
  }
  // clear text fields
  public void clearFields() {
    roomComboBox.setValue("");
    startTimeField.valueProperty().set(null);
    endTimeField.valueProperty().set(null);
    roomBookingDate.clear();
    eventTitleText.clear();
    eventDescriptionText.clear();
    staffMemberComboBox.valueProperty().set(null);
    eventDescriptionText.clear();
    roomComboBox.setStyle("-fx-border-color: NONE");
    startTimeField.setStyle("-fx-border-color: NONE");
    endTimeField.setStyle("-fx-border-color: NONE");
    errorMessage.setText(null);
  }
}
