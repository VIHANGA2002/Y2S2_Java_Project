import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WestminsterShoppingManager implements ShoppingManager {

    private static final int MAX_PRODUCTS = 50;
    private static final String FILE_NAME = "products.txt";

    protected List<Product> productList;
    private final Scanner scanner;

    private ShoppingCart shoppingCart;

    public WestminsterShoppingManager() {
        this.productList = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.shoppingCart = new ShoppingCart();
        loadProductsFromFile();
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }


    @Override
    public void addProduct(Product product) {
        if (productList.size() < MAX_PRODUCTS) {
            productList.add(product);
            System.out.println("Product added successfully! " + product.getProductName());
        } else {
            System.out.println("Cannot add more products. Maximum limit reached.");
        }
    }

    @Override
    public void removeProduct(Product product) {
        if (productList.remove(product)) {
            System.out.println("Product removed: " + product.getProductName());
            System.out.println("Total number of products left: " + productList.size());
        } else {
            System.out.println("Product not found: " + product.getProductName());
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(productList);
    }

    private void displayGUI() {
        new MyFrame(this);
    }

    @Override
    public void displayMainMenu() {




        while(true) {
            System.out.print("""
                    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                 Westminster Shopping Manager
                    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                    
                    1) Admin Panel
                    2) User GUI
                                    
                    option:""");
                Scanner scanner = new Scanner(System.in);

                if (!scanner.hasNextDouble()) {
                    System.out.println("Only numeric values are allowed.\n");
                    return;
                }
                int option = scanner.nextInt();


                if (!(option < 3 && option > 0)) {
                    System.out.println("Invalid Input Try Again!");
                    continue;
                }

                switch (option) {
                    case 1:
                        System.out.println();
                        DisplayMenu();
                        break;
                    case 2:
                        displayGUI();
                        break;
                }
        }

    }

    private void DisplayMenu(){
        int choice;

        do {
            System.out.println();
            System.out.println("-----------------------------------------");
            System.out.println("          Shopping System Menu          ");
            System.out.println("-----------------------------------------");
            System.out.println();
            System.out.println("1. Add a new product");
            System.out.println("2. Delete a product");
            System.out.println("3. Print the list of products");
            System.out.println("4. Save to file");
            System.out.println("5. Back");
            System.out.println();
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    addNewProduct();
                    break;
                case 2:
                    deleteProduct();
                    break;
                case 3:
                    printProductList();
                    break;
                case 4:
                    saveProductsToFile();
                    break;
                case 5:
                    saveProductsToFile();
                    displayMainMenu();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (choice != 0);

        scanner.close();
    }
    private void addNewProduct() {
        boolean validProductType = false;

        do {
            try {
                System.out.println();
                System.out.println("-------------------------------------------");
                System.out.println();
                System.out.println("ADD PRODUCTS");
                System.out.println();
                System.out.println("Enter product type (Clothing/Electronics) : ");
                String productType = scanner.next();

                if (productType.equalsIgnoreCase("clothing") || productType.equalsIgnoreCase("electronics")) {
                    validProductType = true;
                    System.out.println("Enter the product id: ");
                    String productID = scanner.next();
                    System.out.println("Enter the product name: ");
                    String name = scanner.next();

                    int availableItem;
                    while (true) {
                        try {
                            System.out.println("Enter the available items: ");
                            availableItem = scanner.nextInt();
                            break;
                        } catch (java.util.InputMismatchException e) {
                            System.err.println("Invalid input. Please enter a valid integer for available items.");
                            scanner.nextLine(); // Clear the buffer
                        }
                    }

                    double price;
                    while (true) {
                        try {
                            System.out.println("Enter price: ");
                            price = scanner.nextDouble();
                            break;
                        } catch (java.util.InputMismatchException e) {
                            System.err.println("Invalid input. Please enter a valid price.");
                            scanner.nextLine(); // Clear the buffer
                        }
                    }

                    if (productType.equalsIgnoreCase("clothing")) {
                        String[] validSizes = {"xs", "s", "m", "l", "xl", "xxl"};
                        String size;
                        while (true) {
                            System.out.println("Enter size (xs, s, m, l, xl, xxl): ");
                            size = scanner.next().toLowerCase();
                            if (java.util.Arrays.asList(validSizes).contains(size)) {
                                break;
                            } else {
                                System.err.println("Please enter a valid size.");
                            }
                        }

                        String color = "";
                        boolean validColor = false;
                        while (!validColor) {
                            try {
                                System.out.println("Enter color: ");
                                String input = scanner.next();
                                if (input.matches("^[a-zA-Z]*$")) {
                                    color = input;
                                    validColor = true;
                                } else {
                                    System.err.println("Invalid input. Please enter a valid color.");
                                }
                            } catch (java.util.InputMismatchException e) {
                                System.err.println("Invalid input. Please enter a valid color.");
                                scanner.nextLine(); // Clear the buffer
                            }
                        }

                        Product product = new Clothing(productID, name, availableItem, price, size, color);
                        addProduct(product);
                    } else if (productType.equalsIgnoreCase("electronics")) {
                        System.out.println("Enter the brand here : ");
                        String brand = scanner.next();

                        Double time;
                        while (true) {
                            try {
                                System.out.println("Enter the warranty period (Years): ");
                                time = scanner.nextDouble();
                                break;
                            } catch (java.util.InputMismatchException e) {
                                System.err.println("Please enter a valid integer for the warranty period.");
                                scanner.nextLine(); // Clear the buffer
                            }
                        }

                        Product product = new Electronics(productID, name, availableItem, price, brand, time);
                        addProduct(product);
                    }
                } else {
                    System.err.println("Please enter a valid product type!");
                }
            } catch (java.util.InputMismatchException e) {
                System.err.println("Invalid input. Please enter the correct data type.");
                scanner.nextLine(); // Clear the buffer
            }
        } while (!validProductType);
    }


    private void deleteProduct() {
        try {
            System.out.println("-------------------------------------------");
            System.out.println();
            System.out.println("DELETE PRODUCTS ");
            System.out.println();
            System.out.println("Enter the product ID to delete: ");
            String productIdToDelete = scanner.next();
            Product productToDelete = findProductById(productIdToDelete);

            if (productToDelete != null) {
                removeProduct(productToDelete);
            } else {
                System.out.println("Product not found with ID: " + productIdToDelete);
            }
        } catch (java.util.InputMismatchException e) {
            System.err.println("Invalid input. Please enter the correct data type.");
            scanner.nextLine(); // Clear the buffer
        }
    }

    private void printProductList() {
        System.out.println("-------------------------------------------");
        System.out.println();
        System.out.println("LIST OF PRODUCT IN THE SYSTEM");
        System.out.println();
        try {
            for (Product product : getAllProducts()) {
                System.out.println("Electronic");
                System.out.println("Product ID :" + product.getProductId());
                System.out.println("Product Name :" + product.getProductName());
                System.out.println("Product Amount :" + product.getAvailableItems());
                System.out.println("Product Price :" + product.getPrice());

            }
        } catch (Exception e) {
            System.err.println("An error occurred while processing the product list: " + e.getMessage());
        }
    }

    private void saveProductsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(productList);
            System.out.println("Products saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving products to file: " + e.getMessage());
        }
    }

    private void loadProductsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) {
                productList = (List<Product>) obj;
                System.out.println("Products loaded from file.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading products from file: " + e.getMessage());
        }
    }

    private Product findProductById(String productId) {
        for (Product product : getAllProducts()) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }
}
