package dk.easv.cs5.mytunes.bll;

import dk.easv.cs5.mytunes.be.Genre;
import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.be.Song;
import dk.easv.cs5.mytunes.bll.exceptions.DuplicateSongException;
import dk.easv.cs5.mytunes.bll.exceptions.LogicException;
import dk.easv.cs5.mytunes.bll.tools.FormattingTool;
import dk.easv.cs5.mytunes.dal.DAO.GenreDAO;
import dk.easv.cs5.mytunes.dal.DAO.PlaylistDAO;
import dk.easv.cs5.mytunes.dal.DAO.SongDAO;
import dk.easv.cs5.mytunes.dal.DAOInterface.IGenreDAO;
import dk.easv.cs5.mytunes.dal.DAOInterface.IPlaylistDAO;
import dk.easv.cs5.mytunes.dal.DAOInterface.ISongDAO;
import dk.easv.cs5.mytunes.gui.helpers.AlertHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Logic implements ILogic {
    private ISongDAO songDAO = new SongDAO();
    private IPlaylistDAO playlistDAO = new PlaylistDAO();
    private IGenreDAO genreDAO = new GenreDAO();
    private FormattingTool formattingTool = new FormattingTool();


    @Override
    public void createSong(Song song) throws LogicException {

        if (song.getTitle() == null) {
            throw new LogicException("Title is empty!");
        }

        if (song.getArtist() == null) {
            throw new LogicException("Artist is empty!");
        }

        if (song.getGenre() == null) {
            throw new LogicException("Genre is empty!");
        }

        if(song.getDuration()<=0){
            throw new LogicException("Duration is empty!");
        }

        if(song.getFilePath() == null  ){
            throw new LogicException("File path is empty!");
        }
        String path = song.getFilePath().toLowerCase();
        if(!path.endsWith(".mp3") && (path.endsWith(".wav")))  {
            throw new LogicException("File path must end with .wav or .mp3!");
        }


        try {
            songDAO.save(song);
        }catch (DuplicateSongException e){
            throw new LogicException("Song with this path already exists!",e);
            }catch (SQLException e){
                e.printStackTrace();
            throw new LogicException("Failed to save song!", e);
        }

    }

    @Override
    public void deleteSong(Song song)
    {
        if(song.getId() == 0){
            return;
        }
        songDAO.remove(song.getId());

    }

    @Override
    public void editSong(Song song) throws LogicException, DuplicateSongException {
        if (song.getTitle() == null) {
            throw new LogicException("Title is empty!");
        }

        if (song.getArtist() == null) {
            throw new LogicException("Artist is empty!");
        }

        if (song.getGenre() == null) {
            throw new LogicException("Genre is empty!");
        }

        if(song.getDuration()<=0){
            throw new LogicException("Duration is empty!");
        }

        if(song.getFilePath() == null  ){
            throw new LogicException("File path is empty!");
        }

        songDAO.edit(song);

    }
    @Override
    public void createGenre(Genre genre) throws LogicException {
        if(genre.getName() == null) {
            throw new LogicException("Genre is empty!");
        }
        try {genreDAO.save(genre);
        }catch (SQLException e){
            e.printStackTrace();
            throw new LogicException("Failed to save a genre");
        }

    }

    @Override
    public void createPlaylist(Playlist playlist) throws LogicException {

        if(playlist.getName() == null){
            throw new LogicException("Name is empty!");
        }

        try {
            playlistDAO.save(playlist);
        }catch (SQLException e){
            e.printStackTrace();
            throw new LogicException("Failed to save playlist",e );
        }

    }

    @Override
    public void deletePlaylist(Playlist playlist) {
        if(playlist.getId() == 0){
            return;
        }
        playlistDAO.remove(playlist.getId());

    }

    @Override
    public void editPlaylist(Playlist playlist) {
        if(playlist.getId() == 0){
            return;
        }

        playlistDAO.edit(playlist);

    }

    @Override
    public void addSongToPlaylist(Song song, Playlist playlist) {
        if(song == null || playlist == null) {
            return;
        }
        System.out.println("Adding song id=" + song.getId() + " to playlist id=" + playlist.getId());
        playlistDAO.addSongToPlaylist(playlist.getId(), song.getId());

    }

    @Override
    public void removeSongFromPlaylist(Song song, Playlist playlist) throws LogicException {
        int songId = song.getId();
        int playlistId = playlist.getId();
        if(song == null || playlist == null || songId == 0 || playlistId == 0){
            throw new LogicException("Invalid song or playlist ID");
        }
        try {
            playlistDAO.deleteSongFromPlaylist(songId, playlistId); //this
        }catch (SQLException e){
            throw new LogicException("Failed to remove song from playlist", e);
        }
    }

    @Override
    public void moveSongInPlaylist(Song song, Playlist playlist) {
        int songId = song.getId();
        int playlistId = playlist.getId();
        playlistDAO.moveSongInPlaylist(playlistId, songId);

    }

    public List<Genre> getAllGenres()  {
        return genreDAO.getAllGenres();
    }

    public List<Song> getAllSongs() {
        return songDAO.getAllSongs();
    }
    public List<Playlist> getAllPlaylists() throws LogicException {
        return playlistDAO.getAllPlaylists();
    }

    public List<Song> getAllSongsInPlaylist(int playlistId){
        try {
            return playlistDAO.getAllSongsForPlaylist(playlistId);
        }catch (SQLException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public String getFormattedDuration(Song song) {
        return formattingTool.format(song.getDuration());
    }

}
