package edu.wpi.teamname.controllers;

import edu.wpi.teamname.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.controlsfx.control.HyperlinkLabel;

public class CreditsController {
  @FXML private MFXButton popupClose;
  @FXML private HyperlinkLabel blossom;
  @FXML private HyperlinkLabel magnolia;
  @FXML private HyperlinkLabel passion;
  @FXML private HyperlinkLabel pastel;
  @FXML private HyperlinkLabel pink;
  @FXML private HyperlinkLabel rainforest;
  @FXML private HyperlinkLabel sapphire;
  @FXML private HyperlinkLabel spring;
  @FXML private HyperlinkLabel summer;
  @FXML private HyperlinkLabel winter;
  @FXML private HyperlinkLabel burger;
  @FXML private HyperlinkLabel stirfry;
  @FXML private HyperlinkLabel chicken;
  @FXML private HyperlinkLabel tacos;
  @FXML private HyperlinkLabel pasta;
  @FXML private HyperlinkLabel bagel;
  @FXML private HyperlinkLabel tea;
  @FXML private HyperlinkLabel orangechicken;
  @FXML private HyperlinkLabel friedrice;
  @FXML private HyperlinkLabel hotdog;
  @FXML private HyperlinkLabel riceandbeans;
  @FXML private HyperlinkLabel quesadillas;
  @FXML private HyperlinkLabel pens;
  @FXML private HyperlinkLabel highlighters;
  @FXML private HyperlinkLabel folders;
  @FXML private HyperlinkLabel stapler;
  @FXML private HyperlinkLabel post_its;
  @FXML private HyperlinkLabel scissors;
  @FXML private HyperlinkLabel whiteboard;
  @FXML private HyperlinkLabel drymarkers;
  @FXML private HyperlinkLabel rubixcube;
  @FXML private HyperlinkLabel pencils;
  @FXML private HyperlinkLabel home;
  @FXML private HyperlinkLabel profile;
  @FXML private HyperlinkLabel logout;
  @FXML private HyperlinkLabel navigation;
  @FXML private HyperlinkLabel signage;
  @FXML private HyperlinkLabel mealrequest;
  @FXML private HyperlinkLabel roomrequest;
  @FXML private HyperlinkLabel flowerrequest;
  @FXML private HyperlinkLabel officerequest;
  @FXML private HyperlinkLabel help;
  @FXML private HyperlinkLabel hospitallogo;
  @FXML private ImageView popup;

  @FXML
  public void initialize() throws IOException {

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
    blossom.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/blossom.png").toString()));
        });
    magnolia.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/magnolia.png").toString()));
        });
    pastel.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/pastel.png").toString()));
        });
    passion.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/passion.png").toString()));
        });
    pink.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/pink.png").toString()));
        });
    rainforest.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/rainforest.png").toString()));
        });
    sapphire.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/sapphire.png").toString()));
        });
    spring.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/spring.png").toString()));
        });
    summer.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/summer.png").toString()));
        });
    winter.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("bouquets/winter.png").toString()));
        });
    burger.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/burger1.png").toString()));
        });
    stirfry.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/stirfry.png").toString()));
        });
    chicken.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/chicken.png").toString()));
        });
    tacos.setOnMouseClicked(
        event -> {
          popup.setImage(new Image(Main.class.getResource("FoodImages/Tacos.png").toString()));
        });
    pasta.setOnMouseClicked(event -> {
      popup.setImage(new Image(Main.class.getResource("FoodImages/pasta.png").toString()));
    });
  }

  void openLink(ActionEvent event) throws URISyntaxException, IOException {
    System.out.println("Link clicked");
    Desktop.getDesktop().browse(new URI("http://nurserys.anshiliaofa.com/norwood-nursery/"));
  }
}
