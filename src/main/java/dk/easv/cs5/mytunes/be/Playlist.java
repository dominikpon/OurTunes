package dk.easv.cs5.mytunes.be;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private int duration;
    private List<Song> songList = new ArrayList<>();

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

    public int getDuration() {
        //dynamically will calculate duration when adding/removing song from playlist
        return songList.stream().mapToInt(Song::getDuration).sum();
    }

    public List<Song> getSongs() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }
}
