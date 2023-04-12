package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.Main;
import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.orms.Floor;
import edu.wpi.teamname.databaseredo.orms.Location;
import edu.wpi.teamname.databaseredo.orms.Node;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import edu.wpi.teamname.pathfinding.AStar;
import edu.wpi.teamname.pathfinding.PathEntity;
import edu.wpi.teamname.pathfinding.PathfindingEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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
  @FXML MFXTextField enterStartingLocation;
  @FXML MFXTextField enterDestination;

  StackPane stackPane = new StackPane();
  AnchorPane anchorPane = new AnchorPane();

  // other shit to do
  // requirements
  // show start and end of path
  // blue = start, green = end or some shit
  // nice to have
  // floor button is shaded when you're on that floor

  public void colorEvent(Circle aCircle, Node node) {
    if (startingLocation.getText().equals("")) {
      aCircle.setFill(Color.BLUE);
      startingLocation.setText(Integer.toString(node.getNodeID()));
    } else {
      if (destination.getText().equals("")) {
        aCircle.setFill(Color.GREEN);
        destination.setText(Integer.toString(node.getNodeID()));
      }
    }
  }

  public void toFloor1() {
    floor.setImage(floor1);
    // sets image

    // circlesOnFloor = floor1Circles;
    nodeList = floor1Nodes;
    // used for path generation

    stackPane.getChildren().remove(floor);
    stackPane.getChildren().add(floor);

    anchorPane = new AnchorPane();
    // makes sure circles don't show on the wrong floor

    generateFloor1Nodes();
    mapPane.setContent(stackPane);

    //    stackPane.getChildren().remove(anchorPane);
    stackPane.getChildren().add(anchorPane);
  }

  public void toFloor2() {
    floor.setImage(floor2);

    // circlesOnFloor = floor2Circles;
    nodeList = floor2Nodes;

    stackPane.getChildren().remove(floor);
    anchorPane = new AnchorPane();
    stackPane.getChildren().add(floor);
    generateFloor2Nodes();
    mapPane.setContent(stackPane);

    stackPane.getChildren().add(anchorPane);
  }

  public void toFloor3() {
    floor.setImage(floor3);

    // circlesOnFloor = floor3Circles;
    nodeList = floor3Nodes;

    stackPane.getChildren().remove(floor);
    anchorPane = new AnchorPane();
    stackPane.getChildren().add(floor);
    generateFloor3Nodes();
    mapPane.setContent(stackPane);

    stackPane.getChildren().add(anchorPane);
  }

  public void toFloorL1() {
    floor.setImage(floorL1);

    // circlesOnFloor = floorL1Circles;
    nodeList = floorL1Nodes;

    stackPane.getChildren().remove(floor);
    anchorPane = new AnchorPane();
    stackPane.getChildren().add(floor);
    generateFloorL1Nodes();
    mapPane.setContent(stackPane);

    stackPane.getChildren().add(anchorPane);
  }

  public void toFloorL2() {
    floor.setImage(floorL2);

    // circlesOnFloor = floorL2Circles;
    nodeList = floorL2Nodes;

    stackPane.getChildren().remove(floor);
    anchorPane = new AnchorPane();
    stackPane.getChildren().add(floor);
    generateFloorL2Nodes();
    mapPane.setContent(stackPane);

    stackPane.getChildren().add(anchorPane);
  }

  DataBaseRepository dataBase;

  List<Node> nodeList = new ArrayList<>();
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
  List<Line> pathLines = new ArrayList<>();
  List<Circle> circlesOnFloor = new ArrayList<>();
  @FXML MFXButton removeStartingLocation;
  @FXML MFXButton removeDestination;

  public void showPath(List<Node> floorNodes) {
    anchorPane.getChildren().removeAll(pathLines);
    pathLines.clear();
    Boolean startNodeInFloor = false;
    Boolean endNodeInFloor = false;
    int startingID = 0;
    int endID = 0;
    double startX = 0.0;
    double startY = 0.0;
    double endX = 0.0;
    double endY = 0.0;
    if (startingLocation.getText().equals("")) {
      startingLocation.setText("Error: make sure this field is filled in");
    } else if (destination.getText().equals("")) {
      destination.setText("Error: make sure this field is filled in");
    }
    if (!startingLocation.getText().equals("") && !destination.getText().equals("")) {
      // check that they're valid node IDs
      try {
        startingID = Integer.parseInt(startingLocation.getText());
        endID = Integer.parseInt(destination.getText());
        // find their nodes and make sure they're on the same floor
        // go through floorNodes
        // make sure both IDs are seen in floorNodes
        for (int i = 0; i < floorNodes.size(); i++) {
          if (floorNodes.get(i).getNodeID() == startingID) {
            startNodeInFloor = true;
          }
          if (floorNodes.get(i).getNodeID() == endID) {
            endNodeInFloor = true;
          }
        }
        if (startNodeInFloor && endNodeInFloor) {
          enterStartingLocation.setText("Enter Starting Location");
          enterDestination.setText("Enter Destination");
          AStar aStar = new AStar();
          ArrayList<PathEntity> pathEntities = new ArrayList<>();
          pfe = new PathfindingEntity(startingLocation.getText(), destination.getText());
          pfe.generatePath();

          for (int i = 0; i < pfe.getPathEntities().size() - 1; i++) {
            for (int j = 0; j < floorNodes.size(); j++) {

              // check if first node is same or whatever
              if (pfe.getPathEntities().get(i).getNodePassed() == floorNodes.get(j).getNodeID()) {
                startX = floorNodes.get(j).getXCoord();
                startY = floorNodes.get(j).getYCoord();
              }
            }
            for (int j = 0; j < floorNodes.size(); j++) {
              if (pfe.getPathEntities().get(i + 1).getNodePassed()
                  == floorNodes.get(j).getNodeID()) {
                endX = floorNodes.get(j).getXCoord();
                endY = floorNodes.get(j).getYCoord();
              }
            }
            // draw line
            Line line = new Line(startX, startY, endX, endY);
            line.setFill(Color.BLACK);
            line.setStrokeWidth(5.0);
            pathLines.add(line);
          }
        } else {
          // bad!!
          if (!startNodeInFloor) {
            enterStartingLocation.setText(
                "Error: the starting location you entered isn't on the floor currently being displayed");
          } else {
            enterStartingLocation.setText("Enter Starting Location");
          }
          if (!endNodeInFloor) {
            enterDestination.setText(
                "Error: the destination you entered isn't on the floor currently being displayed");
          } else {
            enterDestination.setText("Enter Destination");
          }
        }
      } catch (NumberFormatException e) {
        startingLocation.setText("Error: make sure this field is a valid node ID");
        destination.setText("Error: make sure this field is a valid node ID");
      }
    }
    anchorPane.getChildren().addAll(pathLines);
  }

  public void generateFloor1Nodes() {
    floor1Nodes.clear();
    floor2Nodes.clear();
    floor3Nodes.clear();
    floorL1Nodes.clear();
    floorL2Nodes.clear();
    anchorPane.getChildren().clear();
    floor1Circles = new ArrayList<>();
    floor2Circles = new ArrayList<>();
    floor3Circles = new ArrayList<>();
    floorL1Circles = new ArrayList<>();
    floorL2Circles = new ArrayList<>();
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor1)) {
        floor1Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floor1Nodes.size(); i++) {
      Circle newCircle =
          new Circle(
              floor1Nodes.get(i).getXCoord(), floor1Nodes.get(i).getYCoord(), 10.0, Color.RED);
      floor1Circles.add(newCircle);
      Node aNode = floor1Nodes.get(i);
      newCircle.setOnMouseClicked(event -> colorEvent(newCircle, aNode));
      /*
      newCircle.setOnMouseClicked(
          new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              for (int i = 0; i < floor1Circles.size(); i++) {
                floor1Circles.get(i).setFill(Color.RED);
              }
              newCircle.setFill(Color.AQUA);
            }
          });
      */
    }
    anchorPane.getChildren().addAll(floor1Circles);
  }

  public void generateFloor2Nodes() {
    floor1Nodes.clear();
    floor2Nodes.clear();
    floor3Nodes.clear();
    floorL1Nodes.clear();
    floorL2Nodes.clear();
    anchorPane.getChildren().clear();
    floor1Circles = new ArrayList<>();
    floor2Circles = new ArrayList<>();
    floor3Circles = new ArrayList<>();
    floorL1Circles = new ArrayList<>();
    floorL2Circles = new ArrayList<>();
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor2)) {
        floor2Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floor2Nodes.size(); i++) {
      Circle newCircle =
          new Circle(
              floor2Nodes.get(i).getXCoord(), floor2Nodes.get(i).getYCoord(), 10.0, Color.RED);
      floor2Circles.add(newCircle);
      Node aNode = floor2Nodes.get(i);
      newCircle.setOnMouseClicked(event -> colorEvent(newCircle, aNode));
    }
    anchorPane.getChildren().addAll(floor2Circles);
  }

  public void generateFloor3Nodes() {
    floor1Nodes.clear();
    floor2Nodes.clear();
    floor3Nodes.clear();
    floorL1Nodes.clear();
    floorL2Nodes.clear();
    anchorPane.getChildren().clear();
    floor1Circles = new ArrayList<>();
    floor2Circles = new ArrayList<>();
    floor3Circles = new ArrayList<>();
    floorL1Circles = new ArrayList<>();
    floorL2Circles = new ArrayList<>();
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor3)) {
        floor3Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floor3Nodes.size(); i++) {
      Circle newCircle = new Circle(0.0, 0.0, 10.0, Color.RED);
      newCircle.setTranslateX(floor3Nodes.get(i).getXCoord());
      newCircle.setTranslateY(floor3Nodes.get(i).getYCoord());
      floor3Circles.add(newCircle);
      Node aNode = floor3Nodes.get(i);
      newCircle.setOnMouseClicked(event -> colorEvent(newCircle, aNode));
    }
    anchorPane.getChildren().addAll(floor3Circles);
  }

  public void generateFloorL1Nodes() {
    floor1Nodes.clear();
    floor2Nodes.clear();
    floor3Nodes.clear();
    floorL1Nodes.clear();
    floorL2Nodes.clear();
    anchorPane.getChildren().clear();
    floor1Circles = new ArrayList<>();
    floor2Circles = new ArrayList<>();
    floor3Circles = new ArrayList<>();
    floorL1Circles = new ArrayList<>();
    floorL2Circles = new ArrayList<>();
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.FloorL1)) {
        floorL1Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floorL1Nodes.size(); i++) {
      Circle newCircle = new Circle(0.0, 0.0, 10.0, Color.RED);
      newCircle.setTranslateX(floorL1Nodes.get(i).getXCoord());
      newCircle.setTranslateY(floorL1Nodes.get(i).getYCoord());
      floorL1Circles.add(newCircle);
      Node aNode = floorL1Nodes.get(i);
      newCircle.setOnMouseClicked(event -> colorEvent(newCircle, aNode));
    }
    anchorPane.getChildren().addAll(floorL1Circles);
  }

  public void generateFloorL2Nodes() {
    floor1Nodes.clear();
    floor2Nodes.clear();
    floor3Nodes.clear();
    floorL1Nodes.clear();
    floorL2Nodes.clear();
    anchorPane.getChildren().clear();
    floor1Circles = new ArrayList<>();
    floor2Circles = new ArrayList<>();
    floor3Circles = new ArrayList<>();
    floorL1Circles = new ArrayList<>();
    floorL2Circles = new ArrayList<>();
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.FloorL2)) {
        floorL2Nodes.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    for (int i = 0; i < floorL2Nodes.size(); i++) {
      Circle newCircle = new Circle(0.0, 0.0, 10.0, Color.RED);
      newCircle.setTranslateX(floorL2Nodes.get(i).getXCoord());
      newCircle.setTranslateY(floorL2Nodes.get(i).getYCoord());
      floorL2Circles.add(newCircle);
      Node aNode = floorL2Nodes.get(i);
      newCircle.setOnMouseClicked(event -> colorEvent(newCircle, aNode));
    }
    anchorPane.getChildren().addAll(floorL2Circles);
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

  public void clearStartingLocation() {
    startingLocation.setText("");
    if (floor.getImage().equals(floor1)) {
      for (int i = 0; i < floor1Circles.size(); i++) {
        if (floor1Circles.get(i).getFill().equals(Color.BLUE)) {
          floor1Circles.get(i).setFill(Color.RED);
        }
      }
    } else if (floor.getImage().equals(floor2)) {
      for (int i = 0; i < floor2Circles.size(); i++) {
        if (floor2Circles.get(i).getFill().equals(Color.BLUE)) {
          floor2Circles.get(i).setFill(Color.RED);
        }
      }
    } else if (floor.getImage().equals(floor3)) {
      for (int i = 0; i < floor3Circles.size(); i++) {
        if (floor3Circles.get(i).getFill().equals(Color.BLUE)) {
          floor3Circles.get(i).setFill(Color.RED);
        }
      }
    } else if (floor.getImage().equals(floorL1)) {
      for (int i = 0; i < floorL1Circles.size(); i++) {
        if (floorL1Circles.get(i).getFill().equals(Color.BLUE)) {
          floorL1Circles.get(i).setFill(Color.RED);
        }
      }
    } else if (floor.getImage().equals(floorL2)) {
      for (int i = 0; i < floorL2Circles.size(); i++) {
        if (floorL2Circles.get(i).getFill().equals(Color.BLUE)) {
          floorL2Circles.get(i).setFill(Color.RED);
        }
      }
    }
    anchorPane.getChildren().removeAll(pathLines);
  }

  public void clearDestination() {
    destination.setText("");
    if (floor.getImage().equals(floor1)) {
      for (int i = 0; i < floor1Circles.size(); i++) {
        if (floor1Circles.get(i).getFill().equals(Color.GREEN)) {
          floor1Circles.get(i).setFill(Color.RED);
        }
      }
    } else if (floor.getImage().equals(floor2)) {
      for (int i = 0; i < floor2Circles.size(); i++) {
        if (floor2Circles.get(i).getFill().equals(Color.GREEN)) {
          floor2Circles.get(i).setFill(Color.RED);
        }
      }
    } else if (floor.getImage().equals(floor3)) {
      for (int i = 0; i < floor3Circles.size(); i++) {
        if (floor3Circles.get(i).getFill().equals(Color.GREEN)) {
          floor3Circles.get(i).setFill(Color.RED);
        }
      }
    } else if (floor.getImage().equals(floorL1)) {
      for (int i = 0; i < floorL1Circles.size(); i++) {
        if (floorL1Circles.get(i).getFill().equals(Color.GREEN)) {
          floorL1Circles.get(i).setFill(Color.RED);
        }
      }
    } else if (floor.getImage().equals(floorL2)) {
      for (int i = 0; i < floorL2Circles.size(); i++) {
        if (floorL2Circles.get(i).getFill().equals(Color.GREEN)) {
          floorL2Circles.get(i).setFill(Color.RED);
        }
      }
    }
    anchorPane.getChildren().removeAll(pathLines);
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
    anchorPane.getChildren().removeAll(pathLines);
  }

  public void initialize() {
    dataBase = DataBaseRepository.getInstance();
    pathfindingToHomeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.WELCOME_PAGE));
    clearFieldsButton.setOnMouseClicked(event -> clearFields());
    removeStartingLocation.setOnMouseClicked(event -> clearStartingLocation());
    removeDestination.setOnMouseClicked(event -> clearDestination());

    stackPane.setPrefSize(1024.0, 742.0);

    floor =
        new ImageView(
            new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png"))));

    floor.setImage(floor1);
    stackPane.getChildren().add(floor);
    stackPane.getChildren().add(anchorPane);
    anchorPane.setBackground(Background.fill(Color.TRANSPARENT));
    generateFloor1Nodes();

    mapPane.setContent(stackPane);
    mapPane.setMinScale(.0001);
    floor1Button.setOnMouseClicked(event -> toFloor1());
    floor2Button.setOnMouseClicked(event -> toFloor2());
    floor3Button.setOnMouseClicked(event -> toFloor3());
    floorL1Button.setOnMouseClicked(event -> toFloorL1());
    floorL2Button.setOnMouseClicked(event -> toFloorL2());

    circlesOnFloor = floor1Circles;
    nodeList = floor1Nodes;
    findPathButton.setOnMouseClicked(event -> showPath(nodeList));
  }
}
