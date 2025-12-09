package dk.easv.cs5.mytunes.dal.DAOInterface;

import dk.easv.cs5.mytunes.be.Playlist;

import java.sql.SQLException;

public interface IPlaylistDAO  {

    public void save(Playlist playlist) throws SQLException;
    public void edit(Playlist playlist);
    public void remove(int playlistId);

    public void saveSong(int songId, int playlistId);

    public void deleteSong(int songId, int playlistId);

    public void moveSong(int songId, int playlistId);


}
