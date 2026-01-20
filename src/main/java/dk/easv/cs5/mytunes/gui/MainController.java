package dk.easv.cs5.mytunes.gui;

import dk.easv.cs5.mytunes.Application;
import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.be.Song;
import dk.easv.cs5.mytunes.bll.ILogic;
import dk.easv.cs5.mytunes.bll.Logic;
import dk.easv.cs5.mytunes.bll.exceptions.LogicException;
import dk.easv.cs5.mytunes.bll.tools.FormattingTool;
import dk.easv.cs5.mytunes.gui.helpers.AlertHelper;
import dk.easv.cs5.mytunes.gui.model.Model;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Collections;

public class MainController {

    //Table with songs

    @FXML private TableView<Song> songsTable; //<Song> to know that Table will consist of songs
    @FXML private TableColumn<Song, String> colTitle;      //Each column will consist of String from Song table
    @FXML private TableColumn<Song, String> colArtist;
    @FXML private TableColumn<Song, String> colGenre;
    @FXML private TableColumn<Song, String> colDuration;

    //Table with playlists
    @FXML private TableView<Playlist> playlistsTable;
    @FXML private TableColumn<Playlist, String> colPlaylistName;
    @FXML private TableColumn<Playlist, String> colPlaylistDuration;
    @FXML private TableColumn<Playlist, String> colPlaylistSongs;

    //Table with Songs in Playlist
    @FXML private TableView<Song> playlistSongsTable;
    @FXML private TableColumn colSongInPlaylist;

    @FXML private TextField filterField;
    @FXML private Button searchButton;

    //Text for the currently playing song
    @FXML private Label lblCurrentSong;

    //Sliders
    @FXML private Slider volumeSlider;
    @FXML private Slider timeSlider;

    //Observable lists for manual refreshing of Lists


    private Song lastSelectedSong;
    private Playlist lastSelectedPlaylist;


    private ILogic logic = new Logic();
    private Model model = new Model(logic);

    private MediaPlayer mp;
    private Song currentlyPlayingSong;
    private ObservableList<Song> currentPlayQueue;
    private int currentPlayIndex;

    @FXML
    private void initialize() throws LogicException {

        //set columns for Songs table
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));


        //format duration to mm:ss
        colDuration.setCellValueFactory(cellData ->{
            int seconds = cellData.getValue().getDuration();
            String formatted = FormattingTool.format(seconds);
                    return new ReadOnlyStringWrapper(formatted);
        });

        //set columns for Playlists table
        colPlaylistName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPlaylistDuration.setCellValueFactory(cd ->
                new ReadOnlyStringWrapper(FormattingTool.format(cd.getValue().getDuration())));
        colPlaylistSongs.setCellValueFactory(cd ->(new ReadOnlyStringWrapper(String.valueOf(cd.getValue().getSongs().size()))));

        //set column for Songs in Playlists table
        colSongInPlaylist.setCellValueFactory(new PropertyValueFactory<>("title"));

        songsTable.setItems(model.getSongList());
        playlistsTable.setItems(model.getPlaylistList());
        playlistSongsTable.setItems(model.getPlaylistSongList());


        //Tracking the last selected playlist
        trackLastSelectedPlaylist();
        //Tracking the last selected song
        trackSongSelection();


        model.loadEverything();

        timeSlider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
            if (!isChanging && mp != null) {
                mp.seek(Duration.seconds(timeSlider.getValue()));
            }
        });

        //Setting the initial volume value to 50% of the bar
        volumeSlider.setValue(0.5);

        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mp != null) {
                mp.setVolume(newVal.doubleValue());
            }
        });

        timeSlider.setDisable(true);

    }



    private void trackLastSelectedPlaylist(){
        playlistsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null) {
                model.loadSongsInPlaylist(newValue);
            }
        });

    }

    private void trackSongSelection() {
        //Tracking the last selected song firstly in Song table
        songsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                {
                    if (newValue != null) {
                        playlistSongsTable.getSelectionModel().clearSelection();
                        model.setSelectedSong(newValue);
                    }
                }
        );
        //Tracking the last selected song in Song table in playlist
        playlistSongsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null) {
                songsTable.getSelectionModel().clearSelection();
                model.setSelectedSong(newValue);
            }
        });
    }



    @FXML
    private void onNewSongButtonAction(ActionEvent actionEvent) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/SongEditWindow.fxml"));
            Parent root = fxmlLoader.load();
            SongEditController controller = fxmlLoader.getController();
            controller.setModel(this.model);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("New Song");
            stage.setScene(scene);
            stage.show();
        }

    private boolean isFiltered;

    private void clearFilter() {
        songsTable.setItems(model.getSongList());
        filterField.clear();
        searchButton.setText("Filter");
        isFiltered = false;
    }

        @FXML
    private void onSearchButtonAction(ActionEvent actionEvent) {

        if (isFiltered) {
            clearFilter();
            return;
        }

            String search = filterField.getText().toLowerCase().trim();
                if(search.isEmpty()){
                    songsTable.setItems(model.getSongList());
                    return;
                }
               ObservableList<Song> filtered = FXCollections.observableArrayList();

                for(Song s : model.getSongList()) {
                    if (s.getTitle().toLowerCase().contains(search) || (s.getArtist().toLowerCase().contains(search))){
                        filtered.add(s);
                    }
                songsTable.setItems(filtered);
                    searchButton.setText("Clear");
                    isFiltered = true;
            }

    }
    @FXML
    private void onPreviousSongButtonAction(ActionEvent actionEvent) {
        if (mp == null) return;

        Duration currentTime = mp.getCurrentTime();
        Duration rewindTime = currentTime.subtract(Duration.seconds(5));

        if (rewindTime.lessThan(Duration.ZERO)) {
            rewindTime = Duration.ZERO;
        }

        mp.seek(rewindTime);
    }

    @FXML
    private void onPlayPauseButtonAction(ActionEvent actionEvent) {

        if (lastSelectedSong == null) {
            AlertHelper.showInfo("Please select a song to play");
            return;
        }

        // Decide which list we are playing from
        if (playlistSongsTable.getSelectionModel().getSelectedItem() != null) {
            currentPlayQueue =model.getPlaylistSongList();
        } else {
            currentPlayQueue = songsTable.getItems();
        }

        currentPlayIndex = currentPlayQueue.indexOf(lastSelectedSong);

        handlePlayPause(lastSelectedSong);
    }

    @FXML
    private void onNextSongButtonAction(ActionEvent actionEvent) {
        if (mp == null) return;

        Duration currentTime = mp.getCurrentTime();
        Duration totalDuration = mp.getTotalDuration();
        Duration forwardTime = currentTime.add(Duration.seconds(5));

        if (forwardTime.greaterThan(totalDuration)) {
            forwardTime = totalDuration;
        }

        mp.seek(forwardTime);
    }

    private void handlePlayPause(Song selectedSong) {
        if(currentlyPlayingSong != null && selectedSong.getId() == currentlyPlayingSong.getId()) {
            if (mp.getStatus() == MediaPlayer.Status.PLAYING) {
                mp.pause();
            } else  {
                mp.play();
            }
        }else
            playSong(selectedSong);

    }


    private void playSong(Song song) {
        if (mp != null) {
            mp.stop();
        }

        try {
            File file = new File(song.getFilePath());
            Media media = new Media(file.toURI().toString());
            mp = new MediaPlayer(media);
            mp.setVolume(volumeSlider.getValue());
            mp.setOnEndOfMedia(this::playNextSong);
            mp.play();

            timeSlider.setDisable(false);
            timeSlider.setValue(0);

            mp.setOnReady(() -> {
                Duration total = mp.getMedia().getDuration();
                timeSlider.setMax(total.toSeconds());
            });

            mp.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
                if (!timeSlider.isValueChanging()) {
                    timeSlider.setValue(newTime.toSeconds());
                }
            });


            currentlyPlayingSong = song;
            lblCurrentSong.setText(song.getTitle());



        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showError("Could not play song " + song.getTitle() + "\n" + e.getMessage());
        }
    }

    private void playNextSong() {

        if (currentPlayQueue == null || currentPlayQueue.isEmpty()) {
            return;
        }

        //try to find the index of playing song dynamically
        int currentPlayIndex = currentPlayQueue.indexOf(currentlyPlayingSong);

        // if song is removed or not in the playlist anymore
        if ( currentPlayIndex == -1){
            return;
        }

        int nextIndex = currentPlayIndex + 1;
        //stop at the end of loop
        if (nextIndex >= currentPlayQueue.size()) {
            return;
        }
        Song nextSong = currentPlayQueue.get(nextIndex);

        // Update UI selection
        if (currentPlayQueue == model.getPlaylistSongList()) {
            playlistSongsTable.getSelectionModel().select(nextSong);
        } else {
            songsTable.getSelectionModel().select(nextSong);
        }

        playSong(nextSong);
    }


    @FXML
    private void onEditSongButtonAction(ActionEvent actionEvent) throws IOException {
        Song selected = model.getSelectedSong();

        if (selected == null) {
            AlertHelper.showError("Please select a song to edit ");
            return;
        }
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/SongEditWindow.fxml"));
            Parent root = fxmlLoader.load();
            SongEditController controller = fxmlLoader.getController();
            controller.setModel(this.model);
            controller.setSaveButtonLabel("Edit");
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("Edit Song");
            stage.setScene(scene);
            stage.show();
        }

    @FXML
    private void onDeleteSongAction(ActionEvent actionEvent) {
        Song lastSelectedSong = songsTable.getSelectionModel().getSelectedItem();


        if (lastSelectedSong == null) {
            AlertHelper.showError("Please select a song to delete.");
            return;
        }


        AlertHelper.showConfirmation(lastSelectedSong.getTitle()).ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    model.removeSong(lastSelectedSong);
                } catch (Exception e) {
                    AlertHelper.showError(
                            "Could not delete the selected song.\n" + e.getMessage()
                    );
                }
            }
        });
    }

    @FXML
    private void onDeleteSongFromPlaylistAction(ActionEvent actionEvent) {
        Song selectedSong = playlistSongsTable.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = playlistsTable.getSelectionModel().getSelectedItem();

        if (selectedSong == null || selectedPlaylist == null) {
            AlertHelper.showError("Please select a song and playlist to delete from the playlist.");
            return;}

        AlertHelper.showConfirmation(lastSelectedSong.getTitle()).ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    model.removeSongFromPlaylist(selectedSong, selectedPlaylist);
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertHelper.showError("Could not delete the selected song.\n" + e.getMessage());
                }

            }
        });

    }

    @FXML
    private void moveSong(int direction) {
        int index = playlistSongsTable.getSelectionModel().getSelectedIndex();

        // Check if index is valid and within bounds
        if (index < 0 || index + direction < 0 || index + direction >= model.getPlaylistSongList().size()) {
            return;
        }

        // Swap the current song with the one above or below
        Collections.swap(model.getPlaylistSongList(), index, index + direction);

        // Update selection to follow the moved song
        playlistSongsTable.getSelectionModel().select(index + direction);
    }

    @FXML
    private void onUpButtonAction(ActionEvent actionEvent) {
        moveSong(-1); // Move up
    }

    @FXML
    private void onDownButtonAction(ActionEvent actionEvent) {
        moveSong(1); // Move down
    }


    @FXML
    private void onNewButtonPlaylistAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/PlaylistEditWindow.fxml"));
        Parent root = fxmlLoader.load();
        PlaylistEditController controller = fxmlLoader.getController();
        controller.setModel(this.model);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("New Playlist");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void onEditButtonPlaylistAction(ActionEvent actionEvent) throws IOException {
        // get selected playlist
        Playlist selectedPlaylist = playlistsTable.getSelectionModel().getSelectedItem();
        if (selectedPlaylist == null) {
            AlertHelper.showError("Please select a playlist.");
            return;
        }
        // load the edit window
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/PlaylistEditWindow.fxml"));
        Parent root = fxmlLoader.load();

        // give controller the playlist
        PlaylistEditController controller = fxmlLoader.getController();
        controller.setModel(this.model);
        //shows the window
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Edit Playlist");
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void onDeleteButtonPlaylistAction(ActionEvent actionEvent) {
        Playlist selectedPlaylist = playlistsTable.getSelectionModel().getSelectedItem();
        if (selectedPlaylist == null) {
            AlertHelper.showError("Please select a playlist to delete.");
            return;
        }
        AlertHelper.showConfirmation(selectedPlaylist.getName()).ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    model.deletePlaylist(selectedPlaylist);

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertHelper.showError("Could not delete the selected playlist.\n" + e.getMessage());
                }

            }
        });
    }
    @FXML
    private void onCloseButtonAction(ActionEvent actionEvent) {
        javafx.application.Platform.exit();
    }

    public void onAddSongToPlaylistButtonAction(ActionEvent actionEvent) throws LogicException {
        Song lastSelectedSong = model.getSelectedSong();
        Playlist lastSelectedPlaylist = playlistsTable.getSelectionModel().getSelectedItem();

        if (lastSelectedSong == null || lastSelectedPlaylist == null){
            System.out.println("Select both song and playlist to add song to the playlist.");
            return;
        }
        try {
            model.addSongToPlaylist(lastSelectedSong, lastSelectedPlaylist);
        }catch (LogicException e) {
            AlertHelper.showError(e.getMessage());
        }
    }


}

