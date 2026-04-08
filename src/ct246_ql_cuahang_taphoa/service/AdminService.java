package ct246_ql_cuahang_taphoa.service;

import ct246_ql_cuahang_taphoa.dao.ProductDAO;
import ct246_ql_cuahang_taphoa.model.Product;
import ct246_ql_cuahang_taphoa.dao.InventoryLogDAO;
import ct246_ql_cuahang_taphoa.model.InventoryLog;

public class AdminService {
    private ProductDAO productDAO = new ProductDAO();
    private InventoryLogDAO inventoryDAO = new InventoryLogDAO();
            
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
    
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    // Thêm tài khoản nhân viên mới
    public String addNewEmployee(String username, String password, String fullName, 
                                 String phone, String email, String role, double salary) {
        // Kiểm tra dữ liệu bắt buộc (NOT NULL)
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
            return "Lỗi: Tên đăng nhập, mật khẩu và họ tên không được để trống!";
        }
        
        // Kiểm tra Role có hợp lệ không
        if (!role.equalsIgnoreCase("ADMIN") && !role.equalsIgnoreCase("STAFF")) {
            return "Lỗi: Chức vụ chỉ có thể là ADMIN hoặc STAFF!";
        }
        
        // Kiểm tra lương
        if (salary < 0) {
            return "Lỗi: Mức lương không thể là số âm!";
        }

        boolean success = employeeDAO.insertEmployee(username, password, fullName, phone, email, role, salary);
        return success ? "Thành công" : "Lỗi: Không thể thêm nhân viên (Có thể Username đã tồn tại).";
    }
    
    // Khóa tài khoản nhân viên
    public String lockEmployeeAccount(int employeeId, int currentAdminId) {
        if (employeeId == currentAdminId) {
            return "Lỗi: Bạn không thể tự khóa tài khoản của chính mình!";
        }

        boolean success = employeeDAO.lockEmployee(employeeId);
        return success ? "Thành công" : "Lỗi: Không tìm thấy nhân viên với ID này.";
    // Hàm cảnh báo sắp hết hàng
    public void checkLowStockWarning() {
        // Gọi productDAO.getLowStockProducts() để lấy danh sách sản phẩm sắp hết từ thủ tục
        var lowStockProducts = productDAO.getLowStockProducts();
        System.out.println("\n === BÁO CÁO CẢNH BÁO KHO HÀNG === ");
        
        if (lowStockProducts.isEmpty()) {
            System.out.println("Không có sản phẩm nào sắp hết!");
        } else {
            System.out.println("                               NHỮNG MẶT HÀNG SẮP HẾT");
            System.out.println("----------------------------------------------------------------------------------");
            System.out.printf("| %-15s | %-25s | %-10s | %-15s |\n", "Mã vạch", "Tên sản phẩm", "Đơn vị", "Tồn kho hiện tại");
            System.out.println("----------------------------------------------------------------------------------");
            
            for (Product p : lowStockProducts) {
                System.out.printf("| %-15s | %-25s | %-10s | %-15d |\n", 
                                  p.getBarcode(), 
                                  p.getProductName(), 
                                  p.getUnit(), 
                                  p.getStockQuantity());
            }
            System.out.println("----------------------------------------------------------------------------------");
        }
    }
    
    //Hàm hiển thị lịch sử kho
    public void showInventoryLogs() {
        var logs = inventoryDAO.getInventoryHistory();
        System.out.println("\n                                 === LỊCH SỬ BIẾN ĐỘNG KHO ===");
        
        if (logs.isEmpty()) {
            System.out.println("Chưa có dữ liệu lịch sử trong kho!");
            return;
        }

        // Vẽ tiêu đề bảng
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-5s | %-30s | %-15s | %-10s | %-15s | %-20s |\n", 
                "ID", "Tên sản phẩm", "Nhân viên", "Số lượng", "Lý do", "Thời gian");
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        
        // Lặp qua danh sách và in từng dòng
        for (InventoryLog log : logs) {
            // Dấu + hiển thị xanh nếu nhập hàng, trừ hiển thị bình thường
            String quantityStr = (log.getChangeQuantity() > 0 ? "+" : "") + log.getChangeQuantity();
            
            System.out.printf("| %-5d | %-30s | %-15s | %-10s | %-15s | %-20s |\n", 
                    log.getLogId(), 
                    log.getProductName(), 
                    log.getEmployeeName(), 
                    quantityStr, 
                    log.getReason(), 
                    // Cắt bỏ phần mili-giây của Timestamp )
                    log.getCreatedAt().toString().substring(0, 19));
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------");
    }
}
