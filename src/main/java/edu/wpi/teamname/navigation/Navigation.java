package edu.wpi.teamname.navigation;

import edu.wpi.teamname.App;
import edu.wpi.teamname.controllers.PopUpController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Navigation {

  public static void navigate(final Screen screen) {
    final String filename = screen.getFilename();

    try {
      final var resource = App.class.getResource(filename);
      final FXMLLoader loader = new FXMLLoader(resource);

      App.getRootPane().setCenter(loader.load());
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  public static void launchPopUp(final Screen screen) {
    final String filename = screen.getFilename();
    try {
      final var resource = App.class.getResource(filename);
      final FXMLLoader loader = new FXMLLoader(resource);
      Stage stage = new Stage();

      stage.initOwner(App.getPrimaryStage());
      stage.setScene(loader.load());
      PopUpController controller = loader.getController();
      controller.setStage(stage);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
