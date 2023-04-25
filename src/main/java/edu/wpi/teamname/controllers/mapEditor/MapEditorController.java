package edu.wpi.teamname.controllers.mapEditor;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.*;
import edu.wpi.teamname.Main;
import io.github.palexdev.materialfx.controls.MFXButton;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PopupControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.ToggleSwitch;
import org.jetbrains.annotations.NotNull;

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
	ToggleSwitch showEdges;
	@FXML
	MenuItem callAlign;

	// ________________________________________________________________

	// Map generation
	ImageView floor;
	StackPane stackPane = new StackPane();
	AnchorPane anchorPane = new AnchorPane();
	// ________________________________________________________________

	enum MoveState {
		DEFAULT,
		ADD_REMOVE,
		MOVE,
		MAKE_ALIGNMENT_LINE,
		ALIGN
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

	// Used to create edges when shift clicking
	Circle edgeOne;
	Node start;
	Circle edgeTwo;
	Node end;
	// ________________________________________________________________
	Circle prevCircle; // Facilitates the deleting of circles on the map
	Line prevLine; // Facilitates deleting an edge
	// Prevents null pointer errors when deleting circles. Invisible circle that is always drawn
	private final Circle deleteReferenceCircle = new Circle();
	private final Line deleteReferenceLine = new Line();
	private Line alignmentLine;
	private HashMap<Circle, Point2D> undoAlignment = new HashMap<>();

	HashMap<Circle, Node> listOfCircles =
			new HashMap<>(); // Linked a circle on the map to the associated node
	HashMap<Line, Edge> lines = new HashMap<>(); // Stores the list of lines that represents edges
	List<Text> listOfLabels = new ArrayList<>(); // Stores a list of all the node ID labels
	List<Text> listOfShortNames = new ArrayList<>(); // Stores a list of all the short names of nodes

	// Popover controllers
	PopOver popOver = new PopOver();

	// Update queue management
	@FXML
	VBox queuePane;
	AddLocationPopOverController addLocationPopOverController;
	AddNodePopOverController addNodePopOverController;

	EditNodePopOverController editNodePopOverController;
	QueueManager queueManager;

	public void initialize() throws IOException {
		queueManager = new QueueManager(this);
		prevCircle = deleteReferenceCircle;
		prevLine = deleteReferenceLine;
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
					if (event.getCode().equals(KeyCode.DELETE)
						|| event.getCode().equals(KeyCode.BACK_SPACE) && mode == MoveState.ADD_REMOVE) {
						deleteCircle(prevCircle);
						deleteLine(prevLine);
					}
				});
		allowAlignments();
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
				event -> addNodePopOverController.launchPopup(event));
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
					anchorPane.getChildren().removeAll(lines.keySet());
					showShortNames();
					drawEdges();
					// Prevents there being an error with the
					// clearing of the circles since the list of circles gets cleared
					edgeOne = null;
				});
		locationSelect.setOnMouseExited(
				event -> {
					showShortNames();
					event.consume();
				});
		callAlign.setOnAction(
				event -> mode = MoveState.MAKE_ALIGNMENT_LINE);
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
	void generateFloorNodes() {
		anchorPane.getChildren().removeAll(listOfCircles.keySet());
		listOfCircles.clear();
		for (Node floorNode : queueManager.getCurrentNodeChanges()) {
			if (!floorNode.getFloor().equals(currFloor)) continue;
			Circle newCircle = new Circle(floorNode.getXCoord(), floorNode.getYCoord(), 10.0, Color.RED);
			initCircle(newCircle);
			listOfCircles.put(newCircle, floorNode);
			prevCircle = newCircle;
		}
		anchorPane.getChildren().addAll(listOfCircles.keySet());
	}

	private void drawEdges() {
		anchorPane.getChildren().removeAll(lines.keySet());
		lines.clear();
		if (!showEdges.isSelected()) {
			return;
		}
		for (Edge currEdge : queueManager.getCurrentEdgeChanges()) {
			Node start = currEdge.getStartNode();
			Node end = currEdge.getEndNode();
			if (!start.getFloor().equals(currFloor) || !end.getFloor().equals(currFloor)) continue;
			Line line = new Line(start.getXCoord(), start.getYCoord(), end.getXCoord(), end.getYCoord());
			line.setFill(Color.BLACK);
			line.setStrokeWidth(5.0);
			//			line.setViewOrder(1);
			initLine(line);
			lines.put(line, currEdge);
		}
		anchorPane.getChildren().addAll(lines.keySet());
	}

	/**
	 * Draws the names of the displayed nodes the map
	 */
	private void showShortNames() {
		anchorPane.getChildren().removeAll(listOfShortNames);
		listOfShortNames.clear();
		List<NodeType> toShow = getLocationTypesToShow();
		for (Circle circle : listOfCircles.keySet()) {
			Node node = listOfCircles.get(circle);
			if (node.getFloor() != currFloor) continue;
			List<Move> list = repo.getLocationsAtNode(node.getNodeID());
			if (list == null) continue;
			for (Move move : list) {
				if (move.getDate().isAfter(LocalDate.now())) break;
				Location loc = move.getLocation();
				if (loc.getNodeType() == NodeType.HALL || !toShow.contains(loc.getNodeType())) break;
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

	private List<NodeType> getLocationTypesToShow() {
		List<NodeType> toShow = new ArrayList<>();
		List<String> nodeTypes = locationSelect.getCheckModel().getCheckedItems();
		for (String type : nodeTypes) {
			switch (type) {
				case "Conference":
					toShow.add(NodeType.CONF);
					break;
				case "Department":
					toShow.add(NodeType.DEPT);
					break;
				case "Elevator":
					toShow.add(NodeType.ELEV);
					break;
				case "Exit":
					toShow.add(NodeType.EXIT);
					break;
				case "Lab":
					toShow.add(NodeType.LABS);
					break;
				case "Restroom":
					toShow.add(NodeType.REST);
					break;
				case "Retailer":
					toShow.add(NodeType.RETL);
					break;
				case "Service":
					toShow.add(NodeType.SERV);
					break;
				case "Stairs":
					toShow.add(NodeType.STAI);
					break;
				case "Info":
					toShow.add(NodeType.INFO);
					break;
				case "Bathroom":
					toShow.add(NodeType.BATH);
					break;
			}
		}
		return toShow;
	}

	/**
	 * Sets all the lambda functions needed in order to make the circles actually responsive on the map
	 *
	 * @param circle the circle to initialize
	 */
	private void initCircle(@NotNull Circle circle) {
		circle.setViewOrder(0);
		circle.setStrokeWidth(3);
		circle.setMouseTransparent(false);
		circle.setOnContextMenuRequested(
				event -> {
					System.out.println("Circled clicked");
					circle.setStroke(Color.YELLOW);
					prevCircle.setStroke(Color.TRANSPARENT);
					prevCircle = circle;
					prevLine.setFill(Color.BLACK);
					prevLine = deleteReferenceLine;
					editNodePopOverController.launchPopup(circle);
					event.consume();
				});
		// Deals with highlighting nodes
		circle.setOnMouseClicked(
				event -> {
					if (!event.isShiftDown() && start == null) {
						circle.setStroke(Color.YELLOW);
						prevCircle.setStroke(Color.TRANSPARENT);
						prevCircle = circle;
						prevLine = deleteReferenceLine;
						event.consume();
					}
				});
		// Deals with adding edges
		circle.setOnMouseClicked(
				event -> {
					if (mode == MoveState.ADD_REMOVE) {
						if (start == null) {
							edgeOne = circle;
							start = listOfCircles.get(edgeOne);
						} else if (end == null) {
							edgeTwo = circle;
							end = listOfCircles.get(edgeTwo);
						}
						circle.setFill(Color.BLUE);
						if (edgeOne != null && edgeTwo != null) {
							Edge addition = new Edge(start, end);
							addEdgeLine(addition);
							// done in case there is a floor switch
							if (edgeOne != null) {
								edgeOne.setFill(Color.RED);
								edgeOne = null;
							}
							edgeTwo.setFill(Color.RED);
							edgeTwo = null;
							start = end = null;
							queueManager.addToQueue(addition);
						}
					} else {
						if (edgeOne != null) {
							edgeOne.setFill(Color.RED);
							edgeOne = null;
						}
						edgeTwo.setFill(Color.RED);
						edgeTwo = null;
						start = null;
						end = null;
						event.consume();
					}
				});
		// Deals with aligning
		circle.setOnMouseClicked(
				event -> {
					if (mode == MoveState.ALIGN && alignmentLine != null) {
						undoAlignment.put(circle, new Point2D(circle.getCenterX(), circle.getCenterY()));
						moveCircleToLine(circle);
						System.out.println("Should be moving to the line");
						event.consume();
					}
				});
		circle.setOnMouseDragged(
				event -> {
					if (mode == MoveState.MOVE) {
						prevCircle = circle;
						circle.setCenterX((int) Math.round(event.getX()));
						circle.setCenterY((int) Math.round(event.getY()));
						circle.setStroke(Color.YELLOW);
						circle.setStrokeWidth(3);
						Node node = listOfCircles.get(circle);
						node.setXCoord((int) Math.round(circle.getCenterX()));
						node.setYCoord((int) Math.round(circle.getCenterY()));
						drawEdges();
						event.consume();
					}
					//          System.out.println("Why is this not dragging");
					if (mode == MoveState.ALIGN && alignmentLine != null) {
						moveCircleAlongLine(event, circle);
						event.consume();
					}
				});
		circle.setOnMouseReleased(
				event -> {
					if (mode == MoveState.MOVE && listOfCircles.get(prevCircle) != null) {
						queueManager.addNodeMoveToQueue(listOfCircles.get(circle));
//						Node temp = listOfCircles.get(circle);
//						temp.setXCoord((int) Math.round(circle.getCenterX()));
//						temp.setYCoord((int) Math.round(circle.getCenterY()));
						System.out.println("Sent node location update");
						event.consume();
					}
					if (mode == MoveState.ALIGN && alignmentLine != null) {
						queueManager.addNodeMoveToQueue(listOfCircles.get(circle));
						event.consume();
					}
				});
	}

	private void initLine(Line line) {
		line.setViewOrder(1);
		line.setOnMouseClicked(
				event -> {
					System.out.println("Line has been clicked");
					prevLine.setStroke(Color.BLACK);
					line.setStroke(Color.BLUE);
					prevLine = line;
					prevCircle.setStroke(Color.TRANSPARENT);
					prevCircle = deleteReferenceCircle;
				});
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
		queueManager.addToDeleteQueue(node);
		queueManager.queue.remove(node);
		listOfCircles.remove(circle);
		anchorPane.getChildren().remove(circle);
		prevCircle = deleteReferenceCircle;
	}

	private void deleteLine(Line line) {
		if (line == deleteReferenceLine) return;
		Edge edge = lines.get(line);
		queueManager.addToDeleteQueue(edge);
		queueManager.queue.remove(edge);
		lines.remove(line);
		anchorPane.getChildren().remove(line);
		prevCircle = deleteReferenceCircle;
	}

	private void addEdgeLine(Edge edge) {
		Line line =
				new Line(
						edge.getStartNode().getXCoord(),
						edge.getStartNode().getYCoord(),
						edge.getEndNode().getXCoord(),
						edge.getEndNode().getYCoord());
		line.setStroke(Color.BLACK);
		line.setStrokeWidth(5.0);
		initLine(line);
		lines.put(line, edge);
		if (showEdges.isSelected()) anchorPane.getChildren().add(line);
	}

	private void moveCircleToLine(Circle circle) {
		double slope =
				(alignmentLine.getEndY() - alignmentLine.getStartY())
				/ (alignmentLine.getEndX() - alignmentLine.getStartX());
		Point2D lineStart = new Point2D(alignmentLine.getStartX(), alignmentLine.getStartY());
		double currentShortest = Double.MAX_VALUE;
		int xVal = 0;
		int xDistance = (int) Math.round(Math.abs(alignmentLine.getEndX() - alignmentLine.getStartX()));
		for (int i = 0; i < xDistance; i++) {
			Point2D tempPoint = new Point2D(lineStart.getX() + i, lineStart.getY() + slope * i);
			double temp =
					Math.sqrt(
							Math.pow((circle.getCenterX() - tempPoint.getX()), 2)
							+ Math.pow((circle.getCenterY() - tempPoint.getY()), 2));
			if (temp < currentShortest) {
				currentShortest = temp;
				xVal = i;
			}
		}
		circle.setCenterX(lineStart.getX() + xVal);
		circle.setCenterY(lineStart.getY() + slope * xVal);
		Node node = listOfCircles.get(circle);
		node.setXCoord((int) Math.round(circle.getCenterX()));
		node.setYCoord((int) Math.round(circle.getCenterY()));
		drawEdges();
	}

	private void moveCircleAlongLine(MouseEvent event, Circle circle) {
		double slope =
				(alignmentLine.getEndY() - alignmentLine.getStartY())
				/ (alignmentLine.getEndX() - alignmentLine.getStartX());
		double b = alignmentLine.getStartY();
		// If it makes more sense to use the coordinate
		System.out.println(slope);
		if (slope > 45) {
			double currentY = event.getY();
			System.out.println(currentY + "currenty");
			System.out.println(currentY);
			circle.setCenterX(alignmentLine.getStartX() + (b - currentY) * 1 / slope);
			circle.setCenterY(currentY);
		} else {
			double currentX = event.getX();
			circle.setCenterX(currentX);
			circle.setCenterY(b + (currentX - alignmentLine.getStartX()) * slope);
		}

		Node node = listOfCircles.get(circle);
		node.setXCoord((int) Math.round(circle.getCenterX()));
		node.setYCoord((int) Math.round(circle.getCenterY()));
		drawEdges();
	}

	// Keeps all the functions needed for alignment in a single place to make it easier to find
	private void allowAlignments() {
		anchorPane.setOnMouseClicked(
				event -> {
					if (mode == MoveState.MAKE_ALIGNMENT_LINE) {
						if (edgeOne == null) {
							edgeOne = new Circle(event.getX(), event.getY(), 5, Color.BLACK);
							event.consume();
							return;
						} else if (edgeTwo == null) {
							edgeTwo = new Circle(event.getX(), event.getY(), 5, Color.BLACK);
							event.consume();
						}
						alignmentLine =
								new Line(
										edgeOne.getCenterX(),
										edgeOne.getCenterY(),
										edgeTwo.getCenterX(),
										edgeTwo.getCenterY());
						alignmentLine.setStroke(Color.BLACK);
						alignmentLine.setStrokeWidth(5);
						alignmentLine.getStrokeDashArray().addAll(20d, 20d, 20d, 20d);
						alignmentLine.setViewOrder(1);
						anchorPane.getChildren().add(alignmentLine);
						mode = MoveState.ALIGN;
					} else if (alignmentLine != null && mode != MoveState.ALIGN) {
						undoAlign();
					}
				});
		mapPane.setOnKeyPressed(
				event -> {
					if ((mode == MoveState.ALIGN || mode == MoveState.MAKE_ALIGNMENT_LINE)
						&& event.getCode().equals(KeyCode.ESCAPE)) {
						undoAlign();
					}
					if ((mode == MoveState.ALIGN || mode == MoveState.MAKE_ALIGNMENT_LINE)
						&& event.getCode().equals(KeyCode.ENTER)) {
						anchorPane.getChildren().remove(alignmentLine);
						alignmentLine = null;
						for (Circle circle : undoAlignment.keySet()) {
							queueManager.addNodeMoveToQueue(listOfCircles.get(circle));
						}
						edgeOne = edgeTwo = null;
						undoAlignment.clear();
						mode = MoveState.DEFAULT;
					}
				});
	}

	private void undoAlign() {
		anchorPane.getChildren().remove(alignmentLine);
		alignmentLine = null;
		for (Circle circle : undoAlignment.keySet()) {
			Point2D reference = undoAlignment.get(circle);
			circle.setCenterX(reference.getX());
			circle.setCenterY(reference.getY());
			Node node = listOfCircles.get(circle);
			node.setXCoord((int) Math.round(circle.getCenterX()));
			node.setYCoord((int) Math.round(circle.getCenterY()));
		}
		System.out.println("Clearing the line");
		undoAlignment.clear();
		edgeOne = edgeTwo = null;
		mode = MoveState.DEFAULT;
		drawEdges();
	}
}
