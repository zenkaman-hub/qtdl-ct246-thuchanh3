/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ct246_ql_cuahang_taphoa.ui;

import ct246_ql_cuahang_taphoa.service.CustomerService;
import ct246_ql_cuahang_taphoa.util.SessionManager;
import java.util.Scanner;
import ct246_ql_cuahang_taphoa.service.InventoryService;
import ct246_ql_cuahang_taphoa.service.SalesService;
import ct246_ql_cuahang_taphoa.dao.CustomerDAO;

public class StaffUI {
    private Scanner scanner = new Scanner(System.in);
    private InventoryService inventoryService = new InventoryService();
    private SalesService salesService = new SalesService();
    private CustomerService customerService = new CustomerService();
    
    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU NHÂN VIÊN BÁN HÀNG ---");
            System.out.println("Chào mừng: " + SessionManager.getCurrentUser().getUsername());
            // Sửa lại đoạn in Menu trong StaffUI.java
            System.out.println("1. Tạo hóa đơn mới (Bán hàng)");
            System.out.println("2. Kiểm tra giá & Tồn kho");
            System.out.println("3. Nhập hàng vào kho");   
            System.out.println("4. Tra cứu thông tin khách hàng"); 
            System.out.println("0. Đăng xuất");
            System.out.print("Vui lòng chọn chức năng: ");
            
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    handleSalesPOS();
                    break;
                case 2:
                    System.out.println("Đang kiểm tra kho...");
                    handleCheckInventory();
                    break;
                case 3:
                    handleRestock();
                    break;
                case 4:
                    System.out.println("Đang mở màn hình Tra cứu khách hàng...");
                    handleCustomerLookup();
                    break;
                case 0:
                    System.out.println("Đang đăng xuất...");
                    SessionManager.clearSession();
                    running = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }
    private void handleRestock() {
        System.out.println("\n--- NHẬP HÀNG VÀO KHO ---");
        try {
            System.out.print("Nhập ID sản phẩm cần nhập (product_id): ");
            int productId = Integer.parseInt(scanner.nextLine());

            System.out.print("Nhập số lượng muốn thêm vào kho: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            // Lấy ID nhân viên đang thực hiện thao tác từ Session hiện tại
            int employeeId = SessionManager.getCurrentUser().getId(); 

            // Gọi service để thực hiện Transaction
            boolean isSuccess = inventoryService.restock(productId, employeeId, quantity);

            if (isSuccess) {
                System.out.println("Nhập hàng thành công! Đã cập nhật số lượng và lưu lịch sử.");
            } else {
                System.out.println("Nhập hàng thất bại! Vui lòng kiểm tra lại ID sản phẩm hoặc số lượng.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: ID sản phẩm và số lượng phải là số!");
        } catch (Exception e) {
            System.out.println("Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
    }
    private void handleSalesPOS() {
        boolean selling = true;
        while (selling) {
            // Luôn in giỏ hàng ở đầu mỗi lần lặp để nhân viên dễ nhìn
            System.out.println("\n\n==== Giỏ hàng hiện tại ====");
            salesService.showCart(); 
            
            System.out.println("\n[THAO TÁC HÓA ĐƠN]");
            System.out.println("1. Nhập mã vạch thêm vào giỏ");
            System.out.println("2. Hủy giỏ hàng (Khách đổi ý)");
            System.out.println("3. THANH TOÁN & IN HÓA ĐƠN");
            System.out.println("0. Tạm ngưng (Quay lại menu chính)");
            System.out.print("Chọn thao tác: ");
            
            int action = Integer.parseInt(scanner.nextLine()); 
                switch (action) {
                    case 1:
                        System.out.print("Nhập id san pham : ");
                        int id = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nhập số lượng: ");
                        int qty = Integer.parseInt(scanner.nextLine());
                        salesService.addToCart(id, qty);
                        break;
                    case 2:
                        salesService.clearCart();
                        break;
                    case 3:
                        handleCheckout();
                        break;
                    case 0:
                        selling = false;
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ.");
                }
        }
    }
    
    
    private void handleCustomerLookup() {
        System.out.println("\n--- TRA CỨU THÔNG TIN KHÁCH HÀNG ---");
        System.out.print("Nhập số điện thoại khách hàng: ");
        String phone = scanner.nextLine().trim();
        
        Object[] customerData = customerService.searchCustomer(phone);
        
        if (customerData != null) {
            System.out.println("\n----------------------------------------");
            System.out.println(" THÔNG TIN THÀNH VIÊN");
            System.out.println("----------------------------------------");
            System.out.println(" Tên khách hàng   : " + customerData[0]);
            System.out.println(" Điểm tích lũy    : " + customerData[1] + " điểm");
            System.out.println(" Hạng thành viên  : " + customerData[2]);
            System.out.println(" Ngày mua gần nhất: " + customerData[3]);
            System.out.println("----------------------------------------");
            
            if ("VIP".equals(customerData[2])) {
                System.out.println("Đây là khách hàng VIP, áp dụng giảm giá 10% khi thanh toán!");
            }
        } else {
            // Nếu format đúng nhưng không tìm thấy trong DB thì customerData vẫn null
            // Tuy nhiên, do Service đã chặn null nếu sai format, 
            // ta cần check xem lỗi từ định dạng hay do không có thật.
            if (phone.matches("\\d{10}")) {
                System.out.println(" Không tìm thấy khách hàng nào với số điện thoại: " + phone);
                System.out.println("=> Gợi ý: Hãy tạo mới khách hàng lúc thanh toán!");
            }
        }
    }
    private void handleCheckInventory() {
        System.out.println("\n--- KIỂM TRA GIÁ & TỒN KHO ---");
        try {
            System.out.print("Nhập ID sản phẩm cần kiểm tra: ");
            int productId = Integer.parseInt(scanner.nextLine());

            // Gọi qua Service
            String[] info = inventoryService.checkProduct(productId);

            if (info != null) {
                System.out.println("\n TÌM THẤY SẢN PHẨM:");
                System.out.println("--------------------------------");
                System.out.println("Tên sản phẩm : " + info[0]);
                System.out.println("Giá bán      : " + info[1] + " VNĐ");
                
                int stock = Integer.parseInt(info[2]);
                if (stock > 0) {
                    System.out.println("Tồn kho      : " + stock + " (Sẵn sàng bán)");
                } else {
                    System.out.println("Tồn kho       : " + stock + " (ĐÃ HẾT HÀNG!)");
                }
                System.out.println("--------------------------------");
            } else {
                System.out.println("Không tìm thấy sản phẩm nào có ID: " + productId);
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: ID sản phẩm phải là số!");
        }
    }
    
    
    // Hàm hiển thị thanh toán
    private void handleCheckout() {
        System.out.println("\n--- TIẾN HÀNH THANH TOÁN ---");
        
        // Nhập id khách hàng
        System.out.print("Nhập ID Khách hàng (Nhấn Enter nếu là Khách vãng lai): ");
        String input = scanner.nextLine().trim();
        int customerId = (input.isEmpty() || input.equals("0")) ? 1 : Integer.parseInt(input);

        int usedPoints = 0;

        // Xử lý khách thành viên
        if (customerId != 1) {
            CustomerDAO customerDAO = new CustomerDAO();
            int currentPoints = customerDAO.getCustomerPoints(customerId);
            
            System.out.println("========================================");
            System.out.println("Khách hàng thành viên ID: " + customerId);
            System.out.println("SỐ ĐIỂM HIỆN TẠI: " + currentPoints + " điểm (Tương đương " + (currentPoints) + " VNĐ)");
            System.out.println("========================================");
            
            if (currentPoints > 0) {
                while (true) {
                    System.out.print("Khách muốn sử dụng bao nhiêu điểm? (Nhập 0 nếu không dùng): ");
                    try {
                        usedPoints = Integer.parseInt(scanner.nextLine());
                        if (usedPoints >= 0 && usedPoints <= currentPoints) {
                            break;
                        } else {
                            System.out.println("Lỗi: Điểm nhập vào lớn hơn số điểm đang có hoặc bị âm!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Lỗi: Vui lòng nhập một số nguyên!");
                    }
                }
            } else {
                System.out.println("Khách hàng chưa có điểm để sử dụng.");
            }
        } else {
            System.out.println("Khách vãng lai không được tích/đổi điểm).");
        }

        double discountAmount = 0;
        
        // Lấy ID nhân viên đang đăng nhập
        int empId = SessionManager.getCurrentUser().getId(); 
        System.out.print("Xác nhận thanh toán hóa đơn này? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("Y")) {
            // Chuyển thông tin qua Service để xử lý Transaction
            salesService.checkout(empId, customerId, discountAmount, usedPoints);
        } else {
            System.out.println("Đã hủy lệnh thanh toán. Giỏ hàng vẫn được giữ nguyên.");
        }
        
    }
}
