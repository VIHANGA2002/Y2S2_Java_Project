import java.util.List;

interface ShoppingManager {
    void addProduct(Product product);
    void removeProduct(Product product);
    List<Product> getAllProducts();

    void displayMainMenu();
}

