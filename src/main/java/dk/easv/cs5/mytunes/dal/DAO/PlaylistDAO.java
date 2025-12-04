package dk.easv.cs5.mytunes.dal.DAO;

import dk.easv.cs5.mytunes.be.Playlist;
import dk.easv.cs5.mytunes.dal.ConnectionManager;
import dk.easv.cs5.mytunes.dal.DAOInterface.IPlaylistDAO;

import java.io.IOException;

public class PlaylistDAO implements IPlaylistDAO {

    public PlaylistDAO(){
    }


    @Override
    public void save(Playlist playlist) {


    }

    @Override
    public void edit(Playlist playlist) {

    }

    @Override
    public void remove(int playlistId){        // helper

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
