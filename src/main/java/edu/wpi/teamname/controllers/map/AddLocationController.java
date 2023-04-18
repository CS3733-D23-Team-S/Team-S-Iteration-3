package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.NodeType;
import edu.wpi.teamname.controllers.PopUpController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class AddLocationController extends PopUpController {
  DataBaseRepository repo = DataBaseRepository.getInstance();

  @FXML private Button deleteLoc;

  @FXML private ChoiceBox<String> locationSelect;

  @FXML private TextField longNameField;

  @FXML private ChoiceBox<String> nodeTypeSelect;

  @FXML private TextField shortNameField;

  @FXML private Button submit;

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
            super.stage.close();
          }
        });
  }

  private boolean checkFilled() {
    return (longNameField.getText().isEmpty()
        || shortNameField.getText().isEmpty()
        || nodeTypeSelect.getValue().isEmpty());
  }
}
