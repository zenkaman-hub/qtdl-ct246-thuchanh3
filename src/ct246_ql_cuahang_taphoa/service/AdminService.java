package ct246_ql_cuahang_taphoa.service;

import ct246_ql_cuahang_taphoa.dao.ProductDAO;
import ct246_ql_cuahang_taphoa.model.Product;

public class AdminService {
    private ProductDAO productDAO = new ProductDAO();
    // tao san pham moi
    public void createNewProduct(String barcode, String name, int catId, int supId, String unit, double cost, double price, int stock) {
        // Kiểm tra giá bán phải lớn hơn giá vốn
        if (price < cost) {
            System.out.println("Lỗi: Giá bán không được nhỏ hơn giá vốn!");
            return;
        }
        // Tạo đối tượng Product và truyền xuống DAO
        Product newProduct = new Product(barcode, name, catId, supId, unit, cost, price, stock);
        boolean isSuccess = productDAO.addProduct(newProduct);

        if (isSuccess) {
            System.out.println(" Thêm sản phẩm '" + name + "' thành công!");
        } else {
            System.out.println(" Thêm sản phẩm thất bại. Vui lòng kiểm tra lại.");
        }
    }
    
    // Kiểm tra logic khi đổi giá sản phẩm
    public String changeProductPrice(int ProductId,double newPrice){
        if(newPrice < 0){
            return "Lỗi: giá bán không được âm";
        }
        //Lấy giá nhập từ database
        double costPrice = productDAO.getCostPrice(ProductId);

        if (costPrice == -1) {
            return "Lỗi: Không tìm thấy sản phẩm có ID " + ProductId;
        }

        // Giá bán phải cao hơn giá nhập
        if (newPrice <= costPrice) {
            return "Lỗi: Giá bán mới (" + newPrice + ") phải cao hơn giá nhập (" + costPrice + ")";
        }

        boolean success = productDAO.updatePrice(ProductId, newPrice);
        return success ? "Thành công" : "Lỗi: Không thể cập nhật vào cơ sở dữ liệu.";
    } 
}
