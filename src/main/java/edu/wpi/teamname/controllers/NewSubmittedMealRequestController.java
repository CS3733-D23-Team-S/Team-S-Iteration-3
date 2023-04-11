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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

class NewMealRequest {
  @Getter public String mealId;
  @Getter public String orderTime;
  @Getter public String itemsOrdered;
  @Getter public String specialRequests;
  @Getter public String orderStatus;

  NewMealRequest(
      String mealID,
      String orderTime,
      String itemsOrdered,
      String specialRequests,
      String orderStatus) {
    this.orderTime = orderTime;
    this.mealId = mealID;
    this.itemsOrdered = itemsOrdered;
    this.specialRequests = specialRequests;
    this.orderStatus = orderStatus;
  }
}

public class NewSubmittedMealRequestController implements Initializable {
  @FXML TableView<NewMealRequest> mealRequestsTable;
  @FXML TableColumn<NewMealRequest, String> mealID = new TableColumn<>("Meal ID");
  @FXML TableColumn<NewMealRequest, String> orderTime = new TableColumn<>("Order Time");
  @FXML TableColumn<NewMealRequest, String> itemsOrdered = new TableColumn<>("Items Ordered");
  @FXML TableColumn<NewMealRequest, String> specialRequests = new TableColumn<>("Special Requests");
  @FXML TableColumn<NewMealRequest, String> orderStatus = new TableColumn<>("Order Status");

  @FXML ImageView backIcon;
  @FXML ImageView exitIcon;
  @FXML ImageView helpIcon;
  @FXML MFXButton navigationButton;
  @FXML MFXButton signageButton;
  @FXML MFXButton mealButton;
  @FXML MFXButton roomButton;
  @FXML MFXButton flowerButton;
  @FXML ImageView homeIcon;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    backIcon.setOnMouseClicked(event -> goToAdminPage());
    signageButton.setOnMouseClicked(event -> goToSignagePage());
    navigationButton.setOnMouseClicked(event -> goToPathfindingPage());
    mealButton.setOnMouseClicked(event -> goToMealDeliveryPage());
    roomButton.setOnMouseClicked(event -> goToRoomReservationPage());
    homeIcon.setOnMouseClicked(event -> goToHomePage());
    exitIcon.setOnMouseClicked(event -> exitApplication());
    helpIcon.setOnMouseClicked(event -> goToHelpPage());

    //    mealID.setPrefWidth(320.0);
    List<NewMealRequest> mealRequests = new LinkedList<>();
    mealRequests.add(new NewMealRequest("1", "morning", "Coffee", "none", "Done"));
    mealRequests.add(new NewMealRequest("2", "morning", "Rice", "none", "Done"));
    mealRequests.add(new NewMealRequest("3", "noon", "Octopus", "none", "Done"));
    mealRequests.add(new NewMealRequest("4", "noon", "Chocolate", "none", "Done"));
    mealRequests.add(new NewMealRequest("5", "evening", "Cake", "none", "Done"));
    mealRequests.add(new NewMealRequest("6", "evening", "Coffee", "none", "Done"));

    mealID.setCellValueFactory((row) -> new SimpleStringProperty(row.getValue().getMealId()));
    orderTime.setCellValueFactory((row) -> new SimpleStringProperty(row.getValue().getOrderTime()));
    itemsOrdered.setCellValueFactory(
        (row) -> new SimpleStringProperty(row.getValue().getItemsOrdered()));
    specialRequests.setCellValueFactory(
        (row) -> new SimpleStringProperty(row.getValue().getSpecialRequests()));
    orderStatus.setCellValueFactory(
        (row) -> new SimpleStringProperty(row.getValue().getOrderStatus()));

    final ObservableList<NewMealRequest> observableMealList =
        FXCollections.observableList(mealRequests);
    // mealRequestsTable.setItems(observableMealList);
    mealRequestsTable.getItems().addAll(observableMealList);

    System.out.println("i am running (controller)");
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
