package edu.wpi.teamname.controllers.mapEditor;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.IDataPack;
import edu.wpi.teamname.DAOs.orms.*;
import edu.wpi.teamname.Main;
import io.github.palexdev.materialfx.controls.MFXButton;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;

public class MapEditorController {
	DataBaseRepository repo = DataBaseRepository.getInstance();

	// FXML elements for this page
	@FXML
	ComboBox<String> floorSelect;
	@FXML
	CheckComboBox<String> locationSelect;
	@FXML
	MFXButton addAndRemove, moveNode, addLocation;
	@FXML
	GesturePane mapPane;
	@FXML
	ToggleSwitch showEdges, showNames;
	// ________________________________________________________________

	// Map generation
	ImageView floor;
	StackPane stackPane = new StackPane();
	AnchorPane anchorPane = new AnchorPane();
	// ________________________________________________________________

	enum MoveState {
		ADD_REMOVE,
		MOVE,
		REMOVE
	}

	// Keeps track of the current state of the application
	MoveState mode;
	// ________________________________________________________________

	// Images
	Image floor1 = new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png")));
	Image floor2 = new Image(String.valueOf(Main.class.getResource("images/02_thesecondfloor.png")));
	Image floor3 = new Image(String.valueOf(Main.class.getResource("images/03_thethirdfloor.png")));
	Image floorL1 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel1.png")));
	Image floorL2 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel2.png")));
	Floor currFloor = Floor.Floor1;
	// ________________________________________________________________

	// Drawing nodes and labels management
	double imageX, imageY; // Stores the location of mouse clicks
	Node nodeToCircle; // Stores the node of the
	// Used to create edges when shift clicking
	Circle edgeOne;
	Node start;
	Circle edgeTwo;
	Node end;
	// ________________________________________________________________
	Circle prevSelection; // Facilitates the deleting of circles on the map
	// Prevents null pointer errors when deleting circles. Invisible circle that is always drawn
	private final Circle deleteReferenceCircle = new Circle(0, 0, 0, Color.TRANSPARENT);

	HashMap<Circle, Node> listOfCircles =
			new HashMap<>(); // Linked a circle on the map to the associated node
	List<Text> listOfLabels = new ArrayList<>(); // Stores a list of all the node ID labels
	List<Text> listOfShortNames = new ArrayList<>(); // Stores a list of all the short names of nodes
	List<Line> lines = new ArrayList<>(); // Stores the list of lines that represents edges

	List<IDataPack> queue = new ArrayList<>();
	List<IDataPack> deleteQueue = new ArrayList<>();
	Label selectedQueueItem;

	// Popover controllers
	PopOver popOver = new PopOver();

	// Update queue management
	@FXML
	VBox queuePane;
	AddLocationPopOverController addLocationPopOverController;
	AddNodePopOverController addNodePopOverController;

	EditNodePopOverController editNodePopOverController;

	public void initialize() throws IOException {
		initPopOver();
		FXMLLoader loader = new FXMLLoader((Main.class.getResource("views/addLocation.fxml")));

		BorderPane temp = loader.load();
		addLocationPopOverController = loader.getController();
		addLocationPopOverController.setMainController(this);
		addLocationPopOverController.setLocationMenu(temp);
		loader = new FXMLLoader(Main.class.getResource("views/addNodePopUp.fxml"));
		GridPane temptwo = loader.load();
		addNodePopOverController = loader.getController();
		addNodePopOverController.setMainController(this);
		addNodePopOverController.setAddMenu(temptwo);
		loader = new FXMLLoader(Main.class.getResource("views/EditNodePopOver.fxml"));
		VBox tempThree = loader.load();
		editNodePopOverController = loader.getController();
		editNodePopOverController.setMainController(this);
		editNodePopOverController.setEditMenu(tempThree);

		floor = new ImageView(floor1);
		floor.setImage(floor1);
		stackPane.getChildren().addAll(floor, anchorPane);
		anchorPane.setBackground(Background.fill(Color.TRANSPARENT));
		mapPane.setOnKeyPressed(
				event -> {
					//					System.out.println("What in the heck");
					if (selectedQueueItem != null) {
						selectedQueueItem.setStyle("-fx-border-color: \"rgba(0,0,0,0)\"");
						selectedQueueItem = null;
					}
					if (event.getCode().equals(KeyCode.DELETE)
						|| event.getCode().equals(KeyCode.BACK_SPACE) && mode == MoveState.ADD_REMOVE)
						deleteCircle(prevSelection);
					else if (event.getCode().equals(KeyCode.DELETE)
							 || event.getCode().equals(KeyCode.BACK_SPACE)) {

					}
				});
		mapPane.setContent(stackPane);
		mapPane.setMinScale(.0001);
		mapPane.setMaxScale(0.9);
		mapPane.zoomTo(.5, new Point2D(2000, 1500));
		floorSelect.getItems().addAll("Lower 2", "Lower 1", "Floor 1", "Floor 2", "Floor 3");
		locationSelect
				.getItems()
				.addAll(
						"Conference",
						"Department",
						"Elevator",
						"Exit",
						"Lab",
						"Restroom",
						"Retailer",
						"Service",
						"Stairs",
						"Info",
						"Bathroom");

		showEdges.setOnMouseClicked(event -> drawEdges());

		anchorPane.setOnContextMenuRequested(
				event -> {
					addNodePopOverController.launchPopup(event);
				});
		addLocation.setOnMouseClicked(
				event -> {
					resetColors();
					addLocationPopOverController.launchPopup();
				});
		addAndRemove.setOnMouseClicked(
				event -> {
					resetColors();
					addAndRemove.setStyle("-fx-background-color: #122E59");
					mode = MoveState.ADD_REMOVE;
				});
		moveNode.setOnMouseClicked(
				event -> {
					resetColors();
					moveNode.setStyle("-fx-background-color: #122E59");
					mode = MoveState.MOVE;
				});
		generateFloorNodes();
		floorSelect.setOnAction(
				event -> {
					int index = floorSelect.getSelectionModel().getSelectedIndex();
					floor.setImage(swapFloor(index));
					mapPane.centreOn(new Point2D(2000, 1500));
					generateFloorNodes();
					anchorPane.getChildren().removeAll(lines);
					showShortNames();
					drawEdges();
					// Prevents there being an error with the
					// clearing of the circles since the list of circles gets cleared
					edgeOne = null;
				});
	}

	private void resetColors() {
		addAndRemove.setStyle("-fx-background-color: #1D3D94");
		moveNode.setStyle("-fx-background-color: #1D3D94");
		addLocation.setStyle("-fx-background-color: #1D3D94");
	}


	private Image swapFloor(int index) {
		switch (index) {
			case 0:
				currFloor = Floor.FloorL1;
				return floorL1;
			case 1:
				currFloor = Floor.FloorL2;
				return floorL2;
			case 2:
				currFloor = Floor.Floor1;
				return floor1;
			case 3:
				currFloor = Floor.Floor2;
				return floor2;
			case 4:
				currFloor = Floor.Floor3;
				return floor3;
		}
		return floor1;
	}

	/**
	 * Creates all the circle representations of the nodes on the current floor. Then it initializes all the circles to
	 * make them usable and then also stores them in a hashmap to associate the circle to the node it represents
	 */
	private void generateFloorNodes() {
		anchorPane.getChildren().removeAll(listOfCircles.keySet());
		listOfCircles.clear();
		for (Node floorNode : repo.getAllNodes()) {
			if (!floorNode.getFloor().equals(currFloor)) continue;
			Circle newCircle = new Circle(floorNode.getXCoord(), floorNode.getYCoord(), 10.0, Color.RED);
			initCircle(newCircle);
			listOfCircles.put(newCircle, floorNode);
			prevSelection = newCircle;
		}
		anchorPane.getChildren().addAll(listOfCircles.keySet());
	}

	private void drawEdges() {
		boolean edgeShow = showEdges.isSelected();
		anchorPane.getChildren().removeAll(lines);
		lines.clear();
		if (!edgeShow) {
			return;
		}
		for (Node floorNode : repo.getAllNodes()) {
			if (!floorNode.getFloor().equals(currFloor)) continue;
			HashSet<Integer> currNeighbors = repo.getNeighborsOfONode(floorNode.getNodeID());
			if (currNeighbors == null) continue;
			for (Integer currNeighbor : currNeighbors) {
				Node curr = repo.getNode(currNeighbor);
				if (!curr.getFloor().equals(currFloor)) continue;
				Line line =
						new Line(
								floorNode.getXCoord(), floorNode.getYCoord(), curr.getXCoord(), curr.getYCoord());
				line.setFill(Color.BLACK);
				line.setStrokeWidth(5.0);
				line.setViewOrder(1);
				initLine(line);
				lines.add(line);
			}
		}
		anchorPane.getChildren().addAll(lines);
	}

	/**
	 * Draws the names of the displayed nodes the map
	 */
	private void showShortNames() {
		if (!showNames.isSelected()) {
			anchorPane.getChildren().removeAll(listOfShortNames);
			listOfShortNames.clear();
			return;
		}
		for (Circle circle : listOfCircles.keySet()) {
			Node node = listOfCircles.get(circle);
			if (node.getFloor() != currFloor) continue;
			List<Move> list = repo.getLocationsAtNode(node.getNodeID());
			if (list == null) continue;
			for (Move move : list) {
				if (move.getDate().isAfter(LocalDate.now())) break;
				Location loc = move.getLocation();
				if (loc.getNodeType() == NodeType.HALL) break;
				Text newText = new Text(loc.getShortName());
				//        System.out.println(loc.getShortName());
				newText.setFill(Color.WHITE);
				newText.setStroke(Color.BLACK);
				newText.setX(circle.getCenterX() - 20);
				newText.setY(circle.getCenterY() + 20);
				listOfShortNames.add(newText);
			}
		}
		anchorPane.getChildren().addAll(listOfShortNames);
	}

	/**
	 * Sets all the lambda functions needed in order to make the circles actually responsive on the map
	 *
	 * @param circle the circle to initialize
	 */
	private void initCircle(Circle circle) {
		circle.setViewOrder(0);
		circle.setMouseTransparent(false);
		circle.setOnContextMenuRequested(
				event -> {
					System.out.println("Circled clicked");
					editNodePopOverController.launchPopup(circle);
					event.consume();
				});
		circle.setOnMouseClicked(
				event -> {
					if (!event.isShiftDown() && start == null) {
						circle.setStroke(Color.YELLOW);
						circle.setStrokeWidth(3);
						prevSelection.setStroke(Color.TRANSPARENT);
						prevSelection = circle;
						nodeToCircle = listOfCircles.get(circle);
					} else {
						if (start == null) {
							edgeOne = circle;
							start = listOfCircles.get(edgeOne);
							circle.setFill(Color.BLUE);
						} else if (end == null) {
							edgeTwo = circle;
							end = listOfCircles.get(edgeTwo);
							circle.setFill(Color.BLUE);
						}
						if (edgeOne != null && edgeTwo != null) {
							Edge addition = new Edge(start, end);
							addEdgeLine(addition);
							// done in case there is a floor switch
							if (edgeOne != null) {
								edgeOne = null;
								edgeOne.setFill(Color.RED);
							}
							edgeTwo.setFill(Color.RED);
							edgeTwo = null;
							start = null;
							end = null;
							addToQueue(addition);
						}
					}
				});
		circle.setOnMouseDragged(
				event -> {
					if (mode == MoveState.MOVE) {
						prevSelection = circle;
						int currX = (int) Math.round(event.getX());
						int currY = (int) Math.round(event.getY());
						circle.setCenterX(currX);
						circle.setCenterY(currY);
						circle.setStroke(Color.YELLOW);
						circle.setStrokeWidth(3);
						Node temp = listOfCircles.get(circle);
						temp.setXCoord(currX);
						temp.setYCoord(currY);
						anchorPane.getChildren().removeAll(lines);
						lines.clear();
						drawEdges();
						event.consume();
					}
				});
		circle.setOnMouseReleased(
				event -> {
					if (mode == MoveState.MOVE && listOfCircles.get(prevSelection) != null) {
						addNodeMoveToQueue(listOfCircles.get(circle));
						System.out.println("Sent node location update");
						event.consume();
					}
				});
	}

	private void initLine(Line line) {

		line.setOnMouseClicked(event -> System.out.println("fff"));
	}

	/**
	 * Sets up the popover menu for the page before it gets set by other things
	 */
	private void initPopOver() {
		popOver.setHeaderAlwaysVisible(true);
		popOver.setAutoHide(true);
		popOver.setOnAutoHide(
				event -> {
					anchorPane.getChildren().removeAll(listOfLabels);
					listOfLabels.clear();
					generateFloorNodes();
				});
		popOver.setPrefSize(10, 30);
		popOver.setMaxSize(PopupControl.USE_PREF_SIZE, PopupControl.USE_PREF_SIZE);
		popOver.setMinSize(PopupControl.USE_PREF_SIZE, PopupControl.USE_PREF_SIZE);
		popOver.setDetachable(false);
	}

	private void deleteCircle(Circle circle) {
		if (circle == deleteReferenceCircle) return;
		Node node = listOfCircles.get(circle);
		repo.deleteNode(node);
		listOfCircles.remove(circle);
		anchorPane.getChildren().remove(circle);
		prevSelection = deleteReferenceCircle;
	}

	private void addEdgeLine(Edge edge) {
		Line line =
				new Line(
						edge.getStartNode().getXCoord(),
						edge.getStartNode().getYCoord(),
						edge.getEndNode().getXCoord(),
						edge.getEndNode().getYCoord());
		line.setFill(Color.BLACK);
		line.setStrokeWidth(5.0);
		line.setViewOrder(1);
		initLine(line);
		lines.add(line);
		if (showEdges.isSelected()) anchorPane.getChildren().add(line);
	}

	void addToDeleteQueue(IDataPack data) {
		Label label = new Label();
		String name = data.getClass().getSimpleName();
		if (name.equals("Edge")) {
			Edge edge = (Edge) data;
			label.setText(
					"Deleted " + name.toLowerCase()
					+ " between "
					+ edge.getStartNode().getNodeID()
					+ " and "
					+ edge.getEndNode().getNodeID());
		} else if (name.equals("Move")) {
			Move move = (Move) data;
			label.setText("Deleted " + move.toString());
		} else if (name.equals("Node")) {
			Node node = (Node) data;
			label.setText("Deleted " + node.toString());
		} else if (name.equals("Location")) {
			Location location = (Location) data;
			label.setText("Deleted " + location.toString());
		} else {
			return;
		}
		queuePane.getChildren().add(label);
		deleteQueue.add(data);
	}

	void addNodeMoveToQueue(Node node) {
		Label label = new Label();
		label.setText("Moved node " + node.getNodeID() + " to another location");
		initQueueItem(label);
		queuePane.getChildren().add(label);
		queue.add(node);
	}

	void addToQueue(IDataPack data) {
		Label label = new Label();
		String name = data.getClass().getSimpleName();
		if (name.equals("Edge")) {
			Edge edge = (Edge) data;
			label.setText(
					name
					+ " between "
					+ edge.getStartNode().getNodeID()
					+ " and "
					+ edge.getEndNode().getNodeID());
		} else if (name.equals("Move")) {
			Move move = (Move) data;
			label.setText(move.toString());
		} else if (name.equals("Node")) {
			Node node = (Node) data;
			label.setText(node.toString());
		} else if (name.equals("Location")) {
			Location location = (Location) data;
			label.setText(location.toString());
		} else {
			return;
		}
		queuePane.getChildren().add(label);
		queue.add(data);
	}

	void initQueueItem(Label item) {
		item.setOnMouseClicked(
				event -> {
					selectedQueueItem = item;
					item.setStyle("-fx-border-color:\"122E59\"");
				});
	}
}
