package dk.easv.cs5.mytunes.dal.DAO;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.cs5.mytunes.be.Genre;
import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.dal.ConnectionManager;
import dk.easv.cs5.mytunes.dal.DAOInterface.IGenreDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO implements IGenreDAO {
    public void save(Genre genre) {

    }
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM Genre";
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                genres.add(new Genre(rs.getInt("id"), rs.getString("name")));
            }

        }catch (SQLServerException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return genres;
    }
    }
