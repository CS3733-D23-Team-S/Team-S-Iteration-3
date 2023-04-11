package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.ServiceRequests.flowers.Size;
import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class FlowerDeliveryController {

  @FXML ImageView backicon;
  @FXML ImageView exiticon;
  @FXML MFXButton flower1;
  @FXML MFXButton flower10;
  @FXML MFXButton flower2;
  @FXML MFXButton flower3;
  @FXML MFXButton flower4;
  @FXML MFXButton flower5;
  @FXML MFXButton flower6;
  @FXML MFXButton flower7;
  @FXML MFXButton flower8;
  @FXML MFXButton flower9;
  @FXML MFXButton flowerbutton;
  @FXML MFXTextField flowersearch;
  @FXML HBox hbox1;
  @FXML HBox hbox2;
  @FXML ImageView helpicon;
  @FXML ImageView homeicon;
  @FXML MFXButton mealbutton;
  @FXML MFXButton navigationbutton;
  @FXML MenuButton pricedrop;
  @FXML MFXButton roombutton;
  @FXML MFXButton signagebutton;
  @FXML MenuButton sizedrop;
  @FXML ImageView topbarlogo;
  @FXML MFXButton viewcartbutton;
  @FXML MenuItem sizesmall;
  @FXML MenuItem sizenormal;
  @FXML MenuItem sizelarge;
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {

    Flower B1 = new Flower(1, "B1", Size.SMALL, 100, 1, false, "bouquet of flowers", "image");
    Flower B2 = new Flower(2, "B2", Size.MEDIUM, 100, 1, false, "bouquet of flowers", "image");
    Flower B3 = new Flower(3, "B3", Size.LARGE, 100, 1, false, "bouquet of flowers", "image");

    viewcartbutton.setOnMouseClicked(event -> Navigation.navigate(FLOWER_CART));
    backicon.setOnMouseClicked(event -> Navigation.navigate(HOME));
    exiticon.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
    helpicon.setOnMouseClicked(event -> Navigation.navigate(HELP_PAGE));
    homeicon.setOnMouseClicked(event -> Navigation.navigate(HOME));

    dbr.getListOfSize("small").forEach(System.out::println);
    dbr.getListOfSize("medium").forEach(System.out::println);
    sizesmall.setOnAction(event -> filterSmall());
    sizenormal.setOnAction(event -> filterMedium());
    sizelarge.setOnAction(event -> filterLarge());
  }

  public void filterSmall() {
    hbox1.getChildren().clear();
    hbox2.getChildren().clear();
    for (int i = 0; i < dbr.getListOfSize("small").size(); i++) {
      MFXButton btn1 = new MFXButton();
      btn1.setId(dbr.getListOfSize("small").get(i).toString());
      btn1.setText(dbr.getListOfSize("small").get(i).toString());
      btn1.setMaxWidth(103);
      btn1.setMaxHeight(87);
      hbox1.getChildren().add(btn1);
      btn1.setOnMouseClicked(event -> Navigation.navigate(FLOWER_ORDER));
      int finalII = i;
      // btn1.setOnMouseClicked(event -> store(dbr.getListOfSize("small").get(finalII).get);
    }
  }

  public void filterMedium() {
    hbox1.getChildren().clear();
    hbox2.getChildren().clear();
    for (int i = 0; i < dbr.getListOfSize("medium").size(); i++) {
      MFXButton btn1 = new MFXButton();
      btn1.setId(dbr.getListOfSize("medium").get(i).toString());
      btn1.setText(dbr.getListOfSize("medium").get(i).toString());
      btn1.setMaxWidth(103);
      btn1.setMaxHeight(87);
      hbox1.getChildren().add(btn1);
      btn1.setOnMouseClicked(event -> Navigation.navigate(FLOWER_ORDER));
      int finalII = i;
      // btn1.setOnMouseClicked(event -> store(dbr.getListOfSize("medium").get(finalII).get);
    }
  }

  public void filterLarge() {
    hbox1.getChildren().clear();
    hbox2.getChildren().clear();
    for (int i = 0; i < dbr.getListOfSize("large").size(); i++) {
      MFXButton btn1 = new MFXButton();
      btn1.setId(dbr.getListOfSize("large").get(i).toString());
      btn1.setText(dbr.getListOfSize("large").get(i).toString());
      btn1.setMaxWidth(103);
      btn1.setMaxHeight(87);
      hbox1.getChildren().add(btn1);
      btn1.setOnMouseClicked(event -> Navigation.navigate(FLOWER_ORDER));
      int finalII = i;
      // btn1.setOnMouseClicked(event -> store(dbr.getListOfSize("medium").get(finalII).get);
    }
  }

  public void store(int x) {
    Navigation.navigate(FLOWER_ORDER);
  }
}
