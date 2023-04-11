package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import static edu.wpi.teamname.navigation.Screen.*;

public class FlowerConfirmationController {
    @FXML ImageView backicon;
    @FXML ImageView exiticon;
    @FXML MFXButton flowerbutton;
    @FXML ImageView helpicon;
    @FXML ImageView homeicon;
    @FXML MFXButton mealbutton;
    @FXML MFXButton navigationbutton;
    @FXML MFXButton roombutton;
    @FXML MFXButton signagebutton;
    @FXML ImageView topbarlogo;

    public void initialize() {
        backicon.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));
        exiticon.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
        helpicon.setOnMouseClicked(event -> Navigation.navigate(HELP_PAGE));
        homeicon.setOnMouseClicked(event -> Navigation.navigate(HOME));
}
