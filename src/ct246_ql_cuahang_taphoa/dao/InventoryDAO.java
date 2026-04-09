package ct246_ql_cuahang_taphoa.dao;

import ct246_ql_cuahang_taphoa.config.DatabaseConfig;
import java.sql.*;

public class InventoryDAO {
    public boolean restockTransaction(int productId, int employeeId, int quantity) {
       boolean isSuccess = false;
        
        // Gọi Procedure có 3 tham số IN và 1 tham số OUT 
        String sql = "{CALL sp_RestockProduct(?, ?, ?, ?)}";

        try (Connection conn = DatabaseConfig.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
            // Truyền các tham số đầu vào (IN)
            cstmt.setInt(1, productId);
            cstmt.setInt(2, employeeId);
            cstmt.setInt(3, quantity);
            
            //Đăng ký tham số đầu ra (OUT) ở vị trí số 4 với kiểu dữ liệu là Số nguyên (INTEGER)
            cstmt.registerOutParameter(4, Types.INTEGER);

            //Thực thi Procedure
            cstmt.execute();

            //Lấy kết quả từ tham số OUT (1 = Thành công, 0 = Thất bại)
            int result = cstmt.getInt(4);
            
            if (result == 1) {
                isSuccess = true;
            }

        } catch (SQLException e) {
            System.out.println("Lỗi Database khi gọi Procedure nhập hàng: " + e.getMessage());
        }
        
        return isSuccess;
    }
}
