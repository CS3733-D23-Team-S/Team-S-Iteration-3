package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.AlertDAO;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Alert;
import edu.wpi.teamname.ServiceRequests.GeneralRequest.Request;
import edu.wpi.teamname.ServiceRequests.GeneralRequest.RequestDAO;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import java.text.DateFormat;
import java.time.LocalTime;
import java.util.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class StaffHomeController {

  @FXML Label headerGreetingLabel;
  @FXML Label requestCount;
  @FXML VBox taskVBox;
  @FXML VBox locationVBox;
  @FXML VBox announcementVBox;

  @FXML MFXButton aboutUsBtn;
  @FXML MFXButton creditsBtn;
  @FXML Group taskGroup;
  @FXML Group announcementGroup;

  @FXML public static RequestDAO requestDAO = DataBaseRepository.getInstance().getRequestDAO();
  List<Request> requests =
      requestDAO.getRequestsForUser((ActiveUser.getInstance().getCurrentUser()));
  ArrayList<Request> newRequests = new ArrayList<Request>();

  @FXML public static AlertDAO alertDAO = DataBaseRepository.getInstance().getAlertDAO();
  List<Alert> announcements = alertDAO.getListOfAlerts();

  @FXML
  public void initialize() {

    aboutUsBtn.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.ABOUT_US));
    creditsBtn.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.CREDITS_PAGE));

    System.out.println(requests);
    // clean requests
    for (int i = 0; i < requests.size(); i++) {

      if (requests.get(i).getOrderStatus() == null) {
        requests.get(i).setOrderStatus("");
      }

      if (!(requests.get(i).getRequestType().equals("Room"))
          && !(requests.get(i).getOrderStatus().equals(""))
          && !(requests.get(i).getOrderStatus().equals("Complete"))) {
        newRequests.add(requests.get(i));
      }
      newRequests.sort(Comparator.comparing(Request::getDeliveryTime));
    }
    requestCount.setText(String.valueOf(newRequests.size()));
    for (Request request : newRequests) {
      initializeTask(request);
    }

    announcements.sort(
        Comparator.comparing(Alert::getDateOfAlert)
            .reversed()
            .thenComparing(Alert::getTimeOfAlert)
            .reversed());

    System.out.println("ANNOUNCEMENTS SIZZ:E" + announcements.size());
    for (int i = 0; i < announcements.size(); i++) {
      initializeAnnouncements(announcements.get(i));
      System.out.println("Initializign announcement: " + announcements.get(i).getMessage());
    }

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
    MFXComboBox statusBox = new MFXComboBox();

    statusBox.getItems().addAll("Received", "In Progress", "Complete");

    if (request.getOrderStatus().equals("Received")) {
      statusBox.getSelectionModel().selectItem("Received");
      statusBox.setStyle("-fx-background-color: #FECAC7");
    } else if (request.getOrderStatus().equals("In Progress")) {
      statusBox.getSelectionModel().selectItem("In Progress");
      statusBox.setStyle("-fx-background-color: #FEF5C7");
    } else if (request.getOrderStatus().equals("Complete")) {
      statusBox.getSelectionModel().selectItem("Complete");
      statusBox.setStyle("-fx-background-color: #C7FECC");

    } else {
      statusBox.getSelectionModel().selectItem("Received");
      statusBox.setStyle("-fx-background-color: #FECAC7");
    }

    request.setOrderStatus(statusBox.getSelectionModel().getSelectedItem().toString());

    statusBox.setFloatMode(FloatMode.ABOVE);
    statusBox.setStyle("-fx-border-color: #FFFFFF00");

    addTask.getChildren().add(taskRect);
    addTask.getChildren().add(infoVBox);
    addTask.getChildren().add(statusBox);

    statusBox.setLayoutX(230);
    statusBox.setLayoutY(45);

    taskVBox.setAlignment(Pos.TOP_LEFT);

    taskVBox.getChildren().add(addTask);
    taskVBox.setSpacing(10);

    statusBox.setOnAction(
        event -> {
          System.out.println(
              "STATUS CHANGED: " + request.getRequestType() + " " + request.getOrderStatus());

          request.setOrderStatus(statusBox.getSelectionModel().getSelectedItem().toString());

          if (request.getOrderStatus().equals("Received")) {
            statusBox.setStyle("-fx-background-color: #E7D3FF");
          } else if (request.getOrderStatus().equals("In Progress")) {
            statusBox.setStyle("-fx-background-color: #FEF5C7");
          } else if (request.getOrderStatus().equals("Complete")) {
            statusBox.setStyle("-fx-background-color: #C7FECC");
          }

          requestDAO.updateRequest(
              request.getOrderStatus(),
              request.getOrderedBy(),
              request.getDeliveryTime(),
              request.getRequestType());
        });
  }

  public void initializeAnnouncements(Alert announcement) {

    System.out.println("First name: " + announcement.getUser().getFirstName());
    System.out.println("Header: " + announcement.getHeading());
    System.out.println("Text: " + announcement.getMessage());
    System.out.println("Time: " + announcement.getTimeOfAlert());

    Group addAnnouncement = new Group();

    Rectangle annRect = new Rectangle();
    annRect.setWidth(360);
    annRect.setHeight(100);
    annRect.setStroke(Paint.valueOf("#b5c5ee"));
    annRect.getStyleClass().add("announcementrect");

    /*
    ImageView profile = new ImageView();
    profile.setFitHeight(60);
    profile.setFitWidth(60);
    Image i =
        new Image(
            String.valueOf(Main.class.getResource("images/00_thelowerlevel1.png").toString()));
    profile.setImage(i);
    System.out.println("Profile image: " + profile.getImage());

     */

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

    announcementVBox.setAlignment(Pos.CENTER);
    announcementVBox.getChildren().add(addAnnouncement);
    announcementVBox.setSpacing(10);
  }

  public String getTimeString() {
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

    return timeString;
  }
}
