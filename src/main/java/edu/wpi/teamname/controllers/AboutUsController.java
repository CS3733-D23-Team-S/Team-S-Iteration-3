package edu.wpi.teamname.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AboutUsController {

    @FXML Text projManager;
    @FXML Text prodOwner;
    @FXML Text fullEng;
    @FXML Text scrum;

    public void initialize(){
        projManager.setStyle("-fx-font-weight: BOLD");
        prodOwner.setStyle("-fx-font-weight: bold");
        fullEng.setStyle("-fx-font-weight: bold");
        scrum.setStyle("-fx-font-weight: bold");

    }
}
