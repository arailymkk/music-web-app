package backend.web.entity;

public enum Genre {
    POP("pop"),
    ROCK("rock"),
    RNB("rnb"),
    COUNTRY("country"),
    HIPHOP("hiphop"),
    JAZZ("jazz");

    private String type;

    Genre(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
