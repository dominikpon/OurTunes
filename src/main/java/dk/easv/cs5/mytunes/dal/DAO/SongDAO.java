package dk.easv.cs5.mytunes.dal.DAO;

import dk.easv.cs5.mytunes.be.Genre;
import dk.easv.cs5.mytunes.be.Song;
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
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString (1, song.getTitle());
            ps.setString (2, song.getArtist());
            ps.setInt (3, song.getGenre().getId());
            ps.setInt (4, song.getDuration());
            ps.setString (5, song.getFilePath());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()){
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    song.setId(generatedId);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    @Override
    public void edit(Song song) {

    }

    @Override
    public void remove(int id) {
        String deleteFromPlaylistSongs = "DELETE FROM PlaylistSongs WHERE songId = ?";
        String deleteFromSongs = "DELETE FROM Songs WHERE Id = ?";

        try(Connection conn = getConnection()) {

    //Remove songs from PlaylistSongs
    try (PreparedStatement ps1 = conn.prepareStatement(deleteFromPlaylistSongs)){
            ps1.setInt(1, id);
            ps1.executeUpdate();
     }
     try (PreparedStatement ps2 = conn.prepareStatement(deleteFromSongs)){
            ps2.setInt(1,id);
            ps2.executeUpdate();
            }
        }catch (SQLException e){
            throw new RuntimeException("Song could not be deleted", e);
        }
        }

    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.id, s.title,s.artist,g.name as genre ,s.duration, s.filePath " +
                "FROM Songs s " +
                "LEFT JOIN Genres g ON s.genreId = g.id";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

                while (rs.next())
                {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String artist = rs.getString("artist");
                    String genreName = rs.getString("genre");
                    int duration = rs.getInt("duration");
                    String filePath = rs.getString("filePath");

                    Genre genre = new Genre(genreName);

                    Song song = new Song(id,title, artist, genre, duration,filePath);
                    songs.add(song);
                }
            }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return songs;
    }
}
