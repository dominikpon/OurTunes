package dk.easv.cs5.mytunes.bll;

import dk.easv.cs5.mytunes.be.Genre;
import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.be.Song;
import dk.easv.cs5.mytunes.bll.exceptions.DuplicateSongException;
import dk.easv.cs5.mytunes.bll.exceptions.LogicException;

import java.sql.SQLException;
import java.util.List;

public interface ILogic {
    public void createSong(Song song) throws LogicException;
    public void deleteSong(Song song);
    public void editSong(Song song) throws LogicException, DuplicateSongException;

    public void createGenre(Genre genre) throws LogicException;

    public void createPlaylist(Playlist playlist) throws SQLException, LogicException;
    public void deletePlaylist(Playlist playlist);
    public void editPlaylist(Playlist playlist);

    public void addSongToPlaylist(Song song, Playlist playlist);
    public void removeSongFromPlaylist(Song song,  Playlist playlist) throws SQLException;
    public void moveSongInPlaylist(Song song, Playlist playlist);

    List<Genre> getAllGenres();     //List of genres for populating comboBox and filtering
    List<Song> getAllSongs();
    public List<Song> getAllSongsInPlaylist(int playlistId);
    List<Playlist> getAllPlaylists()throws LogicException;

}
