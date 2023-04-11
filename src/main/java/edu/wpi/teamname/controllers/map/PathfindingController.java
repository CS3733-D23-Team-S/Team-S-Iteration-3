package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.Main;
import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.orms.Floor;
import edu.wpi.teamname.databaseredo.orms.Location;
import edu.wpi.teamname.databaseredo.orms.Node;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import edu.wpi.teamname.pathfinding.PathfindingEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
  @FXML MFXButton setStartingLocation;
  @FXML MFXButton setDestination;

  StackPane stackPane = new StackPane();

  public void toFloor1() {
    floor.setImage(floor1);

    stackPane.getChildren().remove(floor);
    stackPane.getChildren().remove(floor2Circles);
    stackPane.getChildren().remove(floor3Circles);
    stackPane.getChildren().remove(floorL1Circles);
    stackPane.getChildren().remove(floorL2Circles);

    stackPane.getChildren().add(floor);
    generateFloor1Nodes();
    mapPane.setContent(stackPane);
  }

  public void toFloor2() {
    floor.setImage(floor2);
    stackPane.getChildren().remove(floor);
    stackPane.getChildren().remove(floor1Circles);
    stackPane.getChildren().remove(floor3Circles);
    stackPane.getChildren().remove(floorL1Circles);
    stackPane.getChildren().remove(floorL2Circles);
    stackPane.getChildren().add(floor);
    generateFloor2Nodes();
    mapPane.setContent(stackPane);
  }

  public void toFloor3() {
    floor.setImage(floor3);
    stackPane.getChildren().remove(floor);
    stackPane.getChildren().remove(floor1Circles);
    stackPane.getChildren().remove(floor2Circles);
    stackPane.getChildren().remove(floorL1Circles);
    stackPane.getChildren().remove(floorL2Circles);
    stackPane.getChildren().add(floor);
    generateFloor3Nodes();
    mapPane.setContent(stackPane);
  }

  public void toFloorL1() {
    floor.setImage(floorL1);
    stackPane.getChildren().remove(floor);
    stackPane.getChildren().remove(floor1Circles);
    stackPane.getChildren().remove(floor2Circles);
    stackPane.getChildren().remove(floor3Circles);
    stackPane.getChildren().remove(floorL2Circles);
    stackPane.getChildren().add(floor);
    generateFloorL1Nodes();
    mapPane.setContent(stackPane);
  }

  public void toFloorL2() {
    floor.setImage(floorL2);
    stackPane.getChildren().remove(floor);
    stackPane.getChildren().remove(floor1Circles);
    stackPane.getChildren().remove(floor2Circles);
    stackPane.getChildren().remove(floor3Circles);
    stackPane.getChildren().remove(floorL1Circles);
    stackPane.getChildren().add(floor);
    generateFloorL2Nodes();
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
  List<Circle> floor1Circles = new ArrayList<>();
  List<Circle> floor2Circles = new ArrayList<>();
  List<Circle> floor3Circles = new ArrayList<>();
  List<Circle> floorL1Circles = new ArrayList<>();
  List<Circle> floorL2Circles = new ArrayList<>();
  List<Integer> floor1PathNodeIDs = new ArrayList<>();
  List<Location> floor1Locations;
  List<Location> floor2Locations;
  List<Location> floor3Locations;
  List<Location> floorL1Locations;
  List<Location> floorL2Locations;
  PathfindingEntity pfe;

  public void getLocationFromNodeID() {

    // one wack way to get the location from the node ID
    // we need to get the node ID from the circle
    // set the circle's xcenter value to (double) i
    // go through list of circles
    // if circle's (Integer) xcenter value == nodeID
    // get the node
    // set the starting location to the location at the node ID
    // if that's even possible
    //
    // just do node IDs it's faster
  }

  public void showPath() {
    int startingID;
    int endID;
    // gets list of integers that are node IDs
    // for loop
    // creates line going from nodeID(i) to nodeID(i+1)
    if (startingLocation.getText().equals("")) {
      startingLocation.setText("Error: make sure this field is filled in");
    }
    if (destination.getText().equals("")) {
      destination.setText("Error: make sure this field is filled in");
    }
    if (!startingLocation.getText().equals("") && !destination.getText().equals("")) {
      // check that they're valid node IDs
      try {
        startingID = Integer.parseInt(startingLocation.getText());
        endID = Integer.parseInt(destination.getText());
        for (int i = 0; i < pfe.getPathEntities().size() - 1; i++) {
          // draw line from node ID to another

        }
      } catch (NumberFormatException e) {
        startingLocation.setText("Error: make sure this field is a valid node ID");
        destination.setText("Error: make sure this field is a valid node ID");
      }
    }
  }

  public void generateFloor1Nodes() {
    floor1Circles = new ArrayList<>();
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor1)) {
        floor1Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floor1Nodes.size(); i++) {
      // for the purpose of this task
      // centerX is going to be the number at which the node is at in the list of nodes
      // centerY is going to be the node ID of the circle
      final Circle newCircle =
          new Circle((double) i, (double) floor1Nodes.get(i).getNodeID(), 10.0, Color.RED);

      // circles need to be reflected across the Y axis
      //      newCircle.setTranslateX(((-1.0) * floor1Nodes.get(i).getXCoord()) + 2500.0);
      //      newCircle.setTranslateY(((1.0) * floor1Nodes.get(i).getYCoord()) - 1700.0);

      // circles need to be reflected across the X axis
      //      newCircle.setTranslateX(((1.0) * floor1Nodes.get(i).getXCoord()) - 2500.0);
      //      newCircle.setTranslateY(((-1.0) * floor1Nodes.get(i).getYCoord()) + 1700.0);

      newCircle.setTranslateX(((1.0) * floor1Nodes.get(i).getXCoord()) - 2500.0);
      newCircle.setTranslateY(((1.0) * floor1Nodes.get(i).getYCoord()) - 1700.0);
      floor1Circles.add(newCircle);
      newCircle.setOnMouseClicked(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              for (int i = 0; i < floor1Circles.size(); i++) {
                floor1Circles.get(i).setFill(Color.RED);
              }
              newCircle.setFill(Color.AQUA);
              startingLocation.setText(Integer.toString((int) newCircle.getCenterY()));
            }
          });
    }
    stackPane.getChildren().addAll(floor1Circles);
  }

  public void generateFloor2Nodes() {
    floor2Circles = new ArrayList<>();
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor2)) {
        floor2Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floor2Nodes.size(); i++) {
      Circle newCircle = new Circle(0.0, 0.0, 10.0, Color.RED);
      newCircle.setTranslateX(((1.0) * floor2Nodes.get(i).getXCoord()) - 2500.0);
      newCircle.setTranslateY(((1.0) * floor2Nodes.get(i).getYCoord()) - 1700.0);
      floor2Circles.add(newCircle);
      newCircle.setOnMouseClicked(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              for (int i = 0; i < floor2Circles.size(); i++) {
                floor2Circles.get(i).setFill(Color.RED);
              }
              newCircle.setFill(Color.AQUA);
            }
          });
    }
    stackPane.getChildren().addAll(floor2Circles);
  }

  public void generateFloor3Nodes() {
    floor3Circles = new ArrayList<>();
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor3)) {
        floor3Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floor3Nodes.size(); i++) {
      Circle newCircle = new Circle(0.0, 0.0, 10.0, Color.RED);
      newCircle.setTranslateX(((1.0) * floor3Nodes.get(i).getXCoord()) - 2500.0);
      newCircle.setTranslateY(((1.0) * floor3Nodes.get(i).getYCoord()) - 1700.0);
      floor3Circles.add(newCircle);
      newCircle.setOnMouseClicked(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              for (int i = 0; i < floor3Circles.size(); i++) {
                floor3Circles.get(i).setFill(Color.RED);
              }
              newCircle.setFill(Color.AQUA);
            }
          });
    }
    stackPane.getChildren().addAll(floor3Circles);
  }

  public void generateFloorL1Nodes() {
    floorL1Circles = new ArrayList<>();
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.FloorL1)) {
        floorL1Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floorL1Nodes.size(); i++) {
      Circle newCircle = new Circle(0.0, 0.0, 10.0, Color.RED);
      newCircle.setTranslateX(((1.0) * floorL1Nodes.get(i).getXCoord()) - 2500.0);
      newCircle.setTranslateY(((1.0) * floorL1Nodes.get(i).getYCoord()) - 1700.0);
      floorL1Circles.add(newCircle);
      newCircle.setOnMouseClicked(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              for (int i = 0; i < floorL1Circles.size(); i++) {
                floorL1Circles.get(i).setFill(Color.RED);
              }
              newCircle.setFill(Color.AQUA);
            }
          });
    }
    stackPane.getChildren().addAll(floorL1Circles);
  }

  public void generateFloorL2Nodes() {
    floorL2Circles = new ArrayList<>();
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.FloorL2)) {
        floorL2Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floorL2Nodes.size(); i++) {
      Circle newCircle = new Circle(0.0, 0.0, 10.0, Color.RED);
      newCircle.setTranslateX(((1.0) * floorL2Nodes.get(i).getXCoord()) - 2500.0);
      newCircle.setTranslateY(((1.0) * floorL2Nodes.get(i).getYCoord()) - 1700.0);
      floorL2Circles.add(newCircle);
      newCircle.setOnMouseClicked(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              for (int i = 0; i < floorL2Circles.size(); i++) {
                floorL2Circles.get(i).setFill(Color.RED);
              }
              newCircle.setFill(Color.AQUA);
            }
          });
    }
    stackPane.getChildren().addAll(floorL2Circles);
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

  // process of pathfinding below
  // get list of nodes in the path via astar
  // compare list of nodes to list of nodes retrieved from astar
  // go through list of nodes in both lists
  // create an aqua circle at each node
  // create lines going from each circle to the next

  // other things to add
  // click on circles and it pulls up the location name
  // needs to be able to connect to back end - get location name from a node and i don't know if
  // that's possible
  // clear fields removes the colors of the start and end nodes
  // nice to have

  public void makePathfindingEntity(List<Integer> nodeIDsList) {
    pfe = new PathfindingEntity(startingLocation.getText(), destination.getText());
    pfe.setPathEntities(new ArrayList<>());
    pfe.generatePath();
    for (int i = 0; i < pfe.getPathEntities().size(); i++) {
      nodeIDsList.add(pfe.getPathEntities().get(i).getNodePassed());
    }
  }

  public void showSharedNodes(
      List<Integer> nodeIDsList, List<Node> floorNodes, List<Circle> circles) {
    for (int i = 0; i < floorNodes.size(); i++) {
      if (nodeIDsList.contains(floorNodes.get(i).getNodeID())) {
        int index = floorNodes.indexOf(floorNodes.get(i));
        circles.set(index, new Circle(0.0, 0.0, 10.0, Color.AQUA));
      }
    }
  }

  public void clearFields() {
    startingLocation.setText("");
    destination.setText("");
    if (floor.getImage().equals(floor1)) {
      for (int i = 0; i < floor1Circles.size(); i++) {
        floor1Circles.get(i).setFill(Color.RED);
      }
    } else if (floor.getImage().equals(floor2)) {
      for (int i = 0; i < floor2Circles.size(); i++) {
        floor2Circles.get(i).setFill(Color.RED);
      }
    } else if (floor.getImage().equals(floor3)) {
      for (int i = 0; i < floor3Circles.size(); i++) {
        floor3Circles.get(i).setFill(Color.RED);
      }
    } else if (floor.getImage().equals(floorL1)) {
      for (int i = 0; i < floorL1Circles.size(); i++) {
        floorL1Circles.get(i).setFill(Color.RED);
      }
    } else if (floor.getImage().equals(floorL2)) {
      for (int i = 0; i < floorL2Circles.size(); i++) {
        floorL2Circles.get(i).setFill(Color.RED);
      }
    }
  }

  public void initialize() {
    dataBase = DataBaseRepository.getInstance();
    pathfindingToHomeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.WELCOME_PAGE));
    clearFieldsButton.setOnMouseClicked(event -> clearFields());

    stackPane.setPrefSize(1024.0, 742.0);

    floor =
        new ImageView(
            new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png"))));

    floor.setImage(floor1);
    stackPane.getChildren().add(floor);
    generateFloor1Nodes();

    // mapPane.setContent(floor);
    mapPane.setContent(stackPane);
    mapPane.setMinScale(.0001);
    floor1Button.setOnMouseClicked(event -> toFloor1());
    floor2Button.setOnMouseClicked(event -> toFloor2());
    floor3Button.setOnMouseClicked(event -> toFloor3());
    floorL1Button.setOnMouseClicked(event -> toFloorL1());
    floorL2Button.setOnMouseClicked(event -> toFloorL2());

    // findPathButton.setOnMouseClicked(event -> showF1Path());
  }
}
