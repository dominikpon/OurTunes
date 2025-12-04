package dk.easv.cs5.mytunes.dal.DAO;

import dk.easv.cs5.mytunes.be.Song;
import dk.easv.cs5.mytunes.bll.ILogic;
import dk.easv.cs5.mytunes.bll.Logic;
import dk.easv.cs5.mytunes.dal.ConnectionManager;
import dk.easv.cs5.mytunes.dal.DAOInterface.ISongDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SongDAO implements ISongDAO {


    @Override
    public void save(Song song) {
        String sql = "INSERT INTO Song (title, artist, genreId, duration, filePath ) VALUES (?,?,?,?,?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString (1, song.getTitle());
            stmt.setString (2, song.getArtist());
            stmt.setInt (3, song.getGenre().getId());
            stmt.setInt (4, song.getDuration());
            stmt.setString (5, song.getFilePath());

            stmt.executeUpdate();

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

}
