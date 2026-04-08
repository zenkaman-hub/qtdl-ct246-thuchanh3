package ct246_ql_cuahang_taphoa.dao;

import ct246_ql_cuahang_taphoa.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InventoryDAO {
    public boolean restockTransaction(int productId, int employeeId, int quantity) {
        Connection conn = null;
        PreparedStatement updateProductStmt = null;
        PreparedStatement insertLogStmt = null;
        boolean isSuccess = false;

        try {
            conn = DatabaseConfig.getConnection();
            
            // Tắt auto-commit để bắt đầu Transaction 
            conn.setAutoCommit(false);

            // Bước 1: Cộng thêm số lượng vào stock_quantity của bảng products 
            String updateSql = "UPDATE products SET stock_quantity = stock_quantity + ? WHERE product_id = ?";
            updateProductStmt = conn.prepareStatement(updateSql);
            updateProductStmt.setInt(1, quantity);
            updateProductStmt.setInt(2, productId);
            
            int rowsAffected = updateProductStmt.executeUpdate();
            if (rowsAffected == 0) {
                // Không tìm thấy sản phẩm (sai ID), rollback và kết thúc
                conn.rollback();
                return false;
            }

            // Bước 2: Thêm dòng nhật ký vào bảng inventory_logs 
            String logSql = "INSERT INTO inventory_logs (product_id, employee_id, change_quantity, reason) VALUES (?, ?, ?, ?)";
            insertLogStmt = conn.prepareStatement(logSql);
            insertLogStmt.setInt(1, productId);   
            insertLogStmt.setInt(2, employeeId);  
            insertLogStmt.setInt(3, quantity);   
            insertLogStmt.setString(4, "Nhập hàng"); 
            insertLogStmt.executeUpdate();

            // Nếu không có lỗi xảy ra thì COMMIT để lưu toàn bộ thay đổi 
            conn.commit();
            isSuccess = true;

        } catch (SQLException e) {
            // Nếu có lỗi hệ thống, ROLLBACK lại toàn bộ để tránh dữ liệu sai lệch 
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Lỗi Transaction: " + e.getMessage());
        } finally {
            // Đóng kết nối và dọn dẹp bộ nhớ
            try {
                if (updateProductStmt != null) updateProductStmt.close();
                if (insertLogStmt != null) insertLogStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Trả lại trạng thái mặc định
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }
}
