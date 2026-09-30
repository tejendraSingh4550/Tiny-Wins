public enum Mood {
    PROUD("Proud"),
    PEACEFUL("Peaceful"),
    GRATEFUL("Grateful"),
    BRAVE("Brave"),
    JOYFUL("Joyful");

    private final String label;

    Mood(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
