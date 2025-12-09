package dk.easv.cs5.mytunes.gui;

import dk.easv.cs5.mytunes.be.Genre;
import dk.easv.cs5.mytunes.bll.ILogic;
import dk.easv.cs5.mytunes.bll.Logic;
import dk.easv.cs5.mytunes.gui.helpers.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GenreAddController {

    private ObservableList<Genre> genresList = FXCollections.observableArrayList();
    public void setGenresList(ObservableList<Genre> genresList){
        this.genresList = genresList;
    }


    private ILogic logic = new Logic();

    @FXML private Button btnCancel;
    @FXML private TextField txtName;

    public void onCancelButton(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();

    }

    public void onSaveButton(ActionEvent actionEvent) {
        String name = txtName.getText().trim();

        List<String> missingFields = new ArrayList<>();
        if (name.isEmpty()) {missingFields.add("Name");}

        if (!missingFields.isEmpty()) {
            AlertHelper.showWarning("Please fill out the name field!");
            return;
        }
        Genre genre = new Genre(name);

        try{
            logic.createGenre(genre);
            if(this.genresList != null){
                this.genresList.add(genre);
            }
            AlertHelper.showInfo("Genre added successfully!");

            txtName.clear();
        }   catch (Exception e){
            AlertHelper.showError("Error while adding genre!");
            e.printStackTrace();
        }


    }
}
