package dk.easv.cs5.mytunes.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PlaylistEditController {
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    @FXML
    private void onCancelButton(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void onSaveButton(ActionEvent actionEvent) {
    }
}
