package edu.wpi.teamname.controllers.mainpages;

import static javafx.scene.paint.Color.WHITE;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class SinageEditorController {
  @FXML private VBox UpBox;
  @FXML private VBox RightBox;
  @FXML private VBox LeftBox;
  @FXML private VBox DownBox;
  @FXML private SearchableComboBox LocationCB;
  @FXML private SearchableComboBox DirectionCB;
  @FXML private MFXDatePicker DateP;
  @FXML private MFXButton AUBTN;
  @FXML private MFXButton RemBTN;
  @FXML private MFXButton ClearBTN;
  @FXML private MFXButton SubmitBTN;
  @FXML private MFXComboBox KioskCB;

  DataBaseRepository DBR = DataBaseRepository.getInstance();

  @FXML
  public void initialize() {

    AUBTN.setDisable(true);

    LocationCB.valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              AUBTN.setDisable(
                  LocationCB.getValue() == null
                      || DirectionCB.getValue() == null
                      || DateP.getValue() == null
                      || KioskCB.getValue() == null);
            }));

    DirectionCB.valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              AUBTN.setDisable(
                  LocationCB.getValue() == null
                      || DirectionCB.getValue() == null
                      || DateP.getValue() == null
                      || KioskCB.getValue() == null);
            }));

    DateP.valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              AUBTN.setDisable(
                  LocationCB.getValue() == null
                      || DirectionCB.getValue() == null
                      || DateP.getValue() == null
                      || KioskCB.getValue() == null);
            }));

    KioskCB.valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              AUBTN.setDisable(
                  LocationCB.getValue() == null
                      || DirectionCB.getValue() == null
                      || DateP.getValue() == null
                      || KioskCB.getValue() == null);
            }));

    LocationCB.getItems().addAll(DBR.getListOfEligibleRooms());

    DirectionCB.getItems().add("^");
    DirectionCB.getItems().add("->");
    DirectionCB.getItems().add("<-");
    DirectionCB.getItems().add("v");

    AUBTN.setOnMouseClicked(
        event -> {
          Label newLabel = new Label();
          newLabel.setAlignment(Pos.CENTER_LEFT);
          newLabel.setTextFill(WHITE);
          newLabel.setPrefSize(800, 25);
          newLabel.setMaxSize(800, 25);
          newLabel.setMinSize(800, 25);

          newLabel.setText(
              DirectionCB.getValue().toString() + " " + LocationCB.getValue().toString());
          newLabel.setStyle("-fx-font-size: 16;");

          if (DirectionCB.getValue().toString().equals("^")) {
            UpBox.getChildren().add(newLabel);
          } else if (DirectionCB.getValue().toString().equals("->")) {
            RightBox.getChildren().add(newLabel);
          } else if (DirectionCB.getValue().toString().equals("<-")) {
            LeftBox.getChildren().add(newLabel);
          } else {
            DownBox.getChildren().add(newLabel);
          }

          clearFields();
        });

    ClearBTN.setOnMouseClicked(event -> clearFields());
  }

  private void clearFields() {
    DirectionCB.valueProperty().set(null);
    LocationCB.valueProperty().set(null);
  }
}
