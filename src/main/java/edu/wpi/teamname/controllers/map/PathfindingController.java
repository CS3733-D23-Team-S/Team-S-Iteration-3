package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.navigation.Navigation;
import com.lynden.gmapsfx.GoogleMapView;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import org.controlsfx.control.WorldMapView;

public class PathfindingController {
  @FXML MFXButton pathfindingToHomeButton;

  @FXML MFXButton findPathButton;

  @FXML MFXTextField startingLocation;

  @FXML MFXTextField destination;

  @FXML MFXButton pathfindingToProfileButton;
  @FXML MFXButton clearFieldsButton;
  @FXML MFXButton emailTextualDirections;

  // @FXML TableView<Object> stepsTable;

  // @FXML TableColumn<Object, Integer> nodesTraversedCol = new TableColumn<>("nodes passed");
  @FXML GoogleMapView map;
  WorldMapView.Country usa = WorldMapView.Country.US;
  ObservableList<WorldMapView.Country> countries = FXCollections.observableArrayList(usa);
  ObservableList<WorldMapView.Location> locations = FXCollections.observableArrayList();
  ObservableList<Object> list;

  final ObservableList olist = FXCollections.observableArrayList();

  // basic building observable list code
  /*
  public void buildOList() {
    for (int i = 0; i < 10; i++) {
      olist.add(new PathEntity(i));
    }
  }
   */

  // building olist through pfentity pathEntities
  // creates new PathfindingEntity
  // later iterations will have an actual starting and ending location
  // goes through a loop from 1 to 10
  // adds each element to pfe.getPathEntities() --- adds elements to pfe's list of PathfindingEntity
  // goes through loop of pfe.getPathEntities().size();
  // adds each element to olist;
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
    /*
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
    pathfindingToHomeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.WELCOME_PAGE));

    clearFieldsButton.setOnMouseClicked(event -> clearFields());

    map.setCountries(countries);
    map.setZoomFactor(2.0);

    // findPathButton.setOnMouseClicked(event -> makePathfindingEntity());

    // code below goes from pathfinding to admin screen
    // pathfindingToProfileButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ADMIN));

    // creates columns and populates them

    // nodesTraversedCol.setCellValueFactory(new PropertyValueFactory<>("nodePassed"));
    // stepsTable.getColumns().addAll(nodesTraversedCol);
    // stepsTable.setItems(olist);
  }
}
