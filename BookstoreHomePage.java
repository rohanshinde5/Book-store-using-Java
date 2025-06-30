import javax.swing.*;
import java.awt.*;

public class BookstoreHomePage extends JFrame {

    public BookstoreHomePage() {
        setTitle("Tales & Texts - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        Color khaki = Color.decode("#F0E68C");       // Main background
        Color brownKhaki = new Color(210, 180, 140); // Left panel color
        Color greyText = Color.decode("#555555");

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());

        // ----------------- HEADER -------------------
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(1000, 100));

        // Left portion of header (matches left panel)
        JPanel headerLeft = new JPanel();
        headerLeft.setPreferredSize(new Dimension(220, 100));
        headerLeft.setBackground(brownKhaki);

        // Right portion with title and tagline stacked
        JPanel headerRight = new JPanel();
        headerRight.setLayout(new BoxLayout(headerRight, BoxLayout.Y_AXIS));
        headerRight.setBackground(khaki);

        JLabel titleLabel = new JLabel("TALES & TEXTS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel taglineLabel = new JLabel("A book is a gift you can open again and again", SwingConstants.CENTER);
        taglineLabel.setFont(new Font("Serif", Font.ITALIC, 16));
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerRight.add(Box.createVerticalStrut(15));
        headerRight.add(titleLabel);
        headerRight.add(Box.createVerticalStrut(5));
        headerRight.add(taglineLabel);

        headerPanel.add(headerLeft, BorderLayout.WEST);
        headerPanel.add(headerRight, BorderLayout.CENTER);

        // ----------------- LEFT PANEL -------------------
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(220, getHeight()));
        leftPanel.setBackground(brownKhaki);

        JPanel leftButtonsPanel = new JPanel();
        leftButtonsPanel.setLayout(new BoxLayout(leftButtonsPanel, BoxLayout.Y_AXIS));
        leftButtonsPanel.setBackground(brownKhaki);
        leftButtonsPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

        JButton editBookButton = new JButton("Edit Book List");
        JButton statsButton = new JButton("Statistics");

        for (JButton btn : new JButton[]{editBookButton, statsButton}) {
            btn.setFont(new Font("Arial", Font.PLAIN, 14));
            btn.setMaximumSize(new Dimension(160, 35));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setBackground(Color.WHITE);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setFocusPainted(false);
        }

        // ✅ Action listener for Edit Book List
        editBookButton.addActionListener(e -> {
            dispose();
            new AddBookPage();
        });

        // (Optional) Action listener for Statistics
        statsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Statistics Page not implemented yet.");
            // new StatisticsPage(); // if you have one
        });

        leftButtonsPanel.add(editBookButton);
        leftButtonsPanel.add(Box.createVerticalStrut(10));
        leftButtonsPanel.add(statsButton);

        JLabel socialLabel = new JLabel("<html><center>Follow us:<br>@TALES & TEXTS</center></html>", SwingConstants.CENTER);
        socialLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        socialLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        leftPanel.add(leftButtonsPanel, BorderLayout.NORTH);
        leftPanel.add(socialLabel, BorderLayout.SOUTH);

        // ----------------- RIGHT PANEL -------------------
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(khaki);

        // EXACT Center Panel using GridBagLayout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(khaki);
        GridBagConstraints gbc = new GridBagConstraints();

        JButton sellRentButton = new JButton("SELL / RENT");
        sellRentButton.setFont(new Font("Arial", Font.BOLD, 26));
        sellRentButton.setBackground(new Color(80, 50, 20));
        sellRentButton.setForeground(Color.WHITE);
        sellRentButton.setFocusPainted(false);
        sellRentButton.setPreferredSize(new Dimension(300, 80));
        sellRentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ✅ Action listener for SELL / RENT
        sellRentButton.addActionListener(e -> {
            dispose();
            new SellBooksPage();
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(sellRentButton, gbc);

        // Footer
        JPanel footerPanel = new JPanel(new GridLayout(2, 1));
        footerPanel.setBackground(khaki);

        JLabel addressLabel = new JLabel("📍 123, Book Lane, Pune, Maharashtra, India", SwingConstants.CENTER);
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addressLabel.setForeground(greyText);

        JLabel hoursLabel = new JLabel("OPEN FROM 11AM to 8PM", SwingConstants.CENTER);
        hoursLabel.setFont(new Font("Arial", Font.BOLD, 14));
        hoursLabel.setForeground(greyText);

        footerPanel.add(addressLabel);
        footerPanel.add(hoursLabel);

        rightPanel.add(centerPanel, BorderLayout.CENTER);
        rightPanel.add(footerPanel, BorderLayout.SOUTH);

        // ----------------- COMPOSITE CENTER -------------------
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.add(leftPanel, BorderLayout.WEST);
        bodyPanel.add(rightPanel, BorderLayout.CENTER);

        // ----------------- ASSEMBLE FRAME -------------------
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(bodyPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(BookstoreHomePage::new);
    }
}


