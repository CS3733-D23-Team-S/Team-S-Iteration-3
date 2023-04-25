package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.NodeType;
import edu.wpi.teamname.controllers.PopUpController;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.controlsfx.control.SearchableComboBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathfindingMessagePopUpController extends PopUpController {
    @FXML public ComboBox<String> startingLocationDropdown = new SearchableComboBox<>();
    @FXML public ComboBox<String> destinationDropdown = new SearchableComboBox<>();
    @FXML MFXTextField messageTF;
    @FXML MFXButton confirmButton;
    @FXML MFXButton cancelButton;
    DataBaseRepository dataBase = DataBaseRepository.getInstance();
    public void fillDropdowns() {
        List<String> locations = new ArrayList<>();
        for (int i = 0; i < dataBase.getLocationDAO().getAll().size(); i++) {
            if (!dataBase.getMoveDAO().getAll().get(i).getLocation().getNodeType().equals(NodeType.HALL)) {
                locations.add(dataBase.getMoveDAO().getAll().get(i).getLocation().getLongName());
            }
        }
        Collections.sort(locations);
        startingLocationDropdown.getItems().addAll(locations);
        destinationDropdown.getItems().addAll(locations);
    }
    public void confirmMessage() {
        // put message on path
        Navigation.navigate(Screen.PATHFINDING);
    }
    public void cancelMessage() {

        Navigation.navigate(Screen.PATHFINDING);
    }
    public void initialize() {
        fillDropdowns();

    }
}
