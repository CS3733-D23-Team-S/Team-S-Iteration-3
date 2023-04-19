package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.*;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

public class BetterMapEditorController {
  @FXML MFXButton floorLowerTwoBTN;
  @FXML MFXButton floorLowerOneBTN;
  @FXML MFXButton floorThreeBTN;
  @FXML MFXButton floorTwoBTN;
  @FXML MFXButton floorOneBTN;
  @FXML MFXButton GroundBTN;
  @FXML MFXButton addNodeBTN;
  @FXML MFXButton removeNodeBTN;
  @FXML MFXButton editNodeBTN;
  @FXML MFXButton addLocationBTN;
  @FXML MFXButton removeLocationBTN;
  @FXML MFXButton editLocationBTN;

  @FXML MFXTextField ntNodeIDTF;
  @FXML MFXTextField xCoordTF;
  @FXML MFXTextField yCoordTF;
  @FXML MFXTextField floorTF;
  @FXML MFXTextField buildingTF;
  @FXML MFXTextField nodeTypeTF;
  @FXML MFXTextField longNameTF;
  @FXML MFXTextField shortNameTF;
  @FXML MFXTextField locNodeIDTXT;

  @FXML MFXButton pathfindingToHomeButton;
  @FXML MFXButton clearFieldsButton;

  @FXML MFXTextField mainTF;

  @FXML GesturePane mapPane;

  ImageView floor;

  Image ground = new Image(String.valueOf(Main.class.getResource("images/00_thegroundfloor.png")));
  Image floorL1 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel1.png")));
  Image floorL2 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel2.png")));
  Image floor1 = new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png")));
  Image floor2 = new Image(String.valueOf(Main.class.getResource("images/02_thesecondfloor.png")));
  Image floor3 = new Image(String.valueOf(Main.class.getResource("images/03_thethirdfloor.png")));

  StackPane stackPane = new StackPane();
  AnchorPane anchorPane = new AnchorPane();

  List<Circle> theCircs = new ArrayList<>();

  List<Circle> floor1Circles = new ArrayList<>();

  public static Node thisNode;

  public boolean doingNode;

  DataBaseRepository dataBase = DataBaseRepository.getInstance();

  public void toFloor(Floor aFloor) {
    anchorPane.getChildren().clear();
    stackPane.getChildren().clear();

    if (aFloor.equals(Floor.Floor1)) {
      floor.setImage(floor1);
    } else if (aFloor.equals(Floor.Floor2)) {
      floor.setImage(floor2);
    } else if (aFloor.equals(Floor.Floor3)) {
      floor.setImage(floor3);
    } else if (aFloor.equals(Floor.FloorL1)) {
      floor.setImage(floorL1);
    } else if (aFloor.equals(Floor.FloorL2)) {
      floor.setImage(floorL2);
    }

    stackPane.getChildren().add(floor);
    stackPane.getChildren().add(anchorPane);
    anchorPane.setBackground(Background.fill(Color.TRANSPARENT));

    List<Node> temp = new ArrayList<>();
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(aFloor)) {
        temp.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }
    generateFloorNodes(temp);

    mapPane.setContent(stackPane);
    mapPane.setMinScale(.0001);
  }

  private void generateFloorNodes(List<Node> floorNodes) { // if

    List<Circle> listOfCircles = new ArrayList<>();
    List<Text> listOfLocationNames = new ArrayList<>();

    for (int i = 0; i < floorNodes.size(); i++) {

      Circle newCircle =
          new Circle(floorNodes.get(i).getXCoord(), floorNodes.get(i).getYCoord(), 10.0, Color.RED);
      listOfCircles.add(newCircle);

      Move aMove = dataBase.getMoveDAO().getLocationsAtNodeID().get(floorNodes.get(i)).get(0);
      if (aMove != null) {
        Location aLoc = dataBase.getLocationDAO().getLocationMap().get(aMove.getLocation());

        if (!aLoc.getNodeType().equals(NodeType.HALL)) {
          Text newText = new Text(aLoc.getShortName());
          newText.setFill(Color.WHITE);
          newText.setStroke(Color.BLACK);
          newText.setX(floorNodes.get(i).getXCoord() - 27);
          newText.setY(floorNodes.get(i).getYCoord() + 20);

          listOfLocationNames.add(newText);
        }
      }

      newCircle.setOnMouseClicked(
          event -> {
            int x = (int) newCircle.getCenterX();
            int y = (int) newCircle.getCenterY();
            thisNode = dataBase.getNodebyXY(x, y);
            Navigation.navigate(Screen.NODE_DETAILS);
          });
    }
    anchorPane.getChildren().addAll(listOfCircles);
    anchorPane.getChildren().addAll(listOfLocationNames);
  }

  private void clearFields() {}

  public void addNode() {
    boolean nodeExists = false;
    boolean floorValid = true;
    int ntNodeID;
    int xCoord;
    int yCoord;
    Floor floor = null;
    // if fields are empty don't do it
    // if fields are inadequate (string in integer field or some shit) don't do it
    if ((ntNodeIDTF.getText().equals(""))
        || (xCoordTF.getText().equals(""))
        || (yCoordTF.getText().equals(""))
        || (floorTF.getText().equals(""))
        || (buildingTF.getText().equals(""))) {
      mainTF.setText("Error: not all fields are filled in");
    } else {
      try {
        ntNodeID = Integer.parseInt(ntNodeIDTF.getText());
        xCoord = Integer.parseInt(xCoordTF.getText());
        yCoord = Integer.parseInt(yCoordTF.getText());
        for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
          if (ntNodeID == dataBase.getNodeDAO().getAll().get(i).getNodeID()) {
            nodeExists = true;
          }
        }
        if (nodeExists) {
          // no!!!
          mainTF.setText("Error: the entered Node ID already exists in the database");
        } else {
          if (floorTF.getText().equals("Floor1")) {
            floor = Floor.Floor1;
          } else if (floorTF.getText().equals("Floor2")) {
            floor = Floor.Floor2;
          } else if (floorTF.getText().equals("Floor3")) {
            floor = Floor.Floor3;
          } else if (floorTF.getText().equals("FloorL1")) {
            floor = Floor.FloorL1;
          } else if (floorTF.getText().equals("FloorL2")) {
            floor = Floor.FloorL2;
          } else {
            floorValid = false;
          }
          if (floorValid) {
            // good!!!

            Node newNode = new Node(ntNodeID, xCoord, yCoord, floor, buildingTF.getText());
            dataBase.getNodeDAO().add(newNode);

            mainTF.setText("Node successfully added");
          } else {
            // bad!!!
            mainTF.setText(
                "Error: invalid floor - floor must be 'Floor1', 'Floor2', 'Floor3', 'FloorL1', or 'FloorL2'");
          }
        }
      } catch (NumberFormatException e) {
        mainTF.setText("Error: make sure the Node ID, XCoord, and YCoord fields are integers");
      }
    }
  }

  public void removeNode() {
    Boolean nodeExists = false;
    int ntNodeID;
    // if node id field is empty don't remove it
    // if node id field is not an integer don't do it
    if (ntNodeIDTF.getText().equals("")) {
      mainTF.setText("Error: Node ID field is not filled in");
    } else {
      try {
        ntNodeID = Integer.parseInt(ntNodeIDTF.getText());
        for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
          if (ntNodeID == dataBase.getNodeDAO().getAll().get(i).getNodeID()) {
            nodeExists = true;
          }
        }
        if (nodeExists) {

          dataBase.getNodeDAO().delete(ntNodeID);

          mainTF.setText("Node with Node ID: " + ntNodeID + " successfully removed");
        } else {
          mainTF.setText("Error: Node ID not found");
        }
      } catch (NumberFormatException e) {
        mainTF.setText("Error: make sure the Node ID is an integer");
      }
    }
  }

  public void editNode() {
    boolean nodeExists = false;
    boolean floorValid = true;
    // if fields are empty don't edit it
    // if certain fields aren't integers don't edit it
    // if node id can't be found don't edit it
    // if node id can be found edit it
    int ntNodeID;
    int xCoord;
    int yCoord;
    Floor floor = null;
    if ((ntNodeIDTF.getText().equals(""))
        || (xCoordTF.getText().equals(""))
        || (yCoordTF.getText().equals(""))
        || (floorTF.getText().equals(""))
        || (buildingTF.getText().equals(""))) {
      mainTF.setText("Error: not all fields are filled in");
    } else {
      try {
        ntNodeID = Integer.parseInt(ntNodeIDTF.getText());
        xCoord = Integer.parseInt(xCoordTF.getText());
        yCoord = Integer.parseInt(yCoordTF.getText());

        for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
          if (ntNodeID == dataBase.getNodeDAO().getAll().get(i).getNodeID()) {
            nodeExists = true;
          }
        }
        if (nodeExists) {
          // check if floor is valid
          try {
            floor = Floor.valueOf(floorTF.getText());
          } catch (IllegalArgumentException e) {
            floorValid = false;
          }
          if (floorValid) {

            dataBase.getNodeDAO().delete(ntNodeID);
            Node newNode = new Node(ntNodeID, xCoord, yCoord, floor, buildingTF.getText());
            dataBase.getNodeDAO().add(newNode);

            mainTF.setText("Successfully edited Node");
          } else {
            mainTF.setText(
                "Error: invalid floor - floor must be 'Floor1', 'Floor2', 'Floor3', 'FloorL1', or 'FloorL2'");
          }
        }
      } catch (NumberFormatException e) {
        mainTF.setText("Error: make sure the Node ID is an integer");
      }
    }
  }

  public void addLocation() {
    NodeType nodeType = null;
    boolean nodeTypeValid = true;
    boolean locationExists = false;
    // make sure all fields are filled in
    if ((longNameTF.getText().equals(""))
        || (shortNameTF.getText().equals(""))
        || (nodeTypeTF.getText().equals(""))
        || (locNodeIDTXT.getText().equals(""))) {
      mainTF.setText("Error: not all fields are filled in");
    } else {
      for (int i = 0; i < dataBase.getLocationDAO().getAll().size(); i++) {
        if (dataBase.getLocationDAO().getAll().get(i).getLongName().equals(longNameTF.getText())) {
          locationExists = true;
        }
      }
      if (locationExists) {
        mainTF.setText("Error: the Long Name you entered is already designated to a location");
      } else {
        // make sure nodeType is an enum and nodeTypeValid is true
        try {
          nodeType = NodeType.valueOf(nodeTypeTF.getText());
        } catch (IllegalArgumentException e) {
          nodeTypeValid = false;
        }

        if (nodeTypeValid) {

          Location newLocation =
              new Location(longNameTF.getText(), shortNameTF.getText(), nodeType);

          dataBase.getLocationDAO().add(newLocation);

          LocalDate thedate = LocalDate.now();
          //          dataBase
          //              .getMoveDAO()
          //              .addToJustDBandLoc(
          //                  new Move(
          //                      Integer.parseInt(locNodeIDTXT.getText()), longNameTF.getText(),
          // thedate));

          mainTF.setText("Location successfully added");
        } else {
          mainTF.setText(
              "Error: invalid Node Type - Node Type may be one of: 'CONF', 'DEPT', 'ELEV', 'EXIT', "
                  + "                + 'HALL', 'LABS', 'REST', 'RETL', 'SERV', 'STAI', 'INFO', 'BATH'");
        }
      }
    }
  }

  public void removeLocation() {
    boolean locationExists = false;
    Location locationToRemove = null;
    if (longNameTF.getText().equals("") || locNodeIDTXT.getText().equals("")) {
      mainTF.setText("Error: make sure the Long Name and NodeID text field are entered");
    } else {
      // make sure it exists
      for (int i = 0; i < dataBase.getLocationDAO().getAll().size(); i++) {
        if (dataBase.getLocationDAO().getAll().get(i).getLongName().equals(longNameTF.getText())) {
          locationExists = true;
          // locationToRemove = dataBase.getLocationDAO().getAll().get(i);
          dataBase.getLocationDAO().delete(longNameTF.getText());

          //          dataBase
          //              .getMoveDAO()
          //              .delete(
          //                  dataBase
          //                      .getMoveDAO()
          //                      .getHashmapOfMoves()
          //                      .get(Integer.parseInt(locNodeIDTXT.getText())));

          // line above removes it from the table visually

          // dataBase.getLocationDAO().getAll().remove(i);
          // something wrong with the line above
        }
      }
      if (locationExists) {

        dataBase.getLocationDAO().delete(longNameTF.getText());

        mainTF.setText(
            "Location with long name: " + longNameTF.getText() + " successfully removed");

      } else {
        mainTF.setText(
            "Error: the Long Name of the location you're trying to remove doesn't exist");
      }
    }
  }

  public void editLocation() {
    NodeType nodeType = null;
    boolean nodeTypeValid = true;
    boolean locationExists = false;
    // make sure all fields are filled in
    if ((longNameTF.getText().equals(""))
        || (shortNameTF.getText().equals(""))
        || (nodeTypeTF.getText().equals(""))
        || (locNodeIDTXT.getText().equals(""))) {
      mainTF.setText("Error: not all fields are filled in");
    } else {
      // location exists
      for (int i = 0; i < dataBase.getLocationDAO().getAll().size(); i++) {
        if (dataBase.getLocationDAO().getAll().get(i).getLongName().equals(longNameTF.getText())) {
          locationExists = true;
        }
      }
      if (locationExists) {
        // make sure nodeType is an enum and nodeTypeValid is true
        try {
          nodeType = NodeType.valueOf(nodeTypeTF.getText());
        } catch (IllegalArgumentException e) {
          nodeTypeValid = false;
        }

        if (nodeTypeValid) {
          // maybe change the values at this node
          // then redo the table
          // worth a shot

          dataBase.getLocationDAO().delete(longNameTF.getText());
          Location newLocation =
              new Location(longNameTF.getText(), shortNameTF.getText(), nodeType);

          dataBase.getLocationDAO().add(newLocation);
          /*
                   for (int i = 0; i < dataBase.getLocationDAO().getAll().size(); i++) {
                     if (dataBase
                         .getLocationDAO()
                         .getAll()
                         .get(i)
                         .getLongName()
                         .equals(longNameTF.getText())) {

                       dataBase.getLocationDAO().getAll().get(i).setLongName(shortNameTF.getText());
                       dataBase.getLocationDAO().getAll().get(i).setShortName(shortNameTF.getText());
                       dataBase.getLocationDAO().getAll().get(i).setNodeType(nodeType);
                     }
                   }

          */

          mainTF.setText("Successfully edited Location");
        } else {
          mainTF.setText(
              "Error: invalid Node Type - Node Type may be one of: 'CONF', 'DEPT', 'ELEV', 'EXIT', "
                  + "'HALL', 'LABS', 'REST', 'RETL', 'SERV', 'STAI', 'INFO', 'BATH'");
        }
      } else {
        mainTF.setText(
            "Error: the Long Name of the location you're trying to remove doesn't exist");
      }
    }
  }

  public void initialize() {

    pathfindingToHomeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    clearFieldsButton.setOnMouseClicked(event -> clearFields());

    stackPane.setPrefSize(1200.0, 742.0);

    floor = new ImageView(floor1);

    floor.setImage(floor1);
    stackPane.getChildren().add(floor);
    stackPane.getChildren().add(anchorPane);
    anchorPane.setBackground(Background.fill(Color.TRANSPARENT));

    List<Node> temp = new ArrayList<>();
    for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
      if (dataBase.getNodeDAO().getAll().get(i).getFloor().equals(Floor.Floor1)) {
        temp.add(dataBase.getNodeDAO().getAll().get(i));
      }
    }

    generateFloorNodes(temp);

    mapPane.setContent(stackPane);
    mapPane.setMinScale(.0001);

    AtomicReference<Floor> current = new AtomicReference<>(Floor.FloorL1);

    floorOneBTN.setOnMouseClicked(
        event -> {
          toFloor(Floor.Floor1);
          current.set(Floor.Floor1);
        });
    floorTwoBTN.setOnMouseClicked(
        event -> {
          toFloor(Floor.Floor2);
          current.set(Floor.Floor2);
        });
    floorThreeBTN.setOnMouseClicked(
        event -> {
          toFloor(Floor.Floor3);
          current.set(Floor.Floor3);
        });
    floorLowerOneBTN.setOnMouseClicked(
        event -> {
          toFloor(Floor.FloorL1);
          current.set(Floor.FloorL1);
        });
    floorLowerTwoBTN.setOnMouseClicked(
        event -> {
          toFloor(Floor.FloorL2);
          current.set(Floor.FloorL2);
        });

    addNodeBTN.setOnMouseClicked(
        event -> {
          doingNode = true;
          addNode();
          toFloor(current.get());
        });
    removeNodeBTN.setOnMouseClicked(
        event -> {
          doingNode = true;
          removeNode();
          toFloor(current.get());
        });
    editNodeBTN.setOnMouseClicked(
        event -> {
          doingNode = true;
          editNode();
          toFloor(current.get());
        });

    addLocationBTN.setOnMouseClicked(
        event -> {
          doingNode = false;
          addLocation();
          toFloor(current.get());
        });
    removeLocationBTN.setOnMouseClicked(
        event -> {
          doingNode = false;
          removeLocation();
          toFloor(current.get());
        });
    editLocationBTN.setOnMouseClicked(
        event -> {
          doingNode = false;
          editLocation();
          toFloor(current.get());
        });

    theCircs = floor1Circles;
  }

  public void connectLocToNode() {}
}
