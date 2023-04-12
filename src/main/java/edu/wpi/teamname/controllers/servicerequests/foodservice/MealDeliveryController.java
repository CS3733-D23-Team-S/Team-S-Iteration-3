package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MealDeliveryController {

  @FXML MFXButton backButton1;
  @FXML MFXButton checkout;
  @FXML HBox wf;
  @FXML HBox qd;
  @FXML SplitMenuButton dietaryButton;
  @FXML SplitMenuButton cuisine;
  @FXML SplitMenuButton price;
  @FXML HBox filter;
  @FXML Text wfLabel;
  @FXML Text qdLabel;
  @FXML MFXButton apply;
  @FXML MFXButton clearButton;

  @FXML MFXButton signagePage1;

  @FXML MFXButton mealbutton;
  @FXML MFXButton roomButton1;
  @FXML MFXButton flowerbutton; // //ADdd path to flowerbutton
  @FXML MFXButton homeButton;
  @FXML MFXButton exit;
  @FXML private DataBaseRepository DBR = DataBaseRepository.getInstance();

  public static int clickedFoodID;

  public ArrayList<MenuItem> filters = new ArrayList<>();
  public ArrayList<String> filterList = new ArrayList<>();

  @FXML
  public void initialize() {

    homeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    backButton1.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    exit.setOnMouseClicked(event -> Navigation.navigate(Screen.LOGIN_PAGE));
    signagePage1.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_PAGE));

    mealbutton.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
    roomButton1.setOnMouseClicked(event -> Navigation.navigate(Screen.ROOM_BOOKING));

    // Dietary Restriction
    MenuItem vegetarian = new MenuItem("Vegetarian");
    MenuItem gf = new MenuItem("Gluten Free");
    MenuItem h = new MenuItem("Halal");
    MenuItem k = new MenuItem("Kosher");
    MenuItem v = new MenuItem("Vegan");

    dietaryButton.getItems().addAll(vegetarian, gf, h, k, v);

    // Cuisine
    MenuItem Am = new MenuItem("American");
    MenuItem It = new MenuItem("Italian");
    MenuItem Mex = new MenuItem("Mexican");
    MenuItem Ind = new MenuItem("Indian");

    cuisine.getItems().addAll(Am, It, Mex, Ind);

    // add filters to filters hbox
    vegetarian.setOnAction(
        (e) -> {
          addFilter(vegetarian);
          System.out.println("vegetarian clicked");
          if (!filterList.contains("vegetarian")) filterList.add("vegetarian");
        });
    gf.setOnAction(
        (e) -> {
          addFilter(gf);
          if (!filterList.contains("gf")) filterList.add("gf");
        });
    h.setOnAction(
        (e) -> {
          addFilter(h);
          if (!filterList.contains("halal")) filterList.add("halal");
        });
    k.setOnAction(
        (e) -> {
          addFilter(k);
          if (!filterList.contains("kosher")) filterList.add("k");
        });
    v.setOnAction(
        (e) -> {
          addFilter(v);
          if (!filterList.contains("vegan")) filterList.add("vg");
        });
    Am.setOnAction(
        (e) -> {
          addFilter(Am);
          if (!filterList.contains("American")) filterList.add("American");
        });
    It.setOnAction(
        (e) -> {
          addFilter(It);
          if (!filterList.contains("Italian")) filterList.add("Italian");
        });
    Mex.setOnAction(
        (e) -> {
          addFilter(Mex);
          if (!filterList.contains("Mexican")) filterList.add("Mexican");
        });
    Ind.setOnAction(
        (e) -> {
          addFilter(Ind);
          if (!filterList.contains("Indian")) filterList.add("Indian");
        });

    apply.setOnMouseClicked(
        event -> {
          clear1();
          applyFilters();
        });

    clearButton.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.MEAL_DELIVERY1);
          filters.clear();
        });

    checkout.setOnMouseClicked(event -> Navigation.navigate(Screen.ORDER_DETAILS));

    // navigationbutton1.setOnMouseClicked(event -> Navigation.navigate(Screen.ORDER_DETAILS));
    // ///ADD NAVIGATION

    //  homeicon1.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));

    walletFriendly();
    quickDelivery();
  }

  public void walletFriendly() {

    for (int i = 0; i < DBR.getWalletFriendlyFood().size(); i++) {

      MFXButton btn1 = new MFXButton();
      btn1.setId(DBR.getWalletFriendlyFood().get(i).toString());
      btn1.setText(DBR.getWalletFriendlyFood().get(i).toString());
      btn1.setMaxWidth(103);
      btn1.setMaxHeight(87);
      wf.getChildren().add(btn1);
      btn1.setOnMouseClicked(event -> Navigation.navigate(Screen.PRODUCT_DETAILS));
      int finalII = i;
      btn1.setOnMouseClicked(event -> store(DBR.getWalletFriendlyFood().get(finalII).getFoodID()));
    }
  }

  public void quickDelivery() {

    for (int i = 0; i < DBR.getQuick().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getQuick().get(i).toString());
      btn.setText(DBR.getQuick().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);
      btn.setOnMouseClicked(event -> Navigation.navigate(Screen.PRODUCT_DETAILS));
      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getQuick().get(finalI).getFoodID()));
    }
  }

  public Method chooseVegetarian() {
    for (int i = 0; i < DBR.getVegetarian().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getVegetarian().get(i).toString());
      btn.setText(DBR.getVegetarian().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);
      btn.setOnMouseClicked(event -> Navigation.navigate(Screen.PRODUCT_DETAILS));
      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getVegetarian().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseVegan() {
    for (int i = 0; i < DBR.getVegan().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getVegan().get(i).toString());
      btn.setText(DBR.getVegan().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);
      btn.setOnMouseClicked(event -> Navigation.navigate(Screen.PRODUCT_DETAILS));
      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getVegan().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseGlutenFree() {
    for (int i = 0; i < DBR.getGlutenFree().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getGlutenFree().get(i).toString());
      btn.setText(DBR.getGlutenFree().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);
      btn.setOnMouseClicked(event -> Navigation.navigate(Screen.PRODUCT_DETAILS));
      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getGlutenFree().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseHalal() {
    for (int i = 0; i < DBR.getHalal().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getHalal().get(i).toString());
      btn.setText(DBR.getHalal().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);
      btn.setOnMouseClicked(event -> Navigation.navigate(Screen.PRODUCT_DETAILS));
      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getHalal().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseKosher() {
    for (int i = 0; i < DBR.getKosher().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getKosher().get(i).toString());
      btn.setText(DBR.getKosher().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);
      btn.setOnMouseClicked(event -> Navigation.navigate(Screen.PRODUCT_DETAILS));
      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseAmerican() {
    for (int i = 0; i < DBR.getAmerican().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getAmerican().get(i).toString());
      btn.setText(DBR.getAmerican().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);
      btn.setOnMouseClicked(event -> Navigation.navigate(Screen.PRODUCT_DETAILS));
      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseItalian() {
    for (int i = 0; i < DBR.getItalian().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getItalian().get(i).toString());
      btn.setText(DBR.getItalian().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);
      btn.setOnMouseClicked(event -> Navigation.navigate(Screen.PRODUCT_DETAILS));
      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseMexican() {
    for (int i = 0; i < DBR.getMexican().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getMexican().get(i).toString());
      btn.setText(DBR.getMexican().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);
      btn.setOnMouseClicked(event -> Navigation.navigate(Screen.PRODUCT_DETAILS));
      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseIndian() {
    for (int i = 0; i < DBR.getIndian().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getIndian().get(i).toString());
      btn.setText(DBR.getIndian().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);
      btn.setOnMouseClicked(event -> Navigation.navigate(Screen.PRODUCT_DETAILS));
      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public void store(int x) {
    clickedFoodID = x;
    Navigation.navigate(Screen.PRODUCT_DETAILS);
  }

  public void addFilter(MenuItem x) {
    ArrayList<MenuItem> filters = new ArrayList<>();
    Label lbl = new Label();
    lbl.setId(x.getText());
    lbl.setText(x.getText());
    lbl.setStyle("-fx-text-fill: #122e59; -fx-font-size: 18px;");
    lbl.setMaxWidth(103);
    lbl.setMaxHeight(87);
    System.out.println("works");
    System.out.println(lbl.getText());
    if (!filters.contains(x)) {
      filters.add(x);
      filter.getChildren().add(lbl);
    }
  }

  public void clear1() {
    wf.getChildren().clear();
    qd.getChildren().clear();
    qdLabel.setText("");
    wfLabel.setText("");
  }

  public void applyFilters() {
    for (int i = 0; i < filterList.size(); i++) {

      if (filterList.get(i) == "vegetarian") {
        chooseVegetarian();
      }
      if (filterList.get(i) == "gf") {
        chooseGlutenFree();
      }
      if (filterList.get(i) == "vg") {
        chooseVegan();
      }
      if (filterList.get(i) == "k") {
        chooseKosher();
      }
      if (filterList.get(i) == "h") {
        chooseHalal();
      }
      if (filterList.get(i) == "american") {
        chooseAmerican();
      }
      if (filterList.get(i) == "italian") {
        chooseItalian();
      }

      if (filterList.get(i) == "mexican") {
        chooseMexican();
      }

      if (filterList.get(i) == "indian") {
        chooseIndian();
      }
    }
  }
}
