package dk.easv.cs5.mytunes.be;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private int duration;
    private List<Song> songList = new ArrayList<Song>();

    public Playlist(String name) {
        this.id = 0; //placeholder for new playlist
        this.name = name;
    }

    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Playlist(int id, String name, List<Song> songList, int duration) {}

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }
}
