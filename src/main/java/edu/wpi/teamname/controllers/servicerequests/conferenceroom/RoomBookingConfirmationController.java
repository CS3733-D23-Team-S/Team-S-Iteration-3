package edu.wpi.teamname.controllers.servicerequests.conferenceroom;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.cell.TextFieldTableCell;

public class RoomBookingConfirmationController {

  @FXML MFXButton editBookingButton;
  @FXML MFXButton confirmBookingButton;
  @FXML TextFieldTableCell confirmationTextBox;

  RoomBookingDetailsController rbdc = new RoomBookingDetailsController();

  public void initialize() {
    editBookingButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING_DETAILS));
    confirmBookingButton.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));

    confirmationTextBox.setText(
        "Booked "
            + rbdc.getRoomLocation()
            + " for "
            + rbdc.getEventTitle()
            + "\n\n"
            + "Date: "
            + rbdc.getEventDate().format(DateTimeFormatter.ofPattern("EE MMM dd yyyy"))
            + "\n\n\n"
            + "Thank you for your booking!");
  }
}
