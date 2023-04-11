package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import static edu.wpi.teamname.navigation.Screen.*;

public class FlowerSubmissionController {
    @FXML
    ImageView backicon;
    @FXML
    VBox cartvbox;
    @FXML
    MFXButton clearbutton;
    @FXML
    ImageView exiticon;
    @FXML
    MFXButton flowerbutton;
    @FXML
    ImageView helpicon;
    @FXML
    ImageView homeicon;
    @FXML
    MenuButton locationdrop;
    @FXML
    MFXButton mealbutton;
    @FXML
    MFXButton navigationbutton;
    @FXML
    MFXTextField requestfield;
    @FXML
    MFXButton roombutton;
    @FXML
    MFXButton signagebutton;
    @FXML
    MFXButton submitbutton;
    @FXML
    MFXButton submitreqbutton;
    @FXML
    ImageView topbarlogo;

    public void initialize() {
        submitbutton.setOnMouseClicked(event -> Navigation.navigate(FLOWER_CONFIRMATION));
        backicon.setOnMouseClicked(event -> Navigation.navigate(FLOWER_DELIVERY));
        exiticon.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
        helpicon.setOnMouseClicked(event -> Navigation.navigate(HELP_PAGE));
        homeicon.setOnMouseClicked(event -> Navigation.navigate(HOME));

    }
}