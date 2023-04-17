package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import edu.wpi.teamname.DAOs.DataBaseRepository;
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
import javafx.scene.image.ImageView;
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
  @FXML ImageView homeicon1;
  @FXML ImageView homeicon;
  @FXML ImageView backicon;
  @FXML ImageView backicon1;
  @FXML ImageView helpicon;

  RoomBookingController rbc = new RoomBookingController();

  ArrayList<ConfRoomLocation> rbcRoomList = rbc.roomList;

  @FXML
  public void initialize() {
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
    staffMemberComboBox.getItems().add("Anthony Titcombe");
    staffMemberComboBox.getItems().add("Ryan Wright");
    staffMemberComboBox.getItems().add("Nick Ho");
    staffMemberComboBox.getItems().add("Jake Olsen");
    staffMemberComboBox.getItems().add("Nikesh Walling");
    staffMemberComboBox.getItems().add("Kashvi Singh");
    staffMemberComboBox.getItems().add("Sarah Kogan");
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
    System.out.println("Took in inputs from RBD Controller");
    rbc.addNewRequest(roomLocation, eventDate, startTime, endTime, eventTitle, eventDescription);
    stage.close();
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
