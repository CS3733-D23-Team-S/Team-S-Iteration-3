package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Edge;
import edu.wpi.teamname.DAOs.orms.Floor;
import edu.wpi.teamname.DAOs.orms.Node;
import edu.wpi.teamname.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;

public class NewMapEditorController {

  enum Move {
    ADD_REMOVE,
    MOVE,
    REMOVE
  }

  Node nodeToCircle;
  boolean edgeShow = false;
  DataBaseRepository repo = DataBaseRepository.getInstance();
  Move mode;
  StackPane stackPane = new StackPane();
  AnchorPane anchorPane = new AnchorPane();
  PopOver popOver = new PopOver();

  @FXML ComboBox<String> floorSelect;
  @FXML MFXButton addAndRemove;
  @FXML MFXButton moveNode;
  @FXML MFXButton addLocation;
  @FXML GesturePane mapPane;
  @FXML ToggleSwitch showEdges;
  // ______________________________________________
  AddNodeController addNodeController;
  @FXML private GridPane addNodeMenu;
  @FXML private Button addNodeButton;
  @FXML private TextField buildingEnter;
  @FXML private TextField nodeIDEnter;

  // ______________________________________________
  EditNodeController editNodeController;
  @FXML private VBox editMenu;

  // ______________________________________________
  AddLocationController addLocationController;
  @FXML BorderPane locationMenu;

  // ______________________________________________
  ImageView floor;
  Floor currFloor = Floor.Floor1;
  Circle prevSelection;
  double imageX, imageY;
  private final Circle deleteReferenceCircle = new Circle(0, 0, 0, Color.TRANSPARENT);
  HashMap<Circle, Node> listOfCircles = new HashMap<>();

  List<Text> listOfLabels = new ArrayList<>();
  List<Line> lines = new ArrayList<>();
  Image floor1 = new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png")));
  Image floor2 = new Image(String.valueOf(Main.class.getResource("images/02_thesecondfloor.png")));
  Image floor3 = new Image(String.valueOf(Main.class.getResource("images/03_thethirdfloor.png")));
  Image floorL1 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel1.png")));
  Image floorL2 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel2.png")));

  public void initialize() throws IOException {

    initAddNodeController();
    initEditController();
    initAddLocationController();
    initPopOver();
    //    stackPane.setPrefSize(1200.0, 742.0);
    floor = new ImageView(floor1);
    floor.setImage(floor1);
    stackPane.getChildren().add(floor);
    stackPane.getChildren().add(anchorPane);
    anchorPane.setBackground(Background.fill(Color.TRANSPARENT));
    //    anchorPane.setPickOnBounds(true);
    //    mapPane.setPickOnBounds(false);
    mapPane.setContent(stackPane);
    mapPane.setMinScale(.0001);
    mapPane.setMaxScale(0.9);
    mapPane.zoomTo(.5, new Point2D(2000, 1500));
    floorSelect.getItems().add("Lower 1");
    floorSelect.getItems().add("Lower 2");
    floorSelect.getItems().add("Floor 1");
    floorSelect.getItems().add("Floor 2");
    floorSelect.getItems().add("Floor 3");
    anchorPane.setOnContextMenuRequested(this::updatePopOver);

    showEdges.setOnMouseClicked(event -> drawEdges());
    addAndRemove.setOnMouseClicked(
        event -> {
          resetColors();
          addAndRemove.setStyle("-fx-background-color: #122E59");
          mode = Move.ADD_REMOVE;
          allowDelete();
        });
    moveNode.setOnMouseClicked(
        event -> {
          resetColors();
          moveNode.setStyle("-fx-background-color: #122E59");
          mode = Move.MOVE;
          stopDelete();
        });
    addLocation.setOnMouseClicked(event -> updateLocationPopOver());
    generateFloorNodes();
    floorSelect.setOnAction(
        event -> {
          int index = floorSelect.getSelectionModel().getSelectedIndex();
          floor.setImage(swapFloor(index));
          mapPane.centreOn(new Point2D(2000, 1500));
          generateFloorNodes();
          anchorPane.getChildren().removeAll(lines);
          lines.clear();
          drawEdges();
        });
  }

  private void allowDelete() {
    Platform.runLater(
        () ->
            stackPane
                .getScene()
                .addEventHandler(
                    KeyEvent.KEY_PRESSED,
                    (event) -> {
                      //                    System.out.println(event.getCode());
                      if (event.getCode().equals(KeyCode.DELETE)
                          || event.getCode().equals(KeyCode.BACK_SPACE)) deleteNode(prevSelection);
                    }));
  }

  private void stopDelete() {
    stackPane
        .getScene()
        .removeEventHandler(
            KeyEvent.KEY_PRESSED,
            (event) -> {
              System.out.println(event.getCode());
              if (event.getCode().equals(KeyCode.DELETE)
                  || event.getCode().equals(KeyCode.BACK_SPACE)) deleteNode(prevSelection);
            });
  }

  private void initAddNodeController() {
    try {
      FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/addNodePopUp.fxml"));
      addNodeMenu = loader.load();
      addNodeController = loader.getController();
      this.addNodeButton = addNodeController.getAddButton();
      this.nodeIDEnter = addNodeController.getNodeIDEnter();
      this.buildingEnter = addNodeController.getBuildingEnter();

      this.addNodeButton.setOnMouseClicked(
          event -> {
            if (addNodeController.checkFieldsFilled()) {
              addNode(
                  Integer.parseInt(nodeIDEnter.getText()),
                  (int) Math.round(imageX),
                  (int) Math.round(imageY),
                  buildingEnter.getText());
              //              addNodeEvent(event);
            }
          });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void initAddLocationController() {
    try {
      FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/addLocation.fxml"));
      locationMenu = loader.load();
      addLocationController = loader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void initEditController() {
    try {
      FXMLLoader loader = new FXMLLoader(Main.class.getResource("views/EditNodePopOver.fxml"));
      editMenu = loader.load();
      editNodeController = loader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //	public void addNodeEvent(MouseEvent event) {
  //		Circle newCircle = new Circle(event.getX(), event.getY(), 10.0, Color.RED);
  //
  //		anchorPane.getChildren().add(newCircle);
  //	}

  public void addNode(int nodeID, int xCoord, int yCoord, String building) {

    if (repo.getNodeDAO().getNodes().containsKey(nodeID)) {
      System.out.println("Already in the keyset");
      return;
    }
    Node node = new Node(nodeID, xCoord, yCoord, currFloor, building);
    repo.getNodeDAO().add(node);
    Circle newCircle = new Circle(xCoord, yCoord, 10.0, Color.RED);
    for (Node potentialEdge : repo.getNodeDAO().getAll()) {
      // Likely to be an elevator or stairs
      if (calcWeight(node, potentialEdge) < 60) {
        Edge edge = new Edge(node, potentialEdge);
        repo.getEdgeDAO().add(edge);
      }
    }
    initCircle(newCircle);
    listOfCircles.put(newCircle, repo.getNodeDAO().get(nodeID));
    generateFloorNodes();
  }

  private double calcWeight(Node node, Node potentialEdge) {
    return Math.sqrt(
        Math.pow((node.getXCoord() - potentialEdge.getXCoord()), 2)
            + Math.pow((node.getYCoord() - potentialEdge.getYCoord()), 2));
  }

  private void deleteNode(Circle circle) {
    if (circle == deleteReferenceCircle) return;
    Node node = listOfCircles.get(circle);
    repo.getNodeDAO().delete(node);
    listOfCircles.remove(circle);
    anchorPane.getChildren().remove(circle);
    prevSelection = deleteReferenceCircle;
  }

  private void generateFloorNodes() {
    anchorPane.getChildren().removeAll(listOfCircles.keySet());
    listOfCircles.clear();
    for (Node floorNode : repo.getNodeDAO().getAll()) {
      if (!floorNode.getFloor().equals(currFloor)) continue;
      Circle newCircle = new Circle(floorNode.getXCoord(), floorNode.getYCoord(), 10.0, Color.RED);
      initCircle(newCircle);
      listOfCircles.put(newCircle, floorNode);
      prevSelection = newCircle;
    }
    anchorPane.getChildren().addAll(listOfCircles.keySet());
  }

  private void showNodeIDs() {
    anchorPane.getChildren().removeAll(listOfLabels);
    listOfLabels.clear();
    for (Circle circle : listOfCircles.keySet()) {
      Node node = listOfCircles.get(circle);
      if (node.getFloor() != currFloor) continue;
      Text newText = new Text(String.valueOf(node.getNodeID()));
      newText.setFill(Color.WHITE);
      newText.setStroke(Color.BLACK);
      newText.setX(circle.getCenterX() - 20);
      newText.setY(circle.getCenterY() + 20);
      listOfLabels.add(newText);
    }
    anchorPane.getChildren().addAll(listOfLabels);
  }

  private void initCircle(Circle circle) {
    circle.setViewOrder(0);
    circle.setMouseTransparent(false);
    circle.setOnContextMenuRequested(
        event -> {
          System.out.println("Circled clicked");
          updateNodePopOver(circle);
          event.consume();
        });
    circle.setOnMouseClicked(
        event -> {
          circle.setStroke(Color.YELLOW);
          circle.setStrokeWidth(3);
          prevSelection.setStroke(Color.TRANSPARENT);
          prevSelection = circle;
          nodeToCircle = listOfCircles.get(circle);
        });
    circle.setOnMouseDragged(
        event -> {
          if (mode == Move.MOVE) {
            prevSelection = circle;
            int currX = (int) event.getX();
            int currY = (int) event.getY();
            circle.setCenterX(currX);
            circle.setCenterY(currY);
            circle.setStroke(Color.YELLOW);
            circle.setStrokeWidth(3);
            Node temp = listOfCircles.get(circle);
            temp.setXCoord(currX);
            temp.setYCoord(currY);
            anchorPane.getChildren().removeAll(lines);
            lines.clear();
            drawEdges();
            event.consume();
          }
        });
    circle.setOnMouseReleased(
        event -> {
          if (mode == Move.MOVE && listOfCircles.get(prevSelection) != null) {
            repo.getNodeDAO().updateNodeLocation(listOfCircles.get(prevSelection));
            System.out.println("Sent node location update");
            //            System.out.println(listOfCircles.get(circle).toString());
            event.consume();
          }
        });
  }

  private void drawEdges() {
    edgeShow = showEdges.isSelected();
    anchorPane.getChildren().removeAll(lines);
    lines.clear();
    if (!edgeShow) {
      return;
    }
    for (Node floorNode : repo.getNodeDAO().getAll()) {
      if (!floorNode.getFloor().equals(currFloor)) continue;
      HashSet<Integer> currNeighbors = repo.getEdgeDAO().getNeighbors(floorNode.getNodeID());
      if (currNeighbors == null) continue;
      for (Integer currNeighbor : currNeighbors) {
        Node curr = repo.getNodeDAO().get(currNeighbor);
        if (!curr.getFloor().equals(currFloor)) continue;
        Line line =
            new Line(
                floorNode.getXCoord(), floorNode.getYCoord(), curr.getXCoord(), curr.getYCoord());
        line.setFill(Color.BLACK);
        line.setStrokeWidth(5.0);
        line.setViewOrder(1);
        lines.add(line);
      }
    }
    anchorPane.getChildren().addAll(lines);
  }

  private Image swapFloor(int index) {
    switch (index) {
      case 0:
        currFloor = Floor.FloorL1;
        return floorL1;
      case 1:
        currFloor = Floor.FloorL2;
        return floorL2;
      case 2:
        currFloor = Floor.Floor1;
        return floor1;
      case 3:
        currFloor = Floor.Floor2;
        return floor2;
      case 4:
        currFloor = Floor.Floor3;
        return floor3;
    }
    return floor1;
  }

  private void resetColors() {
    addAndRemove.setStyle("-fx-background-color: #1D3D94");
    moveNode.setStyle("-fx-background-color: #1D3D94");
    addLocation.setStyle("-fx-background-color: #1D3D94");
  }

  private void updatePopOver(ContextMenuEvent event) {
    popOver.setTitle("Add Node?");
    popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
    nodeIDEnter.clear();
    buildingEnter.clear();
    addNodeController.getWarning().setText("");
    popOver.setContentNode(addNodeMenu);
    //    Point2D localCoords = mapPane.getAffine().transform(event.getX(), event.getY());
    //    Point2D localCoords = mapPane.localToParent(event.getX(), event.getY());
    imageX = event.getX();
    imageY = event.getY();
    //    System.out.println("X " + imageX + "; Y " + imageY);
    popOver.show(anchorPane, event.getScreenX(), event.getScreenY());
  }

  private void initPopOver() {
    popOver.setHeaderAlwaysVisible(true);
    popOver.setAutoHide(true);
    popOver.setOnAutoHide(
        event -> {
          anchorPane.getChildren().removeAll(listOfLabels);
          listOfLabels.clear();
          generateFloorNodes();
        });
    popOver.setPrefSize(10, 30);
    popOver.setMaxSize(PopupControl.USE_PREF_SIZE, PopupControl.USE_PREF_SIZE);
    popOver.setMinSize(PopupControl.USE_PREF_SIZE, PopupControl.USE_PREF_SIZE);
    popOver.setDetachable(false);
  }

  private void updateNodePopOver(Circle circle) {
    popOver.setTitle("Node Information");
    popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
    showNodeIDs();
    editNodeController.setInfo(listOfCircles.get(circle));
    popOver.setContentNode(editMenu);
    popOver.show(circle);
  }

  private void updateLocationPopOver() {
    popOver.setTitle("Edit Locations");
    popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
    popOver.setContentNode(locationMenu);
    popOver.show(addLocation);
  }
}
