package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import edu.wpi.teamname.navigation.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class RoomBookingController {

  @FXML MFXButton addMeetingButton;
  @FXML MFXButton backButton;
  @FXML HBox conferenceRoomsHBox; // hbox containing all conference rooms and schedules

  RoomBooking rb = new RoomBooking();
  static RoomRequestDAO roomRequestDAO = RoomRequestDAO.getInstance();

  @FXML
  public void initialize() throws SQLException {

    addMeetingButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING_DETAILS));
    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));

    for (ConfRoomRequest i : RoomRequestDAO.getInstance().getAllRequests()) {
      //  public void addToUI(String roomLocation, String startTime, String endTime, String
      // eventTitle, String eventDescription, String staffMember) {
      this.addToUI(
          String.valueOf(i.getStartTime()),
          String.valueOf(i.getEndTime().getHour()),
          i.getEventName(),
          i.getEventDescription(),
          i.getAssignedTo());
    }
    // create dummy rooms
    // create dummy reservations
    //    rb.setRoomList(roomList); // later -- read from DB
    //    rb.setRoomRequestList(reservationList)

    // read room requests from DB

    //    rb.createRoomsUI(conferenceRoomsHBox);

  }

  public static void addNewRequest(
      String roomLocation,
      String startTime,
      String endTime,
      String eventTitle,
      String eventDescription,
      String staffMember)
      throws SQLException {

    System.out.println("Adding new request");
    ConfRoomRequest newRequest =
        new ConfRoomRequest(
            LocalDate.now(),
            LocalTime.of(Integer.parseInt(startTime), 0, 0, 0),
            LocalTime.of(Integer.parseInt(endTime), 0, 0, 0),
            roomLocation,
            eventTitle,
            eventDescription,
            staffMember);
    roomRequestDAO.addRequest(newRequest); // TODO need this?
  }

  public void addToUI(
      String roomLocation,
      String startTime,
      String endTime,
      String eventTitle,
      String staffMember) {
    System.out.println("Adding string " + roomLocation + " to UI");

    Group resGroup = new Group(); // create group

    Rectangle rect = new Rectangle(); // create rectangle
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

    conferenceRoomsHBox.getChildren().add(resGroup);
  }

  /*
  public void createDummyRoomRequests() {
    ConfRoomRequest res1 =
        new ConfRoomRequest(
            LocalDate.now(),
            LocalTime.of(6, 0, 0, 0),
            LocalTime.of(8, 0, 0, 0),
            r1,
            "Sarah Kogan",
            "Checking for update",
            "description description description",
            null,
            Status.Received,
            "");
    ConfRoomRequest res2 =
        new ConfRoomRequest(
            LocalDate.now(),
            LocalTime.of(8, 0, 0, 0),
            LocalTime.of(10, 0, 0, 0),
            r1,
            "Jimmy Buffett",
            "Meeting Test 2",
            "description 2",
            null,
            Status.Received,
            "");
    ConfRoomRequest res3 =
        new ConfRoomRequest(
            LocalDate.now(),
            LocalTime.of(12, 0, 0, 0),
            LocalTime.of(4, 0, 0, 0),
            r2,
            "Christine Dion",
            "Conference Rooooooom",
            "description description description",
            null,
            Status.InProgress,
            "");
    roomRequestDAO.addRequest(res1);
    //        roomRequestDAO.addRequest(res2);
    //        roomRequestDAO.addRequest(res3);

    reservationList.add(res1);
    reservationList.add(res2);
    reservationList.add(res3);
  }

     */
}
