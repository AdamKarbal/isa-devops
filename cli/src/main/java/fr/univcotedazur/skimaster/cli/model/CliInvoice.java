package fr.univcotedazur.skimaster.cli.model;

public class CliInvoice {
    private Long id;
    private Long orderRefId;
    private double amount;
    private String bankReceiptId;

    public CliInvoice() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getOrderRefId() { return orderRefId; }
    public void setOrderRefId(Long orderRefId) { this.orderRefId = orderRefId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getBankReceiptId() { return bankReceiptId; }
    public void setBankReceiptId(String bankReceiptId) { this.bankReceiptId = bankReceiptId; }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", orderRefId=" + orderRefId +
                ", amount=" + amount +
                ", bankReceiptId='" + bankReceiptId + '\'' +
                '}';
    }
}
