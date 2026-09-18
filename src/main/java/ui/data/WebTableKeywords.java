package ui.data;

public enum WebTableKeywords {
    COLUMN_AGE("Age");

    private final String value;

    WebTableKeywords(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
