package edu.wpi.teamname.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Setter;

public abstract class PopUpController {
  @FXML @Setter Stage stage;
  @FXML Button exitButton;
}
