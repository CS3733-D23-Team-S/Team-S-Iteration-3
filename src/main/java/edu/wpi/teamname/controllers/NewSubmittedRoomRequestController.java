package edu.wpi.teamname.controllers;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
    @FXML TableView<NewRoomRequest> roomRequestsTable;
    @FXML TableColumn<NewRoomRequest, String> roomBooked = new TableColumn<>("Room Booked");
    @FXML TableColumn<NewRoomRequest, String> timeBooked = new TableColumn<>("Time Booked");
    @FXML TableColumn<NewRoomRequest, String> maxRoomSize = new TableColumn<>("Max Room Size");

    @FXML
    TableColumn<NewRoomRequest, String> specialRoomRequests = new TableColumn<>("Special Requests");

    @FXML ImageView backIcon;
    @FXML ImageView exitIcon;
    @FXML ImageView helpIcon;
    @FXML ImageView homeIcon;
    @FXML MFXButton navigationButton;
    @FXML MFXButton signageButton;
    @FXML MFXButton mealButton;
    @FXML MFXButton roomButton;
    @FXML MFXButton flowerButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        backIcon.setOnMouseClicked(event -> goToAdminPage());
        exitIcon.setOnMouseClicked(event -> exitApplication());
        helpIcon.setOnMouseClicked(event -> goToHelpPage());
        homeIcon.setOnMouseClicked(event -> goToHomePage());
        navigationButton.setOnMouseClicked(event -> goToPathfindingPage());
        signageButton.setOnMouseClicked(event -> goToSignagePage());
        mealButton.setOnMouseClicked(event -> goToMealDeliveryPage());
        roomButton.setOnMouseClicked(event -> goToRoomReservationPage());


        List<NewRoomRequest> roomRequests = new LinkedList<>();
        roomRequests.add(new NewRoomRequest("1", "noon", "5", "None"));
        roomRequests.add(new NewRoomRequest("2", "noon", "5", "None"));
        roomRequests.add(new NewRoomRequest("3", "noon", "5", "None"));
        roomRequests.add(new NewRoomRequest("4", "noon", "5", "None"));
        roomRequests.add(new NewRoomRequest("5", "noon", "5", "None"));


        roomBooked.setCellValueFactory((row) -> new
                SimpleStringProperty(row.getValue().getRoomBooked()));
        timeBooked.setCellValueFactory((row) -> new
     SimpleStringProperty(row.getValue().getTimeBooked()));
        maxRoomSize.setCellValueFactory(
                (row) -> new SimpleStringProperty(row.getValue().getMaxRoomSize()));
        specialRoomRequests.setCellValueFactory(
                (row) -> new SimpleStringProperty(row.getValue().getSpecialRoomRequests()));

        final ObservableList<NewRoomRequest> observableRoomList =
                FXCollections.observableList(roomRequests);
        // mealRequestsTable.setItems(observableMealList);
        roomRequestsTable.getItems().addAll(observableRoomList);

        //    for (RoomRequest Rr : roomRequests) {
        //      roomRequestsTable.getItems().add(Rr);
        //    }
    }
    public void goToAdminPage() {
        Navigation.navigate(Screen.ADMIN_PAGE);
    }

    public void goToHomePage() {
        Navigation.navigate(Screen.HOME);
    }

    public void goToSignagePage() {
        Navigation.navigate(Screen.SIGNAGE_PAGE);
    }
    public void goToPathfindingPage() {
        Navigation.navigate(Screen.PATHFINDING);
    }
    public void goToMealDeliveryPage() {
        Navigation.navigate(Screen.MEAL_DELIVERY1);
    }
    public void goToRoomReservationPage() {
        Navigation.navigate(Screen.ROOM_BOOKING);
    }
    public void exitApplication() {
        Platform.exit();
    }
    public void goToHelpPage() {
        Navigation.navigate(Screen.HELP_PAGE);
    }
}
