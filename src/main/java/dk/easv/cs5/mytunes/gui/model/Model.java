package dk.easv.cs5.mytunes.gui.model;

import dk.easv.cs5.mytunes.be.Genre;
import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.be.Song;
import dk.easv.cs5.mytunes.bll.ILogic;
import dk.easv.cs5.mytunes.bll.exceptions.DuplicateSongException;
import dk.easv.cs5.mytunes.bll.exceptions.LogicException;
import dk.easv.cs5.mytunes.gui.helpers.AlertHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
    private ObservableList<Song> songList = FXCollections.observableArrayList();
    private ObservableList<Playlist> playlistList = FXCollections.observableArrayList();
    private ObservableList<Song> playlistSongList = FXCollections.observableArrayList();
    private ObservableList<Genre> genreList = FXCollections.observableArrayList();

    private ILogic logic;

    private Song selectedSong;

    public Model(ILogic logic) {
        this.logic = logic;
    }
    public ObservableList<Song> getSongList() {return songList;}
    public ObservableList<Playlist> getPlaylistList() {return playlistList;}
    public ObservableList<Song> getPlaylistSongList() {return playlistSongList;}
    public ObservableList<Genre> getGenreList() {
        return FXCollections.observableArrayList(logic.getAllGenres());
    }


    public Song getSelectedSong() {return selectedSong;}
    public void setSelectedSong(Song selectedSong) {this.selectedSong = selectedSong;}

    public void loadEverything() {
        try {
            songList.setAll(logic.getAllSongs());
            playlistList.setAll(logic.getAllPlaylists());
            genreList.setAll(logic.getAllGenres());
            //keep Songs in playlist table empty at start
            playlistSongList.clear();
        }catch (Exception e){
            AlertHelper.showError("Error while loading lists");
        }
    }

    public void loadSongsInPlaylist(Playlist selectedPlaylist) {
        try {
            if (selectedPlaylist != null){
            playlistSongList.setAll(logic.getAllSongsInPlaylist(selectedPlaylist.getId()));
        }}catch (Exception e){
            AlertHelper.showError("Error while loading songs in playlist");
        }
    }

    public void createSong(String title, String artist, Genre genre, int duration, String path) throws LogicException {
        Song newSong = new Song(title, artist, genre, duration, path);
        logic.createSong(newSong);
        songList.add(newSong);

        int index = songList.indexOf(newSong);
        if (index != -1) {
            songList.set(index, newSong);
        }

    }

    public void updateSong(Song song) throws DuplicateSongException, LogicException {
        logic.editSong(song);

        int index = songList.indexOf(song);
        if (index != -1) {
            songList.set(index, song);
        }

    }

    public void removeSong(Song songToDelete){
        //call method in logic
        logic.deleteSong(songToDelete);
        //remove song from songList
        songList.remove(songToDelete);

        //remove song from playlist
        playlistList.removeIf(s -> s.getId() == songToDelete.getId());

        //loop for checking every playlist to remove songToDelete
        for (Playlist playlist : playlistList){
            playlist.getSongs().removeIf(s -> s.getId() == songToDelete.getId());{}
        }
    }

    public void removeSongFromPlaylist(Song song, Playlist playlist) throws LogicException {
        logic.removeSongFromPlaylist(song,playlist);

        playlistSongList.remove(song);

        playlist.getSongs().remove(song);

        int index = playlistList.indexOf(playlist);
        playlistList.set(index, playlist);
    }

    public void deletePlaylist(Playlist playlistToDelete) throws LogicException {
        logic.deletePlaylist(playlistToDelete);

        playlistList.remove(playlistToDelete);

        playlistSongList.remove(playlistToDelete);
    }

    public void addSongToPlaylist(Song songToAdd ,Playlist playlist) throws LogicException {
        boolean alreadyInPlaylist = playlistList.stream().anyMatch(s -> s.getId() == songToAdd.getId());
        if(alreadyInPlaylist){
            throw new LogicException("The song already exists in playlist");
        }

        logic.addSongToPlaylist(songToAdd, playlist);

        playlist.getSongs().add(songToAdd);

        playlistSongList.add(songToAdd);

        //Refresh tables count column
        int index = playlistList.indexOf(playlist);
        playlistList.set(index, playlist);
    }
}



