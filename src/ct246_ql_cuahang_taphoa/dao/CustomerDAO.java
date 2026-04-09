package ct246_ql_cuahang_taphoa.dao;


import ct246_ql_cuahang_taphoa.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {
    
    // Trả về mảng Object chứa: [Tên, Điểm, Hạng, Ngày mua gần nhất]
    public Object[] getCustomerInfoByPhone(String phone) {
        Object[] customerInfo = null;
        
        // Gọi Function fn_GetCustomerTier ngay trong câu SELECT
        // Dùng LEFT JOIN để lỡ khách chưa mua lần nào vẫn hiện thông tin
        String sql = "SELECT c.customer_name, c.points, fn_GetCustomerTier(c.points) AS tier, "
                   + "MAX(o.order_date) AS last_purchase "
                   + "FROM customers c "
                   + "LEFT JOIN orders o ON c.customer_id = o.customer_id "
                   + "WHERE c.phone = ? "
                   + "GROUP BY c.customer_id";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, phone);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    customerInfo = new Object[4];
                    customerInfo[0] = rs.getString("customer_name");
                    customerInfo[1] = rs.getInt("points");
                    customerInfo[2] = rs.getString("tier");
                    
                    // Xử lý ngày mua gần nhất (có thể null nếu khách chưa từng mua)
                    java.sql.Timestamp lastPurchase = rs.getTimestamp("last_purchase");
                    customerInfo[3] = (lastPurchase != null) ? lastPurchase : "Chưa có lịch sử mua hàng";
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());
        }
        
        return customerInfo;
    }
}