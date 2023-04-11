package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.IDAO;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SubmittedRoomRequestsController {

  @FXML TableView submittedRoomRequestsTable;
  @FXML MFXButton backButton;

  IDAO<ConfRoomRequest, String> repo = DataBaseRepository.getInstance().getRoomRequestDAO();

  public void initialize() {

    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));

    TableColumn<ConfRoomRequest, String> column1 = new TableColumn("Order Date");
    column1.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

    TableColumn<ConfRoomRequest, String> column2 = new TableColumn("Event Date");
    column2.setCellValueFactory(new PropertyValueFactory<>("eventDate"));

    TableColumn<ConfRoomRequest, String> column3 = new TableColumn("Start Time");
    column3.setCellValueFactory(new PropertyValueFactory<>("startTime"));

    TableColumn<ConfRoomRequest, String> column4 = new TableColumn("End Time");
    column4.setCellValueFactory(new PropertyValueFactory<>("endTime"));

    TableColumn<ConfRoomRequest, String> column5 = new TableColumn("StartTime");
    column5.setCellValueFactory(new PropertyValueFactory<>("startTime"));

    TableColumn<ConfRoomRequest, String> column6 = new TableColumn("Room");
    column6.setCellValueFactory(new PropertyValueFactory<>("room"));

    TableColumn<ConfRoomRequest, String> column7 = new TableColumn("Reserved By");
    column7.setCellValueFactory(new PropertyValueFactory<>("reservedBy"));

    TableColumn<ConfRoomRequest, String> column8 = new TableColumn("Event Name");
    column8.setCellValueFactory(new PropertyValueFactory<>("eventName"));

    TableColumn<ConfRoomRequest, String> column9 = new TableColumn("Event Description");
    column9.setCellValueFactory(new PropertyValueFactory<>("eventDescription"));

    TableColumn<ConfRoomRequest, String> column10 = new TableColumn("Assigned To");
    column10.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

    TableColumn<ConfRoomRequest, String> column11 = new TableColumn("Order Status");
    column11.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

    TableColumn<ConfRoomRequest, String> column12 = new TableColumn("Notes");
    column12.setCellValueFactory(new PropertyValueFactory<>("notes"));

    submittedRoomRequestsTable.getColumns().add(column1);
    submittedRoomRequestsTable.getColumns().add(column2);
    submittedRoomRequestsTable.getColumns().add(column3);
    submittedRoomRequestsTable.getColumns().add(column4);
    submittedRoomRequestsTable.getColumns().add(column5);
    submittedRoomRequestsTable.getColumns().add(column6);
    submittedRoomRequestsTable.getColumns().add(column7);
    submittedRoomRequestsTable.getColumns().add(column8);
    submittedRoomRequestsTable.getColumns().add(column9);
    submittedRoomRequestsTable.getColumns().add(column10);
    submittedRoomRequestsTable.getColumns().add(column11);
    submittedRoomRequestsTable.getColumns().add(column12);

    for (ConfRoomRequest request : repo.getAll()) {
      submittedRoomRequestsTable.getItems().add(request);
    }
  }
}
