package backend.web.entity;

public enum Gender {
    F("female"), M("male");
    private String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
