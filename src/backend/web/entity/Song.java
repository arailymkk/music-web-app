package backend.web.entity;

import java.util.Date;

public class Song {
    private int id;
    private String name;
    private String singer;
    private Date releaseDate;
    private Genre genre;

    public Song(int id, String name, String singer, Date releaseDate, Genre genre) {
        this.name = name;
        this.singer = singer;
        this.releaseDate = releaseDate;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Song () {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", singer='" + singer + '\'' +
                ", releaseDate=" + releaseDate +
                ", genre=" + genre +
                '}';
    }
}
