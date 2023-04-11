package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.Main;
import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.orms.Floor;
import edu.wpi.teamname.databaseredo.orms.Location;
import edu.wpi.teamname.databaseredo.orms.Node;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import net.kurobako.gesturefx.GesturePane;

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
    stackPane.getChildren().remove(floor);
    stackPane.getChildren().add(floor);
    generateFloor1Nodes();
    mapPane.setContent(stackPane);
  }

  public void toFloor2() {
    floor.setImage(floor2);
    stackPane.getChildren().remove(floor);
    stackPane.getChildren().add(floor);
    mapPane.setContent(stackPane);
  }

  public void toFloor3() {
    floor.setImage(floor3);
    stackPane.getChildren().remove(floor);
    stackPane.getChildren().add(floor);
    mapPane.setContent(stackPane);
  }

  public void toFloorL1() {
    floor.setImage(floorL1);
    stackPane.getChildren().remove(floor);
    stackPane.getChildren().add(floor);
    mapPane.setContent(stackPane);
  }

  public void toFloorL2() {
    floor.setImage(floorL2);
    stackPane.getChildren().remove(floor);
    stackPane.getChildren().add(floor);
    mapPane.setContent(stackPane);
  }

  DataBaseRepository dataBase;

  // building olist through pfentity pathEntities
  // creates new PathfindingEntity
  // later iterations will have an actual starting and ending location
  // goes through a loop from 1 to 10
  // adds each element to pfe.getPathEntities() --- adds elements to pfe's list of PathfindingEntity
  // goes through loop of pfe.getPathEntities().size();
  // adds each element to olist;
  List<Node> floor1Nodes = new ArrayList<>();
  List<Node> floor2Nodes = new ArrayList<>();
  List<Node> floor3Nodes = new ArrayList<>();
  List<Node> floorL1Nodes = new ArrayList<>();
  List<Node> floorL2Nodes = new ArrayList<>();
  List<Location> floor1Locations;
  List<Location> floor2Locations;
  List<Location> floor3Locations;
  List<Location> floorL1Locations;
  List<Location> floorL2Locations;

  public void generateFloor1Nodes() {
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor1)) {
        floor1Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floor1Nodes.size(); i++) {
      // add a circle or some icon at each node

      // test
      Circle a = new Circle(0.0, 0.0, 50.0);
      // Circle b = new Circle(250.0, 250.0, 50.0);
      Circle b = new Circle();
      b.setCenterX(250.0f);
      b.setCenterY(150.0f);
      b.setRadius(25.0f);

      //      Circle b = new Circle(400.0, 400.0, 20.0, Color.RED);
      //      stackPane.getChildren().addAll(a, b);
      stackPane.getChildren().addAll(a, b);
      // stackPane.setAlignment(Pos.TOP_LEFT);
      //      b.setCenterX(400.0);
      a.setVisible(true);

      //      stackPane.setAlignment(a, Pos.TOP_CENTER);
      //      stackPane.setAlignment(b, Pos.TOP_RIGHT);

    }
  }

  public void generateFloor2Nodes() {
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor2)) {
        floor2Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floor2Nodes.size(); i++) {
      // add to stack pane at the right places
    }
  }

  public void generateFloor3Nodes() {
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor3)) {
        floor3Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floor3Nodes.size(); i++) {
      // add to stack pane at the right places
    }
  }

  public void generateFloorL1Nodes() {
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.FloorL1)) {
        floorL1Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floorL1Nodes.size(); i++) {
      // add to stack pane at the right places
    }
  }

  public void generateFloorL2Nodes() {
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.FloorL2)) {
        floorL2Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floorL2Nodes.size(); i++) {
      // add to stack pane at the right places
    }
  }
  /*
  public void generateFloor1Locations() {
    for (int i = 0; i < dataBase.getLocationDAO().getAll().size(); i++) {
      if (floor1Nodes.contains(dataBase.getLocationDAO().getAll().get(i).getNode())) {
        floor1Locations.add
      }
    }
  }
  */
  /*
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
  */

  public void clearFields() {
    startingLocation.setText("");
    destination.setText("");
  }

  public void initialize() {
    dataBase = DataBaseRepository.getInstance();
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

    // stackPane.getChildren().add()

    // findPathButton.setOnMouseClicked(event -> makePathfindingEntity());

    // code below goes from pathfinding to admin screen
    // pathfindingToProfileButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ADMIN));

    // creates columns and populates them

    // nodesTraversedCol.setCellValueFactory(new PropertyValueFactory<>("nodePassed"));
    // stepsTable.getColumns().addAll(nodesTraversedCol);
    // stepsTable.setItems(olist);
  }
}
