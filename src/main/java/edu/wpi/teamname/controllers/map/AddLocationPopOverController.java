package edu.wpi.teamname.controllers.map;

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
import org.controlsfx.control.PopOver;
import org.controlsfx.control.SearchableComboBox;

public class AddLocationPopOverController {

	DataBaseRepository repo = DataBaseRepository.getInstance();
	@Setter
	MapEditorController mainController;
	@FXML
	@Setter
	BorderPane locationMenu;

	@FXML
	private Button deleteLoc, submit;
	@FXML
	private TextField longNameField, shortNameField;

	@FXML
	private SearchableComboBox<String> locationSelect;

	@FXML
	private ChoiceBox<String> nodeTypeSelect;

	@FXML
	public void initialize() {
		deleteLoc.setOnMouseClicked(
				event -> {
					String longName = locationSelect.getSelectionModel().getSelectedItem();
					repo.getLocationDAO().delete(longName);
					locationSelect.getItems().remove(longName);
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
		for (NodeType type : NodeType.values()) {
			nodeTypeSelect.getItems().add(type.toString());
		}
		List<String> temp = new ArrayList<>();
		mainController.queueManager.getCurrentLocationChanges().forEach(x -> temp.add(x.getLongName()));
		Collections.sort(temp);
		locationSelect.getItems().addAll(temp);
		mainController.popOver.setContentNode(this.locationMenu);
		mainController.popOver.setTitle("Add/Delete Locations");
		mainController.popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
		mainController.popOver.show(mainController.addLocation);
	}

	private boolean checkFilled() {
		return (longNameField.getText().isEmpty()
				|| shortNameField.getText().isEmpty()
				|| nodeTypeSelect.getValue().isEmpty());
	}
}
