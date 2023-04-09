package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import static edu.wpi.teamname.Map.NodeType.CONF;

import edu.wpi.teamname.Map.*;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.*;
import edu.wpi.teamname.navigation.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

public class RoomBookingController {

  @FXML MFXButton addMeetingButton;
  @FXML MFXButton backButton;
  @FXML HBox conferenceRoomsHBox; // hbox containing all conference rooms and schedules

  RoomBooking rb = new RoomBooking();
  static RoomRequestDAO roomRequestDAO = RoomRequestDAO.getInstance();

  @Getter @Setter ArrayList<Location> roomList = new ArrayList<>();
  ArrayList<VBox> roomListVBoxes = new ArrayList<>();
  ArrayList<ConfRoomRequest> reservationList = new ArrayList<>();

  @FXML
  public void initialize() throws SQLException {

    addMeetingButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING_DETAILS));
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));

    initializeRooms();

    for (ConfRoomRequest i : RoomRequestDAO.getInstance().getAllRequests()) {
      //  public void addToUI(String roomLocation, String startTime, String endTime, String
      // eventTitle, String eventDescription, String staffMember) {
      this.addToUI(
          i.getRoomId(),
          String.valueOf(i.getStartTime()),
          String.valueOf(i.getEndTime().getHour()),
          i.getEventName(),
          i.getEventDescription(),
          i.getAssignedTo());
    }
  }

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
            new Room(Integer.parseInt(roomLocation), "Cafe", "Floor", 50, "F"),
            "TestReserve",
            eventTitle,
            eventDescription,
            "staff member",
            Status.Received,
            "No notes");
    roomRequestDAO.addRequest(newRequest); // TODO need this?
  }

  public void initializeRooms() {
    // TODO am i doing this right / where can i get a list for this so i can do it with a loop

    roomList.add(new Location("BTM Conference Center", "BTM Conference", CONF));
    roomList.add(new Location("Duncan Reid Conference Room", "Conf B0102", CONF));
    roomList.add(new Location("Anesthesia Conf Floor L1", "Conf C001L1", CONF));
    roomList.add(new Location("Medical Records Conference Room Floor L1", "Conf C002L1", CONF));
    roomList.add(new Location("Abrams Conference Room", "Conf C003L1", CONF));
    roomList.add(
        new Location("Carrie M. Hall Conference Center Floor 2", "Conference Center", CONF));
    roomList.add(new Location("Shapiro Board Room MapNode 20 Floor 1", "Shapiro Board Room", CONF));

    for (int i = 0; i < roomList.size(); i++) {

      // vbox
      VBox roomVBox = new VBox();
      roomVBox.setAlignment(Pos.valueOf("TOP_CENTER"));
      roomVBox.setPrefHeight(612.0);
      roomVBox.setPrefWidth(229.0);
      roomVBox.setId("room" + i);
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
      textField.setText(roomList.get(i).getLongName());
      textField.setTextFill(Paint.valueOf("#1d3d94"));

      // add pieces
      roomVBox.getChildren().add(textField);
      conferenceRoomsHBox.getChildren().add(roomVBox);
    }
  }

  public VBox getVBoxById(String id) {
    for (int i = 0; i < roomListVBoxes.size(); i++) {
      if (roomListVBoxes.get(i).getId().equals("room" + id)) {
        return roomListVBoxes.get(i);
      }
    }
    return null;
  }

  public void addToUI(
      String roomLocation,
      String startTime,
      String endTime,
      String eventTitle,
      String eventDescription,
      String staffMember) {
    System.out.println("Adding string " + roomLocation + " to UI");

    Group resGroup = new Group(); // create group

    Rectangle rect = new Rectangle(); // create rectangle
    rect.getStyleClass().add("room-request-rect");

    rect.setWidth(170);
    rect.setHeight(110);
    rect.setArcHeight(5);
    rect.setFill(Color.LIGHTBLUE);

    VBox eventVBox = new VBox();

    Text title = new Text(); // create text
    title.setText(eventTitle);
    title.setFont(Font.font("Open Sans", 15));
    // title.setStyle("Bold");
    title.setFill(Color.BLACK);

    Text creator = new Text(); // create creator line
    creator.setText(staffMember);
    creator.setFont(Font.font("Open Sans", 12));
    creator.setFill(Color.BLACK);

    Text time = new Text();
    time.setText(startTime + " - " + endTime);
    time.setFont(Font.font("Open Sans", 12));
    time.setFill(Color.BLACK);

    resGroup.getChildren().add(rect);

    eventVBox.setPadding(new Insets(10));
    eventVBox.getChildren().add(title);
    eventVBox.getChildren().add(creator);
    eventVBox.getChildren().add(time);

    resGroup.getChildren().add(eventVBox);

    VBox currVBox = getVBoxById(roomLocation);

    currVBox.getChildren().add(resGroup);
  }
}
