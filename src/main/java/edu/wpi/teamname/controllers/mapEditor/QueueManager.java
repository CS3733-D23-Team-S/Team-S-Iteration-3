package edu.wpi.teamname.controllers.mapEditor;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.IDataPack;
import edu.wpi.teamname.DAOs.orms.Edge;
import edu.wpi.teamname.DAOs.orms.Location;
import edu.wpi.teamname.DAOs.orms.Move;
import edu.wpi.teamname.DAOs.orms.Node;

import java.util.*;

import javafx.scene.control.Label;

public class QueueManager {

	DataBaseRepository repo = DataBaseRepository.getInstance();
	MapEditorController controller;
	List<IDataPack> queue;
	List<IDataPack> deleteQueue;
	Label selectedQueueItem;

	HashMap<Label, IDataPack> labelData = new HashMap<>();

	public QueueManager(MapEditorController controller) {
		deleteQueue = new ArrayList<>();
		queue = new ArrayList<>();
		this.controller = controller;
	}

	void addToDeleteQueue(IDataPack data) {
		Label label = new Label();
		String name = data.getClass().getSimpleName();
		if (name.equals("Edge")) {
			Edge edge = (Edge) data;
			label.setText(
					"Deleted "
					+ name.toLowerCase()
					+ " between "
					+ edge.getStartNode().getNodeID()
					+ " and "
					+ edge.getEndNode().getNodeID());
		} else if (name.equals("Move")) {
			Move move = (Move) data;
			label.setText("Deleted " + move);
		} else if (name.equals("Node")) {
			Node node = (Node) data;
			label.setText("Deleted " + node);
		} else if (name.equals("Location")) {
			Location location = (Location) data;
			label.setText("Deleted " + location);
		} else {
			return;
		}
		initQueueItem(label);
		controller.queuePane.getChildren().add(label);
		deleteQueue.add(data);
		labelData.put(label, data);
		controller.generateFloorNodes();
		controller.drawEdges();
	}

	void addNodeMoveToQueue(Node node) {
		Label label = new Label();
		label.setText("Moved node " + node.getNodeID() + " to another location");
		initQueueItem(label);
		controller.queuePane.getChildren().add(label);
		queue.add(node);
		labelData.put(label, node);
	}

	void addToQueue(IDataPack data) {
		Label label = new Label();
		String name = data.getClass().getSimpleName();
		switch (name) {
			case "Edge" -> {
				Edge edge = (Edge) data;
				label.setText(
						name
						+ " between "
						+ edge.getStartNode().getNodeID()
						+ " and "
						+ edge.getEndNode().getNodeID());
			}
			case "Move" -> {
				Move move = (Move) data;
				label.setText(move.toString());
			}
			case "Node" -> {
				Node node = (Node) data;
				label.setText(node.toString());
			}
			case "Location" -> {
				Location location = (Location) data;
				label.setText(location.toString());
			}
			default -> {
				return;
			}
		}
		controller.queuePane.getChildren().add(label);
		queue.add(data);
		labelData.put(label, data);
		controller.generateFloorNodes();
		controller.drawEdges();
	}

	void initQueueItem(Label item) {
		item.setViewOrder(0);
		item.setOnMouseClicked(
				event -> {
					if (selectedQueueItem != null)
						selectedQueueItem.setStyle("-fx-border-color: \"FFFFFF\"");
					selectedQueueItem = item;
					item.setStyle("-fx-border-color:\"122E59\"");
					if (event.isControlDown()) {
						deleteLabel(selectedQueueItem);
						selectedQueueItem = null;
					}
				});
	}

	void deleteLabel(Label label) {
		if (selectedQueueItem == null) return;
		controller.queuePane.getChildren().remove(label);
		IDataPack data = labelData.get(label);
		if (label.getText().startsWith("D")) deleteQueue.remove(data);
		else queue.remove(data);
		labelData.remove(label);
		controller.generateFloorNodes();
		controller.drawEdges();
	}

	void deleteFromAlign(List<Node> node) {
		Collection<Label> temp = labelData.keySet();
		for (Label label : temp)
			if (labelData.get(label).equals(node)) {
				selectedQueueItem = label;
				deleteLabel(selectedQueueItem);
			}
	}

	List<Node> getCurrentNodeChanges() {
		List<Node> local = new ArrayList<>(List.copyOf(repo.getAllNodes()));
		for (IDataPack data : queue) {
			if (data.getClass().equals(Node.class)) {
				local.add((Node) data);
			}
		}
		for (IDataPack data : deleteQueue) {
			if (data.getClass().equals(Node.class)) {
				local.remove((Node) data);
			}
		}
		return local;
	}

	List<Edge> getCurrentEdgeChanges() {
		List<Edge> local = new ArrayList<>(List.copyOf(repo.getAllEdges()));
		for (IDataPack data : queue) {
			if (data.getClass().equals(Edge.class)) {
				local.add((Edge) data);
			}
		}
		for (IDataPack data : deleteQueue) {
			if (data.getClass().equals(Edge.class)) {
				local.remove((Edge) data);
			}
			if (data.getClass().equals(Node.class)) {
				Node node = (Node) data;
				local.removeIf(edge -> edge.getStartNodeID() == node.getNodeID() || edge.getEndNodeID() == node.getNodeID());
			}
		}
		return local;
	}

	void sendUpdates() {
		for (IDataPack data : queue) {
			String name = data.getClass().getSimpleName();
			switch (name) {
				case "Edge" -> repo.addEdge((Edge) data);
				case "Node" -> repo.addNode((Node) data);
				case "Location" -> repo.addLocation((Location) data);
				case "Move" -> repo.addMove((Move) data);
			}
		}
		for (IDataPack data : deleteQueue) {
			String name = data.getClass().getSimpleName();
			switch (name) {
				case "Edge" -> repo.deleteEdge((Edge) data);
				case "Node" -> repo.deleteNode((Node) data);
				case "Location" -> repo.deleteLocation((Location) data);
				case "Move" -> repo.deleteMove((Move) data);
			}
		}
		System.out.println("Queue");
		queue.forEach(x -> System.out.println(x.toString()));
		System.out.println("Delete queue");
		deleteQueue.forEach(x -> System.out.println(x.toString()));
		repo.forceMapUpdate();
		controller.queuePane.getChildren().removeAll(labelData.keySet());
		labelData.clear();
		queue.clear();
		deleteQueue.clear();
	}

}
