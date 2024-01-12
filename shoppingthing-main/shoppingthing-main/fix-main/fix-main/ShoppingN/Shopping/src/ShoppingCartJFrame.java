import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ShoppingCartJFrame extends JFrame {

    private static final String[] COLUMN_NAMES = {"Product", "Qty", "Price"};
    private final ShoppingCart shoppingCart;
    private JPanel panel;

    public ShoppingCartJFrame(final ShoppingCart shoppingCart) {
        super("Shopping Cart");

        this.shoppingCart = shoppingCart;

        setSize(800, 600);

        initComponents();
        //to handle closing the window              referenced https://www.tutorialspoint.com/awt/awt_window_listener.htm
        addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    e.getWindow().dispose();
                    Main.shoppingManager.getGUI().f.setEnabled(true);
                    Main.shoppingManager.getGUI().shoppingCartJFrame = null;
                }
            }
        );
    }
    //initializing the components of the frame to set up the table displaying the items in the shopping cart and a label showing the total cost.
    private void initComponents() {
        initTable();
        initTotalText();
    }
    // initializing table
    private void initTable() {
        DefaultTableModel tableModel = new DefaultTableModel(shoppingCartToTableComps(shoppingCart), COLUMN_NAMES);
        JTable table = new JTable(tableModel);

        panel = new JPanel(new BorderLayout());

        panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(table);

        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        scrollPane.setPreferredSize(new Dimension(750, 400));
        panel.add(scrollPane);
        add(panel);
    }

    private void initTotalText() {
        JLabel bottomLabel = new JLabel();


        double totalCost = shoppingCart.calculateCost();

        // Apply a 20% discount if the quantity is 3
        double discount = 0;
        if (shoppingCart.size() >= 3) {
            discount = totalCost * 0.2;
            totalCost -= discount;

        }
        bottomLabel.setPreferredSize(new Dimension(600,150));
        bottomLabel.add(new JLabel("Total: "+shoppingCart.calculateCost()+"£"));

        bottomLabel.add(new JLabel("Three items in same Category Discount(20%): " + discount+"£"));
        bottomLabel.setLayout(new BoxLayout(bottomLabel, BoxLayout.Y_AXIS));
        panel.add(bottomLabel);

    }
    //converting the contents of the shopping cart into a 2D array to display in table
    private static Object[][] shoppingCartToTableComps(final ShoppingCart shoppingCart) {
        Object[][] elements = new Object[shoppingCart.size()][COLUMN_NAMES.length];

        int i = 0;
        for (Product product : shoppingCart.stream().toList()) {
            String productInfo = "";
            if (product instanceof Clothing prod) {
                productInfo = String.format("Size: %d, Color: %s", prod.getSize(), prod.getColour());
            } else if (product instanceof Electronics prod) {
                productInfo = String.format("Brand: %s, Warranty: %.2f", prod.getBrand(), prod.getWarranty());
            }

            int qty = shoppingCart.getQty(product);
            double price = product.getPrice();


            String productName = String.format("%s %s [%s]",
                    product.getProductID(),
                    product.getProductName(),
                    productInfo
            );

            elements[i++] = new Object[]{
                    productName,
                    qty,
                    price
            };
        }
        return elements;
    }
}
