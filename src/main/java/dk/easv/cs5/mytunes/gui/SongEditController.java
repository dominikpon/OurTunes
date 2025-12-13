package dk.easv.cs5.mytunes.gui;

import dk.easv.cs5.mytunes.Application;
import dk.easv.cs5.mytunes.be.Genre;
import dk.easv.cs5.mytunes.be.Song;
import dk.easv.cs5.mytunes.bll.ILogic;
import dk.easv.cs5.mytunes.bll.Logic;
import dk.easv.cs5.mytunes.bll.exceptions.LogicException;
import dk.easv.cs5.mytunes.gui.helpers.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.media.Media;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SongEditController {

    private ObservableList<Song> songList = FXCollections.observableArrayList();
    private ObservableList<Genre> genreList = FXCollections.observableArrayList();
    public void setSongList(ObservableList<Song> songList){
        this.songList = songList;
    }
    public ObservableList<Genre> getGenreList(){
        return this.genreList;
    }

    private ILogic logic = new Logic();

    public SongEditController() {

    }


    @FXML private TextField txtTitle;
    @FXML private TextField txtArtist;
    @FXML private ComboBox<Genre> comboGenre;
    @FXML private TextField txtDuration;
    @FXML private TextField txtPath;
    @FXML private Button btnChoosePath;
    private File selectedFile;



    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;


    @FXML
    public void initialize(){
        comboGenre.setItems(genreList);
        loadGenresIntoComboBox();

    }

    private void loadGenresIntoComboBox(){
        List <Genre> genres = logic.getAllGenres();
        genreList.setAll(genres);

        //comboGenre.setItems(FXCollections.observableArrayList(genres));
    }



    @FXML
    private void onCancelButton(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void onSaveButton(ActionEvent actionEvent) throws LogicException {
        String title = txtTitle.getText().trim();
        String artist = txtArtist.getText().trim();
        Genre selectedGenre = comboGenre.getSelectionModel().getSelectedItem();
        String duration = txtDuration.getText().trim();
        String path = txtPath.getText().trim();

        List<String> missingFields = new ArrayList<>();
        if(title.isEmpty()) missingFields.add("Title");
        if(artist.isEmpty()) missingFields.add("Artist");
        if(selectedGenre == null) missingFields.add("Genre");
        if(duration.isEmpty()) missingFields.add("Duration");
        if(path.isEmpty()) missingFields.add("Path");

        if(!missingFields.isEmpty()) {
            AlertHelper.showWarning("Please fill out all the required fields " + String.join (", ",  missingFields));
            return;
        }

        int durationInSeconds;          //duration of the song needs to be formatted to seconds before saving
                                        // for later saving in database
        try {
            String[] parts = duration.split(":"); //splits mm:ss
            durationInSeconds = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e)
        {
            AlertHelper.showError("Invalid duration format. Please enter mm:ss or duration in seconds.");
            return;
        }
        //f

        if (selectedGenre.getId() == 0){
            logic.createGenre(selectedGenre);

        }
        System.out.println("Saving song:");
        System.out.println("Title: " + title);
        System.out.println("Artist: " + artist);
        System.out.println("Genre: " + selectedGenre.getName() + " id=" + selectedGenre.getId());
        System.out.println("Duration: " + durationInSeconds);
        System.out.println("Path: " + path);
        Song song = new Song(title, artist, selectedGenre, durationInSeconds, path);

            try {
                logic.createSong(song);
                songList.add(song);
                AlertHelper.showInfo("Song was saved!");

                txtTitle.clear();
                txtArtist.clear();
                comboGenre.getSelectionModel().clearSelection();
                txtDuration.clear();
                txtPath.clear();
            } catch(Exception e) {
                AlertHelper.showError("An error occured while trying to save the song! ");
                e.printStackTrace();}
    }





    public void onChoosePath(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a song file");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Audio FIles", "*.mp3","*.wav"));
        File file = fileChooser.showOpenDialog(btnChoosePath.getScene().getWindow());
        if (file != null) {
            selectedFile = file;
            String baseFolder = "src/main/resources/songs/";
            String relativePath = baseFolder + selectedFile.getName();
            txtPath.setText(relativePath);
            try{
                Media media = new Media(file.toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnReady (() -> {
                    int durationInSeconds = (int) media.getDuration().toSeconds();      //formatter should be in BLL
                    int minutes = durationInSeconds / 60;
                     int seconds = durationInSeconds - minutes * 60;
                     String formatedDuration = String.format("%02d:%02d" ,minutes, seconds);

                    txtDuration.setText(formatedDuration);
                });
            } catch (Exception e) {
                e.printStackTrace();
                AlertHelper.showError("Cannot read the duration from the file!");
            }
        }

    }

    public void onMoreGenresButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/GenreWindow.fxml"));
        Parent root = fxmlLoader.load();
        GenreAddController controller = fxmlLoader.getController();
        controller.setGenresList(getGenreList());
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("New Genre");
        stage.setScene(scene);
        stage.show();
    }
}
