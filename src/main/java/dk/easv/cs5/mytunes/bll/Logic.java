package dk.easv.cs5.mytunes.bll;

import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.be.Song;
import dk.easv.cs5.mytunes.dal.DAO.PlaylistDAO;
import dk.easv.cs5.mytunes.dal.DAO.SongDAO;
import dk.easv.cs5.mytunes.dal.DAOInterface.IPlaylistDAO;
import dk.easv.cs5.mytunes.dal.DAOInterface.ISongDAO;

public class Logic implements ILogic {
    private final ISongDAO songDAO = new SongDAO();                 // Logic communicates with Interface ISongDAO
    private final IPlaylistDAO playlistDAO = new PlaylistDAO();     // -//- with IPlaylistDAO

    @Override
    public void createSong(Song song) {
        songDAO.save(song);


    }

    @Override
    public void deleteSong(Song song) {
        songDAO.remove(song.getId());

    }

    @Override
    public void editSong(Song song) {
        songDAO.edit(song);

    }

    @Override
    public void createPlaylist(Playlist playlist) {
        playlistDAO.save(playlist);

    }

    @Override
    public void deletePlaylist(Playlist playlist) {
        playlistDAO.remove(playlist.getId());

    }

    @Override
    public void editPlaylist(Playlist playlist) {
        playlistDAO.edit(playlist);

    }

    @Override
    public void addSongToPlaylist(Song song, Playlist playlist) {
        int songId = song.getId();
        int playlistId = playlist.getId();
        playlistDAO.saveSong(playlistId, songId);

    }

    @Override
    public void removeSongFromPlaylist(Song song, Playlist playlist) {
        int songId = song.getId();
        int playlistId = playlist.getId();
        playlistDAO.deleteSong(playlistId, songId);

    }

    @Override
    public void moveSongInPlaylist(Song song, Playlist playlist) {
        int songId = song.getId();
        int playlistId = playlist.getId();
        playlistDAO.moveSong(playlistId, songId);

    }

}
