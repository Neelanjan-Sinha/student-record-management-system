
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentManagement extends JFrame {

    // Your Oracle database connection details
static final String URL ="jdbc:oracle:thin:_YOUR URL";//your oracle/database url/connection
static final String USER = "_YOUR USERNAME_";//your oracle/database username
static final String PASSWORD = "_YOUR PASSWORD_";//your oracle/database password

    JTextField idField, nameField, ageField, courseField, emailField;
    JTable table;
    DefaultTableModel model;

    public StudentManagement() {
        setTitle("Student Record Management System");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        idField = new JTextField();
        nameField = new JTextField();
        ageField = new JTextField();
        courseField = new JTextField();
        emailField = new JTextField();

        form.add(new JLabel("Student ID:"));
        form.add(idField);
        form.add(new JLabel("Student Name:"));
        form.add(nameField);
        form.add(new JLabel("Age:"));
        form.add(ageField);
        form.add(new JLabel("Course:"));
        form.add(courseField);
        form.add(new JLabel("Email:"));
        form.add(emailField);

        JButton add = new JButton("Add");
        JButton search = new JButton("Search");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton view = new JButton("View All");
        JButton clear = new JButton("Clear");

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(add);
        buttons.add(search);
        buttons.add(update);
        buttons.add(delete);
        buttons.add(view);
        buttons.add(clear);

        model = new DefaultTableModel(
            new String[]{"ID", "Name", "Age", "Course", "Email"}, 0
        );
        table = new JTable(model);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        add.addActionListener(e -> addStudent());
        search.addActionListener(e -> searchStudent());
        update.addActionListener(e -> updateStudent());
        delete.addActionListener(e -> deleteStudent());
        view.addActionListener(e -> viewStudents());
        clear.addActionListener(e -> clearFields());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() &&
                table.getSelectedRow() >= 0) {

                int r = table.getSelectedRow();
                idField.setText(model.getValueAt(r, 0).toString());
                nameField.setText(model.getValueAt(r, 1).toString());
                ageField.setText(model.getValueAt(r, 2).toString());
                courseField.setText(model.getValueAt(r, 3).toString());
                emailField.setText(model.getValueAt(r, 4).toString());
            }
        });

        viewStudents();
    }

    Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    boolean validateFields() {
        try {
            if (idField.getText().trim().isEmpty() ||
                nameField.getText().trim().isEmpty() ||
                ageField.getText().trim().isEmpty() ||
                courseField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                    "Please fill all fields.");
                return false;
            }

            int id = Integer.parseInt(idField.getText().trim());
            int age = Integer.parseInt(ageField.getText().trim());

            if (id <= 0 || age <= 0) {
                JOptionPane.showMessageDialog(this,
                    "ID and age must be positive.");
                return false;
            }

            return true;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "ID and age must be integers.");
            return false;
        }
    }

    // ADD
    void addStudent() {
        if (!validateFields()) return;

        String sql =
            "INSERT INTO students (id, name, age, course, email) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            setStudentValues(ps, false);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Student added successfully.");

            viewStudents();
            clearFields();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    // Set values for INSERT or UPDATE
    void setStudentValues(PreparedStatement ps, boolean update)
            throws SQLException {

        if (update) {
            ps.setString(1, nameField.getText().trim());
            ps.setInt(2, Integer.parseInt(ageField.getText().trim()));
            ps.setString(3, courseField.getText().trim());
            ps.setString(4, emailField.getText().trim());
            ps.setInt(5, Integer.parseInt(idField.getText().trim()));
        } else {
            ps.setInt(1, Integer.parseInt(idField.getText().trim()));
            ps.setString(2, nameField.getText().trim());
            ps.setInt(3, Integer.parseInt(ageField.getText().trim()));
            ps.setString(4, courseField.getText().trim());
            ps.setString(5, emailField.getText().trim());
        }
    }

    // SEARCH by ID
    void searchStudent() {
        try {
            if (idField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Enter Student ID.");
                return;
            }

            int id = Integer.parseInt(idField.getText().trim());

            String sql = "SELECT * FROM students WHERE id = ?";

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    model.setRowCount(0);

                    if (rs.next()) {
                        idField.setText(rs.getString("id"));
                        nameField.setText(rs.getString("name"));
                        ageField.setText(rs.getString("age"));
                        courseField.setText(rs.getString("course"));
                        emailField.setText(rs.getString("email"));

                        addRow(rs);
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Student not found.");
                    }
                }
            }

        } catch (SQLException | NumberFormatException ex) {
            showError(ex);
        }
    }

    // UPDATE
    void updateStudent() {
        if (!validateFields()) return;

        String sql =
            "UPDATE students SET name=?, age=?, course=?, email=? " +
            "WHERE id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            setStudentValues(ps, true);
            int rows = ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                rows > 0 ? "Student updated successfully."
                         : "Student ID not found.");

            viewStudents();
            clearFields();

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    // DELETE
    void deleteStudent() {
        try {
            if (idField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Enter Student ID.");
                return;
            }

            int id = Integer.parseInt(idField.getText().trim());

            int choice = JOptionPane.showConfirmDialog(this,
                "Delete this student?", "Confirm",
                JOptionPane.YES_NO_OPTION);

            if (choice != JOptionPane.YES_OPTION) return;

            String sql = "DELETE FROM students WHERE id=?";

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, id);
                int rows = ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                    rows > 0 ? "Student deleted successfully."
                             : "Student ID not found.");

                viewStudents();
                clearFields();
            }

        } catch (SQLException | NumberFormatException ex) {
            showError(ex);
        }
    }

    // VIEW ALL
    void viewStudents() {
        String sql = "SELECT * FROM students ORDER BY id";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            model.setRowCount(0);

            while (rs.next()) {
                addRow(rs);
            }

        } catch (SQLException ex) {
            showError(ex);
        }
    }

    void addRow(ResultSet rs) throws SQLException {
        model.addRow(new Object[]{
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("age"),
            rs.getString("course"),
            rs.getString("email")
        });
    }

    void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        courseField.setText("");
        emailField.setText("");
        table.clearSelection();
    }

    void showError(Exception ex) {
        JOptionPane.showMessageDialog(this,
            "Error: " + ex.getMessage(),
            "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentManagement().setVisible(true);
        });
    }
}
