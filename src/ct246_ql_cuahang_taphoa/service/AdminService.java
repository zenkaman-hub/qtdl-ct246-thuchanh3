package ct246_ql_cuahang_taphoa.service;

import ct246_ql_cuahang_taphoa.dao.ProductDAO;
import ct246_ql_cuahang_taphoa.model.Product;

public class AdminService {
    private ProductDAO productDAO = new ProductDAO();
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
}
