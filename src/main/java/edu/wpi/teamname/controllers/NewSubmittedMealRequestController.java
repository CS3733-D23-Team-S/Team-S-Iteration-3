package edu.wpi.teamname.controllers;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
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
  @FXML TableView<MealRequest> mealRequestsTable;
  @FXML TableColumn<MealRequest, String> mealID = new TableColumn<>("Meal ID");
  @FXML TableColumn<MealRequest, String> orderTime = new TableColumn<>("Order Time");
  @FXML TableColumn<MealRequest, String> itemsOrdered = new TableColumn<>("Items Ordered");
  @FXML TableColumn<MealRequest, String> specialRequests = new TableColumn<>("Special Requests");
  @FXML TableColumn<MealRequest, String> orderStatus = new TableColumn<>("Order Status");

  @FXML MFXButton backButton;
  @FXML MFXButton navigationButton;
  @FXML MFXButton signageButton;
  @FXML MFXButton mealButton;
  @FXML MFXButton roomButton;
  @FXML MFXButton flowerButton;
  @FXML ImageView homeIcon;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    backButton.setOnMouseClicked(event -> goToAdminPage());
    signageButton.setOnMouseClicked(event -> goToSignagePage());
    navigationButton.setOnMouseClicked(event -> goToPathfindingPage());
    mealButton.setOnMouseClicked(event -> goToMealDeliveryPage());
    roomButton.setOnMouseClicked(event -> goToRoomReservationPage());
    homeIcon.setOnMouseClicked(event -> goToHomePage());

    //    mealID.setPrefWidth(320.0);
    List<MealRequest> mealRequests = new LinkedList<>();
    mealRequests.add(new MealRequest("1", "morning", "Coffee", "none", "Done"));
    mealRequests.add(new MealRequest("2", "morning", "Rice", "none", "Done"));
    mealRequests.add(new MealRequest("3", "noon", "Octopus", "none", "Done"));
    mealRequests.add(new MealRequest("4", "noon", "Chocolate", "none", "Done"));
    mealRequests.add(new MealRequest("5", "evening", "Cake", "none", "Done"));
    mealRequests.add(new MealRequest("6", "evening", "Coffee", "none", "Done"));

    mealID.setCellValueFactory((row) -> new SimpleStringProperty(row.getValue().getMealId()));
    orderTime.setCellValueFactory((row) -> new SimpleStringProperty(row.getValue().getOrderTime()));
    itemsOrdered.setCellValueFactory(
        (row) -> new SimpleStringProperty(row.getValue().getItemsOrdered()));
    specialRequests.setCellValueFactory(
        (row) -> new SimpleStringProperty(row.getValue().getSpecialRequests()));
    orderStatus.setCellValueFactory(
        (row) -> new SimpleStringProperty(row.getValue().getOrderStatus()));

    final ObservableList<MealRequest> observableMealList =
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


}
