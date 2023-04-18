package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.Move;
import edu.wpi.teamname.DAOs.orms.Node;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class EditNodeController {

	DataBaseRepository repo = DataBaseRepository.getInstance();

	@FXML
	private Button addEdge;

	@FXML
	private Button addLocation;

	@FXML
	private VBox edgeBox;

	@FXML
	private SearchableComboBox<Integer> edgeField;

	@FXML
	private SearchableComboBox<String> locationField;

	@FXML
	private VBox locationBox;

	@FXML
	private VBox moveBox;

	@FXML
	private TextField nodeIDLabel;

	@FXML
	private Button submitUpdate;

	Node node;

	public EditNodeController() {
	}

	public void initialize() {
		for (Location loc : repo.getLocationDAO().getAll()) {
			locationField.getItems().add(loc.getLongName());
		}
		for (Integer id : repo.getNodeDAO().getNodes().keySet())
			edgeField.getItems().add(id);

		addEdge.setOnMouseClicked(event -> {
			int id = edgeField.getSelectionModel().getSelectedItem();
			if (repo.getNodeDAO().get(id) != null) {
				edgeBox.getChildren().add(new Label(String.valueOf(id)));
			}
		});
		addLocation.setOnMouseClicked(event -> {
			String longName = locationField.getValue();
			if (!longName.isEmpty()) {
				for (Move move : repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID()))
					if (longName.equals(move.getLocationName())) return;
			}
			locationBox.getChildren().add(new Label(longName));
		});
	}

	private void update() {
		for (Location loc : repo.getLocationDAO().getAll()) {
			if (!locationField.getItems().contains(loc.getLongName())) locationField.getItems().add(loc.getLongName());
		}
	}

	public void setInfo(Node node) {
		this.node = node;
		update();
		nodeIDLabel.clear();
		edgeBox.getChildren().clear();
		moveBox.getChildren().clear();
		locationBox.getChildren().clear();
		nodeIDLabel.setText(String.valueOf(node.getNodeID()));
		if (repo.getEdgeDAO().getNeighbors().get(node.getNodeID()) == null) return;
		for (Integer neighbor : repo.getEdgeDAO().getNeighbors().get(node.getNodeID())) {
			edgeBox.getChildren().add(new Label(String.valueOf(neighbor)));
		}
		if (repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID()) == null) return;
		for (Move move : repo.getMoveDAO().getLocationsAtNodeID().get(node.getNodeID())) {
			moveBox.getChildren().add(new Label(move.getLocationName() + " on " + move.getDate().toString()));
			if (move.getDate().toEpochDay() < LocalDate.now().toEpochDay()) {
				locationBox.getChildren().add(new Label(move.getLocationName()));
			}
		}
	}
}
