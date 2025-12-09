package dk.easv.cs5.mytunes.dal.DAOInterface;

import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.be.Song;

import java.sql.SQLException;
import java.util.List;

public interface ISongDAO {
    public void save(Song song) throws SQLException;
    public void edit(Song song);
    public void remove(int songId);
    public List<Song> getAllSongs();
}
