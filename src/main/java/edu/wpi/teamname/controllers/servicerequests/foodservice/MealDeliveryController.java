package edu.wpi.teamname.controllers.servicerequests.foodservice;

import static javafx.geometry.Pos.CENTER;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.User;
import edu.wpi.teamname.Main;
import edu.wpi.teamname.ServiceRequests.FoodService.Food;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDAOImpl;
import edu.wpi.teamname.ServiceRequests.FoodService.FoodDelivery;
import edu.wpi.teamname.ServiceRequests.FoodService.OrderItem;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.*;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.SearchableComboBox;

public class MealDeliveryController {
  DecimalFormat df = new DecimalFormat("0.00");
  @FXML CheckComboBox checkBox;
  public static int mealDevID;
  @FXML MFXButton clearfilter;
  @FXML FlowPane flowpane;
  @FXML VBox cartBox;
  @FXML VBox checkOutBox;
  @FXML VBox cartPane;
  @FXML MFXButton clearCart;
  @FXML MFXButton proceed;
  @FXML Label totalPrice;
  @FXML VBox lowerCart;
  @FXML SearchableComboBox employeedrop;
  @FXML SearchableComboBox locationdrop;
  @FXML MFXTextField requestfield;
  @FXML MFXButton submitButton;
  @FXML MFXButton clearSubmit;

  @FXML private DataBaseRepository dbr = DataBaseRepository.getInstance();

  private int cartID = 1;
  public static int delID;
  public static OrderItem mealCart;

  public static ArrayList<String> filterList = new ArrayList<String>();
  public FoodDAOImpl foodDAO = dbr.getFoodDAO();
  public static ArrayList<Food> allFood = new ArrayList<Food>();

  @FXML
  public void initialize() {

    mealCart = new OrderItem(cartID++);
    delID = dbr.getLastFoodDevID();
    lowerCart.setVisible(false);

    mealCart
        .getCartItems()
        .addListener(
            (ListChangeListener<Food>)
                change -> {
                  displayCart();
                });

    openCart();

    proceed.setOnMouseClicked(event -> checkOutHandler());

    clearfilter.setOnMouseClicked(event -> noFilter());
    clearCart.setOnMouseClicked(event -> clearCart());
    checkOutBox.setVisible(false);

    filterFood(checkBox.getCheckModel().getCheckedItems());

    submitButton.setDisable(true);

    employeedrop
        .valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitButton.setDisable(
                  employeedrop.getValue() == null
                      || locationdrop.getValue() == null
                      || requestfield.getText().trim().isEmpty());
            }));

    locationdrop
        .valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitButton.setDisable(
                  employeedrop.getValue() == null
                      || locationdrop.getValue() == null
                      || requestfield.getText().trim().isEmpty());
            }));

    requestfield
        .textProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              submitButton.setDisable(
                  employeedrop.getValue() == null
                      || locationdrop.getValue() == null
                      || requestfield.getText().trim().isEmpty());
            }));

    for (User u : dbr.getUserDAO().getListOfUsers().values()) {
      employeedrop.getItems().add(u.getFirstName() + " " + u.getLastName());
    }

    locationdrop.getItems().addAll(dbr.getListOfEligibleRooms());

    clearSubmit.setOnMouseClicked(event -> clearCheckoutFields());

    submitButton.setOnMouseClicked(event -> submitHandler());

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
            "glutenfree",
            "italian",
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
  }

  public void noFilter() {
    flowpane.getChildren().clear();
    for (String s : filterList) {
      filterList.remove(s);
    }
    checkBox.getCheckModel().check(1);
    checkBox.getCheckModel().clearChecks();
  }

  public Method chooseVegetarian() {
    for (Food f : dbr.getFoodDAO().getVegetarian()) {
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
    for (Food f : dbr.getFoodDAO().getVegan()) {
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
    for (Food f : dbr.getFoodDAO().getGlutenFree()) {
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
    for (Food f : dbr.getFoodDAO().getHalal()) {
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
    for (Food f : dbr.getFoodDAO().getKosher()) {
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

      btn1.setOnMouseClicked(
          event -> {
            store(f.getFoodID());
          });
    }

    return null;
  }

  public Method chooseAmerican() {
    for (int i = 0; i < dbr.getFoodDAO().getAmerican().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(dbr.getFoodDAO().getAmerican().get(i).toString());
      btn.setText(dbr.getFoodDAO().getAmerican().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      flowpane.getChildren().add(btn);

      int finalI = i;
      btn.setOnMouseClicked(event -> store(dbr.getFoodDAO().getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseItalian() {
    for (int i = 0; i < dbr.getFoodDAO().getItalian().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(dbr.getFoodDAO().getItalian().get(i).toString());
      btn.setText(dbr.getFoodDAO().getItalian().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      flowpane.getChildren().add(btn);

      int finalI = i;
      btn.setOnMouseClicked(event -> store(dbr.getFoodDAO().getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseMexican() {
    for (int i = 0; i < dbr.getFoodDAO().getMexican().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(dbr.getFoodDAO().getMexican().get(i).toString());

      btn.setText(dbr.getFoodDAO().getMexican().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      flowpane.getChildren().add(btn);

      int finalI = i;
      btn.setOnMouseClicked(event -> store(dbr.getFoodDAO().getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public Method chooseIndian() {
    for (int i = 0; i < dbr.getFoodDAO().getIndian().size(); i++) {
      MFXButton btn = new MFXButton();
      btn.setId(dbr.getFoodDAO().getIndian().get(i).toString());
      btn.setText(dbr.getFoodDAO().getIndian().get(i).toString());
      btn.setMaxWidth(103);
      btn.setMaxHeight(87);
      flowpane.getChildren().add(btn);

      btn.setOnMouseClicked(event -> Navigation.launchPopUp(Screen.PRODUCT_DETAILS));
      int finalI = i;
      btn.setOnMouseClicked(event -> store(dbr.getFoodDAO().getKosher().get(finalI).getFoodID()));
    }
    return null;
  }

  public void store(int x) {
    mealDevID = x;
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

  public void filterFood(ObservableList<String> filterNeeds) {
    flowpane.getChildren().clear();

    allFood = (ArrayList<Food>) foodDAO.queriedFoods(filterNeeds);

    System.out.println(allFood);

    for (Food f : allFood) {

      Image image = new Image(Main.class.getResource(f.getImage()).toString());
      ImageView view = new ImageView(image);
      view.setPreserveRatio(false);
      view.setFitHeight(80);
      view.setFitWidth(80);

      MFXButton btn1 = new MFXButton();
      btn1.setId(f.toString());
      btn1.setText(f.getFoodName());
      btn1.setPrefWidth(175);
      btn1.setPrefHeight(100);
      btn1.setStyle(
          "-fx-background-radius:10 10 10 10; -fx-font-size: 12;-fx-effect: dropshadow(three-pass-box, rgba(42,42,38,0.35), 10, 0, 0, 5);");
      btn1.setWrapText(true);
      btn1.setGraphic(view);

      flowpane.getChildren().add(btn1);
      flowpane.setHgap(20);
      flowpane.setVgap(20);

      btn1.setOnMouseClicked(
          event -> {
            store(f.getFoodID());
          });
    }
  }

  public void openCart() {
    totalPrice.setText(String.valueOf("Total Price: $" + df.format(mealCart.getTotalPrice())));
    lowerCart.setVisible(true);
    cartPane.getChildren().clear();
    displayCart();
  }

  public void checkOutHandler() {
    if (mealCart.getTotalPrice() != 0) {
      checkOutBox.setVisible(true);
      cartBox.getChildren().clear();
    }
  }

  public void displayCart() {
    System.out.println("Displaying flowers");
    cartPane.getChildren().clear();
    totalPrice.setText(String.valueOf("Total Price: $" + df.format(mealCart.getTotalPrice())));

    if (mealCart.getCartItems().size() == 0) {

    } else {
      for (Food food : mealCart.getCartItems()) {

        System.out.println("works");

        HBox newRow = new HBox();
        newRow.setSpacing(5);
        newRow.setMaxHeight(300);
        newRow.setMaxWidth(200);

        ImageView flowerImage = new ImageView();
        Image image = new Image(Main.class.getResource(food.getImage()).toString());
        flowerImage.setImage(image);
        flowerImage.setStyle("-fx-background-radius: 10 10 10 10;");

        ImageView delete = new ImageView();
        Image imageDelete =
            new Image(String.valueOf(Main.class.getResource("images/TrashCan.png")));
        delete.setImage(imageDelete);
        delete.setStyle("-fx-background-radius: 10 10 10 10;");

        flowerImage.setFitHeight(60);
        flowerImage.setFitWidth(60);
        flowerImage.setPreserveRatio(false);

        delete.setFitHeight(20);
        delete.setFitWidth(20);
        delete.setPreserveRatio(false);

        VBox itemInfo = new VBox();
        itemInfo.setSpacing(5);
        itemInfo.setPrefWidth(276);
        itemInfo.setMaxHeight(300);

        HBox quantityChange = new HBox();
        quantityChange.setAlignment(CENTER);
        quantityChange.setSpacing(5);

        MFXButton increaseB = new MFXButton();
        increaseB.setStyle(
            "-fx-background-color: transparent; -fx-font-family: 'Open Sans'; -fx-font-size: 16; -fx-text-fill:#1d3d94");
        increaseB.setText("+");

        MFXButton decreaseB = new MFXButton();
        decreaseB.setStyle(
            "-fx-background-color: transparent; -fx-font-family: 'Open Sans'; -fx-font-size: 16; -fx-text-fill:#1d3d94");
        decreaseB.setText("-");

        Label qLabel = new Label();
        qLabel.setAlignment(CENTER);
        qLabel.setMinWidth(30);
        qLabel.setText(String.valueOf(food.getQuantity()));
        qLabel.setStyle(
            "-fx-background-color: #FFFFFF; -fx-background-radius: 10 10 10 10; -fx-font-family: 'Open Sans'; -fx-font-size: 16; -fx-text-fill:#1d3d94");

        quantityChange.getChildren().add(decreaseB);
        quantityChange.getChildren().add(qLabel);
        quantityChange.getChildren().add(increaseB);

        decreaseB.setOnMouseClicked(
            event -> {
              qLabel.setText(Integer.toString(food.getQuantity()));
              if (food.getQuantity() > 1) {
                food.setQuantity(food.getQuantity() - 1);
              }
              displayCart();
            });

        increaseB.setOnMouseClicked(
            event -> {
              food.setQuantity(food.getQuantity() + 1);
              qLabel.setText(Integer.toString(food.getQuantity()));
              displayCart();
            });

        HBox priceQ = new HBox();
        priceQ.setSpacing(5);
        priceQ.setMaxWidth(276);

        VBox deleteBox = new VBox();
        deleteBox.setSpacing(5);
        deleteBox.setPrefWidth(276);
        deleteBox.setMaxHeight(300);

        Label name = new Label();
        Label quantity = new Label();

        name.setText(food.getFoodName());
        name.setStyle(
            "-fx-text-fill: #000000; -fx-font-size: 16px; -fx-font-weight: bold; -fx-font-style: open sans");

        quantity.setText(String.valueOf("QTY: " + food.getQuantity() + "x"));
        quantity.setStyle(
            "-fx-text-fill: #000000; -fx-font-size: 16px; -fx-font-style: open sans;");

        cartPane.getChildren().add(newRow);
        cartPane.setSpacing(10);
        newRow.getChildren().add(flowerImage);
        newRow.getChildren().add(itemInfo);
        newRow.getChildren().add(delete);

        itemInfo.getChildren().add(name);
        // itemInfo.getChildren().add(priceQ);
        itemInfo.getChildren().add(quantityChange);

        priceQ.getChildren().add(quantity);

        delete.setOnMouseClicked(event -> deleteFood(food));
      }
    }
  }

  public void deleteFood(Food food) {
    mealCart.removeFoodItem(food);
    displayCart();
  }

  public void clearCheckoutFields() {
    requestfield.clear();
    employeedrop.valueProperty().set(null);
    locationdrop.valueProperty().set(null);
  }

  public void submitHandler() {
    try {
      String Emp = employeedrop.getValue().toString();
      String deliveryRoom = locationdrop.getValue().toString();

      String n = requestfield.getText();

      Date d = Date.valueOf(LocalDate.now());
      Time t = Time.valueOf(LocalTime.now());

      FoodDelivery currentFoodDev =
          new FoodDelivery(
              MealDeliveryController.delID++,
              MealDeliveryController.mealCart.toString(),
              d,
              t,
              deliveryRoom,
              ActiveUser.getInstance().getCurrentUser().getUserName(),
              Emp,
              "Recieved",
              MealDeliveryController.mealCart.getTotalPrice(),
              n);

      dbr.getFoodDeliveryDAO().add(currentFoodDev);

      checkOutBox.getChildren().clear();

      Label confirm = new Label();
      ImageView checkMark = new ImageView();
      Label thanks = new Label();
      Image checkMark1 = new Image(String.valueOf(Main.class.getResource("images/checkMark.png")));
      checkMark.setImage(checkMark1);
      checkMark.setStyle("-fx-background-radius: 10 10 10 10;");

      checkMark.setFitHeight(180);
      checkMark.setFitWidth(180);
      checkMark.setPreserveRatio(false);
      confirm.setText("Order Submitted!");
      thanks.setText("Thank you for your order!");
      confirm.setStyle("-fx-font-size: 30;");
      thanks.setStyle("-fx-font-size:18; -fx-font-style: italic;");
      checkOutBox.setAlignment(Pos.CENTER);
      checkOutBox.getChildren().add(confirm);
      checkOutBox.getChildren().add(checkMark);
      checkOutBox.getChildren().add(thanks);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void clearCart() {
    mealCart.removeAll();
    displayCart();
  }
}
