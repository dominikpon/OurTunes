package dk.easv.cs5.mytunes.be;

public class Genre {
    private int id;
    private String name;

    public Genre(String name) {
        this.id = 0; //placeholder for ID from DB
        this.name = name;
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {      //for future editing
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }

}
