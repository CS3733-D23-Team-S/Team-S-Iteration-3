package edu.wpi.teamname.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  WELCOME_PAGE("views/WelcomePage.fxml"),
  LOGIN_PAGE("views/LoginPage.fxml"),
  HOME("views/Home.fxml"),
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
  SIGNAGE_PAGE("edu/wpi/teamname/views/SignagePage.fxml"),
  ORDER_CONFIRMATION("views/MealDeliveryOrderConfirmation.fxml"),
  SUBMITTED_MEALS("views/ServiceRequestPages/SubmittedMeals.fxml"),
  MEAL_DELIVERY_ORDER_CONFIRMATION("views/ServiceRequestPages.MealDeliveryOrderConfirmation.fxml"),
  // ORDER_CONFIRMATION("views/OrderConfirmation.fxml"),
  CSV_MANAGE("views/CSVPage.fxml"),
  NEW_USER("views/newUserPopUp.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
