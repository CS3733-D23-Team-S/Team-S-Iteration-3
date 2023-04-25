package edu.wpi.teamname.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;

class RoomRequest {
  @Getter private final String roomBooked;
  @Getter private final String timeBooked;

  RoomRequest(String roomBooked, String timeBooked) {
    this.roomBooked = roomBooked;
    this.timeBooked = timeBooked;
  }
}

public class SubmittedRoomRequestController implements Initializable {
  @FXML TableView roomRequestsTable;
  @FXML TableColumn<MealRequest, String> roomBooked = new TableColumn<>("Room Booked");
  @FXML TableColumn<MealRequest, String> timeBooked = new TableColumn<>("Time Booked");

  @FXML MFXButton backButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    List<RoomRequest> roomRequests = new LinkedList<>();
    roomRequests.add(new RoomRequest("1", "noon"));

    roomBooked.setCellValueFactory(new PropertyValueFactory<>("mealID"));
    timeBooked.setCellValueFactory(new PropertyValueFactory<>("orderTime"));

    for (RoomRequest Rr : roomRequests) {
      roomRequestsTable.getItems().add(Rr);
    }
  }
}
