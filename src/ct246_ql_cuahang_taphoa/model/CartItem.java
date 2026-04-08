
package ct246_ql_cuahang_taphoa.model;

public class CartItem {
    private Product product;
    private int quantity;
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    // Hàm tính thành tiền của sản phẩm
    public double getSubTotal() {
        return getProduct().getSellingPrice() * getQuantity();
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }
    
}
