package dk.easv.cs5.mytunes.gui;

import dk.easv.cs5.mytunes.bll.ILogic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller {

    public Button btnNewSong;
    private ILogic logic;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
