package edu.wpi.teamname.controllers.Games;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SmashGameController {
  @FXML private Canvas canvas;
  @FXML private Button playAgain;
  private Player p1;
  private Player p2;
  private double playerHeight = 20;
  private double playerWidth = 10;
  private GraphicsContext gc;
  private boolean gameOver = false;

  public void initialize() {
    gc = canvas.getGraphicsContext2D();
    canvas.setFocusTraversable(true);

    p1 = new Player(10, 50, Color.RED);
    p2 = new Player(50, 50, Color.RED);

    start();
  }

  public void start() {
    gc.setFill(p1.color);
    gc.fillRect(p1.X, p1.Y, playerWidth, playerHeight);

    gc.setFill(p2.color);
    gc.fillRect(p2.X, p2.Y, playerWidth, playerHeight);

    new AnimationTimer() {
      long lastTick = 0;

      public void handle(long now) {
        if (lastTick == 0) {
          lastTick = now;
          tick(gc);
          return;
        }

        if (now - lastTick > 1000000000 / 50) { // /speed
          lastTick = now;
          tick(gc);
        }
      }
    }.start();
  }

  public void tick(GraphicsContext gc) {
    if (gameOver) {
      gc.setFill(Color.RED);
      gc.setFont(new Font("", 50));
      gc.fillText("GAME OVER", 250, 400);
      playAgain.setVisible(true);
      return;
    }
  }

  private class Player {
    private double X;
    private double Y;
    private Color color;

    public Player(double x, double y, Color color) {
      X = x;
      Y = y;
      this.color = color;
    }
  }
}
