package edu.wpi.teamname.mainpages;

import edu.wpi.teamname.App;
import edu.wpi.teamname.controllers.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HomePage extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(App.class.getResource("views/Home.fxml"));
    final BorderPane root = loader.load();
    HomeController homeController = new HomeController();
    loader.setController(homeController);

    final Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.setTitle("HomePage");
    primaryStage.show();
  }
}
