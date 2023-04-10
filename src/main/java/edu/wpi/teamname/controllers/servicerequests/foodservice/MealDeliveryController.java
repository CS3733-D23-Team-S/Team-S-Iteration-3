package edu.wpi.teamname.controllers.servicerequests.foodservice;

import edu.wpi.teamname.databaseredo.DataBaseRepository;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javafx.fxml.FXML;
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
  @FXML HBox mp;
  @FXML SplitMenuButton dietaryButton;
  @FXML SplitMenuButton cuisine;
  @FXML SplitMenuButton price;
  @FXML HBox filter;
  @FXML Text wfLabel;
  @FXML Text qdLabel;
  @FXML Text mpLabel;
  @FXML MFXButton apply;
  @FXML MFXButton clearButton;
  @FXML private DataBaseRepository DBR = DataBaseRepository.getInstance();

  public static int clickedFoodID;

  @FXML
  public void initialize() {

    // Dietary Restriction
    MenuItem vegetarian = new MenuItem("Vegetarian");
    MenuItem gf = new MenuItem("Gluten Free");
    MenuItem h = new MenuItem("Halal");
    MenuItem k = new MenuItem("Kosher");
    MenuItem v = new MenuItem("Vegan");
    System.out.println(gf.getText());

    dietaryButton.getItems().addAll(vegetarian, gf, h, k, v);

    // Cuisine
    MenuItem Am = new MenuItem("American");
    MenuItem It = new MenuItem("Italian");
    MenuItem Mex = new MenuItem("Mexican");
    MenuItem Ind = new MenuItem("Indian");

    System.out.println(gf.getText());

    cuisine.getItems().addAll(Am, It, Mex, Ind);

    // add filters to filters hbox
    vegetarian.setOnAction(
        (e) -> {
          addFilter(vegetarian);
          filterList.add("vegetarian");
        });
    gf.setOnAction(
        (e) -> {
          addFilter(gf);
          filterList.add("gf");
        });
    h.setOnAction(
        (e) -> {
          addFilter(h);
          filterList.add("h");
        });
    k.setOnAction(
        (e) -> {
          addFilter(k);
          filterList.add("k");
        });
    v.setOnAction(
        (e) -> {
          addFilter(v);
          filterList.add("vg");
        });
    Am.setOnAction(
        (e) -> {
          addFilter(Am);
        });
    It.setOnAction(
        (e) -> {
          addFilter(It);
        });
    Mex.setOnAction(
        (e) -> {
          addFilter(Mex);
        });
    Ind.setOnAction(
        (e) -> {
          addFilter(Ind);
        });

    apply.setOnMouseClicked(
        event -> {
          clear1();
          applyFilters();
        });

    clearButton.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_DELIVERY1));
    /*
        // adding Foods
        Food Pizza =
            new Food(
                1,
                "Pizza",
                "Entree",
                10,
                "American",
                10,
                "Bread with sauce and cheese on it",
                1,
                false,
                "image",
                20,
                " ",
                true,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                false);

        Food Burger =
            new Food(
                2,
                "Burger",
                "Entree",
                10,
                "American",
                12,
                "Unhealthy",
                1,
                false,
                "image",
                14,
                " ",
                false,
                false,
                false,
                false,
                false,
                false,
                true,
                false,
                false);

        Food StirFry =
            new Food(
                3,
                "StirFry",
                "Entree",
                10,
                "Other",
                10,
                "Noodles and veggies",
                1,
                false,
                "image",
                5,
                " ",
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                true,
                false);

        Food Chicken =
            new Food(
                4,
                "Chicken",
                "Entree",
                10,
                "Other",
                21,
                "its chicken",
                1,
                false,
                "image",
                12,
                " ",
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                true);

        Food Tacos =
            new Food(
                5,
                "Tacos",
                "Entree",
                10,
                "Mexican",
                11,
                "Delicious",
                1,
                false,
                "image",
                20,
                " ",
                false,
                false,
                false,
                false,
                true,
                true,
                false,
                false,
                false);

        Food Pasta =
            new Food(
                6,
                "Pasta",
                "Entree",
                10,
                "Italian",
                14,
                "bowties and sauce",
                1,
                false,
                "image",
                15,
                " ",
                false,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                false);

        Food Bagel =
            new Food(
                7,
                "Bagel",
                "Breakfast",
                10,
                "American?",
                13,
                "Boiled Bread",
                1,
                false,
                "image",
                10,
                " ",
                false,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                false);

        Food Tea =
            new Food(
                8,
                "Tea",
                "Drink",
                10,
                "Other",
                65,
                "From England",
                1,
                false,
                "image",
                11,
                " ",
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false);

        Food OrangeChicken =
            new Food(
                9,
                "OrangeChicken",
                "Entree",
                10,
                "String fc",
                15,
                "String fd",
                1,
                false,
                "image",
                10,
                "Here is a note",
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false);

        Food FriedRice =
            new Food(
                10,
                "FriedRice",
                "Hello",
                10,
                "String fc",
                10,
                "String fd",
                1,
                false,
                "image",
                1,
                "Here is a note",
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false);

        Food HotDog =
            new Food(
                11,
                "HotDog",
                "Hello",
                10,
                "String fc",
                12,
                "String fd",
                1,
                false,
                "image",
                10,
                "Here is a note",
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false);

        Food RiceAndBeans =
            new Food(
                12,
                "RiceAndBeans",
                "Hello",
                10,
                "String fc",
                2,
                "String fd",
                1,
                false,
                "image",
                69,
                "Here is a note",
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false);

        Food Quesadillas =
            new Food(
                13,
                "Quesadillas",
                "Hello",
                10,
                "String fc",
                2,
                "String fd",
                1,
                false,
                "image",
                69,
                "Here is a note",
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false);

        foodDAO.add(Pizza);
        foodDAO.add(Burger);
        foodDAO.add(StirFry);
        foodDAO.add(Chicken);
        foodDAO.add(Tacos);
        foodDAO.add(Pasta);
        foodDAO.add(Bagel);
        foodDAO.add(Tea);
        foodDAO.add(OrangeChicken);
        foodDAO.add(FriedRice);
        foodDAO.add(HotDog);
        foodDAO.add(RiceAndBeans);
        foodDAO.add(Quesadillas);
    */
    backButton1.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    checkout.setOnMouseClicked(event -> Navigation.navigate(Screen.ORDER_DETAILS));
    // vegetarianButton.setOnAction(event -> Navigation.navigate(Screen.ORDER_CONFIRMATION));

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
    System.out.println(DBR.getVegetarian());
    ArrayList<MenuItem> filters = new ArrayList<>();
    Label lbl = new Label();
    lbl.setId(x.getText());
    lbl.setText(x.getText());
    lbl.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
    lbl.setMaxWidth(103);
    lbl.setMaxHeight(87);
    System.out.println("works");
    System.out.println(lbl.getText());

    filter.getChildren().add(lbl);
    filters.add(x);
  }

  public void clear1() {
    wf.getChildren().clear();
    qd.getChildren().clear();
    qdLabel.setText("");
    wfLabel.setText("");
    mpLabel.setText("");
  }

  public ArrayList<String> filterList = new ArrayList<>();

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
