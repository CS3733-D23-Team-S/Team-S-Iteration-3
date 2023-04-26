package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.AlertDAO;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.MoveDAOImpl;
import edu.wpi.teamname.DAOs.orms.Alert;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.GeneralRequest.Request;
import edu.wpi.teamname.ServiceRequests.GeneralRequest.RequestDAO;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.enums.FloatMode;

import java.text.DateFormat;
import java.time.LocalTime;
import java.util.*;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import net.kurobako.gesturefx.GesturePane;

public class StaffHomeController {

	@FXML
	Label headerGreetingLabel;
	@FXML
	Label requestCount;
	@FXML
	VBox taskVBox;
	@FXML
	VBox announcementVBox;

	@FXML
	MFXButton aboutUsBtn;
	@FXML
	MFXButton creditsBtn;
	@FXML
	Group taskGroup;
	@FXML
	Group announcementGroup;
	@FXML
	MFXScrollPane moveScrollPane;
	@FXML
	VBox movesVBox;
	@FXML
	GesturePane locationGesturePane;

	@FXML
	RequestDAO requestDAO = DataBaseRepository.getInstance().getRequestDAO();
	List<Request> requests =
			requestDAO.getRequestsForUser((ActiveUser.getInstance().getCurrentUser()));
	ArrayList<Request> newRequests = new ArrayList<>();

	AlertDAO alertDAO = DataBaseRepository.getInstance().getAlertDAO();

	List<Alert> announcements = alertDAO.getListOfAlerts();

	MoveDAOImpl moveDAO = DataBaseRepository.getInstance().getMoveDAO();
	ArrayList<MoveDAOImpl.futureMoves> futureMovesList = moveDAO.getFutureMoves();

	@FXML
	public void initialize() {

		aboutUsBtn.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.ABOUT_US));
		creditsBtn.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.CREDITS_PAGE));

		System.out.println(requests);
		// clean requests
		// requests
		for (Request value : requests) {

			if (value.getOrderStatus() == null) {
				value.setOrderStatus("");
			}

			if (!(value.getRequestType().equals("Room"))
				&& !(value.getOrderStatus().equals(""))
				&& !(value.getOrderStatus().equals("Complete"))) {
				newRequests.add(value);
			}
			newRequests.sort(Comparator.comparing(Request::getDeliveryTime));
		}
		requestCount.setText(String.valueOf(newRequests.size()));
		for (Request request : newRequests) {
			initializeTask(request);
		}

		// announcements
		announcements.sort(
				Comparator.comparing(Alert::getDateOfAlert)
						.reversed()
						.thenComparing(Alert::getTimeOfAlert)
						.reversed());

		for (Alert announcement : announcements) {
			initializeAnnouncements(announcement);
		}

		futureMovesList.sort(Comparator.comparing(MoveDAOImpl.futureMoves::getMoveDate).reversed());

		// moves
		System.out.println("MOVES LIST TEST");
		System.out.println(futureMovesList.size() + " future moves size");
		for (MoveDAOImpl.futureMoves futureMoves : futureMovesList) {
			System.out.println("Adding move " + futureMoves.getLocName());
			initializeMoves(futureMoves);
		}

		ImageView content =
				new ImageView(
						new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png"))));

		locationGesturePane.setContent(content);
		locationGesturePane.setMinScale(.0001);
		locationGesturePane.setFitHeight(true);
		locationGesturePane.setFitWidth(true);
		locationGesturePane.zoomTo(.15, .15, new Point2D(2500, 1700));

		getTimeString();
	}

	public void initializeTask(Request request) {

		String title = request.getRequestType();
		String location = request.getLocation();
		String organizer = request.getOrderedBy();
		String orderstatus = request.getOrderStatus();

		Group addTask = new Group();

		// rectangle styling
		Rectangle taskRect = new Rectangle();
		taskRect.setWidth(360);
		taskRect.setHeight(120);
		taskRect.setFill(Paint.valueOf("#b5c5ee"));
		taskRect.getStyleClass().add("taskrectangle");

		// labels
		Label titleLabel = new Label(title + " Delivery");
		titleLabel.setAlignment(Pos.CENTER_LEFT);
		titleLabel.getStyleClass().add("titlelabel");

		Label locationLabel = new Label(location);
		locationLabel.getStyleClass().add("locationlabel");

		Label organizerLabel = new Label("Organized by " + organizer);
		organizerLabel.getStyleClass().add("organizerlabel");

		VBox infoVBox = new VBox(titleLabel, locationLabel, organizerLabel);
		infoVBox.setPadding(new Insets(10));
		infoVBox.setMinHeight(110);
		infoVBox.setAlignment(Pos.CENTER_LEFT);

		// status box
		MFXComboBox<String> statusBox = new MFXComboBox<>();

		statusBox.getItems().addAll("Received", "In Progress", "Complete");

		switch (request.getOrderStatus()) {
			case "In Progress" -> {
				statusBox.getSelectionModel().selectItem("In Progress");
				statusBox.setStyle("-fx-background-color: #FEF5C7");
			}
			case "Complete" -> {
				statusBox.getSelectionModel().selectItem("Complete");
				statusBox.setStyle("-fx-background-color: #C7FECC");
			}
			default -> {
				statusBox.getSelectionModel().selectItem("Received");
				statusBox.setStyle("-fx-background-color: #FECAC7");
			}
		}

		request.setOrderStatus(statusBox.getSelectionModel().getSelectedItem());

		statusBox.setFloatMode(FloatMode.ABOVE);
		statusBox.setStyle("-fx-border-color: #FFFFFF00");

		addTask.getChildren().add(taskRect);
		addTask.getChildren().add(infoVBox);
		addTask.getChildren().add(statusBox);

		statusBox.setLayoutX(230);
		statusBox.setLayoutY(45);

		taskVBox.setAlignment(Pos.TOP_LEFT);

		taskVBox.getChildren().add(addTask);
		taskVBox.setSpacing(5);

		statusBox.setOnAction(
				event -> {
					request.setOrderStatus(statusBox.getSelectionModel().getSelectedItem());

					switch (request.getOrderStatus()) {
						case "Received" -> statusBox.setStyle("-fx-background-color: #E7D3FF");
						case "In Progress" -> statusBox.setStyle("-fx-background-color: #FEF5C7");
						case "Complete" -> statusBox.setStyle("-fx-background-color: #C7FECC");
					}

					requestDAO.updateRequest(
							request.getOrderStatus(),
							request.getOrderedBy(),
							request.getDeliveryTime(),
							request.getRequestType());
				});

		statusBox
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(options, oldValue, newValue) -> {
							Navigation.navigate(Screen.STAFFHOME);
						});
	}

	public void initializeAnnouncements(Alert announcement) {
		Group addAnnouncement = new Group();

		Rectangle annRect = new Rectangle();
		annRect.setWidth(360);
		annRect.setHeight(100);
		annRect.setStroke(Paint.valueOf("#b5c5ee"));
		annRect.getStyleClass().add("announcementrect");

		Label nameLabel =
				new Label(
						announcement.getUser().getFirstName() + " " + announcement.getUser().getLastName());
		nameLabel.setStyle("-fx-font-weight: bold");
		Label announcementLabel = new Label(announcement.getMessage());
		announcementLabel.setWrapText(true);
		VBox annInfo = new VBox(nameLabel, announcementLabel);
		annInfo.setPrefWidth(240);
		annInfo.setPadding(new Insets(10));

		DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.US);

		Label timeLabel = new Label(announcement.getTimeOfAlert().toString());
		timeLabel.setAlignment(Pos.CENTER_RIGHT);
		timeLabel.setPrefWidth(100);

		HBox annhbox = new HBox(annInfo, timeLabel);
		annhbox.setPrefWidth(350);
		annhbox.setPrefHeight(90);
		annhbox.setAlignment(Pos.CENTER);
		annhbox.setPadding(new Insets(10, 0, 0, 0));

		addAnnouncement.getChildren().add(annRect);
		addAnnouncement.getChildren().add(annhbox);

		announcementVBox.setAlignment(Pos.TOP_LEFT);
		announcementVBox.getChildren().add(addAnnouncement);
		announcementVBox.setSpacing(10);
	}

	public void initializeMoves(MoveDAOImpl.futureMoves move) {

		Label dateLabel = new Label(move.getMoveDate().toString());
		dateLabel.setAlignment(Pos.TOP_CENTER);
		dateLabel.setMinWidth(100);
		dateLabel.setStyle("-fx-text-style: italic");
		dateLabel.setStyle("-fx-font-size: 16");
		dateLabel.setPadding(new Insets(10));
		TextFieldTableCell<String, String> moveText = new TextFieldTableCell<>();
		moveText.setMinWidth(250);
		moveText.setMinHeight(50);
		moveText.setWrapText(true);
		moveText.setStyle("-fx-font-size: 12");
		moveText.setAlignment(Pos.TOP_CENTER);
		dateLabel.setPadding(new Insets(15));

		moveText.setText(
				move.getLocName()
				+ " is relocating to node "
				+ move.getNodeId()
				+ " (floor "
				+ move.getFloor().substring(5)
				+ ")");

		HBox moveHBox = new HBox(dateLabel, moveText);
		moveHBox.setPadding(new Insets(5));
		moveHBox.setPrefWidth(400);
		moveHBox.setMaxWidth(400);

		movesVBox.getChildren().add(moveHBox);
	}

	public void getTimeString() {
		String timeString;
		int currentHour = LocalTime.now().getHour();
		if (currentHour >= 5 && currentHour <= 11) {
			timeString = "Good morning";
		} else if (currentHour > 11 && currentHour <= 17) {
			timeString = "Good afternoon";
		} else {
			timeString = "Good evening";
		}

		headerGreetingLabel.setText(
				timeString + ", " + ActiveUser.getInstance().getCurrentUser().getFirstName() + "!");

	}
}
