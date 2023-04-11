package edu.wpi.teamname.controllers.servicerequests.flowerdelivery;

import edu.wpi.teamname.ServiceRequests.flowers.Flower;
import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.flowers.FlowerDAOImpl;
import edu.wpi.teamname.controllers.PopUpController;
import edu.wpi.teamname.navigation.Navigation;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.lang.reflect.Method;

import static edu.wpi.teamname.navigation.Screen.*;

public class FlowerDeliveryController {

    @FXML ImageView backicon;
    @FXML ImageView exiticon;
    @FXML MFXButton flower1;
    @FXML MFXButton flower10;
    @FXML MFXButton flower2;
    @FXML MFXButton flower3;
    @FXML MFXButton flower4;
    @FXML MFXButton flower5;
    @FXML MFXButton flower6;
    @FXML MFXButton flower7;
    @FXML MFXButton flower8;
    @FXML MFXButton flower9;
    @FXML MFXButton flowerbutton;
    @FXML MFXTextField flowersearch;
    @FXML HBox hbox1;
    @FXML HBox hbox2;
    @FXML ImageView helpicon;
    @FXML ImageView homeicon;
    @FXML MFXButton mealbutton;
    @FXML MFXButton navigationbutton;
    @FXML MenuButton pricedrop;
    @FXML MFXButton roombutton;
    @FXML MFXButton signagebutton;
    @FXML MenuButton sizedrop;
    @FXML ImageView topbarlogo;
    @FXML MFXButton viewcartbutton;
    @FXML MenuItem sizesmall;
    @FXML MenuItem sizenormal;
    @FXML MenuItem sizelarge;
    @FXML private DataBaseRepository DBR = DataBaseRepository.getInstance();


    public void initialize() {

        Flower B1 =
                new Flower(1,"B1", "small",100,1,false,"bouquet of flowers","image");
        Flower B2 =
                new Flower(2,"B2", "medium",100,1,false,"bouquet of flowers","image");
        Flower B3 =
                new Flower(3,"B3", "large",100,1,false,"bouquet of flowers","image");

        viewcartbutton.setOnMouseClicked(event -> Navigation.navigate(FLOWER_CART));
        backicon.setOnMouseClicked(event -> Navigation.navigate(HOME));
        exiticon.setOnMouseClicked(event -> Navigation.navigate(SIGNAGE_PAGE));
        helpicon.setOnMouseClicked(event -> Navigation.navigate(HELP_PAGE));
        homeicon.setOnMouseClicked(event -> Navigation.navigate(HOME));

        sizesmall.setOnAction(event -> {selectSmall(String )});

    }
    public void selectSmall(String siz) {
        for (int i = 0; i < DBR.getListOfSize(siz).size(); i++) {

            MFXButton btn1 = new MFXButton();
            btn1.setId(DBR.getListOfSize(siz).get(i).toString());
            btn1.setText(DBR.getListOfSize(siz).get(i).toString());
            btn1.setMaxHeight(103);
            btn1.setMaxHeight(87);
            hbox1.getChildren().add(btn1);
            btn1.setOnMouseClicked(event -> Navigation.navigate(PRODUCT_DETAILS));
            int finalII = i;
        }
    }

}
