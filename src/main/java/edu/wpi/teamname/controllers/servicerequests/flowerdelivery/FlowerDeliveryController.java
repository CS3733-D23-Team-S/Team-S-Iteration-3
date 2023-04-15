package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import static edu.wpi.teamname.navigation.Screen.*;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
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
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  public void initialize() {

    viewcartbutton.setOnMouseClicked(event -> Navigation.navigate(FLOWER_CART));

    dbr.getListOfSize("small").forEach(System.out::println);
    dbr.getListOfSize("medium").forEach(System.out::println);
    sizesmall.setOnAction(event -> filterSmall());
    sizenormal.setOnAction(event -> filterMedium());
    sizelarge.setOnAction(event -> filterLarge());
    clearfilter.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));

    System.out.println(dbr.getListOfSize("medium"));
    noFilter();
  }

  public void filterSmall() {
    hbox1.getChildren().clear();
    hbox2.getChildren().clear();
    for (int i = 0; i < dbr.getListOfSize("small").size(); i++) {
      MFXButton btn1 = new MFXButton();
      btn1.setId(dbr.getListOfSize("small").get(i).toString());
      btn1.setText(dbr.getListOfSize("small").get(i).getName());
      btn1.setMaxWidth(200);
      btn1.setMaxHeight(200);
      hbox1.getChildren().add(btn1);
      btn1.setOnMouseClicked(event -> Navigation.navigate(Screen.FLOWER_ORDER));
      int finalII = i;
      btn1.setOnMouseClicked(event -> store(dbr.getListOfSize("small").get(finalII).getID()));
    }
  }

  public void filterMedium() {
    hbox1.getChildren().clear();
    hbox2.getChildren().clear();
    for (int i = 0; i < dbr.getListOfSize("medium").size(); i++) {
      MFXButton btn1 = new MFXButton();
      btn1.setId(dbr.getListOfSize("medium").get(i).toString());
      btn1.setText(dbr.getListOfSize("medium").get(i).getName());
      btn1.setMaxWidth(200);
      btn1.setMaxHeight(200);
      hbox1.getChildren().add(btn1);
      btn1.setOnMouseClicked(event -> Navigation.navigate(Screen.FLOWER_ORDER));
      int finalII = i;
      btn1.setOnMouseClicked(event -> store(dbr.getListOfSize("medium").get(finalII).getID()));
    }
  }

  public void filterLarge() {
    hbox1.getChildren().clear();
    hbox2.getChildren().clear();
    for (int i = 0; i < dbr.getListOfSize("large").size(); i++) {
      MFXButton btn1 = new MFXButton();
      btn1.setId(dbr.getListOfSize("large").get(i).toString());
      btn1.setText(dbr.getListOfSize("large").get(i).getName());
      btn1.setMaxWidth(200);
      btn1.setMaxHeight(200);
      hbox1.getChildren().add(btn1);
      btn1.setOnMouseClicked(event -> Navigation.navigate(Screen.FLOWER_ORDER));
      int finalII = i;
      btn1.setOnMouseClicked(event -> store(dbr.getListOfSize("medium").get(finalII).getID()));
    }
  }

  public void noFilter() {
    hbox1.getChildren().clear();
    hbox2.getChildren().clear();
    for (int i = 0; i < dbr.getListOfSize("large").size(); i++) {
      MFXButton btn1 = new MFXButton();
      btn1.setId(dbr.getListOfSize("large").get(i).toString());
      btn1.setText(dbr.getListOfSize("large").get(i).getName());
      btn1.setMaxWidth(200);
      btn1.setMaxHeight(200);
      hbox1.getChildren().add(btn1);
      btn1.setOnMouseClicked(event -> Navigation.navigate(Screen.FLOWER_ORDER));
      int finalII = i;
      btn1.setOnMouseClicked(event -> store(dbr.getListOfSize("medium").get(finalII).getID()));
    }
  }

  public void store(int x) {
    flowerID = x;
    Navigation.navigate(FLOWER_ORDER);
  }
}
