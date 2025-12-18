package dk.easv.cs5.mytunes.gui;

import dk.easv.cs5.mytunes.Application;
import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.be.Song;
import dk.easv.cs5.mytunes.bll.ILogic;
import dk.easv.cs5.mytunes.bll.Logic;
import dk.easv.cs5.mytunes.bll.exceptions.LogicException;
import dk.easv.cs5.mytunes.bll.tools.FormattingTool;
import dk.easv.cs5.mytunes.gui.helpers.AlertHelper;
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
    private ObservableList<Song> songList = FXCollections.observableArrayList();
    private ObservableList<Playlist> playlistList = FXCollections.observableArrayList();
    private ObservableList<Song> playlistSongList = FXCollections.observableArrayList();

    private Song lastSelectedSong;
    private Playlist lastSelectedPlaylist;


    private ILogic logic = new Logic();

    private MediaPlayer mp;
    private Song currentlyPlayingSong;

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

        songsTable.setItems(songList);
        playlistsTable.setItems(playlistList);
        playlistSongsTable.setItems(playlistSongList);


        //Tracking the last selected playlist
        trackLastSelectedPlaylist();
        //Tracking the last selected song
        trackSongSelection();



        //Load songs from database
        songList.setAll(logic.getAllSongs());
        //Load playlists from database
        playlistList.setAll(logic.getAllPlaylists());
        //Load songs in playlist from database

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
                lastSelectedPlaylist = newValue;
                System.out.println("Selected playlist: " + newValue.getName());
                loadSongsForPlaylist(newValue.getId());
            }

        });

    }

    private void trackSongSelection() {
        //Tracking the last selected song firstly in Song table
        songsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                {
                    if (newValue != null) {
                        playlistSongsTable.getSelectionModel().clearSelection();
                        lastSelectedSong = newValue;
                        System.out.println("Selected song: " + newValue);
                    }
                }
        );
        //Tracking the last selected song in Song table in playlist
        playlistSongsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue != null) {
                songsTable.getSelectionModel().clearSelection();
                lastSelectedSong = newValue;
                System.out.println("Selected song: " + newValue);
            }
        });
    }

    private void loadSongsForPlaylist(int playlistId){
        try {
            ObservableList <Song> songs = FXCollections.observableArrayList(
            logic.getAllSongsInPlaylist(playlistId));
            lastSelectedPlaylist.setSongList(songs);
            playlistSongList.setAll(logic.getAllSongsInPlaylist(playlistId));
            playlistSongsTable.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showInfo("Could not load songs for playlist " + playlistId + "\n" + e.getMessage());
        }
    }


    @FXML
    private void onNewSongButtonAction(ActionEvent actionEvent) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/SongEditWindow.fxml"));
            Parent root = fxmlLoader.load();
            SongEditController controller = fxmlLoader.getController();
            controller.setSongList(songList);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("New/Edit Song");
            stage.setScene(scene);
            stage.show();
        }

    private boolean isFiltered;

    private void clearFilter() {
        songsTable.setItems(songList);
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
                    songsTable.setItems(songList);
                    return;
                }
               ObservableList<Song> filtered = FXCollections.observableArrayList();

                for(Song s : songList) {
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
        }

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
    @FXML
    private void onEditSongButtonAction(ActionEvent actionEvent) throws IOException {
        if (lastSelectedSong != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("gui/SongEditWindow.fxml"));
            Parent root = fxmlLoader.load();
            SongEditController controller = fxmlLoader.getController();
            controller.setSongToEdit(lastSelectedSong);
            controller.setSongList(songList);
            controller.setSaveButtonLabel("Edit");
            //controller.setEditMode(true);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("New/Edit Song");
            stage.setScene(scene);
            stage.show();
        }
        else  {
            AlertHelper.showInfo("Please select a song to edit.");
        }

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
                    // 1) Delete from database
                    logic.deleteSong(lastSelectedSong);

                    // 2) Remove from main song list
                    songList.remove(lastSelectedSong);
                    songsTable.refresh();

                    // 3) Remove from playlist table (if it's there)
                    playlistSongList.removeIf(s -> s.getId() == lastSelectedSong.getId());
                    playlistSongsTable.refresh();
                    if(lastSelectedPlaylist != null) {
                    lastSelectedPlaylist.setSongList(playlistSongList);}
                    playlistsTable.refresh();

                } catch (Exception e) {
                    e.printStackTrace();
                    AlertHelper.showError(
                            "Could not delete the selected song.\n" + e.getMessage()
                    );
                }
            }
        });
    }

    @FXML
    private void onDeleteSongFromPlaylistAction(ActionEvent actionEvent) {
        Song lastSelectedSong = playlistSongsTable.getSelectionModel().getSelectedItem();
        Playlist lastSelectedPlaylist = playlistsTable.getSelectionModel().getSelectedItem();

        if (lastSelectedSong == null) {
            AlertHelper.showError("Please select a song to delete from the playlist.");
            return;}

        if (lastSelectedPlaylist == null) {
            AlertHelper.showError("Please select a playlist.");
            return;
        }
        AlertHelper.showConfirmation(lastSelectedSong.getTitle()).ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    logic.removeSongFromPlaylist(lastSelectedSong,lastSelectedPlaylist);
                    playlistSongList.removeIf(s -> s.getId() == lastSelectedSong.getId());
                    playlistSongsTable.refresh();

                    lastSelectedPlaylist.setSongList(playlistSongList);
                    playlistsTable.refresh();
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
        if (index < 0 || index + direction < 0 || index + direction >= playlistSongList.size()) {
            return;
        }

        // Swap the current song with the one above or below
        Collections.swap(playlistSongList, index, index + direction);

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
        controller.setPlaylistList(playlistList);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("New/Edit Playlist");
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
        controller.setPlaylistList(playlistList);
        controller.setPlaylistToEdit(selectedPlaylist);
        //shows the window
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("New/Edit Playlist");
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void onDeleteButtonPlaylistAction(ActionEvent actionEvent) {
        Playlist lastSelectedPlaylist = playlistsTable.getSelectionModel().getSelectedItem();
        if (lastSelectedPlaylist == null) {
            AlertHelper.showError("Please select a playlist to delete.");
            return;
        }
        AlertHelper.showConfirmation(lastSelectedPlaylist.getName()).ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    logic.deletePlaylist(lastSelectedPlaylist);
                    playlistList.remove(lastSelectedPlaylist);
                    playlistSongList.remove(lastSelectedPlaylist);
                    playlistSongsTable.refresh();
                    playlistsTable.refresh();
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

    public void onAddSongToPlaylistButtonAction(ActionEvent actionEvent) {
        if (lastSelectedPlaylist == null){
            System.out.println("Select a playlist");
            return;
        }

        if (lastSelectedSong == null) {
            System.out.println("Select a song first");
        }

        boolean alreadyInPlaylist = playlistSongList.stream().anyMatch(s -> s.getId() == lastSelectedSong.getId());
            if(alreadyInPlaylist){
                AlertHelper.showError("Song " + lastSelectedSong.getTitle() + " already exists in playlist");
            return;}
        logic.addSongToPlaylist(lastSelectedSong, lastSelectedPlaylist);
        lastSelectedPlaylist.getSongs().add(lastSelectedSong);
        playlistSongList.add(lastSelectedSong);
        playlistsTable.refresh();

        System.out.println("Added: " + lastSelectedSong.getTitle()+ " to " + lastSelectedPlaylist.getName()); //If I try to add lastSelectedPlaylist.getName() it just shows ID of playlist
    }


    public void refreshSongsTable() {
        songsTable.refresh();
    }

}

