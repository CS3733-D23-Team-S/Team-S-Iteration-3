package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
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
  @FXML MFXButton navigation1;
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
    navigation1.setOnMouseClicked(event -> Navigation.navigate(Screen.PATHFINDING));
    flowerbutton.setOnMouseClicked(event -> Navigation.navigate(Screen.FLOWER_DELIVERY));

    mealbutton.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY));
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
          Navigation.navigate(Screen.MEAL_DELIVERY);
          filters.clear();
        });

    checkout.setOnMouseClicked(event -> Navigation.navigate(Screen.ORDER_DETAILS));

    // navigationbutton1.setOnMouseClicked(event -> Navigation.navigate(Screen.ORDER_DETAILS));
    // ///ADD NAVIGATION

    //  homeicon1.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));

    walletFriendly();
    quickDelivery();
  }

  public void noFilter() {

    for (Food f : DBR.getFoodDAO().getFoods().values()) {
      MFXButton btn1 = new MFXButton();

      btn1.setId(f.toString());
      btn1.setText(f.toString());

      btn1.setMaxWidth(103);
      btn1.setMaxHeight(87);

      wf.getChildren().add(btn1);
      btn1.setOnMouseClicked(event -> store(f.getFoodID()));
    }
  }

  public void walletFriendly() {

    for (int i = 0; i < DBR.getFoodDAO().getWalletFriendlyFood().size(); i++) {

      MFXButton btn1 = new MFXButton();

      btn1.setId(DBR.getFoodDAO().getWalletFriendlyFood().get(i).toString());
      btn1.setText(DBR.getFoodDAO().getWalletFriendlyFood().get(i).toString());

      btn1.setMaxWidth(103);
      btn1.setMaxHeight(87);

      wf.getChildren().add(btn1);

      int finalII = i;
      btn1.setOnMouseClicked(
          event -> store(DBR.getFoodDAO().getWalletFriendlyFood().get(finalII).getFoodID()));
    }
  }

  public void quickDelivery() {

    for (int i = 0; i < DBR.getFoodDAO().getQuick().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getFoodDAO().getQuick().get(i).toString());
      btn.setText(DBR.getFoodDAO().getQuick().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);

      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getFoodDAO().getQuick().get(finalI).getFoodID()));
    }
  }

  public Method chooseVegetarian() {
    for (int i = 0; i < DBR.getFoodDAO().getVegetarian().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getFoodDAO().getVegetarian().get(i).toString());
      btn.setText(DBR.getFoodDAO().getVegetarian().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);
      int finalI = i;
      btn.setOnMouseClicked(
          event -> store(DBR.getFoodDAO().getVegetarian().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseVegan() {
    for (int i = 0; i < DBR.getFoodDAO().getVegan().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getFoodDAO().getVegan().get(i).toString());
      btn.setText(DBR.getFoodDAO().getVegan().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);

      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getFoodDAO().getVegan().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseGlutenFree() {
    for (int i = 0; i < DBR.getFoodDAO().getGlutenFree().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getFoodDAO().getGlutenFree().get(i).toString());
      btn.setText(DBR.getFoodDAO().getGlutenFree().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);

      int finalI = i;
      btn.setOnMouseClicked(
          event -> store(DBR.getFoodDAO().getGlutenFree().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseHalal() {
    for (int i = 0; i < DBR.getFoodDAO().getHalal().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getFoodDAO().getHalal().get(i).toString());
      btn.setText(DBR.getFoodDAO().getHalal().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);

      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getFoodDAO().getHalal().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseKosher() {
    for (int i = 0; i < DBR.getFoodDAO().getKosher().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getFoodDAO().getKosher().get(i).toString());
      btn.setText(DBR.getFoodDAO().getKosher().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);

      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getFoodDAO().getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseAmerican() {
    for (int i = 0; i < DBR.getFoodDAO().getAmerican().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getFoodDAO().getAmerican().get(i).toString());
      btn.setText(DBR.getFoodDAO().getAmerican().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);

      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getFoodDAO().getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseItalian() {
    for (int i = 0; i < DBR.getFoodDAO().getItalian().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getFoodDAO().getItalian().get(i).toString());
      btn.setText(DBR.getFoodDAO().getItalian().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);

      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getFoodDAO().getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseMexican() {
    for (int i = 0; i < DBR.getFoodDAO().getMexican().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getFoodDAO().getMexican().get(i).toString());
      btn.setText(DBR.getFoodDAO().getMexican().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);

      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getFoodDAO().getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseIndian() {
    for (int i = 0; i < DBR.getFoodDAO().getIndian().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(DBR.getFoodDAO().getIndian().get(i).toString());
      btn.setText(DBR.getFoodDAO().getIndian().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      qd.getChildren().add(btn);

      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getFoodDAO().getKosher().get(finalI).getFoodID()));
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
