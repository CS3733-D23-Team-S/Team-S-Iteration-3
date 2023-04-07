package edu.wpi.teamname.navigation;

import static javafx.geometry.Pos.*;

import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.Room;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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

public class RoomBooking {

  // set lists
  @Getter @Setter ArrayList<Room> roomList = new ArrayList<>();
  @Getter @Setter ArrayList<ConfRoomRequest> roomRequestList = new ArrayList<>();

  @FXML HBox conferenceRoomsHBox;
  private TextFieldTableCell roomListing;

  // pull rooms into UI
  public void createRoomsUI(HBox conferenceRoomsHBox) {
    for (int i = 0; i < roomList.size(); i++) {
      // add vbox
      VBox vb = new VBox();
      // styling
      vb.setAlignment(TOP_CENTER);
      vb.setPrefSize(612, 229);

      // add text field - styling
      TextFieldTableCell roomListing = new TextFieldTableCell();
      roomListing.setText(roomList.get(i).getName());
      roomListing.setAlignment(CENTER);
      roomListing.setTextAlignment(TextAlignment.CENTER);
      roomListing.setPrefSize(247, 86);
      roomListing.setFont(Font.font("Open San s", 20));
      roomListing.setTextFill(Paint.valueOf("#1D3D94"));

      vb.setId("room" + roomList.get(i).getId());
      vb.getChildren().add(roomListing); // add textfield into vbox
      vb.setSpacing(10);
      conferenceRoomsHBox.getChildren().add(vb); // add vbox into conf hbox

      // add requests
      for (int j = 0; j < roomRequestList.size(); j++) {
        String requestRoomId = "room1"; // + roomRequestList.get(j).getRoomId();
        //  if (roomRequestList.get(j).getRoom() != null) {requestRoomId = "room" +
        // roomRequestList.get(j).getRoom().getId(); // .getRoomId();}
        if (requestRoomId.equals(vb.getId())) {
          createReservationsUI(vb);
          break;
        } else {
          System.out.println("Different ID: " + vb.getId());
        }
      }
    }
  }

  public void createReservationsUI(VBox roomVBox) {

    for (int i = 0; i < roomRequestList.size(); i++) {
      Group resGroup = new Group(); // create group

      Rectangle rect = new Rectangle(); // create rectangle
      rect.setWidth(170);
      rect.setHeight(110);
      rect.setArcHeight(5);
      rect.setFill(Color.LIGHTBLUE);

      VBox eventVBox = new VBox();

      Text title = new Text(); // create text
      System.out.println("Setting text: " + roomRequestList.get(i).getEventName());
      title.setText(roomRequestList.get(i).getEventName());
      title.setFont(Font.font("Open Sans", 15));
      title.setStyle("Bold");
      title.setFill(Color.BLACK);

      Text creator = new Text(); // create creator line
      System.out.println("Setting text: " + roomRequestList.get(i).getEventName());
      creator.setText(roomRequestList.get(i).getReservedBy());
      creator.setFont(Font.font("Open Sans", 12));
      creator.setFill(Color.BLACK);

      Text time = new Text();
      System.out.println("Setting text: " + roomRequestList.get(i).getEventName());
      time.setText(
          roomRequestList.get(i).getStartTime() + " - " + roomRequestList.get(i).getEndTime());
      time.setFont(Font.font("Open Sans", 12));
      time.setFill(Color.BLACK);

      resGroup.getChildren().add(rect);

      eventVBox.setPadding(new Insets(10));
      eventVBox.getChildren().add(title);
      eventVBox.getChildren().add(creator);
      eventVBox.getChildren().add(time);

      resGroup.getChildren().add(eventVBox);

      System.out.println("CHILDREN: ");
      System.out.println(roomVBox.getChildren().add(resGroup));
    }
    /*
    Rectangle rect = new Rectangle(); // create rectangle
    rect.setWidth(170);
    rect.setHeight(110);
    rect.setFill(Color.ALICEBLUE);
    rect.setArcHeight(5);

    Text title = new Text(); // create text
    title.setText(reservationList.get(i).eventName);
    title.setFont(Font.font("Open Sans", 15));
    title.setStyle("Bold");
    title.setLayoutX(5);
    title.setLayoutY(25);

    resGroup.getChildren().add(rect);
    resGroup.getChildren().add(title);
     */

    // VBox roomVBox = (VBox) conferenceRoomsHBox.lookup("room1");

    // roomVBox.getChildren().add(resGroup);

    /*
    for (int j = 0; j < conferenceRoomsHBox.getChildren().size(); j++) {
      if (conferenceRoomsHBox.getChildren().get(j).getId()
          == ("room" + reservationList.get(i).room().id())) {
        System.out.println("Working");
      }
    }

     */
  }
}
