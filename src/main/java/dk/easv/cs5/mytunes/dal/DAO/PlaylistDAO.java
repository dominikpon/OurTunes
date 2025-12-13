package dk.easv.cs5.mytunes.dal.DAO;

import dk.easv.cs5.mytunes.be.Genre;
import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.be.Song;
import dk.easv.cs5.mytunes.dal.ConnectionManager;
import dk.easv.cs5.mytunes.dal.DAOInterface.IPlaylistDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO implements IPlaylistDAO {

    private Connection getConnection() throws SQLException {
        return ConnectionManager.getConnection();
    }

    public PlaylistDAO() {
    }


    @Override
    public void save(Playlist playlist) throws SQLException {
        String sql = "INSERT INTO Playlists (name) VALUES (?)";

        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, playlist.getName());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void edit(Playlist playlist) {

    }

    @Override
    public void remove(int playlistId) {

    }


    @Override
    public void addSongToPlaylist(int songId, int playlistId) {
        String sql = "INSERT INTO PlaylistSongs (songId, playlistId) VALUES (?, ?)"; // ????????
                                                            //id does not work when order matches with indexes

        try{ Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql); {

                ps.setInt(1, playlistId);           //??????????
                ps.setInt(2, songId);       //??????????
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteSongFromPlaylist(int songId, int playlistId) {
        String sql = "DELETE FROM PlaylistSongs WHERE songId = ? and playlistId = ?";

        try {
            Connection conn = getConnection();
                    PreparedStatement ps = conn.prepareStatement(sql);
                    {
                        ps.setInt(1, songId);
                        ps.setInt(2, playlistId);
                        ps.executeUpdate();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Error deleting playlist song");
        }
    }

    @Override
    public void moveSongInPlaylist(int songId, int playlistId) {
    }
    @Override
    public List<Playlist> getAllPlaylists(){
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM Playlists";

        try (
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Playlist playlist = new Playlist(id, name);     //creates a new instance of playlist

                try {
                    List<Song> songs = getAllSongsForPlaylist(id);
                    playlist.setSongList(songs);            //set songs list before choosing the playlist

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                playlists.add(playlist);
            }
            } catch (SQLException e) {
            e.printStackTrace();}
        return playlists;
        }

    public List<Song> getAllSongsForPlaylist(int playlistId) throws SQLException {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT s.id,s.title, s.artist, s.duration, s.filePath, g.name AS genre " +
                    "FROM Songs s " +
                    "JOIN PlaylistSongs ps ON s.id = ps.songId " +
                    "LEFT JOIN Genres g ON s.genreId = g.id " +
                    "WHERE ps.playlistId = ?";

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                songs.add(new Song(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("artist"),
                        new Genre(rs.getString("genre")),
                        rs.getInt("duration"),
                        rs.getString("filePath")
                ));
            }
        }
        return songs;

    }
}

