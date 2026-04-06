
package ct246_ql_cuahang_taphoa.dao;
import ct246_ql_cuahang_taphoa.model.Product;
import ct246_ql_cuahang_taphoa.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductDAO {
    public boolean addProduct(Product product) {
        // SQL thêm dữ liệu vào bảng product
        String sql = "INSERT INTO products (barcode, product_name, category_id, supplier_id, unit, cost_price, selling_price, stock_quantity) VALUES (?, ?, ?, ?,? , ?, ?, ?)";
                    

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Gán giá trị vào biến sql
            stmt.setString(1, product.getBarcode());
            stmt.setString(2, product.getProductName());
            stmt.setInt(3, product.getCategoryId());
            stmt.setInt(4, product.getSupplierId());
            stmt.setString(5, product.getUnit());
            stmt.setDouble(6, product.getCostPrice());
            stmt.setDouble(7, product.getSellingPrice());
            stmt.setInt(8, product.getStockQuantity());
            
            // Thực thi và kiểm tra xem có dòng nào được thêm không
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
            
        } catch (SQLException e) {
            System.out.println("Lỗi cơ sở dữ liệu: " + e.getMessage());// Báo lỗi nếu có
            return false;
        }
    }
}
