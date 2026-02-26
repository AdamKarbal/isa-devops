package fr.univcotedazur.skimaster.cli.model;

public class CliOrder {
    private Long id;
    private double price;
    private String status;
    private String paymentReceiptId;

    public CliOrder() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPaymentReceiptId() { return paymentReceiptId; }
    public void setPaymentReceiptId(String paymentReceiptId) { this.paymentReceiptId = paymentReceiptId; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", price=" + price +
                ", status='" + status + '\'' +
                (paymentReceiptId != null ? ", receipt='" + paymentReceiptId + '\'' : "") +
                '}';
    }
}
