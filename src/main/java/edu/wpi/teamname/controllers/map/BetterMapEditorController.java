package edu.wpi.teamname.controllers.map;

import edu.wpi.teamname.DAOs.orms.Node;
import edu.wpi.teamname.Main;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import net.kurobako.gesturefx.GesturePane;

import java.util.ArrayList;
import java.util.List;


public class BetterMapEditorController
{
    @FXML MFXButton floorFiveBTN;
    @FXML MFXButton floorFourBTN;
    @FXML MFXButton floorThreeBTN;
    @FXML MFXButton floorTwoBTN;
    @FXML MFXButton floorOneBTN;
    @FXML MFXButton GroundBTN;
    @FXML MFXButton addNodeBTN;
    @FXML MFXButton removeNodeBTN;
    @FXML MFXButton editNodeBTN;
    @FXML MFXButton addLocationBTN;
    @FXML MFXButton removeLocationBTN;
    @FXML MFXButton editLocationBTN;

    @FXML MFXTextField ntNodeIDTF;
    @FXML MFXTextField xCoordTF;
    @FXML MFXTextField yCoordTF;
    @FXML MFXTextField floorTF;
    @FXML MFXTextField buildingTF;
    @FXML MFXTextField nodeTypeTF;
    @FXML MFXTextField longNameTF;
    @FXML MFXTextField shortNameTF;

    @FXML GesturePane mapPane;



    ImageView floor;

    Image ground = new Image(String.valueOf(Main.class.getResource("images/00_thegroundfloor.png")));
    Image floorL1 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel1.png")));
    Image floorL2 = new Image(String.valueOf(Main.class.getResource("images/00_thelowerlevel2.png")));
    Image floor1 = new Image(String.valueOf(Main.class.getResource("images/01_thefirstfloor.png")));
    Image floor2 = new Image(String.valueOf(Main.class.getResource("images/02_thesecondfloor.png")));
    Image floor3 = new Image(String.valueOf(Main.class.getResource("images/03_thethirdfloor.png")));

    StackPane stackPane = new StackPane();

    AnchorPane anchorPane = new AnchorPane();

    List<Circle> theCircs = new ArrayList<>();
    List<Node> groundNodes = new ArrayList<>();
    List<Node> floor1Nodes = new ArrayList<>();
    List<Node> floor2Nodes = new ArrayList<>();
    List<Node> floor3Nodes = new ArrayList<>();
    List<Node> floorL1Nodes = new ArrayList<>();
    List<Node> floorL2Nodes = new ArrayList<>();

    List<Circle> groundCircles = new ArrayList<>();
    List<Circle> floor1Circles = new ArrayList<>();
    List<Circle> floor2Circles = new ArrayList<>();
    List<Circle> floor3Circles = new ArrayList<>();
    List<Circle> floorL1Circles = new ArrayList<>();
    List<Circle> floorL2Circles = new ArrayList<>();

    public void initialize()
    {
        floor.setImage(floor1);
        stackPane.getChildren().add(floor);
        stackPane.getChildren().add(anchorPane);
        anchorPane.setBackground(Background.fill(Color.TRANSPARENT));
        generateFloorNodes(floor1Nodes);
    }

    public void toGround()
    {
        floor.setImage(ground);

        theCircs = groundCircles;

        stackPane.getChildren().remove(floor);
        anchorPane = new AnchorPane();

        stackPane.getChildren().add(floor);
        generateGroundNodes();
        mapPane.setContent(stackPane);
    }

    public void toFloor1()
    {
        floor.setImage(floor1);

        theCircs = floor1Circles;

        stackPane.getChildren().remove(floor);
        stackPane.getChildren().remove(groundCircles);
        stackPane.getChildren().remove(floor2Circles);
        stackPane.getChildren().remove(floor3Circles);
        stackPane.getChildren().remove(floorL1Circles);
        stackPane.getChildren().remove(floorL2Circles);

        stackPane.getChildren().add(floor);
        generateFloor1Nodes();
        mapPane.setContent(stackPane);
    }

    public void toFloor2()
    {
        floor.setImage(floor2);

        theCircs = floor2Circles;

        stackPane.getChildren().remove(floor);
        stackPane.getChildren().remove(floor1Circles);
        stackPane.getChildren().remove(groundCircles);
        stackPane.getChildren().remove(floor3Circles);
        stackPane.getChildren().remove(floorL1Circles);
        stackPane.getChildren().remove(floorL2Circles);

        stackPane.getChildren().add(floor);
        generateFloor2Nodes();
        mapPane.setContent(stackPane);
    }

    public void toFloor3()
    {
        floor.setImage(floor3);

        theCircs = floor3Circles;

        stackPane.getChildren().remove(floor);
        stackPane.getChildren().remove(floor1Circles);
        stackPane.getChildren().remove(floor2Circles);
        stackPane.getChildren().remove(groundCircles);
        stackPane.getChildren().remove(floorL1Circles);
        stackPane.getChildren().remove(floorL2Circles);

        stackPane.getChildren().add(floor);
        generateFloor3Nodes();
        mapPane.setContent(stackPane);
    }

    public void toFloorL1()
    {
        floor.setImage(floorL1);

        theCircs = floorL1Circles;

        stackPane.getChildren().remove(floor);
        stackPane.getChildren().remove(floor1Circles);
        stackPane.getChildren().remove(floor2Circles);
        stackPane.getChildren().remove(floor3Circles);
        stackPane.getChildren().remove(groundCircles);
        stackPane.getChildren().remove(floorL2Circles);

        stackPane.getChildren().add(floor);
        generateFloorL1Nodes();
        mapPane.setContent(stackPane);
    }

    public void toFloorL2()
    {
        floor.setImage(floorL2);

        theCircs = floorL2Circles;

        stackPane.getChildren().remove(floor);
        stackPane.getChildren().remove(floor1Circles);
        stackPane.getChildren().remove(floor2Circles);
        stackPane.getChildren().remove(floor3Circles);
        stackPane.getChildren().remove(floorL1Circles);
        stackPane.getChildren().remove(groundCircles);

        stackPane.getChildren().add(floor);
        generateFloorL2Nodes();
        mapPane.setContent(stackPane);
    }

    private void generateFloorNodes(List<Node> floorNodes) {

    }

    private void generateGroundNodes() {

    }
    private void generateFloor1Nodes() {
    }

    private void generateFloor2Nodes() {
    }

    private void generateFloorL2Nodes() {
    }

    private void generateFloor3Nodes() {
    }

    private void generateFloorL1Nodes() {
    }


}
