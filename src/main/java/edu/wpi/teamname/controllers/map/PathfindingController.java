package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.Main;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import net.kurobako.gesturefx.GesturePane;
import edu.wpi.teamname.databaseredo.DataBaseRepository;

import java.util.zip.DataFormatException;

public class PathfindingController {
  @FXML MFXButton pathfindingToHomeButton;

  @FXML MFXButton findPathButton;

  @FXML MFXTextField startingLocation;

  @FXML MFXTextField destination;

  @FXML MFXButton pathfindingToProfileButton;
  @FXML MFXButton clearFieldsButton;
  @FXML MFXButton emailTextualDirections;

  @FXML GesturePane mapPane;
  ImageView floor;
  Image floor1 = new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png")));
  Image floor2 = new Image(String.valueOf(Main.class.getResource("images/02_thesecondfloor.png")));
  Image floor3 = new Image(String.valueOf(Main.class.getResource("images/03_thethirdfloor.png")));
  Image floorL1 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel1.png")));
  Image floorL2 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel2.png")));
  @FXML MFXButton floor1Button;

  @FXML MFXButton floor2Button;
  @FXML MFXButton floor3Button;
  @FXML MFXButton floorL1Button;
  @FXML MFXButton floorL2Button;

  StackPane stackPane = new StackPane();

  public void toFloor1() {
    floor.setImage(floor1);
    stackPane.getChildren().removeAll();
    stackPane.getChildren().add(floor);
    mapPane.setContent(stackPane);
  }

  public void toFloor2() {
    floor.setImage(floor2);
    stackPane.getChildren().removeAll();
    stackPane.getChildren().add(floor);
    mapPane.setContent(stackPane);
  }

  public void toFloor3() {
    floor.setImage(floor3);
    stackPane.getChildren().removeAll();
    stackPane.getChildren().add(floor);
    mapPane.setContent(stackPane);
  }

  public void toFloorL1() {
    floor.setImage(floorL1);
    stackPane.getChildren().removeAll();
    stackPane.getChildren().add(floor);
    mapPane.setContent(stackPane);
  }

  public void toFloorL2() {
    floor.setImage(floorL2);
    stackPane.getChildren().removeAll();
    stackPane.getChildren().add(floor);
    mapPane.setContent(stackPane);
  }

  DataBaseRepository dataBase;
  dataBase.load();
  // building olist through pfentity pathEntities
  // creates new PathfindingEntity
  // later iterations will have an actual starting and ending location
  // goes through a loop from 1 to 10
  // adds each element to pfe.getPathEntities() --- adds elements to pfe's list of PathfindingEntity
  // goes through loop of pfe.getPathEntities().size();
  // adds each element to olist;

  public void generateFloor1Nodes() {
    for (int i = 0; i < DataBaseRepository)
  }

  public void makePathfindingEntity() {

    // if the table isn't empty, remove all items currently in the table
    // also remove all items from olist
    if (!stepsTable.getItems().isEmpty()) {
      stepsTable.getItems().removeAll(olist);
      olist.removeAll();
    }

    PathfindingEntity pfe =
        new PathfindingEntity(startingLocation.getText(), destination.getText());
    pfe.setPathEntities(new ArrayList<>());
    pfe.generatePath();
    for (int i = 0; i < pfe.getPathEntities().size(); i++) {
      olist.add(pfe.getPathEntities().get(i));
    }

    // dummy code below
    // displays 0-9 in table

    PathfindingEntity pfe = new PathfindingEntity("s", "e");
    pfe.setPathEntities(new ArrayList<>());
    for (int i = 0; i < 10; i++) {
      pfe.getPathEntities().add(new PathEntity(i));
    }
    for (int i = 0; i < pfe.getPathEntities().size(); i++) {
      olist.add(pfe.getPathEntities().get(i));
    }

    stepsTable.setItems(olist);
  }


  public void clearFields() {
    startingLocation.setText("");
    destination.setText("");
  }

  public void initialize() {
    pathfindingToHomeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.WELCOME_PAGE));
    clearFieldsButton.setOnMouseClicked(event -> clearFields());

    stackPane.setPrefSize(1024.0, 742.0);

    floor =
        new ImageView(
            new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png"))));
    stackPane.getChildren().add(floor);

    // mapPane.setContent(floor);
    mapPane.setContent(stackPane);
    mapPane.setMinScale(.0001);
    floor1Button.setOnMouseClicked(event -> toFloor1());
    floor2Button.setOnMouseClicked(event -> toFloor2());
    floor3Button.setOnMouseClicked(event -> toFloor3());
    floorL1Button.setOnMouseClicked(event -> toFloorL1());
    floorL2Button.setOnMouseClicked(event -> toFloorL2());
    //stackPane.getChildren().add()

    // findPathButton.setOnMouseClicked(event -> makePathfindingEntity());

    // code below goes from pathfinding to admin screen
    // pathfindingToProfileButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ADMIN));

    // creates columns and populates them

    // nodesTraversedCol.setCellValueFactory(new PropertyValueFactory<>("nodePassed"));
    // stepsTable.getColumns().addAll(nodesTraversedCol);
    // stepsTable.setItems(olist);
  }
}
