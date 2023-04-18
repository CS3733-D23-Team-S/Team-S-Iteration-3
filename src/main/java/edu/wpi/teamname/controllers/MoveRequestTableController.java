package edu.wpi.teamname.controllers;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.MoveDAOImpl;
import edu.wpi.teamname.DAOs.orms.Move;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MoveRequestTableController {

  @FXML TableView MoveRequestTable;

  MoveDAOImpl repo = DataBaseRepository.getInstance().getMoveDAO();

  @FXML
  public void initialize() {
    TableColumn<MoveDAOImpl, String> column1 = new TableColumn<>("Node ID");
    column1.setCellValueFactory(new PropertyValueFactory<>("nodeid"));

    TableColumn<MoveDAOImpl, String> column2 = new TableColumn<>("Location");
    column2.setCellValueFactory(new PropertyValueFactory<>("location"));

    TableColumn<MoveDAOImpl, String> column3 = new TableColumn<>("Date");
    column3.setCellValueFactory(new PropertyValueFactory<>("date"));

    MoveRequestTable.getColumns().add(column1);
    MoveRequestTable.getColumns().add(column2);
    MoveRequestTable.getColumns().add(column3);

    for (Move m : repo.getAll()) {
      MoveRequestTable.getItems().add(m);
    }
  }
}
