package dk.easv.cs5.mytunes.dal.DAO;

import dk.easv.cs5.mytunes.be.Song;
import dk.easv.cs5.mytunes.bll.ILogic;
import dk.easv.cs5.mytunes.bll.Logic;
import dk.easv.cs5.mytunes.dal.ConnectionManager;
import dk.easv.cs5.mytunes.dal.DAOInterface.ISongDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongDAO implements ISongDAO  {

    private Connection getConnection() throws SQLException {
        return ConnectionManager.getConnection();
    }

    @Override
    public void save(Song song) throws SQLException{
        String sql = "INSERT INTO Songs (title, artist, genreId, duration, filePath ) VALUES (?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString (1, song.getTitle());
            ps.setString (2, song.getArtist());
            ps.setInt (3, song.getGenre().getId());
            ps.setInt (4, song.getDuration());
            ps.setString (5, song.getFilePath());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void edit(Song song) {

    }

    @Override
    public void remove(int id) {

    }

    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.title,s.artist,g.name as genre ,s.duration " +
                "FROM Songs s " +
                "LEFT JOIN Genres g ON s.genreId = g.id";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            {
                while (rs.next())
                {
                    String title = rs.getString("title");
                    String artist = rs.getString("artist");
                    String genreName = rs.getString("genre");
                    int duration = rs.getInt("duration");

                    Song song = new Song(title, artist, genreName, duration);
                    songs.add(song);
                }
            }
            }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return songs;
    }
}
