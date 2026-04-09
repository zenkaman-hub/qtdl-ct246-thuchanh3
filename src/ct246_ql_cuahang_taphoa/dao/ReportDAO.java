/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ct246_ql_cuahang_taphoa.dao;
import ct246_ql_cuahang_taphoa.config.DatabaseConfig;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class ReportDAO {
    
    //mảng double: [0] là doanh thu, [1] là lợi nhuận
    public double[] getDailyRevenueAndProfit(java.sql.Date reportDate) {
        double[] result = new double[2];
        String sql = "{CALL sp_CalculateRevenue(?, ?, ?)}"; 
        
        try (Connection conn = DatabaseConfig.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {
            
//            Truyền tham số IN (Ngày muốn báo cáo)
            cstmt.setDate(1, reportDate);
            
            //Đăng ký tham số OUT (Doanh thu và Lợi nhuận)
            cstmt.registerOutParameter(2, Types.DECIMAL);
            cstmt.registerOutParameter(3, Types.DECIMAL);
            
            //Thực thi thủ tục
            cstmt.execute();
            
            // Lấy kết quả từ biến OUT lưu vào mảng
            result[0] = cstmt.getDouble(2); // total_revenue
            result[1] = cstmt.getDouble(3); // total_profit
            
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy báo cáo: " + e.getMessage());
        }
        return result;
    }
}