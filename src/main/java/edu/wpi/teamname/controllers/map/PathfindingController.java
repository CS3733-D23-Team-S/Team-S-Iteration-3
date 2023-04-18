package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Floor;
import edu.wpi.teamname.DAOs.orms.Node;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.pathfinding.PathfindingEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.SearchableComboBox;

public class PathfindingController {

  @FXML MFXButton findPathButton;

  @FXML MFXTextField startingLocation;

  @FXML MFXTextField destination;
  @FXML MFXButton clearFieldsButton;

  @FXML GesturePane mapPane;
  @FXML public ComboBox<String> startingLocationList = new SearchableComboBox<>();
  @FXML public ComboBox<String> destinationList = new SearchableComboBox<>();
  @FXML public ComboBox<String> algList = new SearchableComboBox<>();

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
  @FXML MFXTextField enterStartingLocation;
  @FXML MFXTextField enterDestination;

  ObservableList<String> allLongNames = FXCollections.observableArrayList();

  StackPane stackPane = new StackPane();
  AnchorPane anchorPane = new AnchorPane();
  @FXML ImageView homeIcon;
  @FXML MFXTextField startingLocationError;
  @FXML MFXTextField destinationError;
  @FXML Label textualDirections;

  public void setLocationLongNames() {
    DataBaseRepository dbr = DataBaseRepository.getInstance();
    for (int i = 0; i < dbr.getMoveDAO().getAll().size(); i++) {
      allLongNames.add(dbr.getMoveDAO().getAll().get(i).getLocation().getLongName());
    }
    // alphabetize
    Collections.sort(allLongNames);
    startingLocationList.getItems().addAll(allLongNames);
    destinationList.getItems().addAll(allLongNames);
  }

  // changing floor button colors depending on which is selected
  public void changeFloorButtonColors() {
    if (floor.getImage().equals(floor1)) {
      floor1Button.setStyle("-fx-background-color: #1D2B94");
      floor2Button.setStyle("-fx-background-color: #CAD6F8");
      floor3Button.setStyle("-fx-background-color: #CAD6F8");
      floorL1Button.setStyle("-fx-background-color: #CAD6F8");
      floorL2Button.setStyle("-fx-background-color: #CAD6F8");
    } else if (floor.getImage().equals(floor2)) {
      floor1Button.setStyle("-fx-background-color: #CAD6F8");
      floor2Button.setStyle("-fx-background-color: #1D2B94");
      floor3Button.setStyle("-fx-background-color: #CAD6F8");
      floorL1Button.setStyle("-fx-background-color: #CAD6F8");
      floorL2Button.setStyle("-fx-background-color: #CAD6F8");
    } else if (floor.getImage().equals(floor3)) {
      floor1Button.setStyle("-fx-background-color: #CAD6F8");
      floor2Button.setStyle("-fx-background-color: #CAD6F8");
      floor3Button.setStyle("-fx-background-color: #1D2B94");
      floorL1Button.setStyle("-fx-background-color: #CAD6F8");
      floorL2Button.setStyle("-fx-background-color: #CAD6F8");
    } else if (floor.getImage().equals(floorL1)) {
      floor1Button.setStyle("-fx-background-color: #CAD6F8");
      floor2Button.setStyle("-fx-background-color: #CAD6F8");
      floor3Button.setStyle("-fx-background-color: #CAD6F8");
      floorL1Button.setStyle("-fx-background-color: #1D2B94");
      floorL2Button.setStyle("-fx-background-color: #CAD6F8");
    } else if (floor.getImage().equals(floorL2)) {
      floor1Button.setStyle("-fx-background-color: #CAD6F8");
      floor2Button.setStyle("-fx-background-color: #CAD6F8");
      floor3Button.setStyle("-fx-background-color: #CAD6F8");
      floorL1Button.setStyle("-fx-background-color: #CAD6F8");
      floorL2Button.setStyle("-fx-background-color: #1D2B94");
    }
  }

  public void colorEvent(Circle aCircle, Node node) {
    DataBaseRepository dbr = DataBaseRepository.getInstance();
    int id = node.getNodeID();
    String longName = "";
    // get location long name
    for (int i = 0; i < dbr.getMoveDAO().getAll().size(); i++) {
      if (id == dbr.getMoveDAO().getAll().get(i).getNodeID()) {
        longName = dbr.getMoveDAO().getAll().get(i).getLocation().getLongName();
      }
    }
    if (startingLocationList.getSelectionModel().isEmpty()) {
      aCircle.setFill(Color.PURPLE);
      for (int i = 0; i < startingLocationList.getItems().size(); i++) {
        if (longName.equals(startingLocationList.getItems().get(i))) {
          startingLocationList.getSelectionModel().select(i);
        }
      }
    } else {
      if (destinationList.getSelectionModel().isEmpty()) {
        aCircle.setFill(Color.GREEN);
        for (int i = 0; i < destinationList.getItems().size(); i++) {
          if (longName.equals(destinationList.getItems().get(i))) {
            destinationList.getSelectionModel().select(i);
          }
        }
      }
    }
  }

  public void toFloor1() {
    clearFields();
    floor.setImage(floor1);

    // sets image

    // clear fields
    // clearFields();
    changeFloorButtonColors();

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
    clearFields();
    floor.setImage(floor2);

    // clearFields();
    changeFloorButtonColors();

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
    clearFields();
    floor.setImage(floor3);

    // clearFields();
    changeFloorButtonColors();

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
    clearFields();
    floor.setImage(floorL1);

    // clearFields();
    changeFloorButtonColors();

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
    clearFields();
    floor.setImage(floorL2);

    // clearFields();
    changeFloorButtonColors();

    // circlesOnFloor = floorL2Circles;
    nodeList = floorL2Nodes;

    stackPane.getChildren().remove(floor);
    anchorPane = new AnchorPane();
    stackPane.getChildren().add(floor);
    generateFloorL2Nodes();
    mapPane.setContent(stackPane);

    stackPane.getChildren().add(anchorPane);
  }

  DataBaseRepository dataBase = DataBaseRepository.getInstance();

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
  PathfindingEntity pfe;
  List<Line> pathLines = new ArrayList<>();
  List<Circle> circlesOnFloor = new ArrayList<>();
  String textDir = "";

  public void showPathNew(List<Node> floorNodes) {
    textDir = "";
    String currDir = "";
    DataBaseRepository dbr = DataBaseRepository.getInstance();
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
    if (startingLocationList.getSelectionModel().isEmpty()) {
      startingLocationError.setText("Error: you haven't filled in the starting location");
    }
    if (destinationList.getSelectionModel().isEmpty()) {
      destinationError.setText("Error: you haven't filled in the destination");
    }
    if (!startingLocationList.getSelectionModel().isEmpty()
        && (!destinationList.getSelectionModel().isEmpty())) {
      startingLocationError.setText("");
      destinationError.setText("");
      // find node IDs through moves
      for (int i = 0; i < dbr.getMoveDAO().getListOfMoves().size(); i++) {
        if (dbr.getMoveDAO()
            .getListOfMoves()
            .get(i)
            .getLocation()
            .getLongName()
            .equals(startingLocationList.getValue())) {
          startingID = dbr.getMoveDAO().getListOfMoves().get(i).getNodeID();
        }
        if (dbr.getMoveDAO()
            .getListOfMoves()
            .get(i)
            .getLocation()
            .getLongName()
            .equals(destinationList.getValue())) {
          endID = dbr.getMoveDAO().getListOfMoves().get(i).getNodeID();
        }
      }
      for (int i = 0; i < floorNodes.size(); i++) {
        if (floorNodes.get(i).getNodeID() == startingID) {
          startNodeInFloor = true;
        }
        if (floorNodes.get(i).getNodeID() == endID) {
          endNodeInFloor = true;
        }
      }
      if (startNodeInFloor && endNodeInFloor) {
        pfe = new PathfindingEntity(startingID, endID);
        if (!algList.getSelectionModel().isEmpty()) {
          pfe.setAlg(algList.getValue());
        }
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
            if (pfe.getPathEntities().get(i + 1).getNodePassed() == floorNodes.get(j).getNodeID()) {
              endX = floorNodes.get(j).getXCoord();
              endY = floorNodes.get(j).getYCoord();
            }
          }
          Line line = new Line(startX, startY, endX, endY);
          line.setFill(Color.BLACK);
          line.setStrokeWidth(5.0);
          if (startX != endX) {
            if (startX > endX) {
              currDir = "East";
            } else {
              currDir = "West";
            }
          }
          if (startY != endY) {
            if (startY > endY) {
              if (currDir.equals("East")) {
                currDir = "Southeast";
              } else if (currDir.equals("West")) {
                currDir = "Southwest";
              } else {
                currDir = "South";
              }
            } else {
              if (currDir.equals("East")) {
                currDir = "Northeast";
              } else if (currDir.equals("West")) {
                currDir = "Northwest";
              } else {
                currDir = "North";
              }
            }
          }
          pathLines.add(line);
          textDir = textDir + i + ". Go " + currDir + " until you reach ...\n";
        }
      } else {
        if (!startNodeInFloor && !endNodeInFloor) {
          startingLocationError.setText("Error: the starting location is not in the current floor");
          destinationError.setText("Error: the destination is not in the current floor");
        } else if (!startNodeInFloor) {
          startingLocationError.setText("Error: the starting location is not in the current floor");
        } else if (!endNodeInFloor) {
          destinationError.setText("Error: the destination is not in the current floor");
        }
      }
    }
    anchorPane.getChildren().addAll(pathLines);
    textualDirections.setText(textDir);
  }

  /*
  public void generateFloorNodes(List<Node> lon, List<Circle> loc, Floor floor) {
      DataBaseRepository dbr = DataBaseRepository.getInstance();
      floor1Nodes.clear();
      floor2Nodes.clear();
      floor3Nodes.clear();
      floorL1Nodes.clear();
      floorL2Nodes.clear();
      anchorPane.getChildren().clear();
      floor1Circles.clear();
      floor2Circles.clear();
      floor3Circles.clear();
      floorL1Circles.clear();
      floorL2Circles.clear();
      for (int i = 0; i < dbr.getNodeDAO().getAll().size(); i++) {
        if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(floor)) {

        }
      }
      for (int i = 0; i < lon.size(); i++) {
        Circle newCircle = new Circle(lon.get(i).getXCoord(), lon.get(i).getYCoord(),
                10.0, Color.RED);
        loc.add(newCircle);
        Node aNode = lon.get(i);
        for (int j = 0; j < dataBase.getMoveDAO().getAll().size(); j++) {
          if (aNode.getNodeID() == dataBase.getMoveDAO().getAll().get(i).getNodeID()) {
            if ()
          }
        }
      }

    }
   */

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

  public void clearFields() {
    startingLocationList.getSelectionModel().clearSelection();
    startingLocationList.setButtonCell(
        new ListCell<String>() {
          @Override
          protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
              setText("Choose Starting Location");
            } else {
              setText(item);
            }
          }
        });
    destinationList.getSelectionModel().clearSelection();
    destinationList.setButtonCell(
        new ListCell<String>() {
          @Override
          protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
              setText("Choose Starting Location");
            } else {
              setText(item);
            }
          }
        });
    textualDirections.setText("");
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
    algList.getItems().addAll("AStar", "Breadth-first search", "Depth-first search");
    dataBase = DataBaseRepository.getInstance();
    clearFieldsButton.setOnMouseClicked(event -> clearFields());
    setLocationLongNames();

    stackPane.setPrefSize(1200, 810);

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
    changeFloorButtonColors();
    findPathButton.setOnMouseClicked(event -> showPathNew(nodeList));
  }
}
