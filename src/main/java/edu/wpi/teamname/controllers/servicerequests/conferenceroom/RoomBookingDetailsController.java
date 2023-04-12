package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomLocation;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
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
  @FXML DatePicker roomBookingDate;
  @FXML MFXTextField startTimeField;
  @FXML MFXTextField endTimeField;
  @FXML Pane startTimePane;

  @Setter @Getter String roomLocation;
  @Setter @Getter LocalDate eventDate;
  @Setter @Getter String startTime;
  @Setter @Getter String endTime;
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
  public void initialize() throws ParseException {
    submitDetailsButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    clearButton.setOnMouseClicked(event -> clearFields());
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));

    navigationbutton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    navigationbutton1.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_PAGE));
    mealbutton.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
    roombutton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));
    flowerbutton.setOnMouseClicked(event -> Navigation.navigate(Screen.FLOWER_DELIVERY));

    homeicon.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.HOME);
          event.consume();
        });
    backicon.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.HOME);
          event.consume();
        });
    backicon1.addEventHandler(
        javafx.scene.input.MouseEvent.MOUSE_CLICKED,
        event -> {
          Navigation.navigate(Screen.SIGNAGE_PAGE);
          event.consume();
        });
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
  public void submitDetails(ActionEvent event) throws Exception {
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
    startTimeField.clear();
    endTimeField.clear();
    eventTitleText.clear();
    eventDescriptionText.clear();
  }
}
