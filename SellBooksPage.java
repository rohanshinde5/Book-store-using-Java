import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SellBooksPage extends JFrame {

    private static final Color BACKGROUND_COLOR = Color.decode("#F0E68C");

    private JTable table;
    private DefaultTableModel model;

    public SellBooksPage() {
        setTitle("SELL BOOKS");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
        setVisible(true);

        JLabel header = new JLabel("SELL BOOKS", JLabel.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 26));
        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{
                "ID", "Title", "Author", "Publisher", "Year", "Price", "Quantity"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        JButton sellBtn = new JButton("SELL BOOK");
        JButton rentBtn = new JButton("RENT BOOK");
        JButton returnBtn = new JButton("RETURN BOOK");
        JButton homeBtn = new JButton("HOME");

        buttonPanel.add(sellBtn);
        buttonPanel.add(rentBtn);
        buttonPanel.add(returnBtn);
        buttonPanel.add(homeBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        loadBooks();

        sellBtn.addActionListener(e -> handleTransaction("SELL"));
        rentBtn.addActionListener(e -> handleTransaction("RENT"));
        homeBtn.addActionListener(e -> {
            dispose(); // Close current window
            new BookstoreHomePage(); // Open the Home Page
        });


        returnBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book to return.");
                return;
            }

            String title = (String) model.getValueAt(row, 1);
            String author = (String) model.getValueAt(row, 2);
            String publisher = (String) model.getValueAt(row, 3);
            int year = (int) model.getValueAt(row, 4);

            JPanel returnForm = new JPanel(new GridLayout(5, 2, 10, 10));
            returnForm.setPreferredSize(new Dimension(400, 200));

            JTextField titleField = new JTextField(title);
            JTextField authorField = new JTextField(author);
            JTextField publisherField = new JTextField(publisher);
            JTextField yearField = new JTextField(String.valueOf(year));
            JTextField quantityField = new JTextField("1");

            returnForm.add(new JLabel("Title:"));
            returnForm.add(titleField);
            returnForm.add(new JLabel("Author:"));
            returnForm.add(authorField);
            returnForm.add(new JLabel("Publisher:"));
            returnForm.add(publisherField);
            returnForm.add(new JLabel("Year:"));
            returnForm.add(yearField);
            returnForm.add(new JLabel("Quantity to Return:"));
            returnForm.add(quantityField);

            int confirm = JOptionPane.showConfirmDialog(this, returnForm, "Return Book", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                try (Connection conn = DBConnection.getConnection()) {
                    String updateQuery = "UPDATE books SET quantity = quantity + ? WHERE title = ? AND author = ? AND publisher = ? AND year = ?";
                    PreparedStatement ps = conn.prepareStatement(updateQuery);

                    ps.setInt(1, Integer.parseInt(quantityField.getText().trim()));
                    ps.setString(2, titleField.getText().trim());
                    ps.setString(3, authorField.getText().trim());
                    ps.setString(4, publisherField.getText().trim());
                    ps.setInt(5, Integer.parseInt(yearField.getText().trim()));

                    int updated = ps.executeUpdate();
                    if (updated > 0) {
                        JOptionPane.showMessageDialog(this, "Book return recorded and database updated.");
                        loadBooks();
                    } else {
                        JOptionPane.showMessageDialog(this, "No matching record found. Return failed.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });
    }

    private void loadBooks() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM books";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getInt("year"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + ex.getMessage());
        }
    }

    private void handleTransaction(String type) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book.");
            return;
        }

        int bookId = (int) model.getValueAt(row, 0);
        String title = (String) model.getValueAt(row, 1);
        double price = (double) model.getValueAt(row, 5);
        int quantity = (int) model.getValueAt(row, 6);

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Book is out of stock.");
            return;
        }

        JPanel form = new JPanel();
        form.setLayout(new GridLayout(5, 2, 10, 10));
        form.setPreferredSize(new Dimension(400, 200));
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField phoneField = new JTextField();
        JComboBox<String> paymentBox = new JComboBox<>(new String[]{"Cash", "UPI", "Card"});

        form.add(new JLabel("Customer Name:"));
        form.add(nameField);
        form.add(new JLabel("Age:"));
        form.add(ageField);
        form.add(new JLabel("Phone Number:"));
        form.add(phoneField);
        form.add(new JLabel("Payment Method:"));
        form.add(paymentBox);

        int result = JOptionPane.showConfirmDialog(this, form, "Enter Customer Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
        if (result != JOptionPane.OK_OPTION) return;

        String name = nameField.getText().trim();
        String ageStr = ageField.getText().trim();
        String phone = phoneField.getText().trim();
        String payment = (String) paymentBox.getSelectedItem();

        if (!name.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(this, "Name must contain only alphabets.");
            return;
        }
        if (!ageStr.matches("\\d+") || Integer.parseInt(ageStr) < 1 || Integer.parseInt(ageStr) > 100) {
            JOptionPane.showMessageDialog(this, "Age must be a number between 1 and 100.");
            return;
        }
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits.");
            return;
        }

        int age = Integer.parseInt(ageStr);
        boolean success = TransactionDAO.processTransaction(bookId, name, phone, type, age, payment);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Receipt\n--------------------------\n" +
                            "Customer: " + name + "\n" +
                            "Phone: " + phone + "\n" +
                            "Age: " + ageStr + "\n" +
                            "Book: " + title + "\n" +
                            "Type: " + type + "\n" +
                            "Payment: " + payment + "\n" +
                            "Amount: ₹" + (type.equals("RENT") ? price / 2 : price) + "\n" +
                            "--------------------------"
            );
            loadBooks();
        } else {
            JOptionPane.showMessageDialog(this, "Transaction failed.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SellBooksPage().setVisible(true));
    }
}



