package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.databaseredo.orms.*;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import edu.wpi.teamname.pathfinding.MapEditorEntity;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MapEditorController {

  DataBaseRepository dataBase;
  @FXML MFXButton mapEditorToHomeButton;
  @FXML TableView<Object> nodeTable;
  @FXML TableView<Object> locationTable;
  @FXML TableView<Edge> edgeTable;
  @FXML TableView<Object> moveTable;
  @FXML TableColumn<Object, Integer> ntNodeIDCol; // = new TableColumn<>("Node ID");
  @FXML TableColumn<Object, Integer> xCoordCol; // = new TableColumn<>("XCoord");
  @FXML TableColumn<Object, String> yCoordCol; // = new TableColumn<>("YCoord");
  @FXML TableColumn<Object, String> floorCol; // = new TableColumn<>("Floor");
  @FXML TableColumn<Object, String> buildingCol; // = new TableColumn<>("Building");
  @FXML TableColumn<Object, NodeType> nodeTypeCol; // = new TableColumn<>("Node Type");
  @FXML TableColumn<Object, String> longNameCol; // = new TableColumn<>("Long Name");
  @FXML TableColumn<Object, String> shortNameCol; // = new TableColumn<>("Short Name");
  @FXML TableColumn<Object, Date> recentMoveCol; // = new TableColumn<>("Most Recent Move");
  @FXML TableColumn<Edge, Node> startNodeCol; // = new TableColumn<>("Start Node");
  @FXML TableColumn<Edge, Node> endNodeCol; // = new TableColumn<>("End Node");
  @FXML TableColumn<Object, Integer> mtNodeIDCol; // = new TableColumn<>("Node ID");
  @FXML TableColumn<Object, String> locationCol; // = new TableColumn<>("Location");
  @FXML TableColumn<Object, ArrayList<LocalDate>> datesCol; // = new TableColumn<>("Dates");
  @FXML MFXTextField ntNodeIDTF;
  @FXML MFXTextField xCoordTF;
  @FXML MFXTextField yCoordTF;
  @FXML MFXTextField floorTF;
  @FXML MFXTextField buildingTF;
  @FXML MFXTextField nodeTypeTF;
  @FXML MFXTextField longNameTF;
  @FXML MFXTextField shortNameTF;
  @FXML MFXTextField recentMoveTF;
  @FXML MFXTextField nodeTF;
  @FXML MFXTextField startNodeTF;
  @FXML MFXTextField endNodeTF;
  @FXML MFXTextField mtNodeIDTF;
  @FXML MFXTextField locationTF;
  @FXML MFXTextField datesTF;
  @FXML MFXButton addNodeButton;
  @FXML MFXButton removeNodeButton;
  @FXML MFXButton editNodeButton;
  @FXML MFXButton addLocationButton;
  @FXML MFXButton removeLocationButton;
  @FXML MFXButton editLocationButton;
  @FXML MFXButton addEdgeButton;
  @FXML MFXButton removeEdgeButton;
  @FXML MFXButton editEdgeButton;
  @FXML MFXButton addMoveButton;
  @FXML MFXButton removeMoveButton;
  @FXML MFXButton editMoveButton;
  @FXML MFXTextField mainNodeTF;
  @FXML MFXTextField mainLocationTF;
  @FXML MFXTextField mainEdgeTF;
  @FXML MFXTextField mainMoveTF;
  @FXML MFXTextField newStartNodeTF;
  @FXML MFXTextField newEndNodeTF;

  public void addNode() {
    Boolean nodeExists = false;
    Boolean floorValid = true;
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
      mainNodeTF.setText("Error: not all fields are filled in");
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
          mainNodeTF.setText("Error: the entered Node ID already exists in the database");
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
            nodeTable
                .getItems()
                .removeAll(FXCollections.observableList(dataBase.getNodeDAO().getAll()));
            Node newNode = new Node(ntNodeID, xCoord, yCoord, floor, buildingTF.getText());
            dataBase.getNodeDAO().add(newNode);
            nodeTable
                .getItems()
                .addAll(FXCollections.observableList(dataBase.getNodeDAO().getAll()));
            mainNodeTF.setText("Node successfully added");
          } else {
            // bad!!!
            mainNodeTF.setText(
                "Error: invalid floor - floor must be 'Floor1', 'Floor2', 'Floor3', 'FloorL1', or 'FloorL2'");
          }
        }
      } catch (NumberFormatException e) {
        mainNodeTF.setText("Error: make sure the Node ID, XCoord, and YCoord fields are integers");
      }
    }
  }

  public void removeNode() {
    Boolean nodeExists = false;
    int ntNodeID;
    // if node id field is empty don't remove it
    // if node id field is not an integer don't do it
    if (ntNodeIDTF.getText().equals("")) {
      mainNodeTF.setText("Error: Node ID field is not filled in");
    } else {
      try {
        ntNodeID = Integer.parseInt(ntNodeIDTF.getText());
        for (int i = 0; i < dataBase.getNodeDAO().getAll().size(); i++) {
          if (ntNodeID == dataBase.getNodeDAO().getAll().get(i).getNodeID()) {
            nodeExists = true;
          }
        }
        if (nodeExists) {
          nodeTable
              .getItems()
              .removeAll(FXCollections.observableList(dataBase.getNodeDAO().getAll()));
          dataBase.getNodeDAO().delete(ntNodeID);
          nodeTable.getItems().addAll(FXCollections.observableList(dataBase.getNodeDAO().getAll()));
          mainNodeTF.setText("Node with Node ID: " + ntNodeID + " successfully removed");
        } else {
          mainNodeTF.setText("Error: Node ID not found");
        }
      } catch (NumberFormatException e) {
        mainNodeTF.setText("Error: make sure the Node ID is an integer");
      }
    }
  }

  public void editNode() {
    Boolean nodeExists = false;
    Boolean floorValid = true;
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
      mainNodeTF.setText("Error: not all fields are filled in");
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
            nodeTable
                .getItems()
                .removeAll(FXCollections.observableList(dataBase.getNodeDAO().getAll()));
            dataBase.getNodeDAO().delete(ntNodeID);
            Node newNode = new Node(ntNodeID, xCoord, yCoord, floor, buildingTF.getText());
            dataBase.getNodeDAO().add(newNode);
            nodeTable
                .getItems()
                .addAll(FXCollections.observableList(dataBase.getNodeDAO().getAll()));
            mainNodeTF.setText("Successfully edited Node");
          } else {
            mainNodeTF.setText(
                "Error: invalid floor - floor must be 'Floor1', 'Floor2', 'Floor3', 'FloorL1', or 'FloorL2'");
          }
        }
      } catch (NumberFormatException e) {
        mainNodeTF.setText("Error: make sure the Node ID is an integer");
      }
    }
  }

  public void addLocation() {
    NodeType nodeType = null;
    Boolean nodeTypeValid = true;
    Boolean locationExists = false;
    // make sure all fields are filled in
    if ((longNameTF.getText().equals(""))
        || (shortNameTF.getText().equals(""))
        || (nodeTypeTF.getText().equals(""))) {
      mainLocationTF.setText("Error: not all fields are filled in");
    } else {
      for (int i = 0; i < dataBase.getLocationDAO().getAll().size(); i++) {
        if (dataBase.getLocationDAO().getAll().get(i).getLongName().equals(longNameTF.getText())) {
          locationExists = true;
        }
      }
      if (locationExists) {
        mainLocationTF.setText(
            "Error: the Long Name you entered is already designated to a location");
      } else {
        // make sure nodeType is an enum and nodeTypeValid is true
        if (nodeTypeTF.getText().equals("CONF")) {
          nodeType = NodeType.CONF;
        } else if (nodeTypeTF.getText().equals("DEPT")) {
          nodeType = NodeType.DEPT;
        } else if (nodeTypeTF.getText().equals("ELEV")) {
          nodeType = NodeType.ELEV;
        } else if (nodeTypeTF.getText().equals("EXIT")) {
          nodeType = NodeType.EXIT;
        } else if (nodeTypeTF.getText().equals("HALL")) {
          nodeType = NodeType.HALL;
        } else if (nodeTypeTF.getText().equals("LABS")) {
          nodeType = NodeType.LABS;
        } else if (nodeTypeTF.getText().equals("REST")) {
          nodeType = NodeType.REST;
        } else if (nodeTypeTF.getText().equals("RETL")) {
          nodeType = NodeType.RETL;
        } else if (nodeTypeTF.getText().equals("SERV")) {
          nodeType = NodeType.SERV;
        } else if (nodeTypeTF.getText().equals("STAI")) {
          nodeType = NodeType.STAI;
        } else if (nodeTypeTF.getText().equals("INFO")) {
          nodeType = NodeType.RETL;
        } else if (nodeTypeTF.getText().equals("BATH")) {
          nodeType = NodeType.BATH;
        } else {
          nodeTypeValid = false;
        }
        if (nodeTypeValid) {
          locationTable
              .getItems()
              .removeAll(FXCollections.observableList(dataBase.getLocationDAO().getAll()));
          Location newLocation =
              new Location(longNameTF.getText(), shortNameTF.getText(), nodeType);
          dataBase.getLocationDAO().add(newLocation);
          locationTable
              .getItems()
              .addAll(FXCollections.observableList(dataBase.getLocationDAO().getAll()));
          mainLocationTF.setText("Location successfully added");
        } else {
          mainLocationTF.setText(
              "Error: invalid Node Type - Node Type may be one of: 'CONF', 'DEPT', 'ELEV', 'EXIT', "
                  + "                + 'HALL', 'LABS', 'REST', 'RETL', 'SERV', 'STAI', 'INFO', 'BATH'");
        }
      }
    }
  }

  public void removeLocation() {
    Boolean locationExists = false;
    int indexToRemove = 0;
    if (longNameTF.getText().equals("")) {
      mainLocationTF.setText("Error: make sure the Long Name text field is entered");
    } else {
      // make sure it exists
      for (int i = 0; i < dataBase.getLocationDAO().getAll().size(); i++) {
        if (dataBase.getLocationDAO().getAll().get(i).getLongName().equals(longNameTF.getText())) {
          locationExists = true;
          indexToRemove = i;
          // dataBase.getLocationDAO().delete(longNameTF.getText());
          // line above removes it from the table visually

          // dataBase.getLocationDAO().getAll().remove(i);
          // something wrong with the line above
        }
      }
      if (locationExists) {
        locationTable
            .getItems()
            .removeAll(FXCollections.observableList(dataBase.getLocationDAO().getAll()));
        // dataBase.getLocationDAO().delete(longNameTF.getText());

        locationTable
            .getItems()
            .addAll(FXCollections.observableList(dataBase.getLocationDAO().getAll()));

        mainLocationTF.setText(
            "Location with long name: " + longNameTF.getText() + " successfully removed");

      } else {
        mainLocationTF.setText(
            "Error: the Long Name of the location you're trying to remove doesn't exist");
      }
    }
  }

  public void editLocation() {
    NodeType nodeType = null;
    Boolean nodeTypeValid = true;
    Boolean locationExists = false;
    // make sure all fields are filled in
    if ((longNameTF.getText().equals(""))
        || (shortNameTF.getText().equals(""))
        || (nodeTypeTF.getText().equals(""))) {
      mainLocationTF.setText("Error: not all fields are filled in");
    } else {
      // location exists
      for (int i = 0; i < dataBase.getLocationDAO().getAll().size(); i++) {
        if (dataBase.getLocationDAO().getAll().get(i).getLongName().equals(longNameTF.getText())) {
          locationExists = true;
        }
      }
      if (locationExists) {
        // make sure nodeType is an enum and nodeTypeValid is true
        if (nodeTypeTF.getText().equals("CONF")) {
          nodeType = NodeType.CONF;
        } else if (nodeTypeTF.getText().equals("DEPT")) {
          nodeType = NodeType.DEPT;
        } else if (nodeTypeTF.getText().equals("ELEV")) {
          nodeType = NodeType.ELEV;
        } else if (nodeTypeTF.getText().equals("EXIT")) {
          nodeType = NodeType.EXIT;
        } else if (nodeTypeTF.getText().equals("HALL")) {
          nodeType = NodeType.HALL;
        } else if (nodeTypeTF.getText().equals("LABS")) {
          nodeType = NodeType.LABS;
        } else if (nodeTypeTF.getText().equals("REST")) {
          nodeType = NodeType.REST;
        } else if (nodeTypeTF.getText().equals("RETL")) {
          nodeType = NodeType.RETL;
        } else if (nodeTypeTF.getText().equals("SERV")) {
          nodeType = NodeType.SERV;
        } else if (nodeTypeTF.getText().equals("STAI")) {
          nodeType = NodeType.STAI;
        } else if (nodeTypeTF.getText().equals("INFO")) {
          nodeType = NodeType.RETL;
        } else if (nodeTypeTF.getText().equals("BATH")) {
          nodeType = NodeType.BATH;
        } else {
          nodeTypeValid = false;
        }
        if (nodeTypeValid) {
          // maybe change the values at this node
          // then redo the table
          // worth a shot
          locationTable
              .getItems()
              .removeAll(FXCollections.observableList(dataBase.getLocationDAO().getAll()));
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
          //          locationTable
          //              .getItems()
          //
          // .removeAll(FXCollections.observableList(dataBase.getLocationDAO().getAll()));
          locationTable
              .getItems()
              .addAll(FXCollections.observableList(dataBase.getLocationDAO().getAll()));
          /*
          locationTable
              .getItems()
              .removeAll(FXCollections.observableList(dataBase.getLocationDAO().getAll()));
          dataBase.getLocationDAO().delete(longNameTF.getText());
          Location newLocation =
              new Location(shortNameTF.getText(), shortNameTF.getText(), nodeType);
          dataBase.getLocationDAO().add(newLocation);
          locationTable
              .getItems()
              .addAll(FXCollections.observableList(dataBase.getLocationDAO().getAll()));
           */
          mainLocationTF.setText("Successfully edited Location");
        } else {
          mainLocationTF.setText(
              "Error: invalid Node Type - Node Type may be one of: 'CONF', 'DEPT', 'ELEV', 'EXIT', "
                  + "'HALL', 'LABS', 'REST', 'RETL', 'SERV', 'STAI', 'INFO', 'BATH'");
        }
      } else {
        mainLocationTF.setText(
            "Error: the Long Name of the location you're trying to remove doesn't exist");
      }
    }
  }

  public void addEdge() {
    int startNodeID;
    int endNodeID;
    Boolean edgeExists = false;
    // make sure fields aren't empty
    if (startNodeTF.getText().equals("") || (endNodeTF.getText().equals(""))) {
      mainEdgeTF.setText("Error: make sure both the start node and end node fields are filled in");
    } else {
      // make sure IDs are valid
      try {
        startNodeID = Integer.parseInt(startNodeTF.getText());
        endNodeID = Integer.parseInt(endNodeTF.getText());
        // make sure edge doesn't already exist
        // make sure start node id and end node id aren't the same
        for (int i = 0; i < dataBase.getEdgeDAO().getAll().size(); i++) {
          if ((dataBase.getEdgeDAO().getAll().get(i).getStartNodeID() == startNodeID)
              && (dataBase.getEdgeDAO().getAll().get(i).getEndNodeID() == endNodeID)) {
            edgeExists = true;
          }
        }
        if (startNodeID == endNodeID) {
          // bad
          mainEdgeTF.setText("Error: the start node and end node IDs cannot be the same value");
        } else if (edgeExists) {
          // bad
          mainEdgeTF.setText(
              "Error: the start node and end node IDs are both attributed to an already existing edge");
        } else {
          // good
          edgeTable
              .getItems()
              .removeAll(FXCollections.observableList(dataBase.getEdgeDAO().getAll()));
          dataBase.getEdgeDAO().getAll().add(new Edge(startNodeID, endNodeID));
          edgeTable.getItems().addAll(FXCollections.observableList(dataBase.getEdgeDAO().getAll()));
          mainEdgeTF.setText(
              "Edge with start node ID: "
                  + startNodeID
                  + " and end node ID: "
                  + endNodeID
                  + " successfully added");
        }
      } catch (NumberFormatException e) {
        mainEdgeTF.setText("Error: make sure both the start node and end node fields are integers");
      }
    }
  }

  public void removeEdge() {
    int startNodeID;
    int endNodeID;
    Boolean edgeExists = false;
    Edge edgeToRemove = null;
    // make sure fields aren't empty
    if (startNodeTF.getText().equals("") || (endNodeTF.getText().equals(""))) {
      mainEdgeTF.setText("Error: make sure both the start node and end node fields are filled in");
    } else {
      try {
        // make sure ids are integers
        startNodeID = Integer.parseInt(startNodeTF.getText());
        endNodeID = Integer.parseInt(endNodeTF.getText());
        // make sure edge exists
        for (int i = 0; i < dataBase.getEdgeDAO().getAll().size(); i++) {
          if ((dataBase.getEdgeDAO().getAll().get(i).getStartNodeID() == startNodeID)
              && (dataBase.getEdgeDAO().getAll().get(i).getEndNodeID() == endNodeID)) {
            edgeExists = true;
            edgeToRemove = dataBase.getEdgeDAO().getAll().get(i);
          }
        }
        if (startNodeID == endNodeID) {
          // bad
          mainEdgeTF.setText("Error: the start node and end node IDs cannot be the same value");
        } else if (!edgeExists) {
          // bad
          mainEdgeTF.setText("Error: the edge you're trying to remove does not exist");
        } else {
          // good
          edgeTable
              .getItems()
              .removeAll(FXCollections.observableList(dataBase.getEdgeDAO().getAll()));
          dataBase.getEdgeDAO().getAll().remove(edgeToRemove);
          // dataBase.getEdgeDAO().delete(edgeToRemove);
          edgeTable.getItems().addAll(FXCollections.observableList(dataBase.getEdgeDAO().getAll()));
          mainEdgeTF.setText(
              "Edge with start node ID: "
                  + startNodeID
                  + " and end node ID: "
                  + endNodeID
                  + " successfully removed");
        }
      } catch (NumberFormatException e) {
        mainEdgeTF.setText("Error: make sure both the start node and end node fields are integers");
      }
    }
  }

  public void editEdge() {
    int startNodeID;
    int endNodeID;
    int newStartNodeID;
    int newEndNodeID;
    Boolean edgeExists = false;
    Boolean newEdgeExists = false;
    Edge edgeToEdit = null;
    // might not be necessary
    // make sure all fields aren't blank
    if (startNodeTF.getText().equals("")
        || (endNodeTF.getText().equals(""))
        || (newStartNodeTF.getText().equals(""))
        || (newEndNodeTF.getText().equals(""))) {
      mainEdgeTF.setText("Error: make sure all text fields are filled in");
    } else {
      // make sure all fields are valid
      try {
        startNodeID = Integer.parseInt(startNodeTF.getText());
        endNodeID = Integer.parseInt(endNodeTF.getText());
        newStartNodeID = Integer.parseInt(newStartNodeTF.getText());
        newEndNodeID = Integer.parseInt(newEndNodeTF.getText());
        // make sure start and end node already exist
        // make sure new start and end node don't already exist
        for (int i = 0; i < dataBase.getEdgeDAO().getAll().size(); i++) {
          if ((dataBase.getEdgeDAO().getAll().get(i).getStartNodeID() == startNodeID)
              && (dataBase.getEdgeDAO().getAll().get(i).getEndNodeID() == endNodeID)) {
            edgeExists = true;
            edgeToEdit = dataBase.getEdgeDAO().getAll().get(i);
          }
          if ((dataBase.getEdgeDAO().getAll().get(i).getStartNodeID() == newStartNodeID)
              && (dataBase.getEdgeDAO().getAll().get(i).getEndNodeID() == newEndNodeID)) {
            newEdgeExists = true;
          }
        }
        if (newStartNodeID == newEndNodeID) {
          // bad
          mainEdgeTF.setText(
              "Error: the new start node and new end node IDs cannot be the same value");
        } else if (!edgeExists) {
          // bad
          mainEdgeTF.setText("Error: the edge you're trying to edit does not exist");
        } else if (newEdgeExists) {
          // bad
          mainEdgeTF.setText(
              "Error: the new start node and end node ID values are attributed to an already existing edge");
        } else {
          edgeTable
              .getItems()
              .removeAll(FXCollections.observableList(dataBase.getEdgeDAO().getAll()));
          edgeToEdit.updateEdge(newStartNodeID, newEndNodeID);
          edgeTable.getItems().addAll(FXCollections.observableList(dataBase.getEdgeDAO().getAll()));
          mainEdgeTF.setText(
              "Edge with previous start node ID: "
                  + startNodeID
                  + " and previous end node ID: "
                  + endNodeID
                  + " successfully edited");
        }
      } catch (NumberFormatException e) {
        mainEdgeTF.setText("Error: make sure both the start node and end node fields are integers");
      }
    }
  }

  public void addMove() {
    int nodeID;
    Boolean nodeIDExists = false;
    Boolean locationExists = false;
    // make sure fields aren't empty
    if (mtNodeIDTF.getText().equals("") || locationTF.getText().equals("")) {
      mainMoveTF.setText("Error: make sure all fields are filled in");
    } else {
      // make sure node ID is valid
      try {
        nodeID = Integer.parseInt(mtNodeIDTF.getText());
        // make sure node ID and location don't already exist
        for (int i = 0; i < dataBase.getMoveDAO().getAll().size(); i++) {
          if (dataBase.getMoveDAO().getAll().get(i).getNodeID() == nodeID) {
            nodeIDExists = true;
          }
          if (dataBase.getMoveDAO().getAll().get(i).getLocation().equals(locationTF.getText())) {
            locationExists = true;
          }
        }
        if (nodeIDExists) {
          mainMoveTF.setText("Error: the node ID you entered cannot be found");
        } else if (locationExists) {
          mainMoveTF.setText("Error: the location name you entered cannot be found");
        } else {
          moveTable
              .getItems()
              .removeAll(FXCollections.observableList(dataBase.getMoveDAO().getAll()));
          LocalDate today = LocalDate.now();
          dataBase.getMoveDAO().getAll().add(new Move(nodeID, locationTF.getText(), today));
          moveTable.getItems().addAll(FXCollections.observableList(dataBase.getMoveDAO().getAll()));
          mainMoveTF.setText(
              "Move with node ID: "
                  + nodeID
                  + " and location: "
                  + locationTF.getText()
                  + " successfully added");
        }
      } catch (NumberFormatException e) {
        mainMoveTF.setText("Error: make sure node ID is entered as an integer");
      }
    }
  }

  public void removeMove() {
    int nodeID;
    Boolean nodeIDExists = false;
    Boolean locationExists = false;
    Boolean moveExists = false;
    Move moveToRemove = null;
    // make sure fields aren't empty
    if (mtNodeIDTF.getText().equals("") || locationTF.getText().equals("")) {
      mainMoveTF.setText("Error: make sure all fields are filled in");
    } else {
      // make sure node ID is valid
      try {
        nodeID = Integer.parseInt(mtNodeIDTF.getText());
        // make sure node ID and location don't already exist
        for (int i = 0; i < dataBase.getMoveDAO().getAll().size(); i++) {
          if (dataBase.getMoveDAO().getAll().get(i).getNodeID() == nodeID) {
            nodeIDExists = true;
          }
          if (dataBase.getMoveDAO().getAll().get(i).getLocation().equals(locationTF.getText())) {
            locationExists = true;
          }
          if ((dataBase.getMoveDAO().getAll().get(i).getNodeID() == nodeID)
              && (dataBase
                  .getMoveDAO()
                  .getAll()
                  .get(i)
                  .getLocation()
                  .equals(locationTF.getText()))) {
            moveExists = true;
            moveToRemove = dataBase.getMoveDAO().getAll().get(i);
          }
        }
        if (!nodeIDExists) {
          mainMoveTF.setText("Error: the node ID you entered doesn't exist");
        } else if (!locationExists) {
          mainMoveTF.setText("Error: the location name you entered already exists");
        } else if (!moveExists) {
          mainMoveTF.setText(
              "Error: a move with the node ID and location you entered could not be found");
        } else {
          moveTable
              .getItems()
              .removeAll(FXCollections.observableList(dataBase.getMoveDAO().getAll()));
          dataBase.getMoveDAO().getAll().remove(moveToRemove);
          moveTable.getItems().addAll(FXCollections.observableList(dataBase.getMoveDAO().getAll()));
          mainMoveTF.setText(
              "Move with node ID: "
                  + nodeID
                  + " and location: "
                  + locationTF.getText()
                  + " successfully removed");
        }
      } catch (NumberFormatException e) {
        mainMoveTF.setText("Error: make sure node ID is entered as an integer");
      }
    }
  }
  public void editMove() {

  }

  public void createLists() {
    final ObservableList nodeList = FXCollections.observableList(dataBase.getNodeDAO().getAll());
    final ObservableList edgeList = FXCollections.observableList(dataBase.getEdgeDAO().getAll());
    final ObservableList locationList =
        FXCollections.observableList(dataBase.getLocationDAO().getAll());
    final ObservableList moveList = FXCollections.observableList(dataBase.getMoveDAO().getAll());

    MapEditorEntity mee = new MapEditorEntity();

    nodeTable.getItems().addAll(nodeList);

    edgeTable.getItems().addAll(edgeList);

    for (int i = 0; i < locationList.size(); i++) {
      locationTable.getItems().add(locationList.get(i));
    }
    for (int i = 0; i < moveList.size(); i++) {
      moveTable.getItems().add(moveList.get(i));
    }
  }

  public void initialize() {
    dataBase = DataBaseRepository.getInstance();
    mapEditorToHomeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    addNodeButton.setOnMouseClicked(event -> addNode());
    removeNodeButton.setOnMouseClicked(event -> removeNode());
    editNodeButton.setOnMouseClicked(event -> editNode());
    addLocationButton.setOnMouseClicked(event -> addLocation());
    removeLocationButton.setOnMouseClicked(event -> removeLocation());
    editLocationButton.setOnMouseClicked(event -> editLocation());
    addEdgeButton.setOnMouseClicked(event -> addEdge());
    removeEdgeButton.setOnMouseClicked(event -> removeEdge());
    editEdgeButton.setOnMouseClicked(event -> editEdge());
    addMoveButton.setOnMouseClicked(event -> addMove());
    removeMoveButton.setOnMouseClicked(event -> removeMove());

    ntNodeIDCol.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    xCoordCol.setCellValueFactory(new PropertyValueFactory<>("xCoord"));
    yCoordCol.setCellValueFactory(new PropertyValueFactory<>("yCoord"));
    floorCol.setCellValueFactory(new PropertyValueFactory<>("floor"));
    buildingCol.setCellValueFactory(new PropertyValueFactory<>("building"));
    //    nodeTable.getColumns().add(0, ntNodeIDCol);
    //    nodeTable.getColumns().add(1, xCoordCol);
    //    nodeTable.getColumns().add(2, yCoordCol);
    //    nodeTable.getColumns().add(3, floorCol);
    //    nodeTable.getColumns().add(4, buildingCol);

    nodeTypeCol.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
    shortNameCol.setCellValueFactory(new PropertyValueFactory<>("shortName"));
    longNameCol.setCellValueFactory(new PropertyValueFactory<>("longName"));
    recentMoveCol.setCellValueFactory(new PropertyValueFactory<>("mostRecentMove"));
    //    locationTable.getColumns().add(0, nodeTypeCol);
    //    locationTable.getColumns().add(1, shortNameCol);
    //    locationTable.getColumns().add(2, longNameCol);
    //    locationTable.getColumns().add(3, recentMoveCol);

    startNodeCol.setCellValueFactory(
        (edge) -> {
          return new SimpleObjectProperty(edge.getValue().getStartNodeID());
        });
    endNodeCol.setCellValueFactory(
        (edge) -> {
          return new SimpleObjectProperty(edge.getValue().getEndNodeID());
        });

    mtNodeIDCol.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
    datesCol.setCellValueFactory(new PropertyValueFactory<>("dates"));

    //    moveTable.getColumns().add(0, mtNodeIDCol);
    //    moveTable.getColumns().add(1, locationCol);
    //    moveTable.getColumns().add(2, datesCol);

    createLists();
  }
}
