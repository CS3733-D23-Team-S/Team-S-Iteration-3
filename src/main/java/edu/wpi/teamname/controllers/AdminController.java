package edu.wpi.teamname.controllers;

import edu.wpi.teamname.Main;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.kurobako.gesturefx.GesturePane;

public class AdminController {
  //  @FXML ImageView homeIcon;

  @FXML MFXButton floorL2Button;
  @FXML MFXButton floorL1Button;
  @FXML MFXButton floor1Button;
  @FXML MFXButton floor2Button;
  @FXML MFXButton floor3Button;
  ImageView floorView;
  @FXML GesturePane mapView;
  StackPane stackpane;
  AnchorPane anchorpane = new AnchorPane();

  Image floorL1 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel1.png")));

  Image floorL2 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel2.png")));

  Image floor1 = new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png")));

  Image floor2 = new Image(String.valueOf(Main.class.getResource("images/02_thesecondfloor.png")));

  Image floor3 = new Image(String.valueOf(Main.class.getResource("images/03_thethirdfloor.png")));

  private static final int maxCheckboxNumber = 5;
  @FXML VBox vboxContainer;
  @FXML CheckBox checkbox;
  private int currentCheckboxNumber = 0;

  @FXML
  public void initialize() {

    stackpane = new StackPane();
    floorView =
        new ImageView(
            new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png"))));
    stackpane.setPrefSize(800, 522);
    mapView.setContent(stackpane);
    // stackpane.setBackground(Background.fill(Color.RED));

    floorView.setImage(floor1);
    stackpane.getChildren().add(floorView);
    stackpane.getChildren().add(anchorpane);
    anchorpane.setBackground(Background.fill(Color.TRANSPARENT));

    floorL1Button.setOnMouseClicked(event -> switchToFloorL1());
    floorL2Button.setOnMouseClicked(event -> switchToFloorL2());
    floor1Button.setOnMouseClicked(event -> switchToFloor1());
    floor2Button.setOnMouseClicked(event -> switchToFloor2());
    floor3Button.setOnMouseClicked(event -> switchToFloor3());

    mapView.setMinScale(0.005);
    mapView.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    //    Platform.runLater({
    //          mapView.zoomTo(0.01, new Point2D(2500, 1750))
    //        }
    //        );
  }

  public void toDoCheckbox() {
    if (currentCheckboxNumber < maxCheckboxNumber) {
      checkbox.setOnAction(event -> removeCheckbox(checkbox));
      vboxContainer.getChildren().add(checkbox);
      currentCheckboxNumber++;
    } else {
      System.out.println("Please complete one of the above tasks!");
    }
  }

  public void removeCheckbox(CheckBox checkbox) {
    vboxContainer.getChildren().remove(checkbox);
    currentCheckboxNumber--;
  }

  public void changeButtonColor() {
    floorL1Button.setStyle("-fx-background-color: #1D2B94");

    if (floorView.getImage().equals(floorL1)) {
      floorL1Button.setStyle("-fx-background-color: #1D2B94");
      floorL2Button.setStyle("-fx-background-color: #CAD6F8");
      floor1Button.setStyle("-fx-background-color: #CAD6F8");
      floor2Button.setStyle("-fx-background-color: #CAD6F8");
      floor3Button.setStyle("-fx-background-color: #CAD6F8");

    } else if (floorView.getImage().equals(floorL2)) {
      floorL1Button.setStyle("-fx-background-color: #CAD6F8");
      floorL2Button.setStyle("-fx-background-color: #1D2B94");
      floor1Button.setStyle("-fx-background-color: #CAD6F8");
      floor2Button.setStyle("-fx-background-color: #CAD6F8");
      floor3Button.setStyle("-fx-background-color: #CAD6F8");

    } else if (floorView.getImage().equals(floor1)) {
      floorL1Button.setStyle("-fx-background-color: #CAD6F8");
      floorL2Button.setStyle("-fx-background-color: #CAD6F8");
      floor1Button.setStyle("-fx-background-color: #1D2B94");
      floor2Button.setStyle("-fx-background-color: #CAD6F8");
      floor3Button.setStyle("-fx-background-color: #CAD6F8");
    } else if (floorView.getImage().equals(floor2)) {
      floorL1Button.setStyle("-fx-background-color: #CAD6F8");
      floorL2Button.setStyle("-fx-background-color: #CAD6F8");
      floor1Button.setStyle("-fx-background-color: #CAD6F8");
      floor2Button.setStyle("-fx-background-color: #1D2B94");
      floor3Button.setStyle("-fx-background-color: #CAD6F8");
    } else if (floorView.getImage().equals(floor3)) {
      floorL1Button.setStyle("-fx-background-color: #CAD6F8");
      floorL2Button.setStyle("-fx-background-color: #CAD6F8");
      floor1Button.setStyle("-fx-background-color: #CAD6F8");
      floor2Button.setStyle("-fx-background-color: #CAD6F8");
      floor3Button.setStyle("-fx-background-color: #1D2B94");
    }
  }

  public void switchToFloorL1() {
    floorView.setImage(floorL1);
    changeButtonColor();
    // mapView.setContent(stackpane);
  }

  public void switchToFloorL2() {
    floorView.setImage(floorL2);
    changeButtonColor();
    //    stackpane.getChildren().remove(floorView);
    //    stackpane.getChildren().add(floorView);
    // mapView.setContent(stackpane);
  }

  public void switchToFloor1() {
    floorView.setImage(floor1);
    changeButtonColor();
    //    stackpane.getChildren().remove(floorView);
    //    stackpane.getChildren().add(floorView);
    //    mapView.setContent(stackpane);
  }

  public void switchToFloor2() {
    floorView.setImage(floor2);
    changeButtonColor();
    //    stackpane.getChildren().remove(floorView);
    //    stackpane.getChildren().add(floorView);
    //    mapView.setContent(stackpane);
  }

  public void switchToFloor3() {
    floorView.setImage(floor3);
    changeButtonColor();
    //    stackpane.getChildren().remove(floorView);
    //    stackpane.getChildren().add(floorView);
    //    mapView.setContent(stackpane);
  }

  List<Circle> floorL1Points = new ArrayList<>();
  List<Circle> floorL2Points = new ArrayList<>();
  List<Circle> floor1Points = new ArrayList<>();
  List<Circle> floor2Points = new ArrayList<>();
  List<Circle> floor3Points = new ArrayList<>();

  public void showFloorL1locations() {
    floorL1Points = new ArrayList<>();
    floorL2Points = new ArrayList<>();
    floor1Points = new ArrayList<>();
    floor2Points = new ArrayList<>();
    floor3Points = new ArrayList<>();
  }

  public void goToMapEditorPage() {
    Navigation.navigate(Screen.BETTER_MAP_EDITOR);
  }
}
