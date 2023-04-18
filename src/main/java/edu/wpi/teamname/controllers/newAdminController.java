package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.DataBaseRepository;
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
import java.sql.Date;
import java.sql.Time;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class newAdminController {

  @FXML TableView mealTable;
  @FXML TableView flowerTable;
  @FXML TableView roomTable;
  @FXML TableView officeTable;
  @FXML TableView moveTable;
  @FXML TableView allTable;
  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();
  FoodDeliveryDAOImp repo = DataBaseRepository.getInstance().getFoodDeliveryDAO();
  RoomRequestDAO roomrepo = DataBaseRepository.getInstance().getRoomRequestDAO();

  public void initialize() {

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
  }
}
