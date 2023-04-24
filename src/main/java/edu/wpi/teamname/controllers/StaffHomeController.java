package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class StaffHomeController {

  @FXML Label headerGreetingLabel;
  @FXML VBox taskVBox;
  @FXML VBox locationVBox;
  @FXML VBox announcementVBox;
  @FXML Group taskGroup;
  @FXML Group announcementGroup;

  public static RoomRequestDAO roomRequestDAO =
      DataBaseRepository.getInstance().getRoomRequestDAO();

  List<ConfRoomRequest> requestList = roomRequestDAO.getAll();

  @FXML
  public void initialize() {

    // add tasks to task vbox
    initializeTasks();

    // add announcements to vbox
  }

  public void initializeTasks() {

    for (int i = 0; i < 3; i++) {
      Group addTask = new Group();

      Rectangle taskRect = new Rectangle();
      taskRect.setWidth(400);
      taskRect.setHeight(120);
      taskRect.setFill(Paint.valueOf("#b5c5ee"));

      taskVBox.getChildren().add(taskRect);

      // taskRect.getStyleClass().add("taskrectangle");

      // Label titleLabel = new Label("Test Label");
      // titleLabel.setAlignment(Pos.CENTER_LEFT);
      // titleLabel.getStyleClass().add("titlelabel");

      // Label roomLabel = new Label("Test Location Label");
      // roomLabel.getStyleClass().add("roomlabel");

      // Label organizerLabel = new Label("Organizer label");
      // organizerLabel.getStyleClass().add("organizerlabel");

      // HBox titleHBox = new HBox(titleLabel);

      //  VBox taskDescriptionVBox = new VBox(titleHBox, roomLabel, organizerLabel);

      //  addTask.getChildren().add(taskRect);
      //  addTask.getChildren().add(taskDescriptionVBox);

      //
      //      Label titleLabel = new Label("Testing!");
      //      HBox titleHBox = new HBox(titleLabel);
      //
      //      // vbox with information
      //      Label locationLabel = new Label("Anna Carroll Teaching Room");
      //      Label orderLabel = new Label("Ordered by meee");
      //
      //      locationLabel.setFont(Font.font("Arial"));
      //      locationLabel.setPrefHeight(12);
      //      orderLabel.setFont(Font.font("Arial"));
      //      orderLabel.setStyle("-fx-font-style: italic");
      //
      //      VBox infoVBox = new VBox();
      //
      //      infoVBox.getChildren().add(titleHBox);
      //      infoVBox.getChildren().add(locationLabel);
      //      infoVBox.getChildren().add(orderLabel);
      //
      //      newGroup.getChildren().add(infoVBox);
      //
      //      taskVBox.getChildren().add(newGroup);
    }
  }
}
