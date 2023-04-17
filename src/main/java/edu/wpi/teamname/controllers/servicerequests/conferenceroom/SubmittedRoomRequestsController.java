package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import edu.wpi.teamname.controllers.PopUpController;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class SubmittedRoomRequestsController extends PopUpController {

  @FXML TableView submittedRoomRequestsTable;
  @FXML MFXButton backButton;

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

  RoomRequestDAO repo = DataBaseRepository.getInstance().getRoomRequestDAO();

  public void initialize() {
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

    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));

    TableColumn<ConfRoomRequest, String> column1 = new TableColumn<>("Date Ordered");
    column1.setCellValueFactory(new PropertyValueFactory<>("dateOrdered"));

    TableColumn<ConfRoomRequest, String> column2 = new TableColumn<>("Event Date");
    column2.setCellValueFactory(new PropertyValueFactory<>("eventDate"));

    TableColumn<ConfRoomRequest, String> column3 = new TableColumn<>("Start Time");
    column3.setCellValueFactory(new PropertyValueFactory<>("startTime"));

    TableColumn<ConfRoomRequest, String> column4 = new TableColumn<>("End Time");
    column4.setCellValueFactory(new PropertyValueFactory<>("EndTime"));

    TableColumn<ConfRoomRequest, String> column5 = new TableColumn<>("Room");
    column5.setCellValueFactory(new PropertyValueFactory<>("room"));

    TableColumn<ConfRoomRequest, String> column6 = new TableColumn<>("Reserved By");
    column6.setCellValueFactory(new PropertyValueFactory<>("reservedBy"));

    TableColumn<ConfRoomRequest, String> column7 = new TableColumn<>("Event Name");
    column7.setCellValueFactory(new PropertyValueFactory<>("eventName"));

    TableColumn<ConfRoomRequest, String> column8 = new TableColumn<>("Event Description");
    column8.setCellValueFactory(new PropertyValueFactory<>("eventDescription"));

    TableColumn<ConfRoomRequest, String> column9 = new TableColumn<>("Assigned To");
    column9.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

    TableColumn<ConfRoomRequest, String> column10 = new TableColumn<>("Order Status");
    column10.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

    TableColumn<ConfRoomRequest, String> column11 = new TableColumn<>("Notes");
    column11.setCellValueFactory(new PropertyValueFactory<>("notes"));

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

    for (ConfRoomRequest req : repo.getAll()) {
      submittedRoomRequestsTable.getItems().add(req);
    }

    /*
    "CREATE TABLE IF NOT EXISTS "
           + roomReservationsTable
           + " "
           + "(reservationID SERIAL PRIMARY KEY,"
           + "dateOrdered Date,"
           + "eventDate Date,"
           + "startTime Time,"
           + "endTime Time,"
           + "room Varchar(100),"
           + "reservedBy Varchar(100),"
           + "eventName Varchar(100),"
           + "eventDescription Varchar(100),"
           + "assignedTo Varchar(100),"
           + "orderStatus Varchar(100),"
           + "notes Varchar(500))";

     }

      */
  }
}
