package dk.easv.cs5.mytunes.gui;

import dk.easv.cs5.mytunes.Application;
import dk.easv.cs5.mytunes.be.Song;
import dk.easv.cs5.mytunes.bll.ILogic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    private ILogic logic;


    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    private void onNewSongButtonAction(ActionEvent actionEvent) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/SongEditWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("New/Edit Song");
            stage.setScene(scene);
            stage.show();
        }
    @FXML
    private void onSearchButtonAction(ActionEvent actionEvent) {
    }
    @FXML
    private void onPreviousSongButtonAction(ActionEvent actionEvent) {
    }
    @FXML
    private void onPlayPauseButtonActtion(ActionEvent actionEvent) {
    }
    @FXML
    private void onNextSongButtonAction(ActionEvent actionEvent) {
    }
    @FXML
    private void onEditSongButtonAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/SongEditWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("New/Edit Song");
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    private void onDeleteSongAction(ActionEvent actionEvent) {
    }
    @FXML
    private void onUpButtonAction(ActionEvent actionEvent) {
    }
    @FXML
    private void onDownButtonAction(ActionEvent actionEvent) {
    }
    @FXML
    private void onDeleteSongInPlaylistAction(ActionEvent actionEvent) {
    }
    @FXML
    private void onNewButtonPlaylistAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/PlaylistEditWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("New/Edit Playlist");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void onEditButtonPlaylistAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/PlaylistEditWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("New/Edit Song");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void onDeleteButtonPlaylistAction(ActionEvent actionEvent) {
    }
    @FXML
    private void onCloseButtonAction(ActionEvent actionEvent) {
    }
}
