package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.NodeType;
import edu.wpi.teamname.controllers.PopUpController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.controlsfx.control.SearchableComboBox;

public class PathfindingMessagePopUpController extends PopUpController {

  PathfindingController pathfindingController = new PathfindingController();
  @FXML public ComboBox<String> startingLocationDropdown = new SearchableComboBox<>();
  @FXML public ComboBox<String> destinationDropdown = new SearchableComboBox<>();
  @FXML MFXTextField messageTF;
  @FXML MFXButton confirmButton;
  @FXML MFXButton cancelButton;
  @FXML Label startingLocationError;
  @FXML Label destinationError;
  @FXML Label messageError;
  @FXML Label messageSuccess;
  DataBaseRepository dataBase = DataBaseRepository.getInstance();

  public void fillDropdowns() {
    List<String> locations = new ArrayList<>();
    for (int i = 0; i < dataBase.getMoveDAO().getAll().size(); i++) {
      if (!dataBase
          .getMoveDAO()
          .getAll()
          .get(i)
          .getLocation()
          .getNodeType()
          .equals(NodeType.HALL)) {
        locations.add(dataBase.getMoveDAO().getAll().get(i).getLocation().getLongName());
      }
    }
    Collections.sort(locations);
    startingLocationDropdown.getItems().addAll(locations);
    destinationDropdown.getItems().addAll(locations);
  }

  public void confirmMessage() {
    // put message on path
    if (messageTF.getText().equals("")) {
      messageError.setText("Error: empty message");
      messageSuccess.setText("");
    } else {
      messageError.setText("");
    }
    if (startingLocationDropdown.getSelectionModel().isEmpty()) {
      startingLocationError.setText("Error: no selected starting location");
      messageSuccess.setText("");
    } else {
      startingLocationError.setText("");
    }
    if (destinationDropdown.getSelectionModel().isEmpty()) {
      destinationError.setText("Error: no selected destination");
      messageSuccess.setText("");
    } else {
      destinationError.setText("");
    }
    if ((!messageTF.getText().equals("")
        && (!startingLocationDropdown.getSelectionModel().isEmpty())
        && (!destinationDropdown.getSelectionModel().isEmpty()))) {
      messageError.setText("");
      startingLocationError.setText("");
      destinationError.setText("");
      // add message
      pathfindingController.adminMessage(messageTF.getText());
      messageSuccess.setText("Message Successfully Added");
    }
  }

  public void cancelMessage() {
    stage.close();
  }

  public void initialize() {
    fillDropdowns();
    confirmButton.setOnMouseClicked(event -> confirmMessage());
    cancelButton.setOnMouseClicked(event -> cancelMessage());
  }
}
