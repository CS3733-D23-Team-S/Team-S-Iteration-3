package edu.wpi.teamname.controllers.mapEditor;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Edge;
import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.Move;
import edu.wpi.teamname.DAOs.orms.Node;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import lombok.Setter;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.SearchableComboBox;

public class EditNodePopOverController {
	DataBaseRepository repo = DataBaseRepository.getInstance();
	@Setter
	MapEditorController mainController;
	@FXML
	@Setter
	private VBox editMenu;

	@FXML
	private Button addEdge, addLocation;
	@FXML
	private VBox edgeBox, locationBox, moveBox;
	@FXML
	private CheckBox checkBox;

	@FXML
	private SearchableComboBox<Integer> edgeField;
	@FXML
	private SearchableComboBox<String> locationField;

	@FXML
	private TextField nodeIDLabel;
	@FXML
	private DatePicker dateSelect;
	private boolean showClosest;
	private HashMap<Label, Move> labelToMove = new HashMap<>();


	Node node;


	public void initialize() {
		for (Location loc : repo.getLocationDAO().getAll()) {
			locationField.getItems().add(loc.getLongName());
		}
		addEdge.setOnMouseClicked(
				event -> {
					int id = edgeField.getSelectionModel().getSelectedItem();
					if (repo.getNode(id) != null) {
						Label label = new Label(String.valueOf(id));
						edgeBox.getChildren().add(label);
						Edge newEdge = new Edge(repo.getNode(id), node);
						mainController.queueManager.addToQueue(newEdge);
					}
				});
		addLocation.setOnMouseClicked(
				event -> {
					String longName = locationField.getValue();
					LocalDate date = dateSelect.getValue();
					if (!longName.isEmpty()) {
						if (repo.getLocationsAtNode(node) != null)
							for (Move move : repo.getLocationsAtNode(node))
								if (longName.equals(move.getLocationName()) && date.isEqual(move.getDate())) return;
					}
					Move newMove = new Move(this.node, repo.getLocationDAO().get(longName), date);
					mainController.queueManager.addToQueue(newMove);
					System.out.println("Added the move: " + newMove);
					locationBox.getChildren().add(new Label(longName));
					Label label =
							new Label(newMove.getLocationName() + " on " + newMove.getDate().toString());
					label.setWrapText(true);
					label.maxWidth(70);
					moveBox.getChildren().add(label);
				});
		checkBox.setOnMouseClicked(
				event -> {
					showClosest = checkBox.isSelected();
					updateEdgeBox();
				});
	}

	public void launchPopup(Circle circle) {
		labelToMove.clear();
		setInfo(mainController.listOfCircles.get(circle));
		mainController.popOver.setContentNode(this.editMenu);
		mainController.popOver.setTitle("Editing Node");
		mainController.popOver.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
		mainController.popOver.show(circle);
	}

	private void update() {
		edgeField.getItems().removeAll();
		for (Node potentialEdge : mainController.queueManager.getCurrentNodeChanges()) {
			if (calcWeight(potentialEdge) < 60
				&& !edgeField.getItems().contains(potentialEdge.getNodeID()))
				edgeField.getItems().add(potentialEdge.getNodeID());
		}
		for (Location loc : mainController.queueManager.getCurrentLocationChanges()) {
			if (!locationField.getItems().contains(loc.getLongName()))
				locationField.getItems().add(loc.getLongName());
		}
	}

	private double calcWeight(Node node) {
		return Math.sqrt(
				Math.pow((this.node.getXCoord() - node.getXCoord()), 2)
				+ Math.pow((this.node.getYCoord() - node.getYCoord()), 2));
	}

	private void updateEdgeBox() {
		edgeField.getItems().removeAll();
		if (!showClosest) {
			edgeField.setPromptText("Nearest Nodes");
			for (Node potentialEdge : mainController.queueManager.getCurrentNodeChanges()) {
				if (calcWeight(potentialEdge) < 60) edgeField.getItems().add(potentialEdge.getNodeID());
			}
		} else {
			edgeField.setPromptText("All Nodes");
			for (Node potentialEdge : mainController.queueManager.getCurrentNodeChanges())
				edgeField.getItems().add(potentialEdge.getNodeID());
		}
	}

	public void setInfo(Node node) {
		this.node = node;
		nodeIDLabel.clear();
		edgeBox.getChildren().clear();
		moveBox.getChildren().clear();
		locationBox.getChildren().clear();
		nodeIDLabel.setText(String.valueOf(node.getNodeID()));
		if (mainController.queueManager.getCurrentEdgeChanges() != null)
			for (Edge edge : mainController.queueManager.getCurrentEdgeChanges()) {
				int id = -1;
				if (edge.getStartNodeID() == node.getNodeID()) id = edge.getEndNodeID();
				if (edge.getEndNodeID() == node.getNodeID()) id = edge.getStartNodeID();
				if (id != -1)
					edgeBox.getChildren().add(new Label(String.valueOf(id)));
			}
		if (repo.getLocationsAtNode(node) != null) {
			List<String> sortedLocations = new ArrayList<>();
			for (Move move : mainController.queueManager.getCurrentMoveChanges()) {
				if (move.getNodeID() != node.getNodeID()) continue;
				Label label = new Label(move.getLocationName() + " on " + move.getDate().toString());
				labelToMove.put(label, move);
				label.setWrapText(true);
				label.maxWidth(70);
				label.setOnMouseClicked(event -> {
					if (event.isControlDown()) {
						Move temp = labelToMove.get(label);
						mainController.queueManager.addToDeleteQueue(temp);
						labelToMove.remove(label);
						moveBox.getChildren().remove(label);
					}
				});
				moveBox.getChildren().add(label);
				sortedLocations.add(
						move.getLocationName() + " - " + move.getLocation().getNodeType().toString());
			}
			Collections.sort(sortedLocations);
			for (String name : sortedLocations) {
				Label label = new Label();
				label.setText(name);
				locationBox.getChildren().add(label);
			}
		}
	}
}
