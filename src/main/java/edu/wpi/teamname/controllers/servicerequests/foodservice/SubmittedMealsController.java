package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.ServiceRequests.FoodService.Food;
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

  @FXML
  public void initialize() {
    submittedBack1.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));

    TableColumn<Food, String> column1 = new TableColumn<>("OrderID");
    column1.setCellValueFactory(new PropertyValueFactory<>("OrderID"));

    TableColumn<Food, String> column2 = new TableColumn<>("Cart");
    column2.setCellValueFactory(new PropertyValueFactory<>("Cart"));

    TableColumn<Food, String> column3 = new TableColumn<>("Order Date");
    column2.setCellValueFactory(new PropertyValueFactory<>("OrderDate"));

    TableColumn<Food, String> column4 = new TableColumn<>("Order Time");
    column2.setCellValueFactory(new PropertyValueFactory<>("OrderTime"));

    TableColumn<Food, String> column5 = new TableColumn<>("Order Location");
    column2.setCellValueFactory(new PropertyValueFactory<>("OrderLocation"));

    TableColumn<Food, String> column6 = new TableColumn<>("Orderer");
    column2.setCellValueFactory(new PropertyValueFactory<>("Orderer"));

    TableColumn<Food, String> column7 = new TableColumn<>("Assigned To");
    column2.setCellValueFactory(new PropertyValueFactory<>("AssignedTo"));

    TableColumn<Food, String> column8 = new TableColumn<>("Status");
    column2.setCellValueFactory(new PropertyValueFactory<>("Status"));

    TableColumn<Food, String> column9 = new TableColumn<>("Cost");
    column2.setCellValueFactory(new PropertyValueFactory<>("Cost"));

    TableColumn<Food, String> column10 = new TableColumn<>("Notes");
    column2.setCellValueFactory(new PropertyValueFactory<>("Notes"));

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

    Food Pizza =
        new Food(
            1,
            "Pizza",
            "Entree",
            10,
            "American",
            10,
            "Bread with sauce and cheese on it",
            1,
            false,
            "image",
            20,
            " ",
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false);

    submittedTableView.getItems().add(Pizza);
  }
}
