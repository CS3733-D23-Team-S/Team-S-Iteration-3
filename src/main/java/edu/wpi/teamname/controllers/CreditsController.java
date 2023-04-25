package edu.wpi.teamname.controllers;

import edu.wpi.teamname.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CreditsController {
  @FXML private Hyperlink Bagel;
  @FXML private Hyperlink Blossom;
  @FXML private Hyperlink Burger;
  @FXML private Hyperlink Chicken;
  @FXML private Hyperlink DryEraseMarkers;
  @FXML private Hyperlink FlowerRequest;
  @FXML private Hyperlink Folders;
  @FXML private Hyperlink FriedRice;
  @FXML private Hyperlink Help;
  @FXML private Hyperlink Highlighters;
  @FXML private Hyperlink Home;
  @FXML private Hyperlink HospitalLogo;
  @FXML private Hyperlink HotDog;
  @FXML private Hyperlink Logout;
  @FXML private Hyperlink Magnolia;
  @FXML private Hyperlink MealRequest;
  @FXML private Hyperlink Navigation;
  @FXML private Hyperlink OfficeRequest;
  @FXML private Hyperlink OrangeChicken;
  @FXML private Hyperlink Passion;
  @FXML private Hyperlink Pasta;
  @FXML private Hyperlink Pastel;
  @FXML private Hyperlink Pencils;
  @FXML private Hyperlink Pens;
  @FXML private Hyperlink Pink;
  @FXML private Hyperlink PostIts;
  @FXML private Hyperlink Profile;
  @FXML private Hyperlink Quesadillas;
  @FXML private Hyperlink Rainforest;
  @FXML private Hyperlink RiceAndBeans;
  @FXML private Hyperlink RoomRequest;
  @FXML private Hyperlink RubixCube;
  @FXML private Hyperlink Sapphire;
  @FXML private Hyperlink Scissors;
  @FXML private Hyperlink Signage;
  @FXML private Hyperlink Spring;
  @FXML private Hyperlink Stapler;
  @FXML private Hyperlink StirFry;
  @FXML private Hyperlink Summer;
  @FXML private Hyperlink Tacos;
  @FXML private Hyperlink Tea;
  @FXML private Hyperlink WhiteBoard;
  @FXML private Hyperlink Winter;
  @FXML private ImageView popup;
  @FXML private MFXButton popupClose;
  @FXML private ImageView HomeIcon;

  @FXML
  public void initialize() throws IOException {

    Image HomeButton = new Image(Main.class.getResource("./templateIcons/homeicon.png").toString());
    HomeIcon.setImage(HomeButton);

    handleclick();
    handleclose();
    initializeBtns();

    //            Hyperlink flowerLink1 = new
    //     Hyperlink("http://nurserys.anshiliaofa.com/norwood-nursery/");

    //            flowerLink1.setOnAction(this ::openLink);
    //            flowerLink1.setOnAction(event -> {
    //              openLink();
    //            });
    //            });
    //        }

  }

  private void initializeBtns() {
    popupClose.setOnMouseClicked(
        event -> {
          handleclose();
        });
  }

  private void handleclose() {
    Stage stage = (Stage) popupClose.getScene().getWindow();
    stage.close();
  }

  private void handleclick() {
    Blossom.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/blossom.png").toString()));
        });
    Magnolia.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/magnolia.png").toString()));
        });
    Pastel.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/pastel.png").toString()));
        });
    Passion.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/passion.png").toString()));
        });
    Pink.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/pink.png").toString()));
        });
    Rainforest.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/rainforest.png").toString()));
        });
    Sapphire.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/sapphire.png").toString()));
        });
    Spring.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/spring.png").toString()));
        });
    Summer.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/summer.png").toString()));
        });
    Winter.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/winter.png").toString()));
        });
    Burger.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/burger1.png").toString()));
        });
    StirFry.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/stirfry.png").toString()));
        });
    Chicken.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/chicken.png").toString()));
        });
    Tacos.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/Tacos.png").toString()));
        });
    Pasta.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/pasta.png").toString()));
        });
    Bagel.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/bagel.png").toString()));
        });
    Tea.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/tea.png").toString()));
        });
    OrangeChicken.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("FoodImages/orangeChicken.png").toString()));
        });
    FriedRice.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/friedRice.png").toString()));
        });
    HotDog.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/hotdog.png").toString()));
        });
    RiceAndBeans.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("FoodImages/riceandbeans.png").toString()));
        });
    Quesadillas.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("FoodImages/quessadilla.png").toString()));
        });
    Pens.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("officesupplies/pen.png").toString()));
        });
    Highlighters.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("officesupplies/highlighter.png").toString()));
        });
    Stapler.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("officesupplies/stapler.png").toString()));
        });
    PostIts.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("officesupplies/post-it.png").toString()));
        });
    Scissors.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("officesupplies/skizzors.png").toString()));
        });
    WhiteBoard.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("officesupplies/whiteBoard.png").toString()));
        });
    DryEraseMarkers.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("officesupplies/dryEraseMarkers.png").toString()));
        });
    RubixCube.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("officesupplies/cube.png").toString()));
        });
    Pencils.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("officesupplies/Pencils.png").toString()));
        });
    Folders.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("officesupplies/folders.png").toString()));
        });
    Home.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("templateIcons/homeicon.png").toString()));
        });
    Profile.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("templateIcons/usericon.png").toString()));
        });
    Logout.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("templateIcons/exiticon.png").toString()));
        });
    Navigation.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("templateIcons/navicon.png").toString()));
        });
    Signage.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("templateIcons/signageicon.png").toString()));
        });
    MealRequest.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("templateIcons/mealicon.png").toString()));
        });
    RoomRequest.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("templateIcons/roomicon.png").toString()));
        });
    FlowerRequest.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("templateIcons/flowericon.png").toString()));
        });
    OfficeRequest.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("templateIcons/pen.png").toString()));
        });
    Help.setOnMouseClicked(
        event -> {
          popup.setImage(
              new Image(Main.class.getResource("templateIcons/helpicon.png").toString()));
        });
    HospitalLogo.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("templateIcons/bwlogo.png").toString()));
        });
  }

  void openLink(ActionEvent event) throws URISyntaxException, IOException {
    System.out.println("Link clicked");
    Desktop.getDesktop().browse(new URI("http://nurserys.anshiliaofa.com/norwood-nursery/"));
  }
}
