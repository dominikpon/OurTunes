package dk.easv.cs5.mytunes.dal.DAO;

import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.dal.ConnectionManager;
import dk.easv.cs5.mytunes.dal.DAOInterface.IPlaylistDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO implements IPlaylistDAO {

    private Connection getConnection() throws SQLException {
        return ConnectionManager.getConnection();
}

    public PlaylistDAO(){
    }


    @Override
    public void save(Playlist playlist) throws SQLException {
        String sql = "INSERT INTO Playlists (name) VALUES (?)";

        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, playlist.getName());

            ps.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }


    }

    @Override
    public void edit(Playlist playlist) {

    }

    @Override
    public void remove(int playlistId){

    }



    @Override
    public void saveSong(int songId, int playlistId) {

    }

    @Override
    public void deleteSong(int songId, int playlistId) {

    }

    @Override
    public void moveSong(int songId, int playlistId) {
    }


}
