package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.*;
import edu.wpi.teamname.navigation.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import lombok.Setter;
import org.controlsfx.control.CheckComboBox;

public class RoomBookingController {

  // FXML
  @FXML MFXButton addMeetingButton;
  @FXML TextFieldTableCell dateHeaderTextField;
  @FXML TextFieldTableCell currentDateText;
  @FXML HBox conferenceRoomsHBox; // hbox containing all conference rooms and schedules
  @FXML CheckComboBox featureFilterComboBox;
  @FXML MFXDatePicker DateFilterPicker;
  @FXML MFXButton dateBackButton;
  @FXML MFXButton dateNextButton;
  @FXML MFXToggleButton meetingsToggle;

  public static RoomRequestDAO roomRequestDAO =
      DataBaseRepository.getInstance().getRoomRequestDAO();
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();
  public ConfRoomDAO confRoomDAO = DataBaseRepository.getInstance().getConfRoomDAO();
  @Getter @Setter ArrayList<ConfRoomLocation> roomList = new ArrayList<>();
  ArrayList<VBox> roomListVBoxes = new ArrayList<>();
  RoomBooking rb = new RoomBooking();

  @FXML
  public void initialize() throws SQLException {

    //  scrollPane.setStyle("-fx-box-border: transparent;");
    addMeetingButton.setOnMouseClicked(
        event -> Navigation.launchPopUp(Screen.ROOM_BOOKING_DETAILS));
    addMeetingButton.setCursor(Cursor.HAND);

    initializeRooms();
    featureFilterComboBox.getItems().addAll("Whiteboard", "DocCamera", "Projector");
    featureFilterComboBox.setStyle("-fx-background-color: #FFFFFF");
    dateHeaderTextField.setText(
        //  LocalDate.now().format(DateTimeFormatter.ofPattern("EE, dd MMM yyyy")));
        LocalDate.now().toString());

    // add current requests to UI
    for (ConfRoomRequest i : roomRequestDAO.getAll()) {
      this.addToUI(i);
    }

    filterDate(LocalDate.now());

    featureFilterComboBox
        .getCheckModel()
        .getCheckedItems()
        .addListener(
            new InvalidationListener() {
              @Override
              public void invalidated(Observable observable) {
                filterByFeature(featureFilterComboBox.getCheckModel().getCheckedItems());
                System.out.println(
                    "\n SELECTED ITEMS: "
                        + featureFilterComboBox.getCheckModel().getCheckedItems());
              }
            });

    DateFilterPicker.valueProperty()
        .addListener(
            new ChangeListener<LocalDate>() {
              @Override
              public void changed(
                  ObservableValue<? extends LocalDate> observable,
                  LocalDate oldValue,
                  LocalDate newValue) {
                filterDate(newValue);
              }
            });

    dateBackButton.setOnMouseClicked(
        event -> {
          LocalDate textDate = LocalDate.parse(dateHeaderTextField.getText());
          LocalDate newdate = textDate.minusDays(1);
          dateHeaderTextField.setText(newdate.toString());
          filterDate(newdate);
        });

    dateNextButton.setOnMouseClicked(
        event -> {
          LocalDate textDate = LocalDate.parse(dateHeaderTextField.getText());
          LocalDate newdate = textDate.plusDays(1);
          dateHeaderTextField.setText(newdate.toString());
          filterDate(newdate);
        });

    meetingsToggle.setOnAction(
        event -> {
          if (meetingsToggle.getText().equals("Your meetings")) {
            meetingsToggle.setText("All meetings");
          } else {
            meetingsToggle.setText("Your meetings");
          }
        });
  }

  public static void addNewRequest(
      String roomLocation,
      LocalDate date,
      LocalTime startTime,
      LocalTime endTime,
      String eventTitle,
      String eventDescription,
      boolean isPrivate)
      throws Exception {

    System.out.println("Adding new request");
    ConfRoomRequest newRequest =
        new ConfRoomRequest(
            date,
            startTime,
            endTime,
            roomLocation,
            ActiveUser.getInstance().getCurrentUser().getUserName(),
            eventTitle,
            eventDescription,
            "staff member",
            isPrivate);
    DataBaseRepository.getInstance().addRoomRequest(newRequest); // TODO need this?
  }

  public void initializeRooms() {
    roomList.addAll(confRoomDAO.getAll()); // TODO alphabetize

    for (int i = 0; i < roomList.size(); i++) {

      Rectangle meetingRect = new Rectangle();
      meetingRect.setWidth(170);
      meetingRect.setHeight(85);
      meetingRect.setArcHeight(15);
      meetingRect.setArcWidth(15);
      meetingRect.setStyle("-fx-border-color: #000000");
      meetingRect.setStyle("-fx-border-style: dashed");
      // rect.setStyle("-fx-background-radius: 15;");
      meetingRect.setFill(Paint.valueOf("#B5C5EE"));

      // vbox
      VBox roomVBox = new VBox();
      roomVBox.setAlignment(Pos.valueOf("TOP_CENTER"));
      roomVBox.setMinHeight(500);
      roomVBox.setPrefWidth(170);
      roomVBox.setMaxWidth(170);
      roomVBox.setSpacing(5);
      // roomVBox.setStyle("-fx-border-style: solid dashed double dotted");
      roomVBox.setStyle("-fx-background-color: WHITE");
      roomVBox.setStyle("-fx-border-color: #00000000 #D8D8D8 #FFFFFF00 #D8D8D8");
      // #D8D8D8
      roomVBox.setId(roomList.get(i).getLocation().getLongName().replaceAll(" ", ""));
      roomVBox.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.ROOM_BOOKING_DETAILS));
      roomVBox.setCursor(Cursor.HAND);
      roomListVBoxes.add(roomVBox);

      // text cell
      TextFieldTableCell textField = new TextFieldTableCell();
      textField.setAlignment(Pos.valueOf("CENTER"));
      textField.setTextAlignment(TextAlignment.valueOf("CENTER"));
      textField.setPrefSize(160, 75);
      textField.setMinWidth(160);
      textField.setMaxWidth(140);
      textField.setMinHeight(75);
      textField.setMaxHeight(75);
      textField.setWrapText(true);

      // textField.setStyle("-fx-border-style: solid dashed double dotted;");
      textField.setStyle("-fx-border-color: #FFFFFF00 #FFFFFF00 #D8D8D8 #FFFFFF00");
      textField.setFont(Font.font("Open Sans"));
      textField.setFont(Font.font(16));
      textField.setPadding(new Insets(0, 5, 0, 5));
      textField.setText(roomList.get(i).getLocation().getLongName());
      textField.setTextFill(Paint.valueOf("#1d3d94"));

      // add pieces
      roomVBox.getChildren().add(textField);
      conferenceRoomsHBox.getChildren().add(roomVBox);
    }
  }

  public VBox getVBoxById(String id) {
    for (int i = 0; i < roomListVBoxes.size(); i++) {
      if (roomListVBoxes.get(i).getId().equals(id)) {
        return roomListVBoxes.get(i);
      }
    }
    return null;
  }

  public void filterDate(LocalDate date) {
    clearUI();
    roomList.clear();
    roomListVBoxes.clear();

    initializeRooms();

    for (ConfRoomRequest i : roomRequestDAO.getAll()) {
      if (i.getEventDate().equals(date)) {
        this.addToUI(i);
      }
    }
  }

  public void filterByFeature(ObservableList<String> features) {
    for (int i = 0; i < roomList.size(); i++) {
      roomListVBoxes.get(i).setVisible(false);
      roomListVBoxes.get(i).managedProperty().bind(roomListVBoxes.get(i).visibleProperty());
    }

    if (features.isEmpty()) {
      for (int i = 0; i < roomList.size(); i++) {
        roomListVBoxes.get(i).setVisible(true);
      }
    }

    for (int i = 0; i < roomList.size(); i++) {
      for (int f = 0; f < features.size(); f++) {
        if (!(roomList.get(i).getFeatures().contains(features.get(f)))) {
          System.out.println(
              roomList.get(i).getLocation().getLongName() + " does not contain " + features.get(f));
          break;
        }
        roomListVBoxes.get(i).setVisible(true);
      }
    }
  }

  public void clearUI() {
    for (int i = 0; i < roomListVBoxes.size(); i++) {
      roomListVBoxes.get(i).getChildren().clear();
    }
    conferenceRoomsHBox.getChildren().clear();
  }

  public void addToUI(ConfRoomRequest roomRequest) {

    Group resGroup = new Group(); // create group

    Rectangle rect = new Rectangle(); // create rectangle
    rect.getStyleClass().add("room-request-rect");

    rect.setWidth(150);
    rect.setArcHeight(15);
    rect.setArcWidth(15);
    rect.setFill(Paint.valueOf("#B5C5EE"));
    long timeBetween =
        roomRequest.getStartTime().until(roomRequest.getEndTime(), ChronoUnit.MINUTES);
    rect.setHeight(85 + timeBetween / 2);

    if (roomRequest
        .getReservedBy()
        .equals(ActiveUser.getInstance().getCurrentUser().getUserName())) {
      System.out.println("This is your meeting!");
      rect.getStyleClass().clear();
    }

    // TODO fix!!!!!!

    meetingsToggle
        .selectedProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              if (meetingsToggle.isSelected()) {
                if (roomRequest
                    .getReservedBy()
                    .equals(ActiveUser.getInstance().getCurrentUser().getUserName())) {
                  System.out.println("This is your meeting!");
                  rect.setFill(Paint.valueOf("#E7D3FF"));
                }
              } else {
                rect.setStyle("-fx-effect: NONE");
              }
            }));

    Label descriptionHover = new Label(roomRequest.getEventDescription());
    descriptionHover.setStyle("-fx-text-fill: #1D3D94");

    VBox eventVBox = new VBox();
    eventVBox.setSpacing(2);

    Label title = new Label(); // create text
    title.setText(roomRequest.getEventName());
    title.setFont(Font.font("Open Sans", FontWeight.BOLD, 13));
    title.setTextFill(Paint.valueOf("#1D3D94"));
    title.setWrapText(false);
    title.maxWidth(130);
    title.setEllipsisString("...");

    Text creator = new Text(); // create creator line
    creator.setText(roomRequest.getReservedBy());
    creator.setFont(Font.font("Open Sans", 13));
    creator.setFill(Paint.valueOf("#1D3D94"));

    Text time = new Text();
    time.setText(roomRequest.getStartTime() + " - " + roomRequest.getEndTime());
    time.setFont(Font.font("Open Sans", 13));
    time.setFill(Paint.valueOf("#1D3D9450"));

    ImageView lockImage = new ImageView((String) "edu/wpi/teamname/images/lockicon.png");
    lockImage.setFitHeight(15);
    lockImage.setFitWidth(15);

    if (roomRequest.isPrivate()) {
      title.setText("Private Meeting");
      creator.setText("Private Organizer");
      descriptionHover.setText(null);
    }

    resGroup.getChildren().add(rect);

    eventVBox.setPadding(new Insets(10));
    if (roomRequest.isPrivate()) {
      title.setPrefWidth(110);
      HBox hbox = new HBox(title, lockImage);
      hbox.setAlignment(Pos.CENTER_LEFT);
      eventVBox.getChildren().add(hbox);
    } else {
      eventVBox.getChildren().add(title);
    }
    eventVBox.getChildren().add(creator);
    eventVBox.getChildren().add(time);
    eventVBox.getChildren().add(descriptionHover);
    descriptionHover.setVisible(false);
    descriptionHover.setWrapText(true);

    resGroup.getChildren().add(eventVBox);

    VBox currVBox = getVBoxById(roomRequest.getRoom().replaceAll(" ", ""));

    currVBox.getChildren().add(resGroup);

    eventVBox.setOnMouseEntered(
        event -> {
          descriptionHover.setVisible(true);
        });
    eventVBox.setOnMouseExited(
        event -> {
          descriptionHover.setVisible(false);
        });

    rect.setOnMouseEntered(
        event -> {
          descriptionHover.setVisible(true);
        });
    rect.setOnMouseExited(
        event -> {
          descriptionHover.setVisible(false);
        });
  }
}
