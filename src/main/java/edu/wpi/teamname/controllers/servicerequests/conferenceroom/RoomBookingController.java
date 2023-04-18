package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.*;
import edu.wpi.teamname.navigation.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.control.CheckComboBox;

// TODO take out filter by location
// TODO add in 15% vbox

public class RoomBookingController {

  // FXML
  @FXML MFXButton addMeetingButton;
  @FXML TextFieldTableCell dateHeaderTextField;
  @FXML TextFieldTableCell currentDateText;
  @FXML HBox conferenceRoomsHBox; // hbox containing all conference rooms and schedules
  @FXML CheckComboBox featureFilterComboBox;
  @FXML MFXDatePicker DateFilterPicker;
  @FXML ScrollPane scrollPane;

  public static RoomRequestDAO roomRequestDAO =
      DataBaseRepository.getInstance().getRoomRequestDAO();
  public ConfRoomDAO confRoomDAO = DataBaseRepository.getInstance().getConfRoomDAO();
  // arraylists
  @Getter @Setter ArrayList<ConfRoomLocation> roomList = new ArrayList<>();
  ArrayList<VBox> roomListVBoxes = new ArrayList<>();
  ArrayList<ConfRoomRequest> reservationList = new ArrayList<>();

  RoomBooking rb = new RoomBooking();

  @FXML
  public void initialize() throws SQLException {

    scrollPane.setStyle("-fx-box-border: transparent;");

    // set navigation
    addMeetingButton.setOnMouseClicked(
        event -> Navigation.launchPopUp(Screen.ROOM_BOOKING_DETAILS));

    initializeRooms();
    initializeFeatureFilter();

    dateHeaderTextField.setText(
        LocalDate.now().format(DateTimeFormatter.ofPattern("EE, dd MMM yyyy")));
    // filterDate(LocalDate.now());

    // add current requests to UI
    for (ConfRoomRequest i : roomRequestDAO.getAll()) {
      this.addToUI(i);
    }

    filterDate(LocalDate.now());

    featureFilterComboBox
        .getCheckModel()
        .getCheckedItems()
        .addListener(
            new InvalidationListener() {
              @Override
              public void invalidated(Observable observable) {
                filterByFeature(featureFilterComboBox.getCheckModel().getCheckedItems());
                System.out.println(
                    "\n SELECTED ITEMS: "
                        + featureFilterComboBox.getCheckModel().getCheckedItems());
              }
            });

    DateFilterPicker.valueProperty()
        .addListener(
            new ChangeListener<LocalDate>() {
              @Override
              public void changed(
                  ObservableValue<? extends LocalDate> observable,
                  LocalDate oldValue,
                  LocalDate newValue) {
                filterDate(newValue);
                System.out.println("Filtering by new date: " + newValue);
              }
            });
  }

  /**
   * add a new request
   *
   * @param roomLocation longName for room location
   * @param startTime
   * @param endTime
   * @param eventTitle
   * @param eventDescription
   * @throws SQLException
   */
  public static void addNewRequest(
      String roomLocation,
      LocalDate date,
      LocalTime startTime,
      LocalTime endTime,
      String eventTitle,
      String eventDescription,
      boolean isPrivate)
      throws Exception {

    System.out.println("Adding new request");
    ConfRoomRequest newRequest =
        new ConfRoomRequest(
            date,
            startTime,
            endTime,
            roomLocation,
            ActiveUser.getInstance().getCurrentUser().getUserName(),
            eventTitle,
            eventDescription,
            "staff member",
            isPrivate);
    DataBaseRepository.getInstance().addRoomRequest(newRequest); // TODO need this?
  }

  /**
   * filter existing list by features; show only room vboxes with the required features
   *
   * @param features ObservableList<String> of features
   */
  public void filterByFeature(ObservableList<String> features) {
    for (int i = 0; i < roomList.size(); i++) {
      roomListVBoxes.get(i).setVisible(false);
      roomListVBoxes.get(i).managedProperty().bind(roomListVBoxes.get(i).visibleProperty());
    }
    System.out.println("\n\nFILTERING BY FEATURE!!!! FEATURES: ");
    System.out.println(features);

    if (features.isEmpty()) {
      System.out.println("Features empty!!!");
      for (int i = 0; i < roomList.size(); i++) {
        roomListVBoxes.get(i).setVisible(true);
      }
    }

    for (int i = 0; i < roomList.size(); i++) {
      for (int f = 0; f < features.size(); f++) {
        if (!(roomList.get(i).getFeatures().contains(features.get(f)))) {
          System.out.println(
              roomList.get(i).getLocation().getLongName() + " does not contain " + features.get(f));
          break;
        }
        roomListVBoxes.get(i).setVisible(true);
      }
    }
    System.out.println("Set things to visible");
  }

  // hard code ConfRoomLocation objects
  public void initializeRooms() {
    // hard coded in rooms
    roomList.addAll(confRoomDAO.getAll()); // TODO alphabetize

    for (int i = 0; i < roomList.size(); i++) {

      Rectangle meetingRect = new Rectangle();
      meetingRect.setWidth(170);
      meetingRect.setHeight(85);
      meetingRect.setArcHeight(15);
      meetingRect.setArcWidth(15);
      meetingRect.setStyle("-fx-border-color: #000000");
      meetingRect.setStyle("-fx-border-style: dashed");
      // rect.setStyle("-fx-background-radius: 15;");
      meetingRect.setFill(Paint.valueOf("#B5C5EE"));

      // vbox
      VBox roomVBox = new VBox();
      roomVBox.setAlignment(Pos.valueOf("TOP_CENTER"));
      roomVBox.setMinHeight(500);
      roomVBox.setPrefWidth(170);
      roomVBox.setMaxWidth(170);
      roomVBox.setSpacing(5);
      // roomVBox.setStyle("-fx-border-style: solid dashed double dotted");
      roomVBox.setStyle("-fx-border-color: #00000000 #D8D8D8 #FFFFFF00 #D8D8D8");
      // #D8D8D8
      roomVBox.setId(roomList.get(i).getLocation().getLongName().replaceAll(" ", ""));
      roomListVBoxes.add(roomVBox);

      // text cell
      TextFieldTableCell textField = new TextFieldTableCell();
      textField.setAlignment(Pos.valueOf("CENTER"));
      textField.setTextAlignment(TextAlignment.valueOf("CENTER"));
      textField.setPrefSize(160, 75);
      textField.setMinWidth(160);
      textField.setMaxWidth(140);
      textField.setMinHeight(75);
      textField.setMaxHeight(75);
      textField.setWrapText(true);

      // textField.setStyle("-fx-border-style: solid dashed double dotted;");
      textField.setStyle("-fx-border-color: #FFFFFF00 #FFFFFF00 #D8D8D8 #FFFFFF00");
      textField.setFont(Font.font("Open Sans"));
      textField.setFont(Font.font(16));
      textField.setPadding(new Insets(0, 5, 0, 5));
      textField.setText(roomList.get(i).getLocation().getLongName());
      textField.setTextFill(Paint.valueOf("#1d3d94"));

      // add pieces
      roomVBox.getChildren().add(textField);
      conferenceRoomsHBox.getChildren().add(roomVBox);
    }
  }

  // add items to feature filter
  public void initializeFeatureFilter() {
    featureFilterComboBox.getItems().addAll("Whiteboard", "DocCamera", "Projector");
  }

  /**
   * get vbox in conferenceRoomsHBox by its ID
   *
   * @param id longname of the room's location with spaces replaced
   * @return VBox in ConferenceRoomsHBox with the correct ID
   */
  public VBox getVBoxById(String id) {
    for (int i = 0; i < roomListVBoxes.size(); i++) {
      if (roomListVBoxes.get(i).getId().equals(id)) {
        return roomListVBoxes.get(i);
      }
    }
    return null;
  }

  /**
   * filter existing list by date; show room bookings for a certain day
   *
   * @param date
   */
  public void filterDate(LocalDate date) {
    clearUI();
    roomList.clear();
    roomListVBoxes.clear();

    initializeRooms();

    for (ConfRoomRequest i : roomRequestDAO.getAll()) {
      if (i.getEventDate().equals(date)) {
        System.out.println("Event date: " + i.getEventDate() + "     filterDate: " + date);
        this.addToUI(i);
        System.out.println("Added request " + i.getEventName());
      }
    }
  }

  /** clear VBoxes in the UI */
  public void clearUI() {
    for (int i = 0; i < roomListVBoxes.size(); i++) {
      roomListVBoxes.get(i).getChildren().clear();
    }
    conferenceRoomsHBox.getChildren().clear();
  }

  /**
   * Place each RoomRequest into a rectangle and add it to the proper HBox
   *
   * @param roomRequest
   */
  public void addToUI(ConfRoomRequest roomRequest) {

    Group resGroup = new Group(); // create group

    Rectangle rect = new Rectangle(); // create rectangle
    rect.getStyleClass().add("room-request-rect");

    rect.setWidth(150);
    rect.setHeight(85);
    rect.setArcHeight(15);
    rect.setArcWidth(15);
    // rect.setStyle("-fx-background-radius: 15;");
    rect.setFill(Paint.valueOf("#B5C5EE"));

    VBox eventVBox = new VBox();
    eventVBox.setSpacing(2);

    Label title = new Label(); // create text
    title.setText(roomRequest.getEventName());
    title.setFont(Font.font("Open Sans", FontWeight.BOLD, 13));
    title.setTextFill(Paint.valueOf("#1D3D94"));
    title.setWrapText(false);
    title.maxWidth(130);
    title.setEllipsisString("...");

    Text creator = new Text(); // create creator line
    creator.setText(roomRequest.getReservedBy());
    creator.setFont(Font.font("Open Sans", 13));
    creator.setFill(Paint.valueOf("#1D3D94"));

    Text time = new Text();
    time.setText(roomRequest.getStartTime() + " - " + roomRequest.getEndTime());
    time.setFont(Font.font("Open Sans", 13));
    time.setFill(Paint.valueOf("#1D3D9450"));

    resGroup.getChildren().add(rect);

    eventVBox.setPadding(new Insets(10));
    eventVBox.getChildren().add(title);
    eventVBox.getChildren().add(creator);
    eventVBox.getChildren().add(time);

    resGroup.getChildren().add(eventVBox);

    VBox currVBox = getVBoxById(roomRequest.getRoom().replaceAll(" ", ""));

    currVBox.getChildren().add(resGroup);
  }
}
