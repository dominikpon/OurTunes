package dk.easv.cs5.mytunes.dal.DAOInterface;

import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.be.Song;

import java.sql.SQLException;
import java.util.List;

public interface IPlaylistDAO  {

    public void save(Playlist playlist) throws SQLException;
    public void edit(Playlist playlist);
    public void remove(int id);

    public void addSongToPlaylist(int songId, int playlistId);
    public void deleteSongFromPlaylist(int songId, int playlistId) throws SQLException;
    public void moveSongInPlaylist(int songId, int playlistId);


    public List<Playlist> getAllPlaylists();
    public List<Song> getAllSongsForPlaylist(int playlistId) throws SQLException;
}
