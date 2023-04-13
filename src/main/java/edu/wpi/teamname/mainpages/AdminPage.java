package edu.wpi.teamname.mainpages;

import edu.wpi.teamname.App;
import edu.wpi.teamname.controllers.AdminController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdminPage extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    final FXMLLoader loader = new FXMLLoader(App.class.getResource("views/AdminPage.fxml"));
    final BorderPane root = loader.load();
    AdminController adminController = new AdminController();
    loader.setController(adminController);

    final Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Adminpage");
    primaryStage.show();
  }
}
