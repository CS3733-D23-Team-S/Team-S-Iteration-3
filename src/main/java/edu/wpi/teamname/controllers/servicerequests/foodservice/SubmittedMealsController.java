package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDelivery;
import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.IDAO;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SubmittedMealsController {
  @FXML private MFXButton submittedBack1;
  @FXML TableView submittedTableView;

  IDAO<FoodDelivery, Integer> repo = DataBaseRepository.getInstance().getFoodDeliveryDAO();

  @FXML
  public void initialize() {
    submittedBack1.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));

    TableColumn<Food, String> column1 = new TableColumn<>("RequestID");
    column1.setCellValueFactory(new PropertyValueFactory<>("deliveryID"));

    TableColumn<Food, String> column2 = new TableColumn<>("Cart");
    column2.setCellValueFactory(new PropertyValueFactory<>("cart"));

    TableColumn<Food, String> column3 = new TableColumn<>("Request Date");
    column3.setCellValueFactory(new PropertyValueFactory<>("date"));

    TableColumn<Food, String> column4 = new TableColumn<>("Request Time");
    column4.setCellValueFactory(new PropertyValueFactory<>("time"));

    TableColumn<Food, String> column5 = new TableColumn<>("Location");
    column5.setCellValueFactory(new PropertyValueFactory<>("location"));

    TableColumn<Food, String> column6 = new TableColumn<>("Orderer");
    column6.setCellValueFactory(new PropertyValueFactory<>("orderer"));

    TableColumn<Food, String> column7 = new TableColumn<>("Assigned To");
    column7.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

    TableColumn<Food, String> column8 = new TableColumn<>("Status");
    column8.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

    TableColumn<Food, String> column9 = new TableColumn<>("Cost");
    column9.setCellValueFactory(new PropertyValueFactory<>("cost"));

    TableColumn<Food, String> column10 = new TableColumn<>("Notes");
    column10.setCellValueFactory(new PropertyValueFactory<>("notes"));

    submittedTableView.getColumns().add(column1);
    submittedTableView.getColumns().add(column2);
    submittedTableView.getColumns().add(column3);
    submittedTableView.getColumns().add(column4);
    submittedTableView.getColumns().add(column5);
    submittedTableView.getColumns().add(column6);
    submittedTableView.getColumns().add(column7);
    submittedTableView.getColumns().add(column8);
    submittedTableView.getColumns().add(column9);
    submittedTableView.getColumns().add(column10);

    for (FoodDelivery order : repo.getAll()) {
      submittedTableView.getItems().add(order);
    }
  }
}
