package edu.wpi.teamname.controllers.mainpages;

import static javafx.scene.paint.Color.WHITE;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Signage;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
  @FXML private MFXRectangleToggleNode Switch;
  @FXML private Text whichKiosk;

  DataBaseRepository DBR = DataBaseRepository.getInstance();

  int theKiosk = 1;

  @FXML
  public void initialize() {

    ArrayList<Signage> a = new ArrayList<>();

    whichKiosk.setText("Kiosk 1");
    constructPage(Date.valueOf(LocalDate.now()), theKiosk);

    AUBTN.setDisable(true);

    LocationCB.valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              AUBTN.setDisable(
                  LocationCB.getValue() == null
                      || DirectionCB.getValue() == null
                      || DateP.getValue() == null);
            }));

    DirectionCB.valueProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              // check if textField1 is non-empty and enable/disable the button accordingly
              AUBTN.setDisable(
                  LocationCB.getValue() == null
                      || DirectionCB.getValue() == null
                      || DateP.getValue() == null);
            }));

    Switch.selectedProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (Switch.isSelected()) {
                if (theKiosk == 1) {
                  theKiosk = 2;
                  whichKiosk.setText("Kiosk 2");
                } else {
                  theKiosk = 1;
                  whichKiosk.setText("Kiosk 1");
                }
              }
            });

    DateP.valueProperty()
        .addListener(
            new ChangeListener<LocalDate>() {
              @Override
              public void changed(
                  ObservableValue<? extends LocalDate> observable,
                  LocalDate oldValue,
                  LocalDate newValue) {
                constructPage(Date.valueOf(newValue), theKiosk);
                System.out.println("Filtering by new date: " + newValue);
              }
            });

    LocationCB.getItems().addAll(DBR.getListOfEligibleRooms());

    DirectionCB.getItems().add("^");
    DirectionCB.getItems().add("->");
    DirectionCB.getItems().add("<-");
    DirectionCB.getItems().add("v");

    // Default to that day
    // Get calendar filter to work
    // Get Kiosk to default to one
    //

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

          Signage.Direction dir;

          if (DirectionCB.getValue().toString().equals("^")) {
            UpBox.getChildren().add(newLabel);
            dir = Signage.Direction.up;
          } else if (DirectionCB.getValue().toString().equals("->")) {
            RightBox.getChildren().add(newLabel);
            dir = Signage.Direction.right;
          } else if (DirectionCB.getValue().toString().equals("<-")) {
            LeftBox.getChildren().add(newLabel);
            dir = Signage.Direction.left;
          } else {
            DownBox.getChildren().add(newLabel);
            dir = Signage.Direction.down;
          }

          Date d = Date.valueOf(DateP.getValue());

          Signage theSign =
              new Signage(
                  theKiosk, dir, DBR.getLocationDAO().get(LocationCB.getValue().toString()), d);

          a.add(theSign);

          clearFields();
        });

    SubmitBTN.setOnMouseClicked(
        event -> {
          for (int i = 0; i < a.size(); i++) {
            DBR.getSignageDAO().add(a.get(i));
          }
        });

    ClearBTN.setOnMouseClicked(event -> clearFields());
  }

  private void clearFields() {
    DirectionCB.valueProperty().set(null);
    LocationCB.valueProperty().set(null);
  }

  private void constructPage(Date datee, int k) {
    ArrayList<Signage> toBePut = DBR.getSignageDAO().getForDateKiosk(datee, k);

    UpBox.getChildren().clear();
    RightBox.getChildren().clear();
    LeftBox.getChildren().clear();
    DownBox.getChildren().clear();

    for (Signage aSign : toBePut) {
      Label newLabel = new Label();
      newLabel.setAlignment(Pos.CENTER_LEFT);
      newLabel.setTextFill(WHITE);
      newLabel.setPrefSize(800, 25);
      newLabel.setMaxSize(800, 25);
      newLabel.setMinSize(800, 25);

      if (aSign.getDirection().name().equals("up")) {
        newLabel.setText("^ " + aSign.getSurroundingLocation().getLongName());
        newLabel.setStyle("-fx-font-size: 16;");

        UpBox.getChildren().add(newLabel);
      } else if (aSign.getDirection().name().equals("right")) {
        newLabel.setText("-> " + aSign.getSurroundingLocation().getLongName());
        newLabel.setStyle("-fx-font-size: 16;");

        RightBox.getChildren().add(newLabel);
      } else if (aSign.getDirection().name().equals("left")) {
        newLabel.setText("<- " + aSign.getSurroundingLocation().getLongName());
        newLabel.setStyle("-fx-font-size: 16;");

        LeftBox.getChildren().add(newLabel);
      } else {
        newLabel.setText("v " + aSign.getSurroundingLocation().getLongName());
        newLabel.setStyle("-fx-font-size: 16;");

        DownBox.getChildren().add(newLabel);
      }
    }
  }
}
