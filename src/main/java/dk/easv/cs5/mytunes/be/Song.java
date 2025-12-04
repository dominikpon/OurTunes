package dk.easv.cs5.mytunes.be;

public class Song {
    private int id;
    private String title;
    private String artist;
    private Genre genre;
    private int duration;
    private String filePath;

    //constructor without auto increment ID from DB
    public Song(String title, String artist, Genre genre, int duration, String filePath) {
        this.id = 0; //placeholder for ID from DB
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.filePath = filePath;
    }
    //constructor with auto increment ID from DB
    public Song(int id, String title, String artist, Genre genre, int duration, String filePath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
        this.filePath = filePath;
    }

    public Song(String title) {
        this.title = title;

    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public Genre getGenre() {
        return genre;
    }

    private String genre() {
        return genre.getName();
    }


    public int getDuration() {
        return duration;
    }
    public String getFormattedDuration() {  // Converting and formatting duration
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String toString() {
        return "ID: "+id+" Title: "+title+" Artist: "+artist+" Genre: "+genre +" Duration: "+getFormattedDuration() + "FIlePath: "+filePath;
    }
}
