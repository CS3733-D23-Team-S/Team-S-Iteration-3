package edu.wpi.teamname.controllers;

import edu.wpi.teamname.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class standardUIController {

  @FXML ImageView helpicon;
  @FXML ImageView homeicon;
  @FXML ImageView topbarlogo;
  @FXML ImageView usericon;
  @FXML ImageView searchicon;
  @FXML ImageView backicon;
  @FXML MFXButton backbutton;
  @FXML MenuButton dropdownmenu;
  @FXML MFXButton flowerbutton;
  @FXML MFXButton mealbutton;
  @FXML MFXButton navigationbutton;
  @FXML MFXButton roombutton;
  @FXML TextField searchbar;
  @FXML MFXButton submitbutton;

  public void initialize() {

    Image homeIcon = new Image(Main.class.getResource("./images/homeicon.png").toString());
    homeicon.setImage(homeIcon);

    Image userIcon = new Image(Main.class.getResource("./images/usericon.png").toString());
    usericon.setImage(userIcon);

    Image helpIcon = new Image(Main.class.getResource("./images/helpicon.png").toString());
    helpicon.setImage(helpIcon);

    Image bwhLogo = new Image(Main.class.getResource("./images/topbarLogo.png").toString());
    topbarlogo.setImage(bwhLogo);

    Image searchIcon = new Image(Main.class.getResource("./images/searchicon.png").toString());
    searchicon.setImage(searchIcon);
  }
}
