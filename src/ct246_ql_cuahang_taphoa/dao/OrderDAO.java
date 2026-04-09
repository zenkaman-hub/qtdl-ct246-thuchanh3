package ct246_ql_cuahang_taphoa.dao;

import ct246_ql_cuahang_taphoa.config.DatabaseConfig;
import ct246_ql_cuahang_taphoa.model.CartItem;
import java.sql.*;
import java.util.Map;

public class OrderDAO {

    public boolean checkoutTransaction(int employeeId, int customerId, Map<Integer, CartItem> cart, double totalAmount, double totalDiscount, double finalAmount, int usedPoints) {
        
        String insertOrderSql = "INSERT INTO orders (employee_id, customer_id, total_amount, discount_amount, final_amount, order_date) VALUES (?, ?, ?, ?, ?, NOW())";
        String insertItemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String updateStockSql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ?";
        // Điểm mới = Điểm hiện tại - Điểm đã dùng + Điểm khuyến mãi sau khi tạo hóa đơn
        String updatePointsSql = "UPDATE customers SET points = points - ? + ? WHERE customer_id = ?";

        Connection conn = null;

        try {
            conn = DatabaseConfig.getConnection();
            // Bắt đầu transaction
            conn.setAutoCommit(false); 

            // Tạo hóa đơn
            int orderId = -1;
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, employeeId);
                orderStmt.setInt(2, customerId);
                orderStmt.setDouble(3, totalAmount);
                orderStmt.setDouble(4, totalDiscount);
                orderStmt.setDouble(5, finalAmount);
                orderStmt.executeUpdate();

                ResultSet rs = orderStmt.getGeneratedKeys();
                if (rs.next()) orderId = rs.getInt(1);
            }

            // Lưu chi tiết hóa đơn và trừ kho
            try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSql);
                 PreparedStatement stockStmt = conn.prepareStatement(updateStockSql)) {
                
                for (Map.Entry<Integer, CartItem> entry : cart.entrySet()) {
                    int productId = entry.getKey(); 
                    CartItem item = entry.getValue();

                    // Thêm vào batch Order Items 
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, productId);
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getProduct().getSellingPrice());
                    itemStmt.addBatch(); 

                    // Thêm vào batch Update Stock
                    stockStmt.setInt(1, item.getQuantity());
                    stockStmt.setInt(2, productId);
                    stockStmt.addBatch();
                }
                // Thực thi các batch
                itemStmt.executeBatch();
                stockStmt.executeBatch();
            }

            // Cập nhật điểm bỏ qua nếu là khách vãng lai(id=1)
            if (customerId != 1) {
                try (PreparedStatement pointStmt = conn.prepareStatement(updatePointsSql)) {
                    pointStmt.setInt(1, usedPoints);
                    pointStmt.setDouble(2, (int)(finalAmount/100));
                    pointStmt.setInt(3, customerId);
                    pointStmt.executeUpdate();
                }
            }

            conn.commit(); //Commit giao dịch
            return true;

        } catch (SQLException e) {
            System.out.println("Lỗi Giao dịch CSDL: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { } // Rollback nếu lỗi bất kì
            }
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { }
            }
        }
    }
}