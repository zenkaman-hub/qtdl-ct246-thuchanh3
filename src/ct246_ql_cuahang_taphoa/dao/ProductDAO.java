
package ct246_ql_cuahang_taphoa.dao;
import ct246_ql_cuahang_taphoa.model.Product;
import ct246_ql_cuahang_taphoa.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    public List<Product> getLowStockProducts() {
        List<Product> lowStockList = new ArrayList<>();
        // Gọi thủ tục sp_GetLowStockProducts()
        String sql = "{CALL sp_GetLowStockProducts()}";
        try (Connection conn = DatabaseConfig.getConnection();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {
            // Duyệt qua từng dòng kết quả trả về từ Procedure
            while (rs.next()) {
                Product p = new Product(
                    rs.getString("barcode"),
                    rs.getString("product_name"),
                    rs.getInt("category_id"),
                    rs.getInt("supplier_id"),
                    rs.getString("unit"),    
                    rs.getDouble("cost_price"),
                    rs.getDouble("selling_price"),
                    rs.getInt("stock_quantity")
                );
                lowStockList.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách sắp hết hàng: " + e.getMessage());
        }
        return lowStockList;
    }
    
    // Tìm sản phẩm theo id
    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Product(
                    rs.getString("barcode"),
                    rs.getString("product_name"),
                    rs.getInt("category_id"),
                    rs.getInt("supplier_id"),
                    rs.getString("unit"),
                    rs.getDouble("cost_price"),
                    rs.getDouble("selling_price"),
                    rs.getInt("stock_quantity")
                );
            }
        } catch (SQLException e) {
            System.out.println("Lỗi truy vấn sản phẩm: ");
        }
        return null; // Không tìm thấy
    }
}
