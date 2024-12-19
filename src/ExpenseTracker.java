import java.sql.*;
import java.util.Scanner;

public class ExpenseTracker {

    public static void main(String[] args) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/expensetracker_db"; 
        String user = "root"; 
        String password = "tiger"; 

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database successfully!");

            Scanner scanner = new Scanner(System.in);

            // Menu
            System.out.println("1. Add Expense");
            System.out.println("2. Update Expense");
            System.out.println("3. View Expenses");
            System.out.println("4. Delete Expense");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addExpense(connection, scanner);
                    break;
                case 2:
                    updateExpense(connection, scanner);
                    break;
                case 3:
                    viewExpenses(connection);
                    break;
                case 4:
                    deleteExpense(connection, scanner);
                    break;
                default:
                    System.out.println("Invalid option.");
                    break;
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addExpense(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter expense amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter expense description: ");
        String description = scanner.nextLine();
        System.out.print("Enter expense date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        String sql = "INSERT INTO expenses (amount, description, date) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDouble(1, amount);
        statement.setString(2, description);
        statement.setString(3, date);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Expense added successfully!");
        }
    }

    private static void updateExpense(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter the expense ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new expense amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new expense description: ");
        String description = scanner.nextLine();
        System.out.print("Enter new expense date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        String sql = "UPDATE expenses SET amount = ?, description = ?, date = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDouble(1, amount);
        statement.setString(2, description);
        statement.setString(3, date);
        statement.setInt(4, id);

        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Expense updated successfully!");
        } else {
            System.out.println("No expense found with ID " + id);
        }
    }

    private static void viewExpenses(Connection connection) throws SQLException {
        String sql = "SELECT * FROM expenses";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            System.out.println("Expense ID: " + resultSet.getInt("id") + ", Amount: " + resultSet.getDouble("amount")
                    + ", Description: " + resultSet.getString("description") + ", Date: " + resultSet.getString("date"));
        }
    }

    private static void deleteExpense(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter the expense ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM expenses WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);

        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Expense deleted successfully!");
        } else {
            System.out.println("No expense found with ID " + id);
        }
    }
}
