package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.AlertDAO;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.MoveDAOImpl;
import edu.wpi.teamname.DAOs.orms.Alert;
import edu.wpi.teamname.DAOs.orms.Move;
import edu.wpi.teamname.DAOs.orms.User;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.ConfRoomRequest;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.RoomRequestDAO;
import edu.wpi.teamname.ServiceRequests.ConferenceRoom.Status;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDelivery;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDeliveryDAOImp;
import edu.wpi.teamname.ServiceRequests.GeneralRequest.Request;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.OfficeSupply;
import edu.wpi.teamname.ServiceRequests.OfficeSupplies.OfficeSupplyDelivery;
import edu.wpi.teamname.ServiceRequests.flowers.FlowerDelivery;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class newAdminController {
  @FXML MFXButton toMapEditor;
  @FXML TableView mealTable;
  @FXML TableView flowerTable;
  @FXML TableView roomTable;
  @FXML TableView officeTable;
  @FXML TableView moveTable;
  @FXML TableView allTable;
  @FXML MFXButton impexpButton;
  @FXML VBox announcementVBox;
  @FXML ImageView submitAnnouncement;
  @FXML TextArea announcementText;
  @FXML Label greetingHeader;
  @FXML MFXButton aboutUsBtn;
  @FXML MFXButton creditsBtn;

  // @FXML MFXButton aboutUSBtn;

  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  FoodDeliveryDAOImp repo = DataBaseRepository.getInstance().getFoodDeliveryDAO();
  RoomRequestDAO roomrepo = DataBaseRepository.getInstance().getRoomRequestDAO();
  MoveDAOImpl moverepo = DataBaseRepository.getInstance().getMoveDAO();

  public ActiveUser activeUser = ActiveUser.getInstance();
  @FXML public static AlertDAO alertDAO = DataBaseRepository.getInstance().getAlertDAO();
  List<Alert> announcements = alertDAO.getListOfAlerts();

  public void initialize() {
    //  aboutUSBtn.setOnMouseClicked(event -> Navigation.navigate(Screen.ABOUT_US));
    aboutUsBtn.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.ABOUT_US));
    creditsBtn.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.CREDITS_PAGE));

    toMapEditor.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP_EDITOR));
    impexpButton.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.CSV_MANAGE));

    Image sendimage = new Image(String.valueOf(Main.class.getResource("images/send.png")));
    submitAnnouncement.setImage(sendimage);

    System.out.println("ESTIENGE!!");
    submitAnnouncement.setOnMouseClicked(
        event -> {
          String message = announcementText.getText();
          User user = activeUser.getCurrentUser();

          Alert alert = new Alert("", message, user);
          alertDAO.add(alert);

          System.out.println("Alert submitted!");
          System.out.println(alertDAO.getListOfAlerts().size());
          System.out.println(alertDAO.getListOfAlerts());

          initializeAnnouncements(alert);

          announcementText.clear();
        });

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

    initializeTables();

    getTimeString();
  }

  public void initializeAnnouncements(Alert announcement) {

    System.out.println("First name: " + announcement.getUser().getFirstName());
    System.out.println("Header: " + announcement.getHeading());
    System.out.println("Text: " + announcement.getMessage());
    System.out.println("Time: " + announcement.getTimeOfAlert());

    Group addAnnouncement = new Group();

    Rectangle annRect = new Rectangle();
    annRect.setWidth(400);
    annRect.setHeight(100);
    annRect.setStroke(Paint.valueOf("#b5c5ee"));
    annRect.setFill(Paint.valueOf("#FFFFFF"));
    annRect.getStyleClass().add("announcementrect");

    annRect.setArcHeight(15);
    annRect.setArcWidth(15);

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
    annInfo.setPadding(new Insets(20));

    DateFormat dateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.US);

    Label timeLabel = new Label(announcement.getTimeOfAlert().toString());
    timeLabel.setAlignment(Pos.CENTER_RIGHT);
    timeLabel.setPrefWidth(150);

    HBox annhbox = new HBox(annInfo, timeLabel);
    annhbox.setPrefWidth(350);
    annhbox.setPrefHeight(90);
    annhbox.setAlignment(Pos.CENTER);
    annhbox.setPadding(new Insets(10, 0, 0, 0));

    addAnnouncement.getChildren().add(annRect);
    addAnnouncement.getChildren().add(annhbox);

    announcements.sort(
        Comparator.comparing(Alert::getDateOfAlert)
            .reversed()
            .thenComparing(Alert::getTimeOfAlert)
            .reversed());

    announcementVBox.setAlignment(Pos.TOP_LEFT);
    announcementVBox.getChildren().add(addAnnouncement);
    announcementVBox.setSpacing(10);
  }

  public void initializeTables() {

    TableColumn<Food, String> Mcolumn1 = new TableColumn<>("RequestID");
    Mcolumn1.setCellValueFactory(new PropertyValueFactory<>("deliveryID"));

    TableColumn<Food, String> Mcolumn2 = new TableColumn<>("Cart");
    Mcolumn2.setCellValueFactory(new PropertyValueFactory<>("cart"));

    TableColumn<Food, String> Mcolumn3 = new TableColumn<>("Request Date");
    Mcolumn3.setCellValueFactory(new PropertyValueFactory<>("date"));

    TableColumn<Food, String> Mcolumn4 = new TableColumn<>("Request Time");
    Mcolumn4.setCellValueFactory(new PropertyValueFactory<>("time"));

    TableColumn<Food, String> Mcolumn5 = new TableColumn<>("Location");
    Mcolumn5.setCellValueFactory(new PropertyValueFactory<>("location"));

    TableColumn<Food, String> Mcolumn6 = new TableColumn<>("Orderer");
    Mcolumn6.setCellValueFactory(new PropertyValueFactory<>("orderer"));

    TableColumn<Food, String> Mcolumn7 = new TableColumn<>("Assigned To");
    Mcolumn7.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

    TableColumn<Food, String> Mcolumn8 = new TableColumn<>("Status");
    Mcolumn8.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

    TableColumn<Food, String> Mcolumn9 = new TableColumn<>("Cost");
    Mcolumn9.setCellValueFactory(new PropertyValueFactory<>("cost"));

    TableColumn<Food, String> Mcolumn10 = new TableColumn<>("Notes");
    Mcolumn10.setCellValueFactory(new PropertyValueFactory<>("notes"));

    mealTable.getColumns().add(Mcolumn1);
    mealTable.getColumns().add(Mcolumn2);
    mealTable.getColumns().add(Mcolumn3);
    mealTable.getColumns().add(Mcolumn4);
    mealTable.getColumns().add(Mcolumn5);
    mealTable.getColumns().add(Mcolumn6);
    mealTable.getColumns().add(Mcolumn7);
    mealTable.getColumns().add(Mcolumn8);
    mealTable.getColumns().add(Mcolumn9);
    mealTable.getColumns().add(Mcolumn10);

    for (FoodDelivery order : repo.getAll()) {
      mealTable.getItems().add(order);
    }

    TableColumn<FlowerDelivery, Integer> Fcolumn1 = new TableColumn<>("DeliveryID");
    Fcolumn1.setCellValueFactory(new PropertyValueFactory<>("ID"));

    TableColumn<FlowerDelivery, String> Fcolumn2 = new TableColumn<>("Cart");
    Fcolumn2.setCellValueFactory(new PropertyValueFactory<>("cart"));

    TableColumn<FlowerDelivery, Date> Fcolumn3 = new TableColumn<>("Order Date");
    Fcolumn3.setCellValueFactory(new PropertyValueFactory<>("date"));

    TableColumn<FlowerDelivery, Time> Fcolumn4 = new TableColumn<>("Order Time");
    Fcolumn4.setCellValueFactory(new PropertyValueFactory<>("time"));

    TableColumn<FlowerDelivery, String> Fcolumn5 = new TableColumn<>("Room");
    Fcolumn5.setCellValueFactory(new PropertyValueFactory<>("room"));

    TableColumn<FlowerDelivery, String> Fcolumn6 = new TableColumn<>("Ordered By");
    Fcolumn6.setCellValueFactory(new PropertyValueFactory<>("orderedBy"));

    TableColumn<FlowerDelivery, String> Fcolumn7 = new TableColumn<>("Assigned Employee");
    Fcolumn7.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

    TableColumn<FlowerDelivery, Status> Fcolumn8 = new TableColumn<>("Order Status");
    Fcolumn8.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

    TableColumn<FlowerDelivery, Double> Fcolumn9 = new TableColumn<>("Cost");
    Fcolumn9.setCellValueFactory(new PropertyValueFactory<>("cost"));

    flowerTable.getColumns().add(Fcolumn1);
    flowerTable.getColumns().add(Fcolumn2);
    flowerTable.getColumns().add(Fcolumn3);
    flowerTable.getColumns().add(Fcolumn4);
    flowerTable.getColumns().add(Fcolumn5);
    flowerTable.getColumns().add(Fcolumn6);
    flowerTable.getColumns().add(Fcolumn7);
    flowerTable.getColumns().add(Fcolumn8);
    flowerTable.getColumns().add(Fcolumn9);

    for (FlowerDelivery order : dbr.getFlowerDeliveryDAO().getAll()) {
      flowerTable.getItems().add(order);
    }

    TableColumn<ConfRoomRequest, String> Rcolumn1 = new TableColumn<>("Date Ordered");
    Rcolumn1.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

    TableColumn<ConfRoomRequest, String> Rcolumn2 = new TableColumn<>("Event Date");
    Rcolumn2.setCellValueFactory(new PropertyValueFactory<>("eventDate"));

    TableColumn<ConfRoomRequest, String> Rcolumn3 = new TableColumn<>("Start Time");
    Rcolumn3.setCellValueFactory(new PropertyValueFactory<>("startTime"));

    TableColumn<ConfRoomRequest, String> Rcolumn4 = new TableColumn<>("End Time");
    Rcolumn4.setCellValueFactory(new PropertyValueFactory<>("EndTime"));

    TableColumn<ConfRoomRequest, String> Rcolumn5 = new TableColumn<>("Room");
    Rcolumn5.setCellValueFactory(new PropertyValueFactory<>("room"));

    TableColumn<ConfRoomRequest, String> Rcolumn6 = new TableColumn<>("Reserved By");
    Rcolumn6.setCellValueFactory(new PropertyValueFactory<>("reservedBy"));

    TableColumn<ConfRoomRequest, String> Rcolumn7 = new TableColumn<>("Event Name");
    Rcolumn7.setCellValueFactory(new PropertyValueFactory<>("eventName"));

    TableColumn<ConfRoomRequest, String> Rcolumn8 = new TableColumn<>("Event Description");
    Rcolumn8.setCellValueFactory(new PropertyValueFactory<>("eventDescription"));

    TableColumn<ConfRoomRequest, String> Rcolumn9 = new TableColumn<>("Assigned To");
    Rcolumn9.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

    TableColumn<ConfRoomRequest, String> Rcolumn10 = new TableColumn<>("Order Status");
    Rcolumn10.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

    TableColumn<ConfRoomRequest, String> Rcolumn11 = new TableColumn<>("Notes");
    Rcolumn11.setCellValueFactory(new PropertyValueFactory<>("notes"));

    roomTable.getColumns().add(Rcolumn1);
    roomTable.getColumns().add(Rcolumn2);
    roomTable.getColumns().add(Rcolumn3);
    roomTable.getColumns().add(Rcolumn4);
    roomTable.getColumns().add(Rcolumn5);
    roomTable.getColumns().add(Rcolumn6);
    roomTable.getColumns().add(Rcolumn7);
    roomTable.getColumns().add(Rcolumn8);
    roomTable.getColumns().add(Rcolumn9);
    roomTable.getColumns().add(Rcolumn10);
    roomTable.getColumns().add(Rcolumn11);

    for (ConfRoomRequest req : roomrepo.getAll()) {
      roomTable.getItems().add(req);
    }

    TableColumn<OfficeSupply, Integer> Ocolumn1 = new TableColumn<>("DeliveryID");
    Ocolumn1.setCellValueFactory(new PropertyValueFactory<>("deliveryid"));

    TableColumn<OfficeSupply, String> Ocolumn2 = new TableColumn<>("Cart");
    Ocolumn2.setCellValueFactory(new PropertyValueFactory<>("cart"));

    TableColumn<OfficeSupply, Date> Ocolumn3 = new TableColumn<>("Order Date");
    Ocolumn3.setCellValueFactory(new PropertyValueFactory<>("date"));

    TableColumn<OfficeSupply, Time> Ocolumn4 = new TableColumn<>("Order Time");
    Ocolumn4.setCellValueFactory(new PropertyValueFactory<>("time"));

    TableColumn<OfficeSupply, String> Ocolumn5 = new TableColumn<>("Room");
    Ocolumn5.setCellValueFactory(new PropertyValueFactory<>("room"));

    TableColumn<OfficeSupply, String> Ocolumn6 = new TableColumn<>("Ordered By");
    Ocolumn6.setCellValueFactory(new PropertyValueFactory<>("orderedBy"));

    TableColumn<OfficeSupply, String> Ocolumn7 = new TableColumn<>("Assigned Employee");
    Ocolumn7.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

    TableColumn<OfficeSupply, Status> Ocolumn8 = new TableColumn<>("Order Status");
    Ocolumn8.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

    TableColumn<OfficeSupply, Double> Ocolumn9 = new TableColumn<>("Cost");
    Ocolumn9.setCellValueFactory(new PropertyValueFactory<>("cost"));

    officeTable.getColumns().add(Ocolumn1);
    officeTable.getColumns().add(Ocolumn2);
    officeTable.getColumns().add(Ocolumn3);
    officeTable.getColumns().add(Ocolumn4);
    officeTable.getColumns().add(Ocolumn5);
    officeTable.getColumns().add(Ocolumn6);
    officeTable.getColumns().add(Ocolumn7);
    officeTable.getColumns().add(Ocolumn8);
    officeTable.getColumns().add(Ocolumn9);

    for (OfficeSupplyDelivery order : dbr.getOfficeSupplyDeliveryDAO().getAll()) {
      officeTable.getItems().add(order);
    }

    TableColumn<OfficeSupply, String> Acolumn1 = new TableColumn<>("Request type");
    Acolumn1.setCellValueFactory(new PropertyValueFactory<>("requestType"));

    TableColumn<OfficeSupply, Date> Acolumn2 = new TableColumn<>("Delivery Location");
    Acolumn2.setCellValueFactory(new PropertyValueFactory<>("location"));

    TableColumn<OfficeSupply, Time> Acolumn3 = new TableColumn<>("Request Time");
    Acolumn3.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));

    TableColumn<OfficeSupply, String> Acolumn4 = new TableColumn<>("Ordered By");
    Acolumn4.setCellValueFactory(new PropertyValueFactory<>("orderedBy"));

    TableColumn<OfficeSupply, String> Acolumn5 = new TableColumn<>("Assigned To");
    Acolumn5.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

    TableColumn<OfficeSupply, String> Acolumn6 = new TableColumn<>("Order Status");
    Acolumn6.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

    allTable.getColumns().add(Acolumn1);
    allTable.getColumns().add(Acolumn2);
    allTable.getColumns().add(Acolumn3);
    allTable.getColumns().add(Acolumn4);
    allTable.getColumns().add(Acolumn5);
    allTable.getColumns().add(Acolumn6);

    dbr.getRequestDAO().loadFromRemote();

    for (Request r : dbr.getRequestDAO().getRequests()) {
      allTable.getItems().add(r);
    }

    TableColumn<Food, String> MOcolumn1 = new TableColumn<>("Node ID");
    MOcolumn1.setCellValueFactory(new PropertyValueFactory<>("nodeID"));

    TableColumn<Food, String> MOcolumn2 = new TableColumn<>("Location");
    MOcolumn2.setCellValueFactory(new PropertyValueFactory<>("location"));

    TableColumn<Food, String> MOcolumn3 = new TableColumn<>("Date");
    MOcolumn3.setCellValueFactory(new PropertyValueFactory<>("date"));

    moveTable.getColumns().add(MOcolumn1);
    moveTable.getColumns().add(MOcolumn2);
    moveTable.getColumns().add(MOcolumn3);

    for (Move m : moverepo.getAll()) {
      moveTable.getItems().add(m);
    }
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

    greetingHeader.setText(
        timeString + ", " + ActiveUser.getInstance().getCurrentUser().getFirstName() + "!");

    return timeString;
  }
}
