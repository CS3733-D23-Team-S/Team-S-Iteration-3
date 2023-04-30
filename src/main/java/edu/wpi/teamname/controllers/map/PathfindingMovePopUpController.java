package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.LocationDAOImpl;
import edu.wpi.teamname.DAOs.MoveDAOImpl;
import edu.wpi.teamname.DAOs.NodeDAOImpl;
import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.Node;
import edu.wpi.teamname.controllers.PopUpController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.time.LocalDate;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.control.SearchableComboBox;

public class PathfindingMovePopUpController extends PopUpController {

  @FXML SearchableComboBox locationBox;
  @FXML MFXDatePicker datePick;
  @FXML SearchableComboBox nodeCombo;

  @FXML MFXButton cancelAdd;

  @FXML MFXButton confirmAdd;

  @FXML Label eLabel;

  @FXML private DataBaseRepository DBR = DataBaseRepository.getInstance();
  @FXML private NodeDAOImpl NDI = DBR.getNodeDAO();
  @FXML private MoveDAOImpl MDI = DBR.getMoveDAO();
  @FXML private LocationDAOImpl LDI = DBR.getLocationDAO();

  public void initialize() {

    cancelAdd.setOnMouseClicked(event -> stage.close());
    confirmAdd.setOnMouseClicked(
        event -> {
          try {
            Node nodeMap = NDI.get((Integer) nodeCombo.getValue());
            Location mapLocation = LDI.get((String) locationBox.getValue());
            LocalDate mapDate = datePick.getValue();
            MDI.add(nodeMap, mapLocation, mapDate);
          } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "Move already happened")) {
              eLabel.setText("Move already happened. Change move date.");
            } else {
              stage.close();
            }
            e.printStackTrace();
          }
          System.out.println("running");
        });

    locationBox.getItems().addAll(DBR.getListOfEligibleRooms());

    nodeCombo.getItems().addAll(NDI.getAllNodeId());
  }
}