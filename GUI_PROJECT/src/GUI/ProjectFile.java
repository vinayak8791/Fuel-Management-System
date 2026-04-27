package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProjectFile extends JFrame implements ActionListener {

    JRadioButton PetrolButton, DieselButton, CNGButton;
    JTextField nameField, qtyField, rateField;
    JButton calcBtn, clearBtn;

    JComboBox<String> unitBox, rateBox;

    JTextArea billArea;
    JButton deleteBtn, updateBtn;
    ArrayList<String> bills = new ArrayList<>();

    ProjectFile() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1100, 600);
        this.setLayout(new BorderLayout(20, 20));
        this.setResizable(false);

        // RADIO BUTTONS
        PetrolButton = new JRadioButton("Petrol");
        DieselButton = new JRadioButton("Diesel");
        CNGButton = new JRadioButton("CNG");

        ButtonGroup group = new ButtonGroup();
        group.add(PetrolButton);
        group.add(DieselButton);
        group.add(CNGButton);

        PetrolButton.setSelected(true);

        PetrolButton.setFocusPainted(false);
        DieselButton.setFocusPainted(false);
        CNGButton.setFocusPainted(false);

        PetrolButton.addActionListener(this);
        DieselButton.addActionListener(this);
        CNGButton.addActionListener(this);

        // PANELS
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();

        panel1.setLayout(new BorderLayout());
        panel1.setPreferredSize(new Dimension(550, 600));
        panel1.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createDashedBorder(Color.BLACK), "Enter Details"));

        panel2.setPreferredSize(new Dimension(530, 600));
        panel2.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createDashedBorder(Color.BLACK), "Output"));

        // RIGHT PANEL (same UI)
        panel2.setLayout(new BorderLayout());

        JLabel rightHeading = new JLabel("Customer Fuel Bill");
        rightHeading.setHorizontalAlignment(JLabel.CENTER);
        rightHeading.setForeground(Color.WHITE);
        rightHeading.setOpaque(true);
        rightHeading.setBackground(Color.GRAY);
        rightHeading.setFont(new Font("Arial", Font.BOLD, 18));
        rightHeading.setPreferredSize(new Dimension(100, 40));

        billArea = new JTextArea();
        billArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        billArea.setEditable(false);

        panel2.add(rightHeading, BorderLayout.NORTH);
        panel2.add(new JScrollPane(billArea), BorderLayout.CENTER);

        // HEADING
        JLabel heading = new JLabel("Fuel Management System");
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(Color.GRAY);
        heading.setFont(new Font("Arial", Font.BOLD, 18));
        heading.setPreferredSize(new Dimension(100, 40));

        // INNER PANEL (same layout)
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(8, 2, 10, 10));

        Font fieldFont = new Font("Arial", Font.BOLD, 16);

        innerPanel.add(PetrolButton); innerPanel.add(new JLabel());
        innerPanel.add(DieselButton); innerPanel.add(new JLabel());
        innerPanel.add(CNGButton); innerPanel.add(new JLabel());

        innerPanel.add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        nameField.setFont(fieldFont);
        innerPanel.add(nameField);

        innerPanel.add(new JLabel("Quantity:"));
        qtyField = new JTextField();
        qtyField.setFont(fieldFont);
        qtyField.setPreferredSize(new Dimension(180, 30));
        qtyField.setHorizontalAlignment(JTextField.RIGHT);
        ((AbstractDocument) qtyField.getDocument()).setDocumentFilter(new NumberFilter());

        unitBox = new JComboBox<>();
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        qtyPanel.add(qtyField);
        qtyPanel.add(unitBox);
        innerPanel.add(qtyPanel);

        innerPanel.add(new JLabel("Rate:"));
        rateField = new JTextField();
        rateField.setFont(fieldFont);
        rateField.setPreferredSize(new Dimension(150, 30));
        rateField.setHorizontalAlignment(JTextField.RIGHT);
        ((AbstractDocument) rateField.getDocument()).setDocumentFilter(new NumberFilter());

        rateBox = new JComboBox<>();
        JPanel ratePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratePanel.add(rateField);
        ratePanel.add(rateBox);
        innerPanel.add(ratePanel);

        calcBtn = new JButton("Add Bill");
        clearBtn = new JButton("Clear");
        deleteBtn = new JButton("Delete Last");
        updateBtn = new JButton("Update Last");

        calcBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        updateBtn.addActionListener(this);

        innerPanel.add(calcBtn);
        innerPanel.add(clearBtn);
        innerPanel.add(updateBtn);
        innerPanel.add(deleteBtn);

        this.add(panel1, BorderLayout.WEST);
        this.add(panel2, BorderLayout.EAST);

        panel1.add(heading, BorderLayout.NORTH);
        panel1.add(innerPanel, BorderLayout.CENTER);

        // 🔥 FIXED: rate dropdown logic
        rateBox.addActionListener(e -> {
            String selected = (String) rateBox.getSelectedItem();
            if (selected != null) {
                String num = selected.replaceAll("[^0-9.]", "");
                rateField.setText(num);
            }
        });

        setFuelData("Petrol");

        this.setVisible(true);
    }

    // NUMBER FILTER
    class NumberFilter extends DocumentFilter {
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string.matches("[0-9.]*"))
                super.insertString(fb, offset, string, attr);
        }
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text.matches("[0-9.]*"))
                super.replace(fb, offset, length, text, attrs);
        }
    }

    // FUEL DATA
    void setFuelData(String fuel) {

        unitBox.removeAllItems();
        rateBox.removeAllItems();

        if (fuel.equals("Petrol")) {
            unitBox.addItem("Litre");
            rateBox.addItem("₹100 / L");
        } else if (fuel.equals("Diesel")) {
            unitBox.addItem("Litre");
            rateBox.addItem("₹90 / L");
        } else {
            unitBox.addItem("Kg");
            rateBox.addItem("₹70 / Kg");
        }

        // 🔥 IMPORTANT FIX
        rateBox.setSelectedIndex(0);

        qtyField.requestFocus();
        qtyField.selectAll();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == PetrolButton) setFuelData("Petrol");
        else if (e.getSource() == DieselButton) setFuelData("Diesel");
        else if (e.getSource() == CNGButton) setFuelData("CNG");

        if (e.getSource() == calcBtn) {
            try {
                double qty = Double.parseDouble(qtyField.getText());
                double rate = Double.parseDouble(rateField.getText());

                double total = qty * rate;

                String fuel = PetrolButton.isSelected() ? "Petrol"
                        : DieselButton.isSelected() ? "Diesel" : "CNG";

                String date = LocalDate.now()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                // ✅ PROFESSIONAL BILL FORMAT
                String bill =
                        "\n========================================\n" +
                        "        FUEL MANAGEMENT SYSTEM\n" +
                        "========================================\n" +
                        "Date      : " + date + "\n" +
                        "Customer  : " + nameField.getText() + "\n" +
                        "Fuel Type : " + fuel + "\n" +
                        "----------------------------------------\n" +
                        "Quantity  : " + qty + " " + unitBox.getSelectedItem() + "\n" +
                        "Rate      : ₹" + rate + " per " + unitBox.getSelectedItem() + "\n" +
                        "----------------------------------------\n" +
                        "TOTAL BILL: ₹" + total + "\n" +
                        "========================================\n";

                bills.add(bill);
                refreshArea();

                FileWriter fw = new FileWriter("Bill.txt", true);
                fw.write(bill);
                fw.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid data!");
            }
        }

        if (e.getSource() == deleteBtn) {
            if (!bills.isEmpty()) {
                bills.remove(bills.size() - 1);
                refreshArea();
            }
        }

        if (e.getSource() == updateBtn) {
            if (!bills.isEmpty()) {
                bills.remove(bills.size() - 1);
                calcBtn.doClick();
            }
        }

        if (e.getSource() == clearBtn) {
            nameField.setText("");
            qtyField.setText("");
            rateField.setText("");
        }
    }

    void refreshArea() {
        billArea.setText("");
        for (String b : bills) {
            billArea.append(b);
        }
    }

    public static void main(String[] args) {
        new ProjectFile();
    }
}