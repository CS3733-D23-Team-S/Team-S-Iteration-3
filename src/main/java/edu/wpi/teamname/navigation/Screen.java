package edu.wpi.teamname.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  WELCOME_PAGE("views/WelcomePage.fxml"),
  LOGIN_PAGE("views/LoginPage.fxml"),
  HOME("views/NewHomepage.fxml"),
  MEAL_DELIVERY("views/MealDelivery/MealDeliveryMain-New.fxml"),
  SERVICE_REQUEST("views/ServiceRequestPages/ServiceRequest.fxml"),
  ROOM_BOOKING("views/RoomReservation/RoomBooking.fxml"),
  ROOM_BOOKING_DETAILS("views/RoomReservation/RoomBookingDetails.fxml"),
  ROOM_BOOKING_CONFIRMATION("views/RoomReservation/RoomBookingConfirmation.fxml"),
  SUBMITTED_ROOM_REQUESTS("views/RoomReservation/SubmittedRoomRequests.fxml"),
  ORDER_DETAILS("views/OrderDetails.fxml"),
  HELP_PAGE("views/HelpPage.fxml"),
  PRODUCT_DETAILS("views/MealDelivery/MealDeliveryPopup.fxml"),
  PATHFINDING("views/Pathfinding.fxml"),
  SIGNAGE_PAGE("views/SignagePage.fxml"),
  SUBMITTED_MEALS("views/ServiceRequestPages/SubmittedMeals.fxml"),
  MEAL_DELIVERY_ORDER_CONFIRMATION("views/MealDelivery/MealDeliveryOrderConfirmation.fxml"),
  // ORDER_CONFIRMATION("views/OrderConfirmation.fxml"),
  ORDER_CONFIRMATION("views/OrderConfirmation.fxml"),
  MAP_EDITOR("views/NewestMapEditor.fxml"),
  NODE_DETAILS("views/NodeDetails.fxml"),
  CSV_MANAGE("views/CSVPage.fxml"),
  ADMIN_PAGE("views/newAdminPage.fxml"),
  USER_PROFILE("views/UserProfile.fxml"),
  USER_PROFILE_POPUP("views/changePasswordPopup.fxml"),
  // MAP_EDITOR("views/MapEditor.fxml"),
  // CSV_MANAGE("views/CSVPage.fxml"),
  FLOWER_DELIVERY("views/FlowerDelivery/flowerDeliveryMain-New.fxml"),
  FLOWER_CART("views/FlowerDelivery/flowersubmissiondetails.fxml"),
  FLOWER_POPUP("views/FlowerDelivery/flowerPopup.fxml"),

  OFFICE_SUPPLIES_DELIVERY("views/OfficeSupplies/officeSuppliesMain-New.fxml"),
  OFFICE_SUPPLIES_REQTABLE("views/officeSuppliesRequestTable.fxml"),
  OFFICE_SUPPLIES_POPUP("views/OfficeSupplies/officeSuppliesPopup.fxml"),
  NEW_ADMIN_PAGE("views/newAdminPage.fxml"),

  NEW_USER("views/newUserPopUp.fxml"),

  STAFF("views/StaffPage.fxml"),
  STAFF1("views/staff1.fxml"),

  STAFFHOME("views/StaffHome.fxml"),

  PATHFINDING_POPUP("views/pathfinderMovePopUp.fxml"),


  SIGNAGE_EDITOR("views/SignageEditor.fxml"),
  ABOUT_US("views/aboutus.fxml"),
  CREDITS_PAGE("views/Credits.fxml"),
  ROOT_LOGOUT_POPUP("views/RootLogoutPopup.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
