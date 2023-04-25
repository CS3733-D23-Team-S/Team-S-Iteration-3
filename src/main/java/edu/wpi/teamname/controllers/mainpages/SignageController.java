package edu.wpi.teamname.controllers.mainpages;

import static javafx.scene.paint.Color.WHITE;

import edu.wpi.teamname.DAOs.ActiveUser;
import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Permission;
import edu.wpi.teamname.DAOs.orms.Signage;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SignageController {
  @FXML Label tapButton;

  @FXML VBox UBox;
  @FXML VBox RBox;
  @FXML VBox LBox;
  @FXML VBox DBox;

  DataBaseRepository DBR = DataBaseRepository.getInstance();

  //  @FXML
  //  public void initialize() {
  //    signageBack.setOnMouseClicked(event -> Navigation.navigate(Screen.WELCOME_PAGE));
  //  }

  public void initialize() {
    // label button
    tapButton.setOnMouseClicked(
        event -> {
          if (ActiveUser.getInstance().isLoggedIn()) {
            System.out.println(ActiveUser.getInstance().getPermission().name());
            if (ActiveUser.getInstance().getPermission() == Permission.ADMIN) {
              Navigation.navigate(Screen.ADMIN_PAGE);
            } else {
              Navigation.navigate(Screen.STAFF);
            }
          } else Navigation.navigate(Screen.LOGIN_PAGE);
        });

    constructPage(Date.valueOf(LocalDate.now()), SinageEditorController.theKiosk);
  }

  private void constructPage(Date datee, int k) {
    ArrayList<Signage> toBePut = DBR.getSignageDAO().getForDateKiosk(datee, k);

    UBox.getChildren().clear();
    RBox.getChildren().clear();
    LBox.getChildren().clear();
    DBox.getChildren().clear();

    for (Signage aSign : toBePut) {
      Label newLabel = new Label();
      newLabel.setAlignment(Pos.CENTER_LEFT);
      newLabel.setTextFill(WHITE);
      newLabel.setPrefSize(800, 80);
      newLabel.setMaxSize(800, 80);
      newLabel.setMinSize(800, 80);

      if (aSign.getDirection().name().equals("up")) {
        newLabel.setText("^ " + aSign.getSurroundingLocation().getLongName());
        newLabel.setStyle("-fx-font-size: 48;");

        UBox.getChildren().add(newLabel);
      } else if (aSign.getDirection().name().equals("right")) {
        newLabel.setText("-> " + aSign.getSurroundingLocation().getLongName());
        newLabel.setStyle("-fx-font-size: 48;");

        RBox.getChildren().add(newLabel);
      } else if (aSign.getDirection().name().equals("left")) {
        newLabel.setText("<- " + aSign.getSurroundingLocation().getLongName());
        newLabel.setStyle("-fx-font-size: 48;");

        LBox.getChildren().add(newLabel);
      } else {
        newLabel.setText("v " + aSign.getSurroundingLocation().getLongName());
        newLabel.setStyle("-fx-font-size: 48;");

        DBox.getChildren().add(newLabel);
      }
    }
  }
}
