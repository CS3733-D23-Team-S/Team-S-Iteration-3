package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.*;
import edu.wpi.teamname.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.ToggleSwitch;

public class NewMapEditorController {

  enum Move {
    ADD,
    MOVE,
    REMOVE
  }

  Node selection;
  boolean editingNodes = true;
  boolean edgeShow = true;
  DataBaseRepository repo = DataBaseRepository.getInstance();
  Move mode;
  StackPane stackPane = new StackPane();
  AnchorPane anchorPane = new AnchorPane();
  @FXML Label nodeLable;
  @FXML Label nodeLocations;
  @FXML ComboBox<String> floorSelect;
  @FXML MFXButton addNode;
  @FXML MFXButton moveNode;
  @FXML MFXButton removeNode;
  @FXML GesturePane mapPane;
  @FXML ToggleSwitch editToggle;
  @FXML ToggleSwitch showEdges;

  ImageView floor;
  Floor currFloor = Floor.Floor1;
  Circle prevSelection;
  HashMap<Circle, Node> listOfCircles = new HashMap<>();
  Image floor1 = new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png")));
  Image floor2 = new Image(String.valueOf(Main.class.getResource("images/02_thesecondfloor.png")));
  Image floor3 = new Image(String.valueOf(Main.class.getResource("images/03_thethirdfloor.png")));
  Image floorL1 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel1.png")));
  Image floorL2 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel2.png")));

  public void initialize() {
    stackPane.setPrefSize(1200.0, 742.0);
    floor = new ImageView(floor1);
    floor.setImage(floor1);
    stackPane.getChildren().add(floor);
    stackPane.getChildren().add(anchorPane);
    anchorPane.setBackground(Background.fill(Color.TRANSPARENT));
    mapPane.setContent(stackPane);
    mapPane.setMinScale(.0001);
    mapPane.setMaxScale(0.9);
    mapPane.zoomTo(.5, new Point2D(2000, 1500));

    floorSelect.getItems().add("Lower 1");
    floorSelect.getItems().add("Lower 2");
    floorSelect.getItems().add("Floor 1");
    floorSelect.getItems().add("Floor 2");
    floorSelect.getItems().add("Floor 3");
    editToggle.setOnMouseClicked(event -> editingNodes = !editingNodes);
    showEdges.setOnMouseClicked(event -> edgeShow = !edgeShow);
    addNode.setOnMouseClicked(
        event -> {
          resetColors();
          addNode.setStyle("-fx-background-color: #122E59");
          mode = Move.ADD;
        });
    moveNode.setOnMouseClicked(
        event -> {
          resetColors();
          moveNode.setStyle("-fx-background-color: #122E59");
          mode = Move.MOVE;
        });
    removeNode.setOnMouseClicked(
        event -> {
          resetColors();
          removeNode.setStyle("-fx-background-color: #122E59");
          mode = Move.REMOVE;
        });
    generateFloorNodes();
    floorSelect.setOnAction(
        event -> {
          int index = floorSelect.getSelectionModel().getSelectedIndex();
          floor.setImage(swapFloor(index));
          mapPane.centreOn(new Point2D(2000, 1500));
          generateFloorNodes();
        });
  }

  private void generateFloorNodes() {
    anchorPane.getChildren().removeAll(listOfCircles.keySet());
    listOfCircles.clear();
    for (Node floorNode : repo.getNodeDAO().getAll()) {
      if (!floorNode.getFloor().equals(currFloor)) continue;
      Circle newCircle = new Circle(floorNode.getXCoord(), floorNode.getYCoord(), 10.0, Color.RED);
      newCircle.setOnMouseClicked(
          event -> {
            newCircle.setStroke(Color.YELLOW);
            newCircle.setStrokeWidth(3);
            prevSelection.setStroke(Color.TRANSPARENT);
            prevSelection = newCircle;
            selection = listOfCircles.get(newCircle);
          });
      listOfCircles.put(newCircle, floorNode);
      prevSelection = newCircle;
    }

    anchorPane.getChildren().addAll(listOfCircles.keySet());
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
    addNode.setStyle("-fx-background-color: #1D3D94");
    moveNode.setStyle("-fx-background-color: #1D3D94");
    removeNode.setStyle("-fx-background-color: #1D3D94");
  }
}
