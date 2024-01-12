import java.util.*;
import java.util.stream.Stream;

public class ShoppingCart {
    private Map<Product, Integer> products;

    public ShoppingCart() {
        this.products = new HashMap<>();
    }

    public void add(Product product){
        products.put(product, products.getOrDefault(product, 0) + 1);
    }

    public int getQty(Product product) {
        return products.get(product);
    }

    public Stream<Product> stream() {
        return products.keySet().stream();
    }

    public int size() {
        return products.size();
    }

    public double calculateCost(){
        double total = 0.0;

        for (Product product : products.keySet()) {
            final int qty = products.get(product);
            total += product.getPrice() * qty;
        }
        return total;
    }
}
