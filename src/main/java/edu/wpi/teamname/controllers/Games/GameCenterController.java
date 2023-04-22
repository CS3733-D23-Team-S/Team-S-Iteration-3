package edu.wpi.teamname.controllers.Games;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class GameCenterController {

  @FXML private Pane snakeGamePane;
  @FXML private Pane minecraftPane;

  public void initialize() {
    snakeGamePane.setOnMouseClicked(event -> Navigation.navigate(Screen.SNAKE_GAME));
    //minecraftPane.setOnMouseClicked(event -> runMinecraft());
  }


}
