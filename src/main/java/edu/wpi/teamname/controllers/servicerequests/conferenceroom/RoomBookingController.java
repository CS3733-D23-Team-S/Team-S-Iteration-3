package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import edu.wpi.teamname.ServiceRequests.ConferenceRoom.*;
import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.navigation.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
  @FXML MFXButton backButton;
  @FXML TextFieldTableCell currentDateText;
  @FXML HBox conferenceRoomsHBox; // hbox containing all conference rooms and schedules
  @FXML CheckComboBox featureFilterComboBox;
  @FXML MFXDatePicker DateFilterPicker;
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

    addMeetingButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING_DETAILS));
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    DateFilterPicker.setOnMouseClicked(event -> filterDate(DateFilterPicker.getCurrentDate()));

    initializeRooms();
    initializeFeatureFilter();

    // add current requests to UI
    for (ConfRoomRequest i : roomRequestDAO.getAll()) {
      System.out.println("i has a value" + i);
      this.addToUI(i);
    }

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

    // filterByFeature((ArrayList<String>) c.getAddedSubList());

    // list of added items, list of items in each thing
    // create featureFilterString

    // for each item in roomList
    // if featureList.contains(a) && featureList.contains(b) && ...
    // show feature
    // else
    // hide feature

    // filterFeatures((ArrayList<String>) c.getAddedSubList());

    // for (int j=0; j<roomListVBoxes.size(); j++) {
    //  if (roomList.get(j).getFeatures().contains(i)) {

    //   }
  }
  // iterate through visible rooms and hide or show depending on feature list
  // for (int i=0; i<roomListVBoxes.size(); i++) {
  //  if (roomList.get(i).getFeatures().contains(c))
  // }

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
      String startTime,
      String endTime,
      String eventTitle,
      String eventDescription)
      throws SQLException {

    System.out.println("Adding new request");
    ConfRoomRequest newRequest =
        new ConfRoomRequest(
            LocalDate.now(),
            LocalTime.of(Integer.parseInt(startTime), 0, 0, 0),
            LocalTime.of(Integer.parseInt(endTime), 0, 0, 0),
            roomLocation,
            "TestReserve",
            eventTitle,
            eventDescription,
            "staff member");
    roomRequestDAO.add(newRequest); // TODO need this?
  }

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
    // TODO am i doing this right / where can i get a list for this so i can do it with a loop
    roomList.addAll(confRoomDAO.getAll());

    for (int i = 0; i < roomList.size(); i++) {

      // vbox
      VBox roomVBox = new VBox();
      roomVBox.setAlignment(Pos.valueOf("TOP_CENTER"));
      roomVBox.setPrefHeight(612.0);
      roomVBox.setPrefWidth(229.0);
      roomVBox.setId(roomList.get(i).getLocation().getLongName().replaceAll(" ", ""));
      roomListVBoxes.add(roomVBox);

      // text cell
      TextFieldTableCell textField = new TextFieldTableCell();
      textField.setAlignment(Pos.valueOf("CENTER"));
      textField.setTextAlignment(TextAlignment.valueOf("CENTER"));
      textField.setPrefSize(250.0, 80.0);
      textField.setMinWidth(200);
      textField.setMinHeight(80);
      textField.setWrapText(true);
      textField.setStyle("-fx-border-style: hidden solid hidden hidden;");
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

  // get vbox in conferenceRoomHBox by ID
  public VBox getVBoxById(String id) {
    for (int i = 0; i < roomListVBoxes.size(); i++) {
      if (roomListVBoxes.get(i).getId().equals(id)) {
        return roomListVBoxes.get(i);
      }
    }
    return null;
  }

  public void filterDate(LocalDate date) {
    clearUI();

    for (ConfRoomRequest i : roomRequestDAO.getAll()) {
      if (i.getOrderDate() == date) {
        this.addToUI(i);
        System.out.println("Added request " + i.getEventName());
      }
    }
  }

  // clear vboxes
  public void clearUI() {
    for (int i = 0; i < roomListVBoxes.size(); i++) {
      roomListVBoxes.get(i).getChildren().clear();
    }
  }

  /*
    public void filterByFeature(String feature) {
      for (int i = 0; i < roomListVBoxes.size(); i++) {
        if (roomList.get(i).getFeatures().contains(feature)) {
          roomListVBoxes.get(i).setVisible(true);
        }
      }
    }
  */
  public void addToUI(ConfRoomRequest roomRequest) {
    Group resGroup = new Group(); // create group

    Rectangle rect = new Rectangle(); // create rectangle
    rect.getStyleClass().add("room-request-rect");

    rect.setWidth(170);
    rect.setHeight(110);
    rect.setArcHeight(5);
    rect.setFill(Color.LIGHTBLUE);

    VBox eventVBox = new VBox();

    Text title = new Text(); // create text
    title.setText(roomRequest.getEventName());
    title.setFont(Font.font("Open Sans", 15));
    // title.setStyle("Bold");
    title.setFill(Color.BLACK);

    Text creator = new Text(); // create creator line
    creator.setText(roomRequest.getReservedBy());
    creator.setFont(Font.font("Open Sans", 12));
    creator.setFill(Color.BLACK);

    Text time = new Text();
    time.setText(roomRequest.getStartTime() + " - " + roomRequest.getEndTime());
    time.setFont(Font.font("Open Sans", 12));
    time.setFill(Color.BLACK);

    resGroup.getChildren().add(rect);

    eventVBox.setPadding(new Insets(10));
    eventVBox.getChildren().add(title);
    eventVBox.getChildren().add(creator);
    eventVBox.getChildren().add(time);

    resGroup.getChildren().add(eventVBox);
    System.out.println(
        "The room request is " + roomRequest + " The room name is " + roomRequest.getRoom());

    VBox currVBox = getVBoxById(roomRequest.getRoom().replaceAll(" ", ""));

    currVBox.getChildren().add(resGroup);
  }

  /*
  public void filterFeatures(ArrayList<String> featureList) {
    for (int i = 0; i < roomList.size(); i++) {
      for (int j = 0; j < featureList.size(); j++) {
        System.out.println("Added feature " + featureList.get.(j));
        if (roomList.get(i).getFeatures().containsAll(featureList)) {
          System.out.println("Room set to visible: " + roomList.get(i).getLocation().getLongName());
          roomListVBoxes.get(i).setVisible(true);
        } else {
          System.out.println("Room set to invisible: " + roomList.get(i).getLocation().getLongName());
          roomListVBoxes.get(i).setVisible(false);
        }
      }
    }
  }
  */
}
