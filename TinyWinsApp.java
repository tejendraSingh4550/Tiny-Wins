import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** TinyWins: a small, warm desktop jar for remembering everyday progress. */
public class TinyWinsApp extends JFrame {
    private static final int MAX_ENTRIES = 100;
    private static final Color INK = new Color(42, 43, 48);
    private static final Color PAPER = new Color(250, 247, 240);
    private static final Color CARD = Color.WHITE;
    private static final Color CORAL = new Color(235, 111, 86);
    private static final Color GOLD = new Color(244, 190, 90);

    private final WinEntry[] entries = new WinEntry[MAX_ENTRIES];
    private final DefaultListModel<WinEntry> listModel = new DefaultListModel<>();
    private final JList<WinEntry> entryList = new JList<>(listModel);
    private final JLabel countLabel = new JLabel("0");
    private final JLabel promptLabel = new JLabel("What felt a little easier today?");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Keep Swing's default look and feel if the system setting is unavailable.
            }
            new TinyWinsApp().setVisible(true);
        });
    }

    private TinyWinsApp() {
        super("TinyWins | A jar for the little things");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(850, 590));
        setSize(980, 680);
        setLocationRelativeTo(null);
        getContentPane().setBackground(PAPER);
        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PAPER);
        header.setBorder(BorderFactory.createEmptyBorder(26, 30, 16, 30));
        JPanel brand = new JPanel(new BorderLayout(0, 5));
        brand.setOpaque(false);
        JLabel eyebrow = label("A LITTLE PROGRESS COUNTS", 10, CORAL, true);
        JLabel title = label("TinyWins", 30, INK, true);
        JLabel subtitle = label("A jar for the things you almost forgot to celebrate.", 13,
                new Color(119, 116, 109), false);
        JPanel titles = new JPanel(new BorderLayout(0, 1));
        titles.setOpaque(false);
        titles.add(title, BorderLayout.NORTH);
        titles.add(subtitle, BorderLayout.SOUTH);
        brand.add(eyebrow, BorderLayout.NORTH);
        brand.add(titles, BorderLayout.CENTER);

        JPanel counter = new JPanel(new BorderLayout(8, 0));
        counter.setBackground(new Color(255, 236, 218));
        counter.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        JLabel count = label("0", 25, INK, true);
        countLabel.setText("0");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        countLabel.setForeground(INK);
        JPanel counterText = new JPanel(new BorderLayout(0, 2));
        counterText.setOpaque(false);
        counterText.add(label("IN YOUR JAR", 9, CORAL, true), BorderLayout.NORTH);
        counterText.add(countLabel, BorderLayout.CENTER);
        JLabel spark = label("✦", 25, CORAL, true);
        counter.add(spark, BorderLayout.WEST);
        counter.add(counterText, BorderLayout.CENTER);
        header.add(brand, BorderLayout.CENTER);
        header.add(counter, BorderLayout.EAST);
        return header;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel(new BorderLayout(18, 0));
        content.setBackground(PAPER);
        content.setBorder(BorderFactory.createEmptyBorder(0, 30, 24, 30));
        content.add(buildPromptCard(), BorderLayout.WEST);
        content.add(buildJournal(), BorderLayout.CENTER);
        return content;
    }

    private JPanel buildPromptCard() {
        JPanel sidebar = new JPanel(new BorderLayout(0, 14));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(255, 229, 198));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 18, 18, 18));
        JLabel jarIcon = label("✿", 34, CORAL, true);
        JLabel heading = label("Today's tiny prompt", 15, INK, true);
        promptLabel.setFont(new Font("Georgia", Font.ITALIC, 17));
        promptLabel.setForeground(new Color(93, 75, 66));
        promptLabel.setVerticalAlignment(SwingConstants.TOP);
        String[] prompts = {
                "What felt a little easier today?",
                "What did you do even though it was hard?",
                "Who made your day a bit brighter?",
                "What would yesterday-you be proud of?",
                "What small thing went right today?"
        };
        int promptIndex = java.time.LocalDate.now().getDayOfYear() % prompts.length;
        promptLabel.setText("<html>“" + prompts[promptIndex] + "”</html>");
        JLabel hint = label("No big milestones needed. Tiny counts.", 11,
                new Color(135, 105, 84), false);
        hint.setVerticalAlignment(SwingConstants.TOP);

        JButton addButton = button("＋   Save a tiny win", true);
        addButton.addActionListener(event -> addEntry());
        JPanel promptBody = new JPanel(new BorderLayout(0, 12));
        promptBody.setOpaque(false);
        promptBody.add(heading, BorderLayout.NORTH);
        JPanel words = new JPanel(new BorderLayout(0, 8));
        words.setOpaque(false);
        words.add(promptLabel, BorderLayout.NORTH);
        words.add(hint, BorderLayout.CENTER);
        promptBody.add(words, BorderLayout.CENTER);
        sidebar.add(jarIcon, BorderLayout.NORTH);
        sidebar.add(promptBody, BorderLayout.CENTER);
        sidebar.add(addButton, BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel buildJournal() {
        JPanel journal = new JPanel(new BorderLayout(0, 12));
        journal.setBackground(PAPER);
        JPanel section = new JPanel(new BorderLayout());
        section.setOpaque(false);
        section.add(label("YOUR LITTLE WINS", 11, INK, true), BorderLayout.WEST);
        JButton editButton = button("Edit", false);
        JButton deleteButton = button("Remove", false);
        JPanel controls = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 7, 0));
        controls.setOpaque(false);
        controls.add(editButton);
        controls.add(deleteButton);
        section.add(controls, BorderLayout.EAST);

        entryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        entryList.setBackground(PAPER);
        entryList.setFixedCellHeight(92);
        entryList.setCellRenderer(new WinRenderer());
        JScrollPane scroll = new JScrollPane(entryList);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(PAPER);
        editButton.addActionListener(event -> editEntry());
        deleteButton.addActionListener(event -> deleteEntry());
        journal.add(section, BorderLayout.NORTH);
        journal.add(scroll, BorderLayout.CENTER);
        return journal;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(246, 239, 226));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        footer.add(label("MADE FOR THE SMALL WINS", 9, new Color(139, 126, 111), true), BorderLayout.WEST);
        footer.add(label("Saved only while this app is open", 10,
                new Color(139, 126, 111), false), BorderLayout.EAST);
        return footer;
    }

    private JButton button(String text, boolean primary) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBorder(BorderFactory.createEmptyBorder(10, 13, 10, 13));
        button.setOpaque(true);
        button.setBackground(primary ? CORAL : CARD);
        button.setForeground(primary ? Color.WHITE : INK);
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }

    private JLabel label(String text, int size, Color color, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(color);
        return label;
    }

    private void addEntry() {
        int empty = findEmptyIndex();
        if (empty == -1) {
            message("Your jar is full. Remove a win before adding another.", "Jar is full");
            return;
        }
        EntryForm form = new EntryForm(this, null);
        if (form.open()) {
            entries[empty] = new WinEntry(nextId(), form.title(), form.note(), form.mood());
            refreshList();
        }
    }

    private void editEntry() {
        int index = selectedEntryIndex();
        if (index < 0) return;
        EntryForm form = new EntryForm(this, entries[index]);
        if (form.open()) {
            entries[index].update(form.title(), form.note(), form.mood());
            refreshList();
        }
    }

    private void deleteEntry() {
        int index = selectedEntryIndex();
        if (index < 0) return;
        int answer = JOptionPane.showConfirmDialog(this,
                "Let this little win go?\n\n“" + entries[index].getTitle() + "”",
                "Remove from your jar", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            entries[index] = null;
            refreshList();
        }
    }

    private int selectedEntryIndex() {
        WinEntry selected = entryList.getSelectedValue();
        if (selected == null) {
            message("Choose a win from your jar first.", "Nothing selected");
            return -1;
        }
        for (int i = 0; i < entries.length; i++) {
            if (entries[i] != null && entries[i].getId() == selected.getId()) return i;
        }
        return -1;
    }

    private void refreshList() {
        listModel.clear();
        int count = 0;
        for (WinEntry entry : entries) {
            if (entry != null) {
                listModel.addElement(entry);
                count++;
            }
        }
        countLabel.setText(String.valueOf(count));
    }

    private int findEmptyIndex() {
        for (int i = 0; i < entries.length; i++) {
            if (entries[i] == null) return i;
        }
        return -1;
    }

    private int nextId() {
        int highest = 1000;
        for (WinEntry entry : entries) {
            if (entry != null && entry.getId() > highest) highest = entry.getId();
        }
        return highest + 1;
    }

    private void message(String text, String title) {
        JOptionPane.showMessageDialog(this, text, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private static class EntryForm {
        private final JTextField titleField = new JTextField(24);
        private final JTextArea noteArea = new JTextArea(4, 24);
        private final JComboBox<Mood> moodBox = new JComboBox<>(Mood.values());
        private final Component parent;

        EntryForm(Component parent, WinEntry existing) {
            this.parent = parent;
            noteArea.setLineWrap(true);
            noteArea.setWrapStyleWord(true);
            if (existing != null) {
                titleField.setText(existing.getTitle());
                noteArea.setText(existing.getNote());
                moodBox.setSelectedItem(existing.getMood());
            }
        }

        boolean open() {
            JPanel form = new JPanel(new GridLayout(0, 1, 0, 6));
            form.setBorder(BorderFactory.createEmptyBorder(5, 3, 5, 3));
            form.add(new JLabel("Your tiny win"));
            form.add(titleField);
            form.add(new JLabel("A few details (optional)"));
            form.add(new JScrollPane(noteArea));
            form.add(new JLabel("How did it feel?"));
            form.add(moodBox);
            while (true) {
                int result = JOptionPane.showConfirmDialog(parent, form, "Save a tiny win",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result != JOptionPane.OK_OPTION) return false;
                if (!titleField.getText().trim().isEmpty()) return true;
                JOptionPane.showMessageDialog(parent, "Give your win a short title first.",
                        "A tiny detail", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        String title() { return titleField.getText().trim(); }
        String note() { return noteArea.getText().trim(); }
        Mood mood() { return (Mood) moodBox.getSelectedItem(); }
    }

    private static class WinRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean selected, boolean focus) {
            WinEntry entry = (WinEntry) value;
            JPanel card = new JPanel(new BorderLayout(12, 4));
            card.setBackground(selected ? new Color(255, 235, 219) : CARD);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, index % 2 == 0 ? CORAL : GOLD),
                    BorderFactory.createEmptyBorder(11, 13, 11, 13)));
            JLabel title = new JLabel(entry.getTitle());
            title.setFont(new Font("Segoe UI", Font.BOLD, 14));
            title.setForeground(INK);
            JLabel mood = new JLabel(entry.getMood().toString().toUpperCase());
            mood.setFont(new Font("Segoe UI", Font.BOLD, 9));
            mood.setForeground(CORAL);
            JLabel note = new JLabel(entry.getNote().isEmpty() ? "A small win, worth remembering." : entry.getNote());
            note.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            note.setForeground(new Color(113, 111, 108));
            JLabel time = new JLabel(entry.getFormattedTime());
            time.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            time.setForeground(new Color(151, 145, 137));
            JPanel text = new JPanel(new BorderLayout(0, 5));
            text.setOpaque(false);
            text.add(title, BorderLayout.NORTH);
            text.add(note, BorderLayout.CENTER);
            card.add(text, BorderLayout.CENTER);
            card.add(mood, BorderLayout.EAST);
            card.add(time, BorderLayout.SOUTH);
            return card;
        }
    }
}
