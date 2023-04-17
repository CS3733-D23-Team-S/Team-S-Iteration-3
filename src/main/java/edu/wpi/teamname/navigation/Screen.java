package edu.wpi.teamname.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  WELCOME_PAGE("views/WelcomePage.fxml"),
  LOGIN_PAGE("views/LoginPage.fxml"),
  HOME("views/NewHomepage.fxml"),
  MEAL_DELIVERY1("views/MealDelivery/MealDelivery.fxml"),
  SERVICE_REQUEST("views/ServiceRequestPages/ServiceRequest.fxml"),
  ROOM_BOOKING("views/RoomReservation/RoomBooking.fxml"),
  ROOM_BOOKING_DETAILS("views/RoomReservation/RoomBookingDetails.fxml"),
  ROOM_BOOKING_CONFIRMATION("views/RoomReservation/RoomBookingConfirmation.fxml"),
  SUBMITTED_ROOM_REQUESTS("views/RoomReservation/SubmittedRoomRequests.fxml"),
  ORDER_DETAILS("views/OrderDetails.fxml"),
  HELP_PAGE("views/HelpPage.fxml"),
  PRODUCT_DETAILS("views/ProductDetails.fxml"),
  PATHFINDING("views/Pathfinding.fxml"),
  SIGNAGE_PAGE("views/SignagePage.fxml"),
  SUBMITTED_MEALS("views/ServiceRequestPages/SubmittedMeals.fxml"),
  MEAL_DELIVERY_ORDER_CONFIRMATION("views/MealDelivery/MealDeliveryOrderConfirmation.fxml"),
  // ORDER_CONFIRMATION("views/OrderConfirmation.fxml"),
  ORDER_CONFIRMATION("views/OrderConfirmation.fxml"),
  MAP_EDITOR("views/MapEditor.fxml"),
  BETTER_MAP_EDITOR("views/BetterMapEditor.fxml"),
  NODE_DETAILS("views/NodeDetails.fxml"),
  CSV_MANAGE("views/CSVPage.fxml"),
  ADMIN_PAGE("views/AdminPage.fxml"),
  SUBMITTED_MEAL_REQUESTS("views/MealDelivery/NewSubmittedMealRequests.fxml"),
  // MAP_EDITOR("views/MapEditor.fxml"),
  // CSV_MANAGE("views/CSVPage.fxml"),

  FLOWER_DELIVERY("views/FlowerDelivery/flowerDeliveryMain.fxml"),
  FLOWER_ORDER("views/FlowerDelivery/flowerorderdetails.fxml"),
  FLOWER_CART("views/FlowerDelivery/flowersubmissiondetails.fxml"),
  FLOWER_REQTABLE("views/FlowerDelivery/flowerrequesttable.fxml"),
  FLOWER_CONFIRMATION("views/FlowerDelivery/flowerconfirmationpage.fxml"),
  FLOWER_POPUP("views/FlowerDelivery/flowerPopup.fxml"),

  OFFICE_SUPPLIES_DELIVERY("views/officeSuppliesMain.fxml"),
  OFFICE_SUPPLIES_ORDER("views/officeSuppliesOrderDetails.fxml"),
  OFFICE_SUPPLIES_CART("views/officeSuppliesSubmissionDetails.fxml"),
  OFFICE_SUPPLIES_REQTABLE("views/officeSuppliesRequestTable.fxml"),
  OFFICE_SUPPLIES_CONFIRMATION("views/officeSuppliesConfirmationPage.fxml"),
  OFFICE_SUPPLIES_POPUP("views/officeSuppliesPopup.fxml"),

  NEW_USER("views/newUserPopUp.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
