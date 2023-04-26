package edu.wpi.teamname.controllers.Games;

import edu.wpi.teamname.navigation.Navigation;
import edu.wpi.teamname.navigation.Screen;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SnakeGameController {
  private int food = 0;
  private int speed = 15;
  private int foodcolor = 0;
  private int width;
  private int height;
  private int foodX = 0;
  private int foodY = 0;
  private int cornersize = 25;
  private List<Corner> snake = new ArrayList<>();
  private Dir direction = Dir.left;
  private Dir lastDir = Dir.left;
  private boolean gameOver = false;
  private Random rand = new Random();
  @FXML private Button playAgain;
  // private Button playAgainCopy;
  @FXML private Canvas canvas;
  @FXML private AnchorPane anchorPane;

  @FXML
  public void initialize() {
    canvas.setFocusTraversable(true);
    this.height = (int) canvas.getHeight() / cornersize;
    this.width = (int) canvas.getWidth() / cornersize;
    start();
    playAgain.setVisible(false);
    playAgain.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.SNAKE_GAME);
        });
  }

  public enum Dir {
    left,
    right,
    up,
    down
  }

  public static class Corner {
    double x, y;

    public Corner(double x, double y) {
      this.x = x;
      this.y = y;
    }
  }

  public void start() {
    try {
      System.out.println(speed);
      speed = 15;
      newFood();

      final GraphicsContext gc = canvas.getGraphicsContext2D();

      new AnimationTimer() {
        long lastTick = 0;

        public void handle(long now) {
          if (lastTick == 0) {
            lastTick = now;
            tick(gc);
            return;
          }

          if (now - lastTick > 1000000000 / speed) {
            lastTick = now;
            tick(gc);
          }
        }
      }.start();

      canvas.setOnKeyPressed(
          event -> {
            switch (event.getCode()) {
              case UP:
                if (lastDir == Dir.down) break;
                direction = Dir.up;
                lastDir = Dir.up;
                break;
              case W:
                if (lastDir == Dir.down) break;
                direction = Dir.up;
                lastDir = Dir.up;
                break;
              case DOWN:
                if (lastDir == Dir.up) break;
                direction = Dir.down;
                lastDir = Dir.down;
                break;
              case S:
                if (lastDir == Dir.up) break;
                direction = Dir.down;
                lastDir = Dir.down;
                break;
              case LEFT:
                if (lastDir == Dir.right) break;
                direction = Dir.left;
                lastDir = Dir.left;
                break;
              case A:
                if (lastDir == Dir.right) break;
                direction = Dir.left;
                lastDir = Dir.left;
                break;
              case RIGHT:
                if (lastDir == Dir.left) break;
                direction = Dir.right;
                lastDir = Dir.right;
                break;
              case D:
                if (lastDir == Dir.left) break;
                direction = Dir.right;
                lastDir = Dir.right;
                break;
            }
          });

      snake.add(new Corner(width / 2, height / 2));
      snake.add(new Corner(width / 2, height / 2));
      snake.add(new Corner(width / 2, height / 2));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void tick(GraphicsContext gc) {
    if (gameOver) {
      gc.setFill(Color.RED);
      gc.setFont(new Font("", 50));
      gc.fillText("GAME OVER", 250, 400);
      playAgain.setVisible(true);
      return;
    }

    for (int i = snake.size() - 1; i >= 1; i--) {
      snake.get(i).x = snake.get(i - 1).x;
      snake.get(i).y = snake.get(i - 1).y;
    }

    switch (direction) {
      case up:
        snake.get(0).y--;
        if (snake.get(0).y < 0) {
          gameOver = true;
        }
        break;
      case down:
        snake.get(0).y++;
        if (snake.get(0).y > height) {
          gameOver = true;
        }
        break;
      case left:
        snake.get(0).x--;
        if (snake.get(0).x < 0) {
          gameOver = true;
        }
        break;
      case right:
        snake.get(0).x++;
        if (snake.get(0).x > width) {
          gameOver = true;
        }
        break;
    }

    if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
      snake.add(new Corner(-1, -1));
      newFood();
    }

    for (int i = 1; i < snake.size(); i++) {
      if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
        gameOver = true;
        break;
      }
    }

    gc.setFill(Color.DARKGREEN);
    gc.fillRect(0, 0, width * cornersize, height * cornersize);

    gc.setFill(Color.BLACK);
    gc.setFont(new Font("", 30));
    gc.fillText("Score: " + (food - 1), 10, 30);

    Color cc = Color.WHITE;

    switch (foodcolor) {
      case 0:
        cc = Color.PURPLE;
        break;
      case 1:
        cc = Color.LIGHTBLUE;
        break;
      case 2:
        cc = Color.YELLOW;
        break;
      case 3:
        cc = Color.PINK;
      case 4:
        cc = Color.ORANGE;
        break;
    }
    gc.setFill(cc);
    gc.fillOval(foodX * cornersize, foodY * cornersize, cornersize, cornersize);

    for (Corner c : snake) {
      gc.setFill(Color.LIGHTGREEN);
      gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
      gc.setFill(Color.LIGHTGREEN);
      gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 2, cornersize - 2);
    }
  }

  public void newFood() {
    start:
    while (true) {
      foodX = rand.nextInt((int) width);
      foodY = rand.nextInt((int) height);

      for (Corner c : snake) {
        if (c.x == foodX && c.y == foodY) {
          continue start;
        }
      }
      foodcolor = rand.nextInt(5);
      food++;
      break;
    }
  }
}
