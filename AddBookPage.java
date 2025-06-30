import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddBookPage extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public AddBookPage() {
        setTitle("Add/Edit Books");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#F0E68C")); // Khaki background
        setVisible(true);

        model = new DefaultTableModel(new String[]{
                "ID", "Title", "Author", "Publisher", "Year", "Price", "Quantity"
        }, 0);

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.decode("#F0E68C")); // Button panel background

        JButton addBtn = new JButton("Add Book");
        JButton deleteBtn = new JButton("Delete Book");
        JButton updateBtn = new JButton("Update Book");
        JButton homeBtn = new JButton("Home");

        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(homeBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        loadBooks();

        addBtn.addActionListener(e -> addBook());
        deleteBtn.addActionListener(e -> deleteBook());
        updateBtn.addActionListener(e -> updateBook());
        homeBtn.addActionListener(e -> {
            dispose();
            new BookstoreHomePage();
            // new HomePage().setVisible(true); // Uncomment and replace with actual home page if needed
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

    

    private void addBook() {
        JPanel form = new JPanel(new GridLayout(7, 2));
        form.setBackground(Color.decode("#F0E68C"));

        JTextField title = new JTextField();
        JTextField author = new JTextField();
        JTextField publisher = new JTextField();
        JTextField year = new JTextField();
        JTextField price = new JTextField();
        JTextField quantity = new JTextField();

        form.add(new JLabel("Title:")); form.add(title);
        form.add(new JLabel("Author:")); form.add(author);
        form.add(new JLabel("Publisher:")); form.add(publisher);
        form.add(new JLabel("Year:")); form.add(year);
        form.add(new JLabel("Price:")); form.add(price);
        form.add(new JLabel("Quantity:")); form.add(quantity);

        int result = JOptionPane.showConfirmDialog(this, form, "Add New Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String insert = "INSERT INTO books (title, author, publisher, year, price, quantity) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insert);
                stmt.setString(1, title.getText().trim());
                stmt.setString(2, author.getText().trim());
                stmt.setString(3, publisher.getText().trim());
                stmt.setInt(4, Integer.parseInt(year.getText().trim()));
                stmt.setDouble(5, Double.parseDouble(price.getText().trim()));
                stmt.setInt(6, Integer.parseInt(quantity.getText().trim()));
                stmt.executeUpdate();
                loadBooks();
                JOptionPane.showMessageDialog(this, "Book added successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error adding book: " + ex.getMessage());
            }
        }
    }

    private void deleteBook() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this book?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String delete = "DELETE FROM books WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(delete);
                stmt.setInt(1, id);
                stmt.executeUpdate();
                loadBooks(); // refresh the table
                JOptionPane.showMessageDialog(this, "Book deleted successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting book: " + ex.getMessage());
            }
        }
    }

    private void updateBook() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to update.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        String title = (String) model.getValueAt(row, 1);
        String author = (String) model.getValueAt(row, 2);
        String publisher = (String) model.getValueAt(row, 3);
        int year = (int) model.getValueAt(row, 4);
        double price = (double) model.getValueAt(row, 5);
        int quantity = (int) model.getValueAt(row, 6);

        JPanel form = new JPanel(new GridLayout(7, 2));
        form.setBackground(Color.decode("#F0E68C"));

        JTextField titleField = new JTextField(title);
        JTextField authorField = new JTextField(author);
        JTextField publisherField = new JTextField(publisher);
        JTextField yearField = new JTextField(String.valueOf(year));
        JTextField priceField = new JTextField(String.valueOf(price));
        JTextField quantityField = new JTextField(String.valueOf(quantity));

        form.add(new JLabel("Title:")); form.add(titleField);
        form.add(new JLabel("Author:")); form.add(authorField);
        form.add(new JLabel("Publisher:")); form.add(publisherField);
        form.add(new JLabel("Year:")); form.add(yearField);
        form.add(new JLabel("Price:")); form.add(priceField);
        form.add(new JLabel("Quantity:")); form.add(quantityField);

        int result = JOptionPane.showConfirmDialog(this, form, "Edit Book Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String update = "UPDATE books SET title=?, author=?, publisher=?, year=?, price=?, quantity=? WHERE id=?";
                PreparedStatement stmt = conn.prepareStatement(update);
                stmt.setString(1, titleField.getText().trim());
                stmt.setString(2, authorField.getText().trim());
                stmt.setString(3, publisherField.getText().trim());
                stmt.setInt(4, Integer.parseInt(yearField.getText().trim()));
                stmt.setDouble(5, Double.parseDouble(priceField.getText().trim()));
                stmt.setInt(6, Integer.parseInt(quantityField.getText().trim()));
                stmt.setInt(7, id);
                stmt.executeUpdate();
                loadBooks();
                JOptionPane.showMessageDialog(this, "Book updated successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating book: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddBookPage().setVisible(true));
    }
}
