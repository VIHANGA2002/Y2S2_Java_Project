import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class  ShoppingCart {
    private List<ShoppingCartItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public void addItem(ShoppingCartItem item) {
        this.items.add(item);
    }

    public List<ShoppingCartItem> getItems() {
        return items;
    }

    public double getTotal() {
        double total = 0.0;
        for (ShoppingCartItem item : items) {
            total += item.getPrice();
        }
        return total;
    }
}

