package edu.wpi.teamname.controllers.servicerequests.officesupplies;

import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.SearchableComboBox;

public class OfficeSuppliesSubmissionController {
  public static String deliveryRoom;

  @FXML MFXButton clearbutton;
  @FXML Text descriptiontext;
  @FXML SearchableComboBox employeedrop;
  @FXML ImageView officeSupplyimage;
  @FXML Text officeSupplynametext;
  @FXML VBox itemvbox;
  @FXML VBox sidepanel;
  @FXML SearchableComboBox locationdrop;
  @FXML Text pricetext;
  @FXML Label pricey;
  @FXML MFXTextField requestfield;
  @FXML MFXButton submitbutton;
  @FXML MFXScrollPane scroll;
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {

    displayCart();

    String stat = "Recieved";

    employeedrop.getItems().add("Nick Ho");
    employeedrop.getItems().add("Nikesh Walling");
    employeedrop.getItems().add("Prahladh Raja");
    employeedrop.getItems().add("Tyler Brown");
    employeedrop.getItems().add("Ryan Wright");
    employeedrop.getItems().add("Jake Olsen");
    employeedrop.getItems().add("Sarah Kogan");
    employeedrop.getItems().add("Kashvi Singh");
    employeedrop.getItems().add("Anthony Ticombe");
    employeedrop.getItems().add("Nat Rubin");

    pricey.setText(
        "$ " + String.valueOf(OfficeSuppliesController.officeSupplyCart.getTotalPrice()));

    submitbutton.setOnMouseClicked(
        event -> {
          try {
            String Emp = employeedrop.getValue().toString();
            deliveryRoom = locationdrop.getValue().toString();

            String n = requestfield.getText();

            Date d = Date.valueOf(LocalDate.now());
            Time t = Time.valueOf(LocalTime.now());

            OfficeSupplyDelivery currentOfficeSupply =
                new OfficeSupplyDelivery(
                    OfficeSuppliesController.officeSupplyID++,
                    OfficeSuppliesController.officeSupplyCart.toString(),
                    d,
                    t,
                    deliveryRoom,
                    ActiveUser.getInstance().getCurrentUser().getUserName(),
                    Emp,
                    stat,
                    OfficeSuppliesController.officeSupplyCart.getTotalPrice(),
                    n);

            dbr.getOfficeSupplyDeliveryDAO().add(currentOfficeSupply);

            sidepanel.getChildren().clear();

            Label confirm = new Label();
            Label thanks = new Label();
            confirm.setText("Order Submitted!");
            thanks.setText("Thank you for your order!");
            confirm.setStyle("-fx-font-size: 48;");
            thanks.setStyle("-fx-font-size:36; -fx-font-style: italic;");
            sidepanel.setAlignment(Pos.CENTER);
            sidepanel.getChildren().add(confirm);
            sidepanel.getChildren().add(thanks);

          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    locationdrop.getItems().addAll(dbr.getListOfEligibleRooms());
    clearbutton.setOnMouseClicked(event -> clearFields());
  }

  public void displayCart() {
    System.out.println("Displaying office Supplies");

    for (OfficeSupply officeSupply : OfficeSuppliesController.officeSupplyCart.getCartItems()) {

      System.out.println("works");

      HBox newRow = new HBox();
      newRow.setSpacing(100);
      newRow.setMaxHeight(300);
      newRow.setMaxWidth(1000);

      ImageView officeSupplyImage = new ImageView();
      Image image = new Image(Main.class.getResource(officeSupply.getImage()).toString());
      officeSupplyImage.setImage(image);

      officeSupplyImage.setFitHeight(160);
      officeSupplyImage.setFitWidth(160);
      officeSupplyImage.setPreserveRatio(false);

      VBox itemInfo = new VBox();
      itemInfo.setSpacing(30);
      itemInfo.setMaxWidth(1000);

      HBox priceQ = new HBox();
      priceQ.setSpacing(20);
      priceQ.setMaxWidth(1000);

      Label name = new Label();
      Label price = new Label();
      Label description = new Label();
      Label quantity = new Label();

      name.setText(officeSupply.getName());
      name.setStyle(
          "-fx-text-fill: #000000; -fx-font-size: 24px; -fx-font-weight: bold; -fx-font-style: open sans");

      description.setText(officeSupply.getDescription());
      description.setStyle(
          "-fx-text-fill: #000000; -fx-font-size: 18px; -fx-font-style: open sans; -fx-wrap-text: true;");

      quantity.setText(String.valueOf(officeSupply.getQuantity() + "x"));
      quantity.setStyle("-fx-text-fill: #000000; -fx-font-size: 24px;");

      price.setText(String.valueOf("$ " + officeSupply.getPrice()));
      price.setStyle("-fx-text-fill: #000000; -fx-font-size: 24px; -fx-font-style: open sans");

      /*recipientLabel.setText(String.valueOf(recipient));
      recipientLabel.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");*/

      itemvbox.getChildren().add(newRow);
      itemvbox.setSpacing(20);
      newRow.getChildren().add(officeSupplyImage);
      newRow.getChildren().add(itemInfo);

      itemInfo.getChildren().add(name);
      itemInfo.getChildren().add(description);
      description.setStyle("-fx-wrap-text: true;");
      itemInfo.getChildren().add(priceQ);

      priceQ.getChildren().add(price);
      priceQ.getChildren().add(quantity);
    }
  }

  public void clearFields() {
    requestfield.clear();
  }
}
