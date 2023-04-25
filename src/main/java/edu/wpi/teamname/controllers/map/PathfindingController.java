package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.*;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import edu.wpi.teamname.pathfinding.PathfindingEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.ToggleSwitch;

public class PathfindingController {

  @FXML MFXButton findPathButton;
  @FXML MFXButton clearFieldsButton;

  @FXML GesturePane mapPane;
  @FXML public ComboBox<String> startingLocationList = new SearchableComboBox<>();
  @FXML public ComboBox<String> destinationList = new SearchableComboBox<>();
  @FXML public ComboBox<String> algList = new SearchableComboBox<>();
  @FXML MFXButton pathfindingToLogin;
  @FXML MFXDatePicker datePicker;

  @FXML MFXButton addMoveBtn;

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

  ObservableList<String> allLongNames = FXCollections.observableArrayList();

  StackPane stackPane = new StackPane();
  AnchorPane anchorPane = new AnchorPane();
  @FXML ImageView homeIcon;
  @FXML Label startingLocationError;
  @FXML Label destinationError;
  @FXML Label textualDirections;
  @FXML Label datePickerError;
  @FXML ToggleSwitch displayLocationNamesToggle;
  @FXML ToggleSwitch displayAllNodesToggle;
  private String prevDirection;
  List<Text> locations = new ArrayList<>();

  // issue with this:
  // when the date is entered after switching floors it doesn't display it properly
  // basically showLocationNames is dependent on the date
  // so for moves like Cafe, when the date is before the move, it doesn't display it post move
  // and this means that if the date is switched to 4/25 before entering
  public void showLocationNames2() {
    List<Move> lom = dataBase.getMoveDAO().constructForGivenDate(datePicker.getValue());
    if (!displayLocationNamesToggle.isSelected()) {
      anchorPane.getChildren().removeAll(locations);
      locations.clear();
      return;
    } else {
      anchorPane.getChildren().removeAll(locations);
      locations.clear();

      for (int i = 0; i < nodeList.size(); i++) {
        for (int j = 0; j < lom.size(); j++) {
          if (lom.get(j).getNodeID() == nodeList.get(i).getNodeID()) {
            Text location = new Text();
            location.setText(lom.get(j).getLocationName());
            location.setX(circlesOnFloor.get(i).getCenterX() - 20.0);
            location.setY(circlesOnFloor.get(i).getCenterY() + 25.0);
            locations.add(location);
          }
        }
      }
    }
    anchorPane.getChildren().addAll(locations);
  }

  public void showNodes() {
    if (!displayAllNodesToggle.isSelected()) {
      anchorPane.getChildren().removeAll(circlesOnFloor);
      // circlesOnFloor.clear();
    } else {
      anchorPane.getChildren().addAll(circlesOnFloor);
    }
  }

  public void setLocationLongNames() {
    DataBaseRepository dbr = DataBaseRepository.getInstance();
    for (int i = 0; i < dbr.getMoveDAO().getAll().size(); i++) {
      // check if the location is a hallway
      if (!dbr.getMoveDAO().getAll().get(i).getLocation().getNodeType().equals(NodeType.HALL)) {
        allLongNames.add(dbr.getMoveDAO().getAll().get(i).getLocation().getLongName());
      }
    }
    // alphabetize
    Collections.sort(allLongNames);
    HashSet<String> allLocation = new HashSet<>(allLongNames);
    startingLocationList.getItems().addAll(allLocation);
    destinationList.getItems().addAll(allLocation);
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
      aCircle.setFill(Color.BLUE);
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
    floor.setImage(floor1);

    // sets image

    // clear fields
    // clearFields();
    changeFloorButtonColors();

    // circlesOnFloor = floor1Circles;
    nodeList = floor1Nodes;
    // used for path generation

    // makes sure circles don't show on the wrong floor

    generateFloor1Nodes();
    if (displayAllNodesToggle.isSelected()) {
      showNodes();
    }
    if (displayLocationNamesToggle.isSelected()) {
      showLocationNames2();
    }
    //    mapPane.setContent(stackPane);

    //    stackPane.getChildren().remove(anchorPane);
    // stackPane.getChildren().add(anchorPane);
    anchorPane.getChildren().removeAll();
    anchorPane.getChildren().addAll(floor1Lines);
    anchorPane.getChildren().addAll(importantCirclesF1);
  }

  public void toFloor2() {
    floor.setImage(floor2);

    // clearFields();
    changeFloorButtonColors();

    // circlesOnFloor = floor2Circles;
    nodeList = floor2Nodes;

    generateFloor2Nodes();
    if (displayAllNodesToggle.isSelected()) {
      showNodes();
    }
    if (displayLocationNamesToggle.isSelected()) {
      showLocationNames2();
    }
    //    mapPane.setContent(stackPane);

    // stackPane.getChildren().add(anchorPane);
    anchorPane.getChildren().removeAll(floor1Lines);
    anchorPane.getChildren().removeAll(floor3Lines);
    anchorPane.getChildren().removeAll(floorL1Lines);
    anchorPane.getChildren().removeAll(floorL2Lines);
    anchorPane.getChildren().addAll(floor2Lines);
    anchorPane.getChildren().removeAll(importantCirclesF1);
    anchorPane.getChildren().removeAll(importantCirclesF3);
    anchorPane.getChildren().removeAll(importantCirclesFL1);
    anchorPane.getChildren().removeAll(importantCirclesFL2);
    anchorPane.getChildren().addAll(importantCirclesF2);
  }

  public void toFloor3() {
    floor.setImage(floor3);

    // clearFields();
    changeFloorButtonColors();

    // circlesOnFloor = floor3Circles;
    nodeList = floor3Nodes;

    // stackPane.getChildren().add(floor);
    generateFloor3Nodes();
    if (displayAllNodesToggle.isSelected()) {
      showNodes();
    }
    if (displayLocationNamesToggle.isSelected()) {
      showLocationNames2();
    }

    anchorPane.getChildren().removeAll();
    anchorPane.getChildren().addAll(floor3Lines);
    anchorPane.getChildren().addAll(importantCirclesF3);
  }

  public void toFloorL1() {
    floor.setImage(floorL1);

    // clearFields();
    changeFloorButtonColors();

    // circlesOnFloor = floorL1Circles;
    nodeList = floorL1Nodes;

    //    stackPane.getChildren().add(floor);
    generateFloorL1Nodes();
    if (displayAllNodesToggle.isSelected()) {
      showNodes();
    }
    if (displayLocationNamesToggle.isSelected()) {
      showLocationNames2();
    }
    mapPane.setContent(stackPane);

    anchorPane.getChildren().removeAll();
    anchorPane.getChildren().addAll(floorL1Lines);
    anchorPane.getChildren().addAll(importantCirclesFL1);
  }

  public void toFloorL2() {
    floor.setImage(floorL2);

    // clearFields();
    changeFloorButtonColors();

    // circlesOnFloor = floorL2Circles;
    nodeList = floorL2Nodes;
    generateFloorL2Nodes();
    if (displayAllNodesToggle.isSelected()) {
      showNodes();
    }
    if (displayLocationNamesToggle.isSelected()) {
      showLocationNames2();
    }
    mapPane.setContent(stackPane);

    anchorPane.getChildren().removeAll();
    anchorPane.getChildren().addAll(floorL2Lines);
    anchorPane.getChildren().addAll(importantCirclesFL2);
  }

  DataBaseRepository dataBase = DataBaseRepository.getInstance();

  List<Node> nodeList = new ArrayList<>();
  List<Node> floor1Nodes = new ArrayList<>();
  List<Node> floor2Nodes = new ArrayList<>();
  List<Node> floor3Nodes = new ArrayList<>();
  List<Node> floorL1Nodes = new ArrayList<>();
  List<Node> floorL2Nodes = new ArrayList<>();
  List<Circle> importantCirclesF1 = new ArrayList<>();
  List<Circle> importantCirclesF2 = new ArrayList<>();
  List<Circle> importantCirclesF3 = new ArrayList<>();
  List<Circle> importantCirclesFL1 = new ArrayList<>();
  List<Circle> importantCirclesFL2 = new ArrayList<>();
  List<Circle> floor1Circles = new ArrayList<>();
  List<Circle> floor2Circles = new ArrayList<>();
  List<Circle> floor3Circles = new ArrayList<>();
  List<Circle> floorL1Circles = new ArrayList<>();
  List<Circle> floorL2Circles = new ArrayList<>();
  List<Line> floor1Lines = new ArrayList<>();
  List<Line> floor2Lines = new ArrayList<>();
  List<Line> floor3Lines = new ArrayList<>();
  List<Line> floorL1Lines = new ArrayList<>();
  List<Line> floorL2Lines = new ArrayList<>();
  PathfindingEntity pfe;
  List<Line> pathLines = new ArrayList<>();
  List<Circle> circlesOnFloor = new ArrayList<>();
  String textDir = "";
  @FXML Label floorOrderLabel;

  public void displayFloorOrder(List<String> floors) {
    floorOrderLabel.setText("");
    for (int i = 0; i < floors.size() - 1; i++) {
      if (!floors.get(i).equals(floors.get(i + 1))) {
        floorOrderLabel.setText(floorOrderLabel.getText() + floors.get(i) + " -> ");
      }
    }
    floorOrderLabel.setText(floorOrderLabel.getText() + floors.get(floors.size() - 1));
  }

  public void centerOnPoint(List<Circle> importantCircles) {
    mapPane.centreOnX(importantCircles.get(0).getCenterX());
    mapPane.centreOnY(importantCircles.get(0).getCenterY());
    mapPane.zoomTo(
        1,
        1,
        new Point2D(importantCircles.get(0).getCenterX(), importantCircles.get(0).getCenterY()));
  }

  // test for showing paths method
  public void showPathTesting() {
    List<String> los = new ArrayList<>();
    String currFloor = "";
    anchorPane.getChildren().removeAll(importantCirclesF1);
    anchorPane.getChildren().removeAll(importantCirclesF2);
    anchorPane.getChildren().removeAll(importantCirclesF3);
    anchorPane.getChildren().removeAll(importantCirclesFL1);
    anchorPane.getChildren().removeAll(importantCirclesFL2);
    importantCirclesF1.clear();
    importantCirclesF2.clear();
    importantCirclesF3.clear();
    importantCirclesFL1.clear();
    importantCirclesFL2.clear();
    List<Circle> importantCirclesF1PH = new ArrayList<>();
    List<Circle> importantCirclesF2PH = new ArrayList<>();
    List<Circle> importantCirclesF3PH = new ArrayList<>();
    List<Circle> importantCirclesFL1PH = new ArrayList<>();
    List<Circle> importantCirclesFL2PH = new ArrayList<>();
    Circle startCircle = new Circle();
    Circle endCircle = new Circle();
    Circle switchFloorsCircle = new Circle();
    // add lines to placeholders
    // set actual lines to placeholders at the end
    anchorPane.getChildren().removeAll(floor1Lines);
    anchorPane.getChildren().removeAll(floor2Lines);
    anchorPane.getChildren().removeAll(floor3Lines);
    anchorPane.getChildren().removeAll(floorL1Lines);
    anchorPane.getChildren().removeAll(floorL2Lines);
    floor1Lines.clear();
    floor2Lines.clear();
    floor3Lines.clear();
    floorL1Lines.clear();
    floorL2Lines.clear();
    List<Line> floor1LinesPlaceholder = new ArrayList<>();
    List<Line> floor2LinesPlaceholder = new ArrayList<>();
    List<Line> floor3LinesPlaceholder = new ArrayList<>();
    List<Line> floorL1LinesPlaceholder = new ArrayList<>();
    List<Line> floorL2LinesPlaceholder = new ArrayList<>();
    String currLocationName = "";
    textDir = "";
    String currDir = "";
    anchorPane.getChildren().removeAll(pathLines);
    pathLines.clear();
    int startingID = 0;
    int endID = 0;
    double startX = 0.0;
    double startY = 0.0;
    double endX = 0.0;
    double endY = 0.0;
    Floor thisFloor = null;
    Floor nextFloor = null;
    if (startingLocationList.getSelectionModel().isEmpty()) {
      startingLocationError.setText("Error: you haven't filled in the starting location");
    }
    if (destinationList.getSelectionModel().isEmpty()) {
      destinationError.setText("Error: you haven't filled in the destination");
    }
    if (datePicker.getValue() == null) {
      datePickerError.setText("Error: you haven't filled in the date");
    }
    if (!startingLocationList.getSelectionModel().isEmpty()
        && (!destinationList.getSelectionModel().isEmpty())
        && (datePicker.getValue() != (null))) {
      startingLocationError.setText("");
      destinationError.setText("");
      datePickerError.setText("");
      // remove selected nodes if they were selected via clicking on them
      for (int i = 0; i < floor1Circles.size(); i++) {
        floor1Circles.get(i).setFill(Color.RED);
      }
      for (int i = 0; i < floor2Circles.size(); i++) {
        floor2Circles.get(i).setFill(Color.RED);
      }
      for (int i = 0; i < floor3Circles.size(); i++) {
        floor3Circles.get(i).setFill(Color.RED);
      }
      for (int i = 0; i < floorL1Circles.size(); i++) {
        floorL1Circles.get(i).setFill(Color.RED);
      }
      for (int i = 0; i < floorL2Circles.size(); i++) {
        floorL2Circles.get(i).setFill(Color.RED);
      }
      // find node IDs through moves
      List<Move> thisMoves = dataBase.getMoveDAO().constructForGivenDate(datePicker.getValue());
      if (datePicker.getValue().getYear() < 2023) {
        datePickerError.setText("Error: the date you entered is before any moves");
        return;
      }
      for (int i = 0; i < thisMoves.size(); i++) {
        if (thisMoves.get(i).getLocation().getLongName().equals(startingLocationList.getValue())) {
          startingID = thisMoves.get(i).getNodeID();
        }
        if (thisMoves.get(i).getLocation().getLongName().equals(destinationList.getValue())) {
          endID = thisMoves.get(i).getNodeID();
        }
      }
      pfe = new PathfindingEntity(startingID, endID);
      if (!algList.getSelectionModel().isEmpty()) {
        pfe.setAlg(algList.getValue());
      }
      pfe.generatePath();
      // go through list of nodes
      // find the floor the node is on
      // add a line to the list of lines for that floor

      for (int i = 0; i < pfe.getPathEntities().size() - 1; i++) {
        for (int j = 0; j < dataBase.getNodeDAO().getAll().size(); j++) {
          // check if first node is same or whatever
          if (pfe.getPathEntities().get(i).getNodePassed()
              == dataBase.getNodeDAO().getAll().get(j).getNodeID()) {
            thisFloor = dataBase.getNodeDAO().getAll().get(j).getFloor();
            startX = dataBase.getNodeDAO().getAll().get(j).getXCoord();
            startY = dataBase.getNodeDAO().getAll().get(j).getYCoord();
          }
        }
        for (int j = 0; j < dataBase.getNodeDAO().getAll().size(); j++) {
          if (pfe.getPathEntities().get(i + 1).getNodePassed()
              == dataBase.getNodeDAO().getAll().get(j).getNodeID()) {
            nextFloor = dataBase.getNodeDAO().getAll().get(j).getFloor();
            endX = dataBase.getNodeDAO().getAll().get(j).getXCoord();
            endY = dataBase.getNodeDAO().getAll().get(j).getYCoord();
            for (int k = 0; k < thisMoves.size(); k++) {
              if (thisMoves.get(k).getNodeID()
                  == dataBase.getNodeDAO().getAll().get(j).getNodeID()) {
                currLocationName = thisMoves.get(k).getLocationName();
              }
            }
          }
        }
        if (i == 0) {
          if (thisFloor.equals(Floor.Floor1) && !floor.getImage().equals(floor1)) {
            toFloor1();
            circlesOnFloor = floor1Circles;
          } else if (thisFloor.equals(Floor.Floor2) && !floor.getImage().equals(floor2)) {
            toFloor2();
            circlesOnFloor = floor2Circles;
          } else if (thisFloor.equals(Floor.Floor3) && !floor.getImage().equals(floor3)) {
            toFloor3();
            circlesOnFloor = floor3Circles;
          } else if (thisFloor.equals(Floor.FloorL1) && !floor.getImage().equals(floorL1)) {
            toFloorL1();
            circlesOnFloor = floorL1Circles;
          } else if (thisFloor.equals(Floor.FloorL2) && !floor.getImage().equals(floorL2)) {
            toFloorL2();
            circlesOnFloor = floorL2Circles;
          }
        }
        if (i == 0) {
          startCircle = new Circle(startX, startY, 10.0, Color.BLUE);
          if (thisFloor.equals(Floor.Floor1)) {
            importantCirclesF1PH.add(startCircle);
            los.add("Floor 1");
          } else if (thisFloor.equals(Floor.Floor2)) {
            importantCirclesF2PH.add(startCircle);
            los.add("Floor 2");
          } else if (thisFloor.equals(Floor.Floor3)) {
            importantCirclesF3PH.add(startCircle);
            los.add("Floor 3");
          } else if (thisFloor.equals(Floor.FloorL1)) {
            importantCirclesFL1PH.add(startCircle);
            los.add("Floor L1");
          } else if (thisFloor.equals(Floor.FloorL2)) {
            importantCirclesFL2PH.add(startCircle);
            los.add("Floor L2");
          }
        }
        // if floors are changing
        if (thisFloor != nextFloor) {
          // displays yellow on last node on the current floor
          switchFloorsCircle = new Circle(startX, startY, 6.0, Color.YELLOW);
          if (thisFloor.equals(Floor.Floor1)) {
            importantCirclesF1PH.add(switchFloorsCircle);
          } else if (thisFloor.equals(Floor.Floor2)) {
            importantCirclesF2PH.add(switchFloorsCircle);
          } else if (thisFloor.equals(Floor.Floor3)) {
            importantCirclesF3PH.add(switchFloorsCircle);
          } else if (thisFloor.equals(Floor.FloorL1)) {
            importantCirclesFL1PH.add(switchFloorsCircle);
          } else if (thisFloor.equals(Floor.FloorL2)) {
            importantCirclesFL2PH.add(switchFloorsCircle);
          }
          // displays yellow on the first node of the next floor
          switchFloorsCircle = new Circle(endX, endY, 6.0, Color.ORANGE);
          if (nextFloor.equals(Floor.Floor1)) {
            importantCirclesF1PH.add(switchFloorsCircle);
            currFloor = "Floor 1";
          } else if (nextFloor.equals(Floor.Floor2)) {
            importantCirclesF2PH.add(switchFloorsCircle);
            currFloor = "Floor 2";
          } else if (nextFloor.equals(Floor.Floor3)) {
            importantCirclesF3PH.add(switchFloorsCircle);
            currFloor = "Floor 3";
          } else if (nextFloor.equals(Floor.FloorL1)) {
            importantCirclesFL1PH.add(switchFloorsCircle);
            currFloor = "Floor L1";
          } else if (nextFloor.equals(Floor.FloorL2)) {
            importantCirclesFL2PH.add(switchFloorsCircle);
            currFloor = "Floor L2";
          }
          los.add(currFloor);
        }
        if (i == pfe.getPathEntities().size() - 2) {
          endCircle = new Circle(endX, endY, 10.0, Color.GREEN);
          if (nextFloor.equals(Floor.Floor1)) {
            importantCirclesF1PH.add(endCircle);
          } else if (nextFloor.equals(Floor.Floor2)) {
            importantCirclesF2PH.add(endCircle);
          } else if (nextFloor.equals(Floor.Floor3)) {
            importantCirclesF3PH.add(endCircle);
          } else if (nextFloor.equals(Floor.FloorL1)) {
            importantCirclesFL1PH.add(endCircle);
          } else if (nextFloor.equals(Floor.FloorL2)) {
            importantCirclesFL2PH.add(endCircle);
          }
        }
        Line line = new Line(startX, startY, endX, endY);
        line.setFill(Color.BLACK);
        line.setStrokeWidth(2.0);
        if (thisFloor != nextFloor) {
          // line stops at this point
        } else {
          if (thisFloor.equals(Floor.Floor1)) {
            floor1LinesPlaceholder.add(line);
          } else if (thisFloor.equals(Floor.Floor2)) {
            floor2LinesPlaceholder.add(line);
          } else if (thisFloor.equals(Floor.Floor3)) {
            floor3LinesPlaceholder.add(line);
          } else if (thisFloor.equals(Floor.FloorL1)) {
            floorL1LinesPlaceholder.add(line);
          } else if (thisFloor.equals(Floor.FloorL2)) {
            floorL2LinesPlaceholder.add(line);
          }
        }
        if (startX != endX) {
          if (startX > endX) {
            currDir = "West";
          } else {
            currDir = "East";
          }
        }
        if (startY != endY) {
          if (startY > endY) {
            if (currDir.equals("East")) {
              currDir = "Northeast";
            } else if (currDir.equals("West")) {
              currDir = "Northwest";
            } else {
              currDir = "North";
            }
          } else {
            if (currDir.equals("East")) {
              currDir = "Southeast";
            } else if (currDir.equals("West")) {
              currDir = "Southwest";
            } else {
              currDir = "South";
            }
          }
          // if (direction == null) direction = "straight";
        }

        String direction;
        if (prevDirection == null) {
          direction = "straight";
        } else if (prevDirection.equals("North")) {
          switch (currDir) {
            case "East":
              direction = "right";
              break;
            case "West":
              direction = "left";
              break;
            case "Northeast":
              direction = "right";
              break;
            case "Northwest":
              direction = "left";
              break;
            case "Southeast":
              direction = "right";
              break;
            case "Southwest":
              direction = "left";
              break;
            default:
              direction = "straight";
              break;
          }
        } else if (prevDirection.equals("South")) {
          switch (currDir) {
            case "East":
              direction = "left";
              break;
            case "West":
              direction = "right";
              break;
            case "Northeast":
              direction = "right";
              break;
            case "Northwest":
              direction = "left";
              break;
            case "Southeast":
              direction = "right";
              break;
            case "Southwest":
              direction = "left";
              break;
            default:
              direction = "straight";
              break;
          }
        } else if (prevDirection.equals("West")) {
          switch (currDir) {
            case "North":
              direction = "right";
              break;
            case "South":
              direction = "left";
              break;
            case "Northeast":
              direction = "right";
              break;
            case "Northwest":
              direction = "right";
              break;
            case "Southeast":
              direction = "left";
              break;
            case "Southwest":
              direction = "left";
              break;
            default:
              direction = "straight";
              break;
          }
        } else if (prevDirection.equals("East")) {
          switch (currDir) {
            case "North":
              direction = "left";
              break;
            case "South":
              direction = "right";
              break;
            case "Northeast":
              direction = "left";
              break;
            case "Northwest":
              direction = "left";
              break;
            case "Southeast":
              direction = "right";
              break;
            case "Southwest":
              direction = "right";
              break;
            default:
              direction = "straight";
              break;
          }
        } else if (prevDirection.equals("Northeast")) {
          if (currDir.equals("North") || currDir.equals("Northwest") || currDir.equals("West")) {
            direction = "left";
          } else {
            direction = "right";
          }
        } else if (prevDirection.equals("Northwest")) {
          if (currDir.equals("West") || currDir.equals("Southwest") || currDir.equals("South")) {
            direction = "left";
          } else {
            direction = "right";
          }
        } else if (prevDirection.equals("Southwest")) {
          if (currDir.equals("South") || currDir.equals("Southeast") || currDir.equals("East")) {
            direction = "left";
          } else {
            direction = "right";
          }
        } else if (prevDirection.equals("Southeast")) {
          if (currDir.equals("East") || currDir.equals("Northeast") || currDir.equals("North")) {
            direction = "left";
          } else {
            direction = "right";
          }
        } else {
          direction = "straight";
        }
        prevDirection = currDir;

        /*if (startY > endY && Math.abs(startX - endX) < 100) { // if Right angle going North
          System.out.println("Going North");
          if (currDir.equals("East")) {
            System.out.println("From East");
            direction = "Left";
          } else if (currDir.equals("West")) {
            System.out.println("From West");
            direction = "Right";
          }
        } else if (startY < endY && Math.abs(startX - endX) < 100) {
          System.out.println("Going South");
          if (currDir.equals("East")) {
            System.out.println("From East");
            direction = "Right";
          } else if (currDir.equals("West")) {
            System.out.println("From West");
            direction = "Left";
          }
        } else if (startX > endX && Math.abs(startY - endY) < 100) {
          System.out.println("Going ");
          if (currDir.equals("North")) {
            System.out.println("From North");
            direction = "Left";
          } else if (currDir.equals("South")) {
            direction = "Right";
          }
        } else if (endX > startX && Math.abs(startY - endY) < 100) {
          if (currDir.equals("North")) {
            direction = "Right";
          } else if (currDir.equals("South")) {
            direction = "Left";
          }
        }*/

        String command;
        if (direction.equals("straight")) {
          command = ". Go " + direction;
        } else {
          command = " Turn " + direction + " and go straight";
        }

        int stepNum = i + 1;
        textDir =
            textDir
                + stepNum
                + command
                // + ". Go "
                // + currDir
                // + direction
                + " until you reach "
                + currLocationName
                + ".\n";
        // + direction;
        currDir = "";
      }
      textualDirections.setText(textDir);
      floor1Lines.addAll(floor1LinesPlaceholder);
      floor2Lines.addAll(floor2LinesPlaceholder);
      floor3Lines.addAll(floor3LinesPlaceholder);
      floorL1Lines.addAll(floorL1LinesPlaceholder);
      floorL2Lines.addAll(floorL2LinesPlaceholder);
      importantCirclesF1.addAll(importantCirclesF1PH);
      importantCirclesF2.addAll(importantCirclesF2PH);
      importantCirclesF3.addAll(importantCirclesF3PH);
      importantCirclesFL1.addAll(importantCirclesFL1PH);
      importantCirclesFL2.addAll(importantCirclesFL2PH);
      if (floor.getImage().equals(floor1)) {
        anchorPane.getChildren().addAll(floor1Lines);
        anchorPane.getChildren().addAll(importantCirclesF1);
        centerOnPoint(importantCirclesF1);
      } else if (floor.getImage().equals(floor2)) {
        anchorPane.getChildren().addAll(floor2Lines);
        anchorPane.getChildren().addAll(importantCirclesF2);
        centerOnPoint(importantCirclesF2);
      } else if (floor.getImage().equals(floor3)) {
        anchorPane.getChildren().addAll(floor3Lines);
        anchorPane.getChildren().addAll(importantCirclesF3);
        centerOnPoint(importantCirclesF3);
      } else if (floor.getImage().equals(floorL1)) {
        anchorPane.getChildren().addAll(floorL1Lines);
        anchorPane.getChildren().addAll(importantCirclesFL1);
        centerOnPoint(importantCirclesFL1);
      } else if (floor.getImage().equals(floorL2)) {
        anchorPane.getChildren().addAll(floorL2Lines);
        anchorPane.getChildren().addAll(importantCirclesFL2);
        centerOnPoint(importantCirclesFL2);
      }
      displayFloorOrder(los);
    }
  }

  public void generateFloor1Nodes() {
    circlesOnFloor.clear();
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
      // find the location that shares the node id
      // if the node type of the location is a hallway, don't add it
      for (int j = 0; j < dataBase.getMoveDAO().getAll().size(); j++) {
        if (dataBase.getMoveDAO().getAll().get(j).getNode().getNodeID()
            == dataBase.getNodeDAO().getAll().get(i).getNodeID()) {
          if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor1)) {
            if (!dataBase
                .getMoveDAO()
                .getAll()
                .get(j)
                .getLocation()
                .getNodeType()
                .equals(NodeType.HALL)) {
              floor1Nodes.add(dataBase.getNodeDAO().getAll().get(i));
            }
          }
        }
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
    circlesOnFloor = floor1Circles;
    // anchorPane.getChildren().addAll(floor1Circles);
  }

  public void generateFloor2Nodes() {
    circlesOnFloor.clear();
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
      for (int j = 0; j < dataBase.getMoveDAO().getAll().size(); j++) {
        if (dataBase.getMoveDAO().getAll().get(j).getNode().getNodeID()
            == dataBase.getNodeDAO().getAll().get(i).getNodeID()) {
          if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor2)) {
            if (!dataBase
                .getMoveDAO()
                .getAll()
                .get(j)
                .getLocation()
                .getNodeType()
                .equals(NodeType.HALL)) {
              floor2Nodes.add(dataBase.getNodeDAO().getAll().get(i));
            }
          }
        }
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
    circlesOnFloor.addAll(floor2Circles);
    // anchorPane.getChildren().addAll(floor2Circles);
  }

  public void generateFloor3Nodes() {
    circlesOnFloor.clear();
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
      for (int j = 0; j < dataBase.getMoveDAO().getAll().size(); j++) {
        if (dataBase.getMoveDAO().getAll().get(j).getNode().getNodeID()
            == dataBase.getNodeDAO().getAll().get(i).getNodeID()) {
          if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor3)) {
            if (!dataBase
                .getMoveDAO()
                .getAll()
                .get(j)
                .getLocation()
                .getNodeType()
                .equals(NodeType.HALL)) {
              floor3Nodes.add(dataBase.getNodeDAO().getAll().get(i));
            }
          }
        }
      }
    }
    for (int i = 0; i < floor3Nodes.size(); i++) {
      Circle newCircle =
          new Circle(
              floor3Nodes.get(i).getXCoord(), floor3Nodes.get(i).getYCoord(), 10.0, Color.RED);
      floor3Circles.add(newCircle);
      Node aNode = floor3Nodes.get(i);
      newCircle.setOnMouseClicked(event -> colorEvent(newCircle, aNode));
    }
    circlesOnFloor.addAll(floor3Circles);
    // anchorPane.getChildren().addAll(floor3Circles);
  }

  public void generateFloorL1Nodes() {
    circlesOnFloor.clear();
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
      // find the location that shares the node id
      // if the node type of the location is a hallway, don't add it
      for (int j = 0; j < dataBase.getMoveDAO().getAll().size(); j++) {
        if (dataBase.getMoveDAO().getAll().get(j).getNode().getNodeID()
            == dataBase.getNodeDAO().getAll().get(i).getNodeID()) {
          if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.FloorL1)) {
            if (!dataBase
                .getMoveDAO()
                .getAll()
                .get(j)
                .getLocation()
                .getNodeType()
                .equals(NodeType.HALL)) {
              floorL1Nodes.add(dataBase.getNodeDAO().getAll().get(i));
            }
          }
        }
      }
    }
    for (int i = 0; i < floorL1Nodes.size(); i++) {
      Circle newCircle =
          new Circle(
              floorL1Nodes.get(i).getXCoord(), floorL1Nodes.get(i).getYCoord(), 10.0, Color.RED);
      floorL1Circles.add(newCircle);
      Node aNode = floorL1Nodes.get(i);
      newCircle.setOnMouseClicked(event -> colorEvent(newCircle, aNode));
    }
    circlesOnFloor = floorL1Circles;
    // anchorPane.getChildren().addAll(floorL1Circles);
  }

  public void generateFloorL2Nodes() {
    circlesOnFloor.clear();
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
      // find the location that shares the node id
      // if the node type of the location is a hallway, don't add it
      for (int j = 0; j < dataBase.getMoveDAO().getAll().size(); j++) {
        if (dataBase.getMoveDAO().getAll().get(j).getNode().getNodeID()
            == dataBase.getNodeDAO().getAll().get(i).getNodeID()) {
          if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.FloorL2)) {
            if (!dataBase
                .getMoveDAO()
                .getAll()
                .get(j)
                .getLocation()
                .getNodeType()
                .equals(NodeType.HALL)) {
              floorL2Nodes.add(dataBase.getNodeDAO().getAll().get(i));
            }
          }
        }
      }
    }
    for (int i = 0; i < floorL2Nodes.size(); i++) {
      Circle newCircle =
          new Circle(
              floorL2Nodes.get(i).getXCoord(), floorL2Nodes.get(i).getYCoord(), 10.0, Color.RED);
      floorL2Circles.add(newCircle);
      Node aNode = floorL2Nodes.get(i);
      newCircle.setOnMouseClicked(event -> colorEvent(newCircle, aNode));
    }
    circlesOnFloor = floorL2Circles;
    // anchorPane.getChildren().addAll(floorL2Circles);
  }

  public void clearFields() {
    floorOrderLabel.setText("");
    datePicker.setValue(LocalDate.now());
    anchorPane.getChildren().removeAll(floor1Lines);
    anchorPane.getChildren().removeAll(floor2Lines);
    anchorPane.getChildren().removeAll(floor3Lines);
    anchorPane.getChildren().removeAll(floorL1Lines);
    anchorPane.getChildren().removeAll(floorL2Lines);
    pathLines.clear();
    floor1Lines.clear();
    floor2Lines.clear();
    floor3Lines.clear();
    floorL1Lines.clear();
    floorL2Lines.clear();
    anchorPane.getChildren().removeAll(importantCirclesF1);
    anchorPane.getChildren().removeAll(importantCirclesF2);
    anchorPane.getChildren().removeAll(importantCirclesF3);
    anchorPane.getChildren().removeAll(importantCirclesFL1);
    anchorPane.getChildren().removeAll(importantCirclesFL2);
    importantCirclesF1.clear();
    importantCirclesF2.clear();
    importantCirclesF3.clear();
    importantCirclesFL1.clear();
    importantCirclesFL2.clear();
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
      if (displayAllNodesToggle.isSelected()) {
        showLocationNames2();
      }
    } else if (floor.getImage().equals(floor2)) {
      for (int i = 0; i < floor2Circles.size(); i++) {
        floor2Circles.get(i).setFill(Color.RED);
      }
      if (displayAllNodesToggle.isSelected()) {
        showLocationNames2();
      }
    } else if (floor.getImage().equals(floor3)) {
      for (int i = 0; i < floor3Circles.size(); i++) {
        floor3Circles.get(i).setFill(Color.RED);
      }
      if (displayAllNodesToggle.isSelected()) {
        showLocationNames2();
      }
    } else if (floor.getImage().equals(floorL1)) {
      for (int i = 0; i < floorL1Circles.size(); i++) {
        floorL1Circles.get(i).setFill(Color.RED);
      }
      if (displayAllNodesToggle.isSelected()) {
        showLocationNames2();
      }
    } else if (floor.getImage().equals(floorL2)) {
      for (int i = 0; i < floorL2Circles.size(); i++) {
        floorL2Circles.get(i).setFill(Color.RED);
      }
      if (displayAllNodesToggle.isSelected()) {
        showLocationNames2();
      }
    }
    anchorPane.getChildren().removeAll(pathLines);
  }

  public void initialize() {
    if (ActiveUser.getInstance().isLoggedIn()) {
      pathfindingToLogin.setVisible(false);
      if (!ActiveUser.getInstance().getPermission().equals(Permission.ADMIN)) {
        addMoveBtn.setVisible(false);
        // addMessageButton.setVisible(false);
      }
    } else {
      addMoveBtn.setVisible(false);
      // addMessageButton.setVisible(false);
    }

    datePicker.setValue(LocalDate.now());

    algList.getItems().addAll("AStar", "Dijkstra's", "Breadth-first search", "Depth-first search");
    pathfindingToLogin.setOnMouseClicked(event -> Navigation.navigate(Screen.LOGIN_PAGE));

    displayAllNodesToggle.setOnMouseClicked(event -> showNodes());
    // displayLocationNamesToggle.setOnMouseClicked(event -> showLocations());
    displayLocationNamesToggle.setOnMouseClicked(event -> showLocationNames2());

    dataBase = DataBaseRepository.getInstance();
    clearFieldsButton.setOnMouseClicked(event -> clearFields());
    setLocationLongNames();

    stackPane.setPrefSize(714, 313);
    /*
    // tried to make a function that sets the floor to the floor of the currently selected location in the starting location list
    if (!startingLocationList.getSelectionModel().isEmpty()) {
      startingLocationList.setOnAction(event -> goToStartingFloor());
    }
    */
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
    mapPane.zoomTo(0.15, 0.15, new Point2D(2500, 1700));
    floor1Button.setOnMouseClicked(event -> toFloor1());
    floor2Button.setOnMouseClicked(event -> toFloor2());
    floor3Button.setOnMouseClicked(event -> toFloor3());
    floorL1Button.setOnMouseClicked(event -> toFloorL1());
    floorL2Button.setOnMouseClicked(event -> toFloorL2());

    circlesOnFloor = floor1Circles;
    nodeList = floor1Nodes;
    changeFloorButtonColors();
    // findPathButton.setOnMouseClicked(event -> showPathNew(nodeList, pathLines));
    // findPathButton.setOnMouseClicked(event -> showPathAcrossFloors(nodeList, pathLines));
    findPathButton.setOnMouseClicked(
        event -> {
          showPathTesting();
          prevDirection = null;
        });

    addMoveBtn.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.PATHFINDING_POPUP));
  }
}
