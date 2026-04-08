package ct246_ql_cuahang_taphoa.dao;

import ct246_ql_cuahang_taphoa.model.InventoryLog;
import ct246_ql_cuahang_taphoa.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryLogDAO {
    public List<InventoryLog> getInventoryHistory() {
        List<InventoryLog> list = new ArrayList<>();
        
        // Lệnh JOIN 3 bảng products, employee, inventorylog: Lấy 50 lịch sử mới nhất
        String sql = "SELECT i.log_id, p.product_name, e.username, i.change_quantity, i.reason, i.created_at " +
                     "FROM inventory_logs i " +
                     "JOIN products p ON i.barcode = p.barcode " +
                     "JOIN employees e ON i.employee_id = e.employee_id " +
                     "ORDER BY i.created_at DESC LIMIT 50";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                InventoryLog log = new InventoryLog(
                    rs.getInt("log_id"),
                    rs.getString("product_name"),
                    rs.getString("full_name"),
                    rs.getInt("change_quantity"),
                    rs.getString("reason"),
                    rs.getTimestamp("created_at")
                );
                list.add(log);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi truy vấn lịch sử kho: " + e.getMessage());
        }
        return list;
    }
}
