package edu.wpi.teamname.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;

class NewRoomRequest {
    @Getter public String roomBooked;
    @Getter public String timeBooked;
    @Getter public String maxRoomSize;
    @Getter public String specialRoomRequests;

    NewRoomRequest(
            String roomBooked, String timeBooked, String maxRoomSize, String specialRoomRequests) {
        this.roomBooked = roomBooked;
        this.timeBooked = timeBooked;
        this.maxRoomSize = maxRoomSize;
        this.specialRoomRequests = specialRoomRequests;
    }
}

public class NewSubmittedRoomRequestController implements Initializable {
    @FXML TableView roomRequestsTable;
    @FXML TableColumn<MealRequest, String> roomBooked = new TableColumn<>("Room Booked");
    @FXML TableColumn<MealRequest, String> timeBooked = new TableColumn<>("Time Booked");
    @FXML TableColumn<MealRequest, String> maxRoomSize = new TableColumn<>("Max Room Size");

    @FXML
    TableColumn<MealRequest, String> specialRoomRequests = new TableColumn<>("Special Requests");

    @FXML MFXButton backButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<NewRoomRequest> roomRequests = new LinkedList<>();
        roomRequests.add(new NewRoomRequest("1", "noon", "5", "None"));
        roomRequests.add(new NewRoomRequest("2", "noon", "5", "None"));
        roomRequests.add(new NewRoomRequest("3", "noon", "5", "None"));
        roomRequests.add(new NewRoomRequest("4", "noon", "5", "None"));
        roomRequests.add(new NewRoomRequest("5", "noon", "5", "None"));

        roomBooked.setCellValueFactory(new PropertyValueFactory<>("mealID"));
        timeBooked.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        maxRoomSize.setCellValueFactory(new PropertyValueFactory<>("maxRoomSize"));
        specialRoomRequests.setCellValueFactory(new PropertyValueFactory<>("specialRoomRequests"));

//        roomBooked.setCellValueFactory((row) -> new
//                SimpleStringProperty(row.getValue().getRoomBooked()));
//        timeBooked.setCellValueFactory((row) -> new
//     SimpleStringProperty(row.getValue().getTimeBooked()));
//        maxRoomSize.setCellValueFactory(
//                (row) -> new SimpleStringProperty(row.getValue().getMaxRoomSize()));
//        specialRoomRequests.setCellValueFactory(
//                (row) -> new SimpleStringProperty(row.getValue().getSpecialRoomRequests()));

        final ObservableList<NewRoomRequest> observableRoomList =
                FXCollections.observableList(roomRequests);
        // mealRequestsTable.setItems(observableMealList);
        roomRequestsTable.getItems().addAll(observableRoomList);

        //    for (RoomRequest Rr : roomRequests) {
        //      roomRequestsTable.getItems().add(Rr);
        //    }
    }
}
