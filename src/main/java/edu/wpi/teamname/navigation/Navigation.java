package edu.wpi.teamname.navigation;

import edu.wpi.teamname.App;
import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.orms.User;
import edu.wpi.teamname.controllers.PopUpController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Navigation {
  private BorderPane originalRootPane = App.getRootPane();
  User user;

  public static void navigate(final Screen screen) {
    final String filename = screen.getFilename();

    try {
      final var resource = App.class.getResource(filename);
      final FXMLLoader loader = new FXMLLoader(resource);

      App.getRootPane().setCenter(loader.load());
      if (filename.equals("views/SignagePage.fxml")
          || filename.equals("views/LoginPage.fxml")
          || (filename.equals("views/Pathfinding.fxml"))
              && (ActiveUser.getInstance().getCurrentUser() == null)) {
        App.getRootPane().setTop(null);
        App.getRootPane().setLeft(null);
      } else {
        App.getRootPane().setTop(App.getTop());
        App.getRootPane().setLeft(App.getLeft());
      }
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
