package edu.wpi.teamname.controllers;

import edu.wpi.teamname.App;
import edu.wpi.teamname.databaseredo.DataBaseRepository;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;

public class CSVController {

  DataBaseRepository repo = DataBaseRepository.getInstance();

  private final FileChooser fileChooser = new FileChooser();
  @FXML @Setter Stage stage;
  DirectoryChooser directoryChooser = new DirectoryChooser();
  @FXML Button exitButton;

  @FXML MFXButton chooseInput;
  @FXML MFXTextField inputField;
  @FXML Label inputError;

  @FXML MFXButton chooseDirectory;
  @FXML MFXTextField outputField;
  @FXML Label outputError;

  @FXML MFXButton importButton;
  @FXML MFXButton exportButton;
  @FXML String inputPath = "";
  @FXML String outputPath = "";

  @FXML
  public void initialize() throws IOException {

    chooseInput.setOnMouseClicked(event -> getPath());
    chooseDirectory.setOnMouseClicked(event -> getDirectory());

    inputField.setOnMousePressed(event -> inputField.clear());
    outputField.setOnMousePressed(event -> outputField.clear());
    importButton.setOnMouseClicked(event -> importCSVOnClick());
    exportButton.setOnMouseClicked(event -> exportCSVOnClick());
    exitButton.setOnMouseClicked(event -> stage.close());
  }

  private void getPath() {
    File selectedFile = fileChooser.showOpenDialog(App.getPrimaryStage());
    inputPath = selectedFile.toString();
    System.out.println(inputPath);
    inputField.setText(inputPath);
  }

  private void getDirectory() {
    File selectedFile = directoryChooser.showDialog(App.getPrimaryStage());
    outputPath = selectedFile.toString();
    System.out.println(outputPath);
    outputField.setText(outputPath);
  }

  private void importCSVOnClick() {
    try {
      if (inputPath.isEmpty()) {
        inputError.setText("Please select an input file");
        return;
      } else {
        inputError.setText("");
      }
      repo.importCSV(inputPath);
    } catch (FileNotFoundException e) {
      inputError.setText("Error importing the file");
      e.printStackTrace();
    } catch (IOException e) {
      outputError.setText("Error importing the CSVs");
      throw new RuntimeException(e);
    }
  }

  private void exportCSVOnClick() {
    try {
      if (outputPath.isEmpty()) {
        outputError.setText("Please select an export directory");
        return;
      } else {
        outputError.setText("");
      }
      repo.exportCSV(outputPath);
    } catch (FileNotFoundException e) {
      outputError.setText("Error finding the directory");
      e.printStackTrace();
    } catch (IOException e) {
      outputError.setText("Error exporting the CSVs");
      throw new RuntimeException(e);
    }
  }
}
