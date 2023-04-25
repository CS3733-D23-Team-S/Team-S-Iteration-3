package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.AlertDAO;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Alert;
import edu.wpi.teamname.ServiceRequests.GeneralRequest.Request;
import edu.wpi.teamname.ServiceRequests.GeneralRequest.RequestDAO;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
  @FXML Group taskGroup;
  @FXML Group announcementGroup;

  @FXML public static RequestDAO requestDAO = DataBaseRepository.getInstance().getRequestDAO();
  ArrayList<Request> requests = requestDAO.getRequests();
  ArrayList<Request> newRequests = new ArrayList<Request>();

  @FXML public static AlertDAO alertDAO = DataBaseRepository.getInstance().getAlertDAO();

  Alert alert1 = new Alert("Test Announcement", "Testing out the announcements!");
  Alert alert2 = new Alert("Hello world!", "Wilson bilson bong");

  List<Alert> announcements = alertDAO.getAll();

  // announcements.

  @FXML
  public void initialize() {

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

    for (int i = 0; i < announcements.size(); i++) {
      initializeAnnouncements(announcements.get(i));
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
      System.out.println("Changed request status to received");
      statusBox.setStyle("-fx-background-color: #FECAC7");
    } else if (request.getOrderStatus().equals("In Progress")) {
      statusBox.getSelectionModel().selectItem("In Progress");
      System.out.println("Changed request status to in progress");
      statusBox.setStyle("-fx-background-color: #FEF5C7");
    } else if (request.getOrderStatus().equals("Complete")) {
      statusBox.getSelectionModel().selectItem("Complete");
      System.out.println("Changed request status to complete");
      statusBox.setStyle("-fx-background-color: #C7FECC");

    } else {
      statusBox.getSelectionModel().selectItem("Received");
      System.out.println("Changed request status to received");
      statusBox.setStyle("-fx-background-color: #FECAC7");
    }

    request.setOrderStatus(statusBox.getSelectionModel().getSelectedItem().toString());
    System.out.println("Selected:" + statusBox.getSelectionModel().getSelectedItem());

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

          System.out.println("Selected:" + statusBox.getSelectionModel().getSelectedItem());

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

    Group addAnnouncement = new Group();

    Rectangle annRect = new Rectangle();
    annRect.setWidth(360);
    annRect.setHeight(100);
    annRect.setStroke(Paint.valueOf("#b5c5ee"));
    annRect.getStyleClass().add("announcementrect");

    ImageView profile = new ImageView();
    profile.setFitHeight(60);
    profile.setFitWidth(60);

    Label nameLabel = new Label("User");
    Label headerLabel = new Label(announcement.getHeading());
    headerLabel.setStyle("-fx-font-weight: bold");
    Label announcementLabel = new Label(announcement.getMessage());
    VBox annInfo = new VBox(nameLabel, headerLabel, announcementLabel);
    annInfo.setPrefWidth(240);
    annInfo.setPadding(new Insets(20, 0, 10, 0));

    Label timeLabel = new Label(announcement.getDateOfAlert().toString());

    HBox annhbox = new HBox(profile, annInfo, timeLabel);
    annhbox.setPrefWidth(350);
    annhbox.setPrefHeight(90);
    annhbox.setAlignment(Pos.CENTER_LEFT);
    annhbox.setPadding(new Insets(10, 0, 0, 0));

    addAnnouncement.getChildren().add(annRect);
    addAnnouncement.getChildren().add(annhbox);

    announcementVBox.setAlignment(Pos.TOP_LEFT);
    announcementVBox.getChildren().add(addAnnouncement);
    announcementVBox.setSpacing(10);
  }

  public String getTimeString() {
    String timeString;
    String name = "Sarah";
    int currentHour = LocalTime.now().getHour();
    if (currentHour >= 5 && currentHour <= 11) {
      timeString = "Good morning";
    } else if (currentHour > 11 && currentHour <= 17) {
      timeString = "Good afternoon";
    } else {
      timeString = "Good evening";
    }

    System.out.println(name);
    headerGreetingLabel.setText(timeString + ", " + name + "!");

    return timeString;
  }
}
