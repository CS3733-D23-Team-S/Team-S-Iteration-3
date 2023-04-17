package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class FlowerDeliveryController {
  public static int flowerID;

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
  @FXML MenuButton sizedrop;
  @FXML MFXButton viewcartbutton;
  @FXML MenuItem sizesmall;
  @FXML MenuItem sizenormal;
  @FXML MenuItem sizelarge;
  @FXML MFXButton clearfilter;
  @FXML FlowPane flowpane;
  @FXML MFXScrollPane scrollpane;
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {

    viewcartbutton.setOnMouseClicked(event -> Navigation.navigate(FLOWER_CART));

    sizesmall.setOnAction(event -> filterSmall());
    sizenormal.setOnAction(event -> filterMedium());
    sizelarge.setOnAction(event -> filterLarge());

    clearfilter.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));

    noFilter();
  }

  public void filterSmall() {
    flowpane.getChildren().clear();
    for (int i = 0; i < dbr.getFlowerDAO().getListOfSize("small").size(); i++) {
      MFXButton btn1 = new MFXButton();
      btn1.setId(dbr.getFlowerDAO().getListOfSize("small").get(i).toString());
      btn1.setText(dbr.getFlowerDAO().getListOfSize("small").get(i).getName());
      btn1.setPrefWidth(200);
      btn1.setPrefHeight(200);
      flowpane.getChildren().add(btn1);
      flowpane.setHgap(20);
      flowpane.setVgap(20);
      int finalII = i;
      btn1.setOnMouseClicked(
          event -> store(dbr.getFlowerDAO().getListOfSize("small").get(finalII).getID()));
    }
  }

  public void filterMedium() {
    flowpane.getChildren().clear();
    for (int i = 0; i < dbr.getFlowerDAO().getListOfSize("medium").size(); i++) {
      MFXButton btn1 = new MFXButton();
      btn1.setId(dbr.getFlowerDAO().getListOfSize("medium").get(i).toString());
      btn1.setText(dbr.getFlowerDAO().getListOfSize("medium").get(i).getName());
      btn1.setPrefWidth(200);
      btn1.setPrefHeight(200);
      flowpane.getChildren().add(btn1);
      flowpane.setHgap(20);
      flowpane.setVgap(20);
      int finalII = i;
      btn1.setOnMouseClicked(
          event -> store(dbr.getFlowerDAO().getListOfSize("medium").get(finalII).getID()));
    }
  }

  public void filterLarge() {
    flowpane.getChildren().clear();
    for (int i = 0; i < dbr.getFlowerDAO().getListOfSize("large").size(); i++) {
      Image image =
          new Image(Main.class.getResource(dbr.getFlowerDAO().get(flowerID).getImage()).toString());
      ImageView view = new ImageView();
      view.setPreserveRatio(true);
      MFXButton btn1 = new MFXButton();
      btn1.setId(dbr.getFlowerDAO().getListOfSize("large").get(i).toString());
      btn1.setText(dbr.getFlowerDAO().getListOfSize("large").get(i).getName());
      btn1.setPrefWidth(200);
      btn1.setPrefHeight(200);
      btn1.setGraphic(view);
      flowpane.getChildren().add(btn1);
      flowpane.setHgap(20);
      flowpane.setVgap(20);
      int finalII = i;
      btn1.setOnMouseClicked(
          event -> store(dbr.getFlowerDAO().getListOfSize("large").get(finalII).getID()));
    }
  }

  public void noFilter() {
    flowpane.getChildren().clear();
    for (Flower f : dbr.getFlowerDAO().getFlowers().values()) {
      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(true);
      view.setFitHeight(150);
      view.setFitWidth(150);

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getName());
      btn1.setPrefWidth(250);
      btn1.setPrefHeight(200);
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      flowpane.getChildren().add(btn1);
      flowpane.setHgap(20);
      flowpane.setVgap(20);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getID());
          });
    }
  }

  public void store(int x) {
    flowerID = x;
    Navigation.launchPopUp(FLOWER_POPUP);
  }
}
