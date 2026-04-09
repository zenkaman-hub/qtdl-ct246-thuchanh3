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
        
        // Gọi Function fn_GetCustomerTier trong câu SELECT
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
    //Lấy điểm khách hàng dựa trên id
    public int getCustomerPoints(int customerId) {
        // id = 1 là Khách vãng lai, không có điểm
        if (customerId == 1) {
            return 0; 
        }
        
        String sql = "SELECT points FROM customers WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("points"); // Lấy cột điểm trả về
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi khi lấy điểm khách hàng: " + e.getMessage());
        }
        return 0; 
    }
    // Hàm lấy hạng của khách hàng dựa vào ID
    public String getCustomerTier(int customerId) {
        if (customerId == 1) return "Khách vãng lai";
        
        String sql = "SELECT fn_GetCustomerTier(points) AS tier FROM customers WHERE customer_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("tier");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy hạng khách hàng: " + e.getMessage());
        }
        return "Thường";
    }
}