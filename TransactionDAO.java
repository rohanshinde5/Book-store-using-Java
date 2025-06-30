import java.sql.*;

public class TransactionDAO {

    public static boolean processTransaction(int bookId, String name, String phone, String type) {
        return processTransaction(bookId, name, phone, type, 0, ""); // fallback if age/payment not used
    }

    // Overloaded method with age and payment method
    public static boolean processTransaction(int bookId, String name, String phone, String type, int age, String paymentMethod) {
        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false); // start transaction

            // Check book availability
            PreparedStatement bookStmt = conn.prepareStatement("SELECT quantity, is_rentable FROM books WHERE id = ?");
            bookStmt.setInt(1, bookId);
            ResultSet rs = bookStmt.executeQuery();

            if (!rs.next()) return false;

            int quantity = rs.getInt("quantity");
            boolean isRentable = rs.getBoolean("is_rentable");

            if (quantity <= 0 || (type.equals("RENT") && !isRentable)) return false;

            // Get or insert customer
            int customerId = getOrCreateCustomer(conn, name, phone, age);

            // Insert transaction
            PreparedStatement tranStmt = conn.prepareStatement(
                    "INSERT INTO transactions (book_id, customer_id, type, date, returned, payment_method) VALUES (?, ?, ?, CURRENT_DATE, ?, ?)"
            );
            tranStmt.setInt(1, bookId);
            tranStmt.setInt(2, customerId);
            tranStmt.setString(3, type);
            tranStmt.setBoolean(4, type.equals("SELL"));
            tranStmt.setString(5, paymentMethod);
            tranStmt.executeUpdate();

            // Update quantity
            PreparedStatement updateQty = conn.prepareStatement(
                    "UPDATE books SET quantity = quantity - 1 WHERE id = ?"
            );
            updateQty.setInt(1, bookId);
            updateQty.executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int getOrCreateCustomer(Connection conn, String name, String phone, int age) throws SQLException {
        PreparedStatement check = conn.prepareStatement("SELECT id FROM customers WHERE name = ? AND phone = ?");
        check.setString(1, name);
        check.setString(2, phone);
        ResultSet rs = check.executeQuery();
        if (rs.next()) return rs.getInt("id");

        PreparedStatement insert = conn.prepareStatement(
                "INSERT INTO customers (name, phone, age) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS
        );
        insert.setString(1, name);
        insert.setString(2, phone);
        insert.setInt(3, age);
        insert.executeUpdate();

        ResultSet keys = insert.getGeneratedKeys();
        if (keys.next()) return keys.getInt(1);
        else throw new SQLException("Failed to insert customer.");
    }
}
