package dk.easv.cs5.mytunes.dal.DAOInterface;

import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.be.Song;
import dk.easv.cs5.mytunes.bll.exceptions.DuplicateSongException;

import java.sql.SQLException;
import java.util.List;

public interface ISongDAO {
    public void save(Song song) throws SQLException, DuplicateSongException;
    public void edit(Song song) throws DuplicateSongException;
    public void remove(int songId);
    public List<Song> getAllSongs();
}
