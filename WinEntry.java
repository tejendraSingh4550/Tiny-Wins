import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WinEntry {
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("MMM d, yyyy  h:mm a");

    private final int id;
    private String title;
    private String note;
    private Mood mood;
    private LocalDateTime savedAt;

    public WinEntry(int id, String title, String note, Mood mood) {
        this.id = id;
        update(title, note, mood);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getNote() { return note; }
    public Mood getMood() { return mood; }
    public LocalDateTime getSavedAt() { return savedAt; }

    public void update(String title, String note, Mood mood) {
        this.title = title;
        this.note = note;
        this.mood = mood;
        this.savedAt = LocalDateTime.now();
    }

    public String getFormattedTime() {
        return savedAt.format(TIME_FORMAT);
    }
}
