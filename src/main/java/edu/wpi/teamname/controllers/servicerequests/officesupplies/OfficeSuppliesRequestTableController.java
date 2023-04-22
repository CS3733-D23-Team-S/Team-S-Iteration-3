package edu.wpi.teamname.controllers.servicerequests.officesupplies;

import static edu.wpi.teamname.navigation.Screen.*;
import static edu.wpi.teamname.navigation.Screen.HOME;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.Status;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.OfficeSupply;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.sql.Date;
import java.sql.Time;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class OfficeSuppliesRequestTableController {
  @FXML private ImageView backicon;

  @FXML private ImageView exiticon;

  @FXML private MFXButton flowerbutton;

  @FXML private ImageView helpicon;

  @FXML private ImageView homeicon;

  @FXML private MFXButton mealbutton;

  @FXML private MFXButton navigationbutton;

  @FXML private MFXButton roombutton;

  @FXML private MFXButton signagebutton;

  @FXML private ImageView topbarlogo;

  @FXML private final DataBaseRepository dbr = DataBaseRepository.getInstance();

  @FXML private TableView requesttable;

  public void initialize() {
    backicon.setOnMouseClicked(event -> Navigation.navigate(OFFICE_SUPPLIES_DELIVERY));
    exiticon.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
    helpicon.setOnMouseClicked(event -> Navigation.navigate(HELP_PAGE));
    homeicon.setOnMouseClicked(event -> Navigation.navigate(HOME));

    navigationbutton.setOnMouseClicked(event -> Navigation.navigate(PATHFINDING));
    signagebutton.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
    mealbutton.setOnMouseClicked(event -> Navigation.navigate(MEAL_DELIVERY1));
    roombutton.setOnMouseClicked(event -> Navigation.navigate(ROOM_BOOKING));
    flowerbutton.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));

    TableColumn<OfficeSupply, Integer> column1 = new TableColumn<>("DeliveryID");
    column1.setCellValueFactory(new PropertyValueFactory<>("deliveryid"));

    TableColumn<OfficeSupply, String> column2 = new TableColumn<>("Cart");
    column2.setCellValueFactory(new PropertyValueFactory<>("cart"));

    TableColumn<OfficeSupply, Date> column3 = new TableColumn<>("Order Date");
    column3.setCellValueFactory(new PropertyValueFactory<>("orderdate"));

    TableColumn<OfficeSupply, Time> column4 = new TableColumn<>("Order Time");
    column4.setCellValueFactory(new PropertyValueFactory<>("ordertime"));

    TableColumn<OfficeSupply, String> column5 = new TableColumn<>("Room");
    column5.setCellValueFactory(new PropertyValueFactory<>("room"));

    TableColumn<OfficeSupply, String> column6 = new TableColumn<>("Ordered By");
    column6.setCellValueFactory(new PropertyValueFactory<>("orderedby"));

    TableColumn<OfficeSupply, String> column7 = new TableColumn<>("Assigned Employee");
    column7.setCellValueFactory(new PropertyValueFactory<>("assignedto"));

    TableColumn<OfficeSupply, Status> column8 = new TableColumn<>("Order Status");
    column8.setCellValueFactory(new PropertyValueFactory<>("orderstatus"));

    TableColumn<OfficeSupply, Double> column9 = new TableColumn<>("Cost");
    column9.setCellValueFactory(new PropertyValueFactory<>("cost"));

    requesttable.getColumns().add(column1);
    requesttable.getColumns().add(column2);
    requesttable.getColumns().add(column3);
    requesttable.getColumns().add(column4);
    requesttable.getColumns().add(column5);
    requesttable.getColumns().add(column6);
    requesttable.getColumns().add(column7);
    requesttable.getColumns().add(column8);
    requesttable.getColumns().add(column9);

    for (OfficeSupply order : dbr.getOfficeSupplyDAO().getAll()) {
      requesttable.getItems().add(order);
    }
  }
}
