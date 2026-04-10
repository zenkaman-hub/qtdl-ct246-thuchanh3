
package ct246_ql_cuahang_taphoa.service;

import ct246_ql_cuahang_taphoa.dao.ProductDAO;
import ct246_ql_cuahang_taphoa.model.CartItem;
import ct246_ql_cuahang_taphoa.model.Product;
import ct246_ql_cuahang_taphoa.dao.OrderDAO;
import ct246_ql_cuahang_taphoa.dao.CustomerDAO;


import java.util.HashMap;
import java.util.Map;

public class SalesService {
    private ProductDAO productDAO = new ProductDAO(); 
    private OrderDAO orderDAO = new OrderDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    // Hashmap chứa các sản phẩm trong giỏ hàng
    private Map<Integer, CartItem> cart = new HashMap<>();

    public void addToCart(int id, int quantity) {
        if (quantity <= 0) {
            System.out.println("Số lượng phải lớn hơn 0!");
            return;
        }

        Product product = productDAO.getProductById(id);
        if (product == null) {
            System.out.println("Lỗi: Không tìm thấy sản phẩm với id '" + id + "'");
            return;
        }

        // Lấy số lượng hiện của sản phẩm đó có trong giỏ 
        int currentQtyInCart = cart.containsKey(id) ? cart.get(id).getQuantity() : 0;

        if (product.getStockQuantity() < (currentQtyInCart + quantity)) {
            System.out.println("Lỗi: Kho không đủ! Chỉ còn " + product.getStockQuantity() + " sản phẩm.");
            return;
        }

        if (cart.containsKey(id)) {
            CartItem item = cart.get(id);
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            cart.put(id, new CartItem(product, quantity));
        }
        System.out.println("Đã thêm " + quantity + " x [" + product.getProductName() + "] vào giỏ.");
    }
    //Xóa giỏ hàng hiện tại
    public void clearCart() {
        cart.clear();
        System.out.println("Đã hủy và xóa trắng giỏ hàng!");
    }
    //Hiển thị giỏ hàng
    public void showCart() {
        if (cart.isEmpty()) {
            System.out.println("\n[Giỏ hàng đang trống]");
            return;
        }
        System.out.printf("%-10s | %-25s | %-10s | %-15s | %-15s\n", "ID SP", "Tên sản phẩm", "SL", "Đơn giá", "Thành tiền");
        System.out.println("-----------------------------------------------------------------------------------------");
        
        double total = 0;
        // Duyệt qua Map có key là Integer
        for (Map.Entry<Integer, CartItem> entry : cart.entrySet()) {
            int productId = entry.getKey();
            CartItem item = entry.getValue();
            
            System.out.printf("%-10d | %-25s | %-10d | %-15.0f | %-15.0f\n", 
                    productId, 
                    item.getProduct().getProductName(), 
                    item.getQuantity(), 
                    item.getProduct().getSellingPrice(), 
                    item.getSubTotal());
            total += item.getSubTotal();
        }
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("TỔNG TIỀN TẠM TÍNH: %.0f VNĐ\n", total);
    }
    public Map<Integer, CartItem> getCart() { return cart; }
    
    //Hàm thanh toán giỏ hàng
    public void checkout(int employeeId, int customerId, double discountAmount, int usedPoints) {
        if (cart.isEmpty()) {
            System.out.println("Giỏ hàng đang trống, không thể thanh toán!");
            return;
        }

        // Tính tổng tiền gốc
        double totalAmount = 0;
        for (CartItem item : cart.values()) {
            totalAmount += item.getSubTotal();
        }
        // Tính tiền giảm cho khách VIP
        double vipDiscount = 0;
        if (customerId != 1) {
            String tier = customerDAO.getCustomerTier(customerId);
            if (tier != null && tier.equalsIgnoreCase("VIP")) {
                vipDiscount = totalAmount * 0.10;
            }
        }
        // Tính tiền giảm
        double discountFromPoints = usedPoints; // 1000 điểm = 1000 VNĐ
        double totalDiscount = vipDiscount + discountFromPoints + discountAmount;
        // Tính tiền thực khách phải trả
        double finalAmount = totalAmount - totalDiscount;
        if (finalAmount < 0) {
            System.out.println("Lỗi: Số tiền giảm giá vượt quá tổng tiền hóa đơn!");
            return;
        }

        // Gọi DB transaction
        boolean isSuccess = orderDAO.checkoutTransaction(employeeId, customerId, cart, totalAmount, totalDiscount, finalAmount, usedPoints);

        // In Bill ra màn hình
        if (isSuccess) {
            System.out.println("\n================================ HÓA ĐƠN THANH TOÁN ==================================");
            
            showCart(); 
            System.out.println("Tổng gốc:             " + totalAmount + " VNĐ");
            if (vipDiscount > 0) 
                System.out.println("Ưu đãi hạng VIP (10%):  -" + vipDiscount + " VNĐ");
            if (discountAmount > 0) 
                System.out.println("Voucher giảm:           -" + discountAmount + " VNĐ");
            if (usedPoints > 0) 
                System.out.println("Trừ điểm ("+usedPoints+"):          -" + discountFromPoints + " VNĐ");
            System.out.println("----------------------------------------------------------");
            System.out.println("THÀNH TIỀN:           " + finalAmount + " VNĐ");
            
            if (customerId != 1) {
                int earnedPoints = (int) (finalAmount / 100);
                System.out.println("Bạn được cộng " + earnedPoints + " điểm vào thẻ thành viên!");
            }
            System.out.println("=========================================================================================\n");
            
            cart.clear(); // Xóa sạch giỏ hàng để đón khách tiếp theo
        } else {
            System.out.println("Thanh toán thất bại! Vui lòng kiểm tra lại tồn kho.");
        }
    }
}
