package edu.wpi.teamname.controllers.mainpages;

import edu.wpi.teamname.DAOs.DataBaseRepository;
import edu.wpi.teamname.DAOs.orms.Signage;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXRectangleToggleNode;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.controlsfx.control.SearchableComboBox;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import static javafx.scene.paint.Color.WHITE;
import static javafx.scene.paint.Color.YELLOW;

public class SignageEditorController {
	static int theKiosk = 1;
	DataBaseRepository DBR = DataBaseRepository.getInstance();
	Signage oldSign = null;
	boolean update = false;
	String[] p = null;
	@FXML
	private VBox UpBox, RightBox, LeftBox, DownBox;
	@FXML
	private VBox BigBox;
	@FXML
	private SearchableComboBox<String> LocationCB, DirectionCB;
	@FXML
	private MFXDatePicker DateP;
	@FXML
	private MFXButton AUBTN, RemBTN, ClearBTN;
	@FXML
	private MFXButton Switch;
	@FXML
	private Text whichKiosk;
	private Label lastLabel = null;

	@FXML
	public void initialize() {
		LocationCB.getItems().addAll(DBR.getAllLocationNames());
		DirectionCB.getItems().addAll("^", "->", "<-", "v", "Stop here for");

		whichKiosk.setText("Kiosk 1");
		DateP.setValue(LocalDate.now());
		constructPage(Date.valueOf(LocalDate.now()), theKiosk);

		AUBTN.setDisable(true);
		// check if textField1 is non-empty and enable/disable the button accordingly
		LocationCB.valueProperty().addListener(((observable, oldValue, newValue) -> {
			AUBTN.setDisable(LocationCB.getValue() == null || DirectionCB.getValue() == null);
		}));
		// check if textField1 is non-empty and enable/disable the button accordingly
		DirectionCB.valueProperty().addListener(((observable, oldValue, newValue) -> {
			AUBTN.setDisable(LocationCB.getValue() == null || DirectionCB.getValue() == null);
		}));

		Switch.setOnMouseClicked( event -> {
			if (theKiosk == 1) {
				theKiosk = 2;
				whichKiosk.setText("Kiosk " + theKiosk);
				constructPage(Date.valueOf(DateP.getValue()), theKiosk);
			} else {
				theKiosk = 1;
				whichKiosk.setText("Kiosk " + theKiosk);
				constructPage(Date.valueOf(DateP.getValue()), theKiosk);
			}
		});

		DateP.valueProperty().addListener((observable, oldValue, newValue) -> {
			constructPage(Date.valueOf(newValue), theKiosk);
			System.out.println("Filtering by new date: " + newValue);
		});


		// Default to that day
		// Get calendar filter to work
		// Get Kiosk to default to one
		AUBTN.setOnMouseClicked(event -> {
			Label newLabel = new Label();
			initLabel(newLabel);

			newLabel.setText(DirectionCB.getValue() + " " + LocationCB.getValue());
			Date date = Date.valueOf(DateP.getValue());

			if (!update) {
				Signage.Direction dir;
				String directionValue = DirectionCB.getValue();
				switch (directionValue) {
					case "^" -> {
						UpBox.getChildren().add(newLabel);
						dir = Signage.Direction.up;
					}
					case "->" -> {
						RightBox.getChildren().add(newLabel);
						dir = Signage.Direction.right;
					}
					case "<-" -> {
						LeftBox.getChildren().add(newLabel);
						dir = Signage.Direction.left;
					}
					case "v" -> {
						DownBox.getChildren().add(newLabel);
						dir = Signage.Direction.down;
					}
					default -> {
						RightBox.getChildren().add(newLabel);
						dir = Signage.Direction.stop;
					}
				}

				Signage theSign = new Signage(theKiosk, dir, DBR.getLocationDAO().get(LocationCB.getValue()), date);
				DBR.getSignageDAO().add(theSign);

				constructPage(date, theKiosk);
			} else {
				Signage.Direction orientation = switch (p[0]) {
					case "^" -> Signage.Direction.up;
					case "->" -> Signage.Direction.right;
					case "<-" -> Signage.Direction.left;
					case "v" -> Signage.Direction.down;
					default -> Signage.Direction.stop;
				};

				String directionValue = DirectionCB.getValue();
				Signage.Direction w = switch (directionValue) {
					case "^" -> Signage.Direction.up;
					case "->" -> Signage.Direction.right;
					case "<-" -> Signage.Direction.left;
					case "v" -> Signage.Direction.down;
					default -> Signage.Direction.stop;
				};

				Signage oldSign = new Signage(theKiosk, orientation, DBR.getLocationDAO().get(p[1]), date);

				Signage newSign = new Signage(theKiosk, w, DBR.getLocationDAO().get(LocationCB.getValue()), date);

				System.out.println(newSign.toCSVString());

				DBR.getSignageDAO().update(oldSign, newSign);
				constructPage(date, theKiosk);
			}

			newLabel.setOnMouseClicked(e -> {
				update = true;
				p = colorEvent(newLabel);
			});

			update = false;
			clearFields();
		});

		RemBTN.setOnMouseClicked(event -> {
			Date date = Date.valueOf(DateP.getValue());
			String loc = LocationCB.getValue();
			DBR.getSignageDAO().deleteSignage(date, loc, theKiosk);
			constructPage(date, theKiosk);
			update = false;
			clearFields();
		});

		ClearBTN.setOnMouseClicked(event -> {
			clearFields();
			update = false;
		});
	}

	private void clearFields() {
		if (lastLabel != null) {
			lastLabel.setTextFill(WHITE);
		}

		DirectionCB.valueProperty().set(null);
		LocationCB.valueProperty().set(null);
	}

	private void constructPage(Date date, int k) {
		ArrayList<Signage> toBePut = DBR.getSignageDAO().getForDateKiosk(date, k);

		UpBox.getChildren().clear();
		RightBox.getChildren().clear();
		LeftBox.getChildren().clear();
		DownBox.getChildren().clear();

		boolean stopAlready = false;


		for (Signage aSign : toBePut) {
			Label newLabel = new Label();
			initLabel(newLabel);

			switch (aSign.getDirection().name()) {
				case "up" -> {
					newLabel.setText("^ " + aSign.getSurroundingLocation().getLongName());
					UpBox.getChildren().add(newLabel);
				}
				case "right" -> {
					newLabel.setText("-> " + aSign.getSurroundingLocation().getLongName());
					RightBox.getChildren().add(newLabel);
				}
				case "left" -> {
					newLabel.setText("<- " + aSign.getSurroundingLocation().getLongName());
					LeftBox.getChildren().add(newLabel);
				}
				case "down" -> {
					newLabel.setText("v " + aSign.getSurroundingLocation().getLongName());
					DownBox.getChildren().add(newLabel);
				}
				default -> {
					if(!stopAlready) {
						newLabel.setWrapText(true);
						newLabel.setMaxWidth(5);

						newLabel.setText("Stop here for " + aSign.getSurroundingLocation().getLongName());

						RightBox.getChildren().add(newLabel);

						stopAlready = true;
					}
					else {
						Label nextLabel = new Label();
						initLabel(nextLabel);

						newLabel.setText(aSign.getSurroundingLocation().getLongName());
						RightBox.getChildren().add(newLabel);
					}
				}
			}

			newLabel.setOnMouseClicked(event -> {
				update = true;
				p = colorEvent(newLabel);
			});
		}
	}

	private String[] colorEvent(Label l) {

		if (lastLabel != null) {
			lastLabel.setTextFill(WHITE);
		}
		l.setTextFill(YELLOW);
		lastLabel = l;

		String theText = l.getText();

		String[] parts = theText.split(" ", 2);

		String direction;
		boolean aStop = false;

		switch (parts[0]) {
			case "^" : {
				direction = "^";
				break;
			}
			case "->" : {
				direction = "->";
				break;
			}
			case "<-" : {
				direction = "<-";
				break;
			}
			case "v" : {
				direction = "v";
				break;
			}
			default : {
				direction = "Stop";
				aStop = true;
				break;
			}
		};

		DirectionCB.setValue(direction);

		if(aStop) {
			String[] firstStop = theText.split(" ");
			if(firstStop[0].equals("Stop"))
			{
				String newText = "";
				for(int a = 3; a < firstStop.length; a++)
				{
					newText += firstStop[a];

					if(! (a == firstStop.length - 1))
					{
						newText += " ";
					}
				}

				System.out.println(newText);
				LocationCB.setValue(newText);
				parts[1] = newText;
			}
			else {
				System.out.println(theText);
				LocationCB.setValue(theText);
				parts[1] = theText;
			}
		}
		else {
			LocationCB.setValue(parts[1]);
		}

		parts[0] = direction;

		return parts;
	}


	private void initLabel(Label newLabel) {
		newLabel.setAlignment(Pos.CENTER_LEFT);
		newLabel.setTextFill(WHITE);
		newLabel.setPrefSize(800, 25);
		newLabel.setMaxSize(800, 25);
		newLabel.setMinSize(800, 25);
		newLabel.setStyle("-fx-font-size: 16;");
	}
}
