import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class DataStreams extends JFrame {

    private JTextArea originalArea;
    private JTextArea resultsArea;
    private JTextField searchField;
    private Path selectedFile;

    public DataStreams() {

        setTitle("Data Streams");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        originalArea = new JTextArea();
        resultsArea = new JTextArea();

        originalArea.setEditable(false);
        resultsArea.setEditable(false);

        JScrollPane leftScroll = new JScrollPane(originalArea);
        JScrollPane rightScroll = new JScrollPane(resultsArea);

        JPanel textPanel = new JPanel(new GridLayout(1, 2));
        textPanel.add(leftScroll);
        textPanel.add(rightScroll);

        searchField = new JTextField(15);

        JButton loadButton = new JButton("Load File");
        JButton searchButton = new JButton("Search");
        JButton quitButton = new JButton("Quit");

        JPanel topPanel = new JPanel();

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(loadButton);
        topPanel.add(searchButton);
        topPanel.add(quitButton);

        add(topPanel, BorderLayout.NORTH);
        add(textPanel, BorderLayout.CENTER);

        loadButton.addActionListener(e -> loadFile());
        searchButton.addActionListener(e -> searchFile());
        quitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void loadFile() {

        JFileChooser chooser = new JFileChooser();

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            selectedFile = chooser.getSelectedFile().toPath();

            try {
                originalArea.setText(Files.readString(selectedFile));
                resultsArea.setText("");

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading file.");
            }
        }
    }

    private void searchFile() {

        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Load a file first.");
            return;
        }

        String search = searchField.getText();

        resultsArea.setText("");

        try (Stream<String> lines = Files.lines(selectedFile)) {

            lines.filter(line -> line.toLowerCase().contains(search.toLowerCase()))
                    .forEach(line -> resultsArea.append(line + "\n"));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error searching file.");
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new DataStreams());
    }
}