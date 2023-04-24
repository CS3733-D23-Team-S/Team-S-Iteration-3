package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDAOImpl;
import edu.wpi.teamname.ServiceRequests.FoodService.OrderItem;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;

public class MealDeliveryController {

  // @FXML MFXButton backButton1;
  // @FXML MFXButton navigation1;
  @FXML MFXButton checkout;

  @FXML CheckComboBox checkBox;
  // @FXML HBox picBox;
  // @FXML HBox wf;
  // @FXML HBox qd;
  // @FXML SplitMenuButton dietaryButton;
  // @FXML CheckComboBox dietCheck;

  // @FXML SplitMenuButton cuisine;
  // @FXML SplitMenuButton price;
  // @FXML HBox filter;
  // @FXML Text wfLabel;
  // @FXML Text qdLabel;
  // @FXML MFXButton apply;
  // @FXML MFXButton clearButton;

  @FXML ScrollPane scrollPane;
  @FXML FlowPane flowPane;

  // @FXML MFXButton signagePage1;

  // @FXML MFXButton mealbutton;
  // @FXML MFXButton roomButton1;
  // @FXML MFXButton flowerbutton; // //ADdd path to flowerbutton
  // @FXML MFXButton homeButton;
  // @FXML MFXButton exit;
  @FXML private DataBaseRepository DBR = DataBaseRepository.getInstance();

  public static int clickedFoodID;

  public ArrayList<MenuItem> filters = new ArrayList<>();
  public ArrayList<String> filterList = new ArrayList<>();
  public ArrayList<String> needFilters = new ArrayList<>();

  public static int delID;
  public static OrderItem cart;

  public static boolean trueVeg;
  public static boolean trueVegan;
  public static boolean trueHalal;
  public static boolean trueKosher;
  public static boolean trueGF;

  public static ArrayList<Food> allFood = new ArrayList<>();
  public FoodDAOImpl foodDAO = DBR.getFoodDAO();
  public static ArrayList<String> foodFilter = new ArrayList<>();

  @FXML
  public void initialize() {

    delID = DBR.getLastFoodDevID();

    cart = new OrderItem(1);

    // Dietary Restriction
    MenuItem vegetarian = new MenuItem("Vegetarian");
    MenuItem gf = new MenuItem("Gluten Free");
    MenuItem h = new MenuItem("Halal");
    MenuItem k = new MenuItem("Kosher");
    MenuItem v = new MenuItem("Vegan");

    // dietaryButton.getItems().addAll(vegetarian, gf, h, k, v);
    // dietCheck.getItems().addAll("vegetarian", "gf", "h", "k", "v");
    checkBox
        .getItems()
        .addAll(
            "vegetarian",
            "vegan",
            "halal",
            "kosher",
            "glutenfree",
            "american",
            "indian",
            "mexican");

    checkBox
        .getCheckModel()
        .getCheckedItems()
        .addListener(
            new InvalidationListener() {
              @Override
              public void invalidated(Observable observable) {
                filterFood(checkBox.getCheckModel().getCheckedItems());

                System.out.println(
                    "\n SELECTED ITEMS: " + checkBox.getCheckModel().getCheckedItems());
              }
            });

    // Cuisine
    MenuItem Am = new MenuItem("American");
    MenuItem It = new MenuItem("Italian");
    MenuItem Mex = new MenuItem("Mexican");
    MenuItem Ind = new MenuItem("Indian");

    //  cuisine.getItems().addAll(Am, It, Mex, Ind);

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

    /* apply.setOnMouseClicked(
        event -> {
          clear1();
          applyFilters();
        });

    */

    /*clearButton.setOnMouseClicked(
       event -> {
         Navigation.navigate(Screen.MEAL_DELIVERY1);
         filters.clear();
       });

    */

    checkout.setOnMouseClicked(event -> Navigation.navigate(Screen.ORDER_DETAILS));

    walletFriendly();
    // quickDelivery();
    noFilter();
    // scrollFix();
  }

  public void walletFriendly() {

    for (int i = 0; i < DBR.getFoodDAO().getWalletFriendlyFood().size(); i++) {

      MFXButton btn1 = new MFXButton();

      btn1.setId(DBR.getFoodDAO().getWalletFriendlyFood().get(i).toString());
      btn1.setText(DBR.getFoodDAO().getWalletFriendlyFood().get(i).toString());

      btn1.setMaxWidth(103);
      btn1.setMaxHeight(87);

      // wf.getChildren().add(btn1);

      int finalII = i;
      btn1.setOnMouseClicked(
          event -> store(DBR.getFoodDAO().getWalletFriendlyFood().get(finalII).getFoodID()));
    }
  }

  public void noFilter() {
    for (Food f : DBR.getFoodDAO().getFoods().values()) {
      allFood.add(f);

      VBox food = new VBox();
      food.setMaxWidth(50);
      food.setMaxHeight(100);

      Image pic = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView foodPic = new ImageView(pic);

      foodPic.setPreserveRatio(true);
      foodPic.setFitHeight(80);
      foodPic.setFitWidth(80);
      foodPic.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(14,14,12,0.8), 10, 0, 0, 15);");

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getFoodName());
      btn1.setPrefWidth(150);
      btn1.setPrefHeight(100);
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
      btn1.setWrapText(true);
      btn1.setGraphic(foodPic);

      flowPane.setPrefWrapLength(10);
      flowPane.setHgap(20);
      flowPane.setVgap(20);
      flowPane.getChildren().add(btn1);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getFoodID());
          });
    }
  }

  public void scrollFix() {
    // Food f : DBR.getFoodDAO().getFoods().values()
    for (Food f : DBR.getFoodDAO().getFoods().values()) {

      VBox food = new VBox();
      food.setMaxWidth(100);
      food.setMaxHeight(200);

      //   picBox.setSpacing(10);

      Image pic = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView foodPic = new ImageView(pic);

      foodPic.setPreserveRatio(true);
      foodPic.setFitHeight(150);
      foodPic.setFitWidth(150);

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getFoodName());
      btn1.setPrefWidth(250);
      btn1.setPrefHeight(200);
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
      btn1.setWrapText(true);
      btn1.setGraphic(foodPic);

      scrollPane = new ScrollPane(food);

      // food.setPrefWrapLength(10);
      // food.setHgap(20);
      // food.setVgap(20);

      food.getChildren().add(btn1);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getFoodID());
          });
    }
  }

  public Method chooseVegetarian() {
    for (Food f : DBR.getFoodDAO().getVegetarian()) {
      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(false);
      view.setFitHeight(150);
      view.setFitWidth(150);
      if (!allFood.contains(f)) {
        allFood.add(f);
      }

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getFoodName());
      btn1.setPrefWidth(250);
      btn1.setPrefHeight(200);
      btn1.setStyle("-fx-spacing: 10;");
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      //  flowPane.getChildren().add(btn1);
      //  flowPane.setHgap(25);
      //   flowPane.setVgap(25);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getFoodID());
          });
    }

    return null;
  }

  public Method chooseVegan() {
    for (Food f : DBR.getFoodDAO().getVegan()) {
      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(false);
      view.setFitHeight(150);
      view.setFitWidth(150);
      if (!allFood.contains(f)) {
        allFood.add(f);
      }

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getFoodName());
      btn1.setPrefWidth(250);
      btn1.setPrefHeight(200);
      btn1.setStyle("-fx-spacing: 10;");
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      // flowPane.getChildren().add(btn1);
      // flowPane.setHgap(25);
      // flowPane.setVgap(25);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getFoodID());
          });
    }

    return null;
  }

  public Method chooseGlutenFree() {
    for (Food f : DBR.getFoodDAO().getGlutenFree()) {
      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(false);
      view.setFitHeight(150);
      view.setFitWidth(150);
      if (!allFood.contains(f)) {
        allFood.add(f);
      }

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getFoodName());
      btn1.setPrefWidth(250);
      btn1.setPrefHeight(200);
      btn1.setStyle("-fx-spacing: 10;");
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      // flowPane.getChildren().add(btn1);
      // flowPane.setHgap(25);
      /// flowPane.setVgap(25);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getFoodID());
          });
    }

    return null;
  }

  public Method chooseHalal() {
    for (Food f : DBR.getFoodDAO().getHalal()) {
      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(false);
      view.setFitHeight(150);
      view.setFitWidth(150);
      if (!allFood.contains(f)) {
        allFood.add(f);
      }

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getFoodName());
      btn1.setPrefWidth(250);
      btn1.setPrefHeight(200);
      btn1.setStyle("-fx-spacing: 10;");
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      // flowPane.getChildren().add(btn1);
      // flowPane.setHgap(25);
      // flowPane.setVgap(25);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getFoodID());
          });
    }

    return null;
  }

  public Method chooseKosher() {
    for (Food f : DBR.getFoodDAO().getKosher()) {
      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(false);
      view.setFitHeight(150);
      view.setFitWidth(150);
      if (!allFood.contains(f)) {
        allFood.add(f);
      }

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getFoodName());
      btn1.setPrefWidth(250);
      btn1.setPrefHeight(200);
      btn1.setStyle("-fx-spacing: 10;");
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      // flowPane.getChildren().add(btn1);
      // flowPane.setHgap(25);
      // flowPane.setVgap(25);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getFoodID());
          });
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
      flowPane.getChildren().add(btn);

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
      flowPane.getChildren().add(btn);

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
      flowPane.getChildren().add(btn);

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
      flowPane.getChildren().add(btn);

      btn.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.PRODUCT_DETAILS));
      int finalI = i;
      btn.setOnMouseClicked(event -> store(DBR.getFoodDAO().getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public void store(int x) {
    clickedFoodID = x;
    Navigation.launchPopUp(Screen.PRODUCT_DETAILS);
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
      // filter.getChildren().add(lbl);
    }
  }

  public void clear1() {
    // wf.getChildren().clear();
    flowPane.getChildren().clear();
    // qdLabel.setText("");
    // wfLabel.setText("");
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

  public void filterByDiet(ObservableList<String> restrictions) {
    for (int i = 0; i < allFood.size(); i++) {

      // allFood.get(i).setVisible(false);
      // flowPane.getChildren(i).managedProperty().bind(roomListVBoxes.get(i).visibleProperty());
    }
    System.out.println("\n\nFILTERING BY FEATURE!!!! FEATURES: ");
    System.out.println(restrictions);

    if (restrictions.isEmpty()) {
      System.out.println("Features empty!!!");
      noFilter();
    }

    for (int i = 0; i < allFood.size(); i++) {
      for (int f = 0; f < restrictions.size(); f++) {
        if (!(allFood.get(i).getFoodCuisine().contains(restrictions.get(f)))) {

          System.out.println(
              allFood.get(i).getFoodCuisine() + " does not contain " + restrictions.get(f));
          break;
        }
        // allFood.get(i).setVisible(true);
      }
    }
    System.out.println("Set things to visible");
  }

  public void filterFood(ObservableList<String> filterNeeds) {

    flowPane.getChildren().clear();

    /*
    if (filterNeeds.isEmpty()) {
      System.out.println("Features empty!!!");
      noFilter();
    }

     */
    System.out.println("veg" + trueVeg);

    allFood = (ArrayList<Food>) foodDAO.queriedFoods(filterNeeds);

    System.out.println(allFood);

    for (int f = 0; f < allFood.size(); f++) {

      VBox food = new VBox();
      food.setMaxWidth(50);
      food.setMaxHeight(100);

      Image pic = new Image(Main.class.getResource(allFood.get(f).getImage()).toString());
      ImageView foodPic = new ImageView(pic);

      foodPic.setPreserveRatio(true);
      foodPic.setFitHeight(80);
      foodPic.setFitWidth(80);
      foodPic.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(14,14,12,0.8), 10, 0, 0, 15);");

      MFXButton btn1 = new MFXButton();
      btn1.setId(allFood.get(f).toString());
      btn1.setText(allFood.get(f).getFoodName());
      btn1.setPrefWidth(150);
      btn1.setPrefHeight(100);
      btn1.setStyle("-fx-background-radius:10 10 10 10;");
      btn1.setWrapText(true);
      btn1.setGraphic(foodPic);

      flowPane.setPrefWrapLength(10);
      flowPane.setHgap(20);
      flowPane.setVgap(20);
      flowPane.getChildren().add(btn1);

      int finalF = f;
      btn1.setOnMouseClicked(
          event -> {
            store(allFood.get(finalF).getFoodID());
          });
    }
  }
}
