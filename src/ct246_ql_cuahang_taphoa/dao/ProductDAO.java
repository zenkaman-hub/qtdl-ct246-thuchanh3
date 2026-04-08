
package ct246_ql_cuahang_taphoa.dao;
import ct246_ql_cuahang_taphoa.model.Product;
import ct246_ql_cuahang_taphoa.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

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
    
    // phuong thuc cap nhat gia ban le 
    public boolean updatePrice(int productID, double newPrice){
        String sql = "UPDATE products SET selling_price = ? where product_id = ?";
        try(Connection conn = DatabaseConfig.getConnection()){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, productID);
            
           int affectedRow = pstmt.executeUpdate();
           return affectedRow > 0; // trả về True nếu cập nhật thành công ít nhất 1 dòng 
        }catch(SQLException e){
            System.out.println("Lỗi SQL khi đổi giá" + e.getMessage());
            return false;
        }
    }
    public double getCostPrice(int productId) {
        String sql = "SELECT cost_price FROM products WHERE product_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("cost_price");
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy giá nhập: " + e.getMessage());
        }
        return -1; // Trả về -1 nếu không tìm thấy sản phẩm
    }
}
