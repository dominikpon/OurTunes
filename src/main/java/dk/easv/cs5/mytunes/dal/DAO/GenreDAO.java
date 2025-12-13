package dk.easv.cs5.mytunes.dal.DAO;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.cs5.mytunes.be.Genre;
import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.dal.ConnectionManager;
import dk.easv.cs5.mytunes.dal.DAOInterface.IGenreDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO implements IGenreDAO {

    private Connection getConnection() throws SQLException {
        return ConnectionManager.getConnection();
    }
    public void save(Genre genre) throws SQLException {
        String sql = "INSERT INTO Genres (name) VALUES (?)";

        try( Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, genre.getName());
            stmt.executeUpdate();
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                genre.setId(generatedId);
            }
        }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM Genres";
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
