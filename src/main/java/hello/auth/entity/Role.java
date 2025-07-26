package hello.auth.entity;

public enum Role {
    USER("USER"),
    ADMIN("Admin");

    private final String value;

    Role(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
