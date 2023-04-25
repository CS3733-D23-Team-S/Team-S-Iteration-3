package edu.wpi.teamname.controllers.mapEditor;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.NodeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import lombok.Setter;
import org.controlsfx.control.SearchableComboBox;

public class AddLocationPopOverController {

  DataBaseRepository repo = DataBaseRepository.getInstance();
  @Setter MapEditorController mainController;
  @FXML @Setter BorderPane locationMenu;

  @FXML private Button deleteLoc, submit;
  @FXML private TextField longNameField, shortNameField;

  @FXML private SearchableComboBox<String> locationSelect;

  @FXML private ChoiceBox<String> nodeTypeSelect;

  @FXML
  public void initialize() {
    for (NodeType type : NodeType.values()) {
      nodeTypeSelect.getItems().add(type.toString());
    }
    List<String> temp = new ArrayList<>();
    for (Location loc : repo.getLocationDAO().getAll()) {
      temp.add(loc.getLongName());
    }
    Collections.sort(temp);
    locationSelect.getItems().addAll(temp);
    deleteLoc.setOnMouseClicked(
        event -> {
          String longname = locationSelect.getSelectionModel().getSelectedItem();
          repo.getLocationDAO().delete(longname);
          locationSelect.getItems().remove(longname);
        });
    submit.setOnMouseClicked(
        event -> {
          if (checkFilled()) {
            Location location =
                new Location(
                    longNameField.getText(),
                    shortNameField.getText(),
                    NodeType.values()[nodeTypeSelect.getSelectionModel().getSelectedIndex()]);
            repo.getLocationDAO().add(location);
          }
        });
  }

  public void launchPopup() {
    mainController.popOver.setContentNode(this.locationMenu);
    mainController.popOver.show(mainController.addLocation);
  }

  private boolean checkFilled() {
    return (longNameField.getText().isEmpty()
        || shortNameField.getText().isEmpty()
        || nodeTypeSelect.getValue().isEmpty());
  }
}
