import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class PayTrack {
    private JFrame frame;
    private JTextField nameField;
    private JTextField rateField;
    private JTextField daysField;
    private JTextField overtimeField;

    public PayTrack() {
        frame = new JFrame("PayTrack - Salary Calculator");
        frame.setSize(500, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(245, 248, 252));
        mainPanel.setBorder(new EmptyBorder(25, 35, 25, 35));
        mainPanel.setLayout(new BorderLayout(10, 15));

        JLabel title = new JLabel("PAYTRACK", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(35, 80, 130));

        JLabel subtitle = new JLabel(
                "Employee Salary Calculator",
                SwingConstants.CENTER
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel heading = new JPanel(new GridLayout(2, 1, 5, 5));
        heading.setBackground(mainPanel.getBackground());
        heading.add(title);
        heading.add(subtitle);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        formPanel.setBackground(mainPanel.getBackground());

        nameField = new JTextField();
        rateField = new JTextField();
        daysField = new JTextField();
        overtimeField = new JTextField();

        addFormRow(formPanel, "Employee Name:", nameField);
        addFormRow(formPanel, "Daily Rate (₱):", rateField);
        addFormRow(formPanel, "Days Worked:", daysField);
        addFormRow(formPanel, "Overtime Hours:", overtimeField);

        JButton calculateButton = new JButton("CALCULATE SALARY");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 14));
        calculateButton.setBackground(new Color(35, 80, 130));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.setFocusPainted(false);

        calculateButton.addActionListener(e -> calculateSalary());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(mainPanel.getBackground());
        bottomPanel.add(calculateButton);

        mainPanel.add(heading, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void addFormRow(
            JPanel panel,
            String labelText,
            JTextField field
    ) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        field.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(label);
        panel.add(field);
    }

    private void calculateSalary() {
        try {
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter the employee name.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            double dailyRate = Double.parseDouble(rateField.getText().trim());
            int daysWorked = Integer.parseInt(daysField.getText().trim());
            double overtimeHours =
                    Double.parseDouble(overtimeField.getText().trim());

            if (dailyRate < 0 || daysWorked < 0 || overtimeHours < 0) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter non-negative values.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            Employee employee = new Employee(
                    name,
                    dailyRate,
                    daysWorked,
                    overtimeHours
            );

            Salary salary = new Salary(employee);
            showSalarySummary(employee, salary);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Please enter valid numbers for rate, days worked, and overtime.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showSalarySummary(Employee employee, Salary salary) {
        DecimalFormat money = new DecimalFormat("#,##0.00");

        String result =
                "Employee Name: " + employee.getName() + "\n\n" +
                "Gross Salary: ₱" +
                money.format(salary.getGross()) + "\n" +
                "SSS Deduction (10%): ₱" +
                money.format(salary.getSSS()) + "\n" +
                "PhilHealth Deduction (2.5%): ₱" +
                money.format(salary.getPhilHealth()) + "\n\n" +
                "Net Salary: ₱" +
                money.format(salary.getNet());

        JTextArea resultArea = new JTextArea(result);
        resultArea.setFont(new Font("Arial", Font.BOLD, 15));
        resultArea.setEditable(false);
        resultArea.setOpaque(false);
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JOptionPane.showMessageDialog(
                frame,
                resultArea,
                "PayTrack - Salary Summary",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PayTrack::new);
    }
}

class Employee {
    private String name;
    private double dailyRate;
    private int daysWorked;
    private double overtimeHours;

    public Employee(
            String name,
            double dailyRate,
            int daysWorked,
            double overtimeHours
    ) {
        this.name = name;
        this.dailyRate = dailyRate;
        this.daysWorked = daysWorked;
        this.overtimeHours = overtimeHours;
    }

    public String getName() {
        return name;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public int getDaysWorked() {
        return daysWorked;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }
}

class Salary {
    private Employee employee;
    private double gross;
    private double sss;
    private double philHealth;
    private double net;

    public Salary(Employee employee) {
        this.employee = employee;
        calculateSalary();
    }

    private void calculateSalary() {
        gross = employee.getDailyRate() * employee.getDaysWorked()
                + (employee.getDailyRate() / 8)
                * employee.getOvertimeHours();

        sss = gross * 0.10;
        philHealth = gross * 0.025;

        net = gross - sss - philHealth;
    }

    public double getGross() {
        return gross;
    }

    public double getSSS() {
        return sss;
    }

    public double getPhilHealth() {
        return philHealth;
    }

    public double getNet() {
        return net;
    }
}
