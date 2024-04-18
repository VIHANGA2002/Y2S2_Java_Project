import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyFrame extends JFrame implements ActionListener {
    private JButton myButton;
    private JComboBox<String> comboBox;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private WestminsterShoppingManager shoppingManager;
    private JLabel selectCategoryLabel;
    private JButton shoppingCartButton;

    private JTable shoppingCartTable;

    private JLabel selectProductsLabel;
    private JLabel productIDLabel;
    private JLabel categoryLabel;
    private JLabel nameLabel;
    private JLabel sizeLabel;
    private JLabel colorLabel;
    private JLabel ItemsAvailableLabel;
    private JLabel brandLabel;
    private JLabel WarrantyLabel;
    private JPanel shoppingCartPanel;
    private JFrame shoppingCartFrame;
    private JLabel totalLabel;
    private JLabel discountTenLabel;
    private JLabel discountTwentyLabel;
    private JLabel finalTotalLabel;

    private void updateTableData() {
        tableModel.setRowCount(0);

        for (Product product : shoppingManager.getAllProducts()) {
            if (comboBox.getSelectedItem().equals("All") ||
                    (comboBox.getSelectedItem().equals("Clothes") && product instanceof Clothing) ||
                    (comboBox.getSelectedItem().equals("Electronics") && product instanceof Electronics)) {

                Object[] rowData = {
                        product.getProductId(),
                        product.getProductName(),
                        product instanceof Clothing ? "Clothing" : "Electronics",
                        product.getPrice(),
                        getProductInfo(product)
                };
                tableModel.addRow(rowData);
            }
        }
    }

    private String getProductInfo(Product product) {
        if (product instanceof Electronics) {
            return "Brand: " + ((Electronics) product).getBrand() +
                    ", Warranty: " + ((Electronics) product).getWarrantyPeriod() + " years, "+
                    "Available Items: " + product.getAvailableItems();
        } else if (product instanceof Clothing) {
            return "Size: " + ((Clothing) product).getSize() +
                    ", Color: " + ((Clothing) product).getColor()+ ", Available Items: " +
                    product.getAvailableItems();
        } else {
            return "N/A";
        }
    }

    private Product getProductInfo(String info) {
        String[] tokens = info.split(", ");
        String type = tokens[0];

        if (type.contains("Brand")) {
            String brand = tokens[0].substring(tokens[0].indexOf(":") + 1).trim();
            String warranty = tokens[1].substring(tokens[1].indexOf(":") + 1).trim();
            return new Electronics("", "", 0, 0, brand, Double.parseDouble(warranty.split(" ")[0]));
        } else if (type.contains("Size")) {
            String size = tokens[0].substring(tokens[0].indexOf(":") + 1).trim();
            String color = tokens[1].substring(tokens[1].indexOf(":") + 1).trim();
            return new Clothing("", "", 0, 0, size, color);
        } else {
            return null;
        }
    }


    MyFrame(WestminsterShoppingManager shoppingManager) {
        this.shoppingManager = shoppingManager;
        this.setPreferredSize(new Dimension(1200, 720));

        String[] items = {"All", "Clothes", "Electronics"};

        selectCategoryLabel = new JLabel("Select Product Category");

        shoppingCartPanel = new JPanel();
        shoppingCartPanel.setPreferredSize(new Dimension(500, 500));
        shoppingCartPanel.setVisible(false);

        productIDLabel = new JLabel("   Product ID :");
        categoryLabel = new JLabel("   Category :");
        nameLabel = new JLabel("   Name :");
        sizeLabel = new JLabel("   Size :");
        colorLabel = new JLabel("   Colour :");
        ItemsAvailableLabel = new JLabel();
        brandLabel = new JLabel("   Brand :");
        WarrantyLabel = new JLabel("   Warranty :");

        totalLabel = new JLabel("    Total -");
        discountTenLabel = new JLabel("    First Purchase Discount (10%) -");
        discountTwentyLabel = new JLabel("    Three item in same Category (20%) -");
        finalTotalLabel = new JLabel("   Final Total -");
        finalTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));

        comboBox = new JComboBox<>(items);
        comboBox.addActionListener(this);

        myButton = new JButton("Add to Shopping Cart");
        myButton.setFocusable(false);
        myButton.addActionListener(this);

        selectProductsLabel = new JLabel("  Selected Products - Details");
        selectProductsLabel.setFont(new Font("Arial", Font.BOLD, 14));

        shoppingCartButton = new JButton("Shopping Cart");
        shoppingCartButton.setFocusable(false);
        shoppingCartButton.addActionListener(this);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Category");
        tableModel.addColumn("Price \u00A3");
        tableModel.addColumn("Info");

        productTable = new JTable(tableModel);
        productTable.setRowHeight(30);

        updateTableData();
        highlightLowStockProducts();

        productTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                Object category = tableModel.getValueAt(selectedRow, 2);

                Object productId = tableModel.getValueAt(selectedRow, 0);
                Object productName = tableModel.getValueAt(selectedRow, 1);
                Product product = getProductInfo(tableModel.getValueAt(selectedRow, 4).toString());


                productIDLabel.setText("   Product ID: " + productId.toString());
                nameLabel.setText("   Name: " + productName.toString());
                categoryLabel.setText("   Category: " + category.toString());

                if(product instanceof Clothing) {
                    sizeLabel.setText("   Size :"+((Clothing) product).getSize());
                    colorLabel.setText("   Color :"+((Clothing) product).getColor());
                } else if(product instanceof Electronics) {
                    brandLabel.setText("   Brand :"+((Electronics) product).getBrand());
                    WarrantyLabel.setText("   Warranty :"+((Electronics) product).getWarrantyPeriod()+" years");

                }

                if (category.toString().equals("Clothing")) {
                    sizeLabel.setVisible(true);
                    colorLabel.setVisible(true);
                    ItemsAvailableLabel.setVisible(true);

                    brandLabel.setVisible(false);
                    WarrantyLabel.setVisible(false);
                } else if (category.toString().equals("Electronics")) {
                    brandLabel.setVisible(true);
                    WarrantyLabel.setVisible(true);

                    sizeLabel.setVisible(false);
                    colorLabel.setVisible(false);
                    ItemsAvailableLabel.setVisible(false);
                }
            }
        });



        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int selectedRow = productTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Object productId = tableModel.getValueAt(selectedRow, 0);
                        Object productName = tableModel.getValueAt(selectedRow, 1);

                        productIDLabel.setText("   Product ID: " + productId.toString());
                        nameLabel.setText("   Name: " + productName.toString());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(productTable);

        this.setTitle("Westminster Shopping Center");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.getContentPane().setBackground(Color.blue);
        this.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        northPanel.add(selectCategoryLabel);
        northPanel.add(comboBox);
        northPanel.add(shoppingCartButton);
        this.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(selectProductsLabel, BorderLayout.NORTH);

        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));

        labelsPanel.add(productIDLabel);
        labelsPanel.add(categoryLabel);
        labelsPanel.add(nameLabel);
        labelsPanel.add(sizeLabel);
        labelsPanel.add(colorLabel);
        labelsPanel.add(ItemsAvailableLabel);
        labelsPanel.add(brandLabel);
        labelsPanel.add(WarrantyLabel);

        southPanel.add(labelsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(myButton);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(southPanel, BorderLayout.SOUTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }

    private void highlightLowStockProducts() {
        productTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                int availableItemsColumnIndex = 4; // Assuming available items info is in the 5th column

                Object availableItemsInfo = table.getValueAt(row, availableItemsColumnIndex);
                String availableItemsString = availableItemsInfo.toString().replaceAll("\\D+", ""); // Extracting only numbers
                int availableItems = 0;
                try {
                    availableItems = Integer.parseInt(availableItemsString);
                } catch (NumberFormatException e) {
                    // Handle the exception, you might want to set a default value or take appropriate action
                    System.err.println("Error parsing available items: " + e.getMessage());
                }

                if (availableItems < 3) {
                    cellComponent.setBackground(Color.RED);
                } else {
                    cellComponent.setBackground(table.getBackground());
                }
                return cellComponent;
            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == myButton) {
            addToShoppingCart();
        }
        if (e.getSource() == shoppingCartButton) {
            openShoppingCartWindow();
        }

        if (e.getSource() == comboBox) {
            String selectedCategory = (String) comboBox.getSelectedItem();
            if (!selectedCategory.equals("All") && !selectedCategory.equals("Clothes") &&
                    !selectedCategory.equals("Electronics")) {
                System.out.println(selectedCategory);
            }
            updateTableData();
        }
    }

    private void addToShoppingCart() {
        if (shoppingCartTable == null) {
            initializeShoppingCart();
        }
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            Object productId = tableModel.getValueAt(selectedRow, 0);
            Object productName = tableModel.getValueAt(selectedRow, 1);
            Object price = tableModel.getValueAt(selectedRow, 3);

            DefaultTableModel shoppingCartTableModel = (DefaultTableModel) shoppingCartTable.getModel();
            boolean itemAlreadyExists = false;

            for (int i = 0; i < shoppingCartTableModel.getRowCount(); i++) {
                String cartProductId = shoppingCartTableModel.getValueAt(i, 0).toString().split(",")[0].replace("ID: ", "").trim();
                if (cartProductId.equals(productId.toString())) {
                    // If the product already exists in the cart, update the quantity and price
                    int currentQuantity = (int) shoppingCartTableModel.getValueAt(i, 1);
                    shoppingCartTableModel.setValueAt(currentQuantity + 1, i, 1);
                    double totalPrice = (currentQuantity + 1) * Double.parseDouble(price.toString());
                    shoppingCartTableModel.setValueAt(totalPrice, i, 2);
                    itemAlreadyExists = true;
                    break;
                }
            }

            if (!itemAlreadyExists) {
                // If the product is not in the cart, add it with quantity 1
                Object[] rowData = {"ID: " + productId.toString() + ", Name: " + productName.toString(), 1, price};
                shoppingCartTableModel.addRow(rowData);
            }

            updateShoppingCartDetails();
        }
    }


    private void updateShoppingCartDetails() {
        DefaultTableModel shoppingCartTableModel = (DefaultTableModel) shoppingCartTable.getModel();

        double total = 0;
        int itemCount = shoppingCartTableModel.getRowCount();

        for (int i = 0; i < itemCount; i++) {
            double price = (double) shoppingCartTableModel.getValueAt(i, 2);
            int quantity = (int) shoppingCartTableModel.getValueAt(i, 1);
            total += price * quantity;
        }

        double discountTen = total * 0.1;
        double discountTwenty = total >= 3 ? total * 0.2 : 0;
        double finalTotal = total - discountTen - discountTwenty;

        totalLabel.setText("    Total - " + String.format("%.2f", total)+" \u00A3");
        discountTenLabel.setText("    First Purchase Discount (10%) - " + String.format("%.2f", discountTen)+" \u00A3");
        discountTwentyLabel.setText("    Three items in the same Category Discount (20%) - " + String.format("%.2f", discountTwenty)+" \u00A3");
        finalTotalLabel.setText("    Final Total - " + String.format("%.2f", finalTotal)+" \u00A3");
    }

    private void openShoppingCartWindow() {
        if (shoppingCartFrame == null) {
            initializeShoppingCart();
        }

        shoppingCartFrame.setVisible(true);
    }

    private void initializeShoppingCart() {
        shoppingCartFrame = new JFrame("Shopping Cart");
        shoppingCartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        shoppingCartFrame.setSize(500, 500);
        shoppingCartFrame.setLayout(new BorderLayout());

        JPanel shoppingCartPanel = new JPanel();
        shoppingCartPanel.setLayout(new BorderLayout());

        DefaultTableModel shoppingCartTableModel = new DefaultTableModel();
        shoppingCartTableModel.addColumn("Product");
        shoppingCartTableModel.addColumn("Quantity");
        shoppingCartTableModel.addColumn("Price \u00A3");

        shoppingCartTable = new JTable(shoppingCartTableModel);
        JScrollPane shoppingCartScrollPane = new JScrollPane(shoppingCartTable);
        shoppingCartTable.setRowHeight(30);

        JPanel labelsPanel = new JPanel();
        labelsPanel.setLayout(new GridLayout(4, 1));

        labelsPanel.add(totalLabel);
        labelsPanel.add(discountTenLabel);
        labelsPanel.add(discountTwentyLabel);
        labelsPanel.add(finalTotalLabel);

        shoppingCartPanel.add(shoppingCartScrollPane, BorderLayout.CENTER);
        shoppingCartPanel.add(labelsPanel, BorderLayout.SOUTH);

        shoppingCartFrame.add(shoppingCartPanel);
    }
}