package dk.easv.cs5.mytunes.bll;

import dk.easv.cs5.mytunes.be.Genre;
import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.be.Song;

import java.util.List;

public interface ILogic {
    public void createSong(Song song);
    public void deleteSong(Song song);
    public void editSong(Song song);

    public void createPlaylist(Playlist playlist);
    public void deletePlaylist(Playlist playlist);
    public void editPlaylist(Playlist playlist);

    public void addSongToPlaylist(Song song, Playlist playlist);
    public void removeSongFromPlaylist(Song song,  Playlist playlist);
    public void moveSongInPlaylist(Song song, Playlist playlist);

    List<Genre> getAllGenres();     //List of genres for populating comboBox and filtering
}
