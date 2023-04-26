package edu.wpi.teamname.controllers.Games;

import edu.wpi.teamname.Main;
import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GameCenterController {

  @FXML private Pane snakeGamePane;
  @FXML private Pane smashPane;
  @FXML ImageView imageViewSnake;
  Image fixedSnake =
      new Image(String.valueOf(Main.class.getResource("gameImages/snakeGame/fixedSnake.png")));

  public void initialize() {
    imageViewSnake.setImage(fixedSnake);
    snakeGamePane.setOnMouseClicked(event -> Navigation.navigate(Screen.SNAKE_GAME));
    // smashPane.setOnMouseClicked(event -> Navigation.navigate(Screen.SMASH_GAME));
    smashPane.setVisible(false);
  }
}
