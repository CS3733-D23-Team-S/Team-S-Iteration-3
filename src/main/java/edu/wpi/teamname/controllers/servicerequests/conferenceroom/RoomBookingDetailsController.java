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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
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

  @Setter @Getter String roomLocation;
  @Setter @Getter LocalDate eventDate;
  @Setter @Getter LocalTime startTime;
  @Setter @Getter LocalTime endTime;
  @Setter @Getter String eventTitle;
  @Setter @Getter String eventDescription;
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
                      || roomBookingDate.valueProperty().toString().length() == 0
                      || staffMemberComboBox.getValue() == null);
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
                      || roomBookingDate.valueProperty().toString().length() == 0
                      || staffMemberComboBox.getValue() == null);
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
                      || endTimeField.valueProperty().toString().length() == 0
                      || staffMemberComboBox.getValue() == null);
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
                      || endTimeField.valueProperty().toString().length() == 0
                      || staffMemberComboBox.getValue() == null);
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
                      || endTimeField.valueProperty().toString().length() == 0
                      || staffMemberComboBox.getValue() == null);
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
    boolean isPrivate = false;
    roomLocation = roomComboBox.getValue().toString().replaceAll(" ", "");
    eventDate = roomBookingDate.getValue();
    startTime = (LocalTime) startTimeField.getValue();
    endTime = (LocalTime) endTimeField.getValue();
    eventTitle = eventTitleText.getText();
    eventDescription = eventDescriptionText.getText();
    System.out.println("Took in inputs from RBD Controller");
    try {
      rbc.addNewRequest(
          roomLocation, eventDate, startTime, endTime, eventTitle, eventDescription, isPrivate);
      stage.close();
    } catch (Exception e) {
      Alert errorAlert = new Alert(Alert.AlertType.ERROR);
      errorAlert.setTitle("Time Dialog");
      errorAlert.setContentText("There's scheduling clashes");

      DialogPane dialogPane = errorAlert.getDialogPane();
      dialogPane.setStyle("-fx-background-color: #000000");
      // dialogPane.getStylesheets().add("myDialogs.css");
      // dialogPane.getStyleClass().add("myDialog");
      errorAlert.showAndWait();
    }

    // clearFields();
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
  }
}
