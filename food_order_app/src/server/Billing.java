package server;

import java.io.Serializable;
import java.util.Map;

public class Billing implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final double TAX_RATE = 0.1; // 10% tax

    private Order order;
    private double taxAmount;
    private double totalAmountWithTax;

    public Billing(Order order) {
        this.order = order;
        calculateBill();
    }

    private void calculateBill() {
        double subtotal = order.getTotalAmount();
        this.taxAmount = subtotal * TAX_RATE;
        this.totalAmountWithTax = subtotal + taxAmount;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getTotalAmountWithTax() {
        return totalAmountWithTax;
    }

    @Override
    public String toString() {
        StringBuilder billDetails = new StringBuilder("Billing Statement:\n");
        for (Map.Entry<String, Integer> entry : order.getItems().entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double price = order.getItemPrice(itemName); // assuming order has a method to fetch item price
            double itemTotal = price * quantity;
            billDetails.append(String.format("%s x %d @ $%.2f each: $%.2f\n", itemName, quantity, price, itemTotal));
        }
        billDetails.append("\nSubtotal: $").append(order.getTotalAmount());
        billDetails.append("\nTax (10%): $").append(taxAmount);
        billDetails.append("\nTotal Amount Due: $").append(totalAmountWithTax);
        return billDetails.toString();
    }
}
