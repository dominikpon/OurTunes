package dk.easv.cs5.mytunes.gui;

import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.bll.ILogic;
import dk.easv.cs5.mytunes.bll.Logic;
import dk.easv.cs5.mytunes.gui.helpers.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;





public class PlaylistEditController {

    private ObservableList<Playlist> playlistList = FXCollections.observableArrayList();

    public void setPlaylist(ObservableList<Playlist> playlistList) {
        this.playlistList = playlistList;
    }

    ILogic logic = new Logic();

    @FXML private TextField txtName;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    public void initialize(){

    }

    @FXML
    private void onCancelButton(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void onSaveButton(ActionEvent actionEvent) {
        String name = txtName.getText().trim();

        List<String> missingFields = new ArrayList();
        if(name.isEmpty()) missingFields.add("name");

        if(!missingFields.isEmpty()) {
            AlertHelper.showWarning("Missing Fields" + missingFields);
        }
        Playlist playlist = new Playlist(name);

        try {
            logic.createPlaylist(playlist);
            playlistList.add(playlist);
            AlertHelper.showInfo("Playlist Created");

            txtName.clear();

        }catch (Exception e){
            AlertHelper.showError("Error");
            e.printStackTrace();
        }


    }
}
