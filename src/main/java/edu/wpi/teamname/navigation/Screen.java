package edu.wpi.teamname.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  WELCOME_PAGE("views/WelcomePage.fxml"),
  LOGIN_PAGE("views/LoginPage.fxml"),
  HOME("views/NewHomepage.fxml"),
  MEAL_DELIVERY("edu/wpi/teamname/views/MealDelivery.fxml"),
  MEAL_DELIVERY1("views/MealDelivery1.fxml"),
  SERVICE_REQUEST("views/ServiceRequestPages/ServiceRequest.fxml"),
  ROOM_BOOKING("views/RoomBooking.fxml"),
  ROOM_BOOKING_DETAILS("views/RoomBookingDetails.fxml"),
  ROOM_BOOKING_CONFIRMATION("views/RoomBookingConfirmation.fxml"),
  SUBMITTED_ROOM_REQUESTS("views/SubmittedRoomRequests.fxml"),
  ORDER_DETAILS("views/OrderDetails.fxml"),
  HELP_PAGE("views/HelpPage.fxml"),
  PRODUCT_DETAILS("views/ProductDetails.fxml"),
  PATHFINDING("views/Pathfinding.fxml"),
  SIGNAGE_PAGE("views/SignagePage.fxml"),
  SUBMITTED_MEALS("views/ServiceRequestPages/SubmittedMeals.fxml"),
  MEAL_DELIVERY_ORDER_CONFIRMATION("views/MealDeliveryOrderConfirmation.fxml"),
  // ORDER_CONFIRMATION("views/OrderConfirmation.fxml"),
  ORDER_CONFIRMATION("views/OrderConfirmation.fxml"),
  MAP_EDITOR("views/MapEditor.fxml"),
  BETTER_MAP_EDITOR("views/BetterMapEditor.fxml"),
  NODE_DETAILS("views/NodeDetails.fxml"),
  CSV_MANAGE("views/CSVPage.fxml"),
  ADMIN_PAGE("views/AdminPage.fxml"),
  SUBMITTED_MEAL_REQUESTS("views/NewSubmittedMealRequests.fxml"),
  // MAP_EDITOR("views/MapEditor.fxml"),
  // CSV_MANAGE("views/CSVPage.fxml"),

  FLOWER_DELIVERY("views/flowerDeliveryMain.fxml"),
  FLOWER_ORDER("views/flowerorderdetails.fxml"),
  FLOWER_CART("views/flowersubmissiondetails.fxml"),
  FLOWER_REQTABLE("views/flowerrequesttable.fxml"),
  FLOWER_CONFIRMATION("views/flowerconfirmationpage.fxml"),
  FLOWER_POPUP("views/flowerPopup.fxml"),

  NEW_USER("views/newUserPopUp.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
