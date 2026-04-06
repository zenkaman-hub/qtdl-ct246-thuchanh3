/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ct246_ql_cuahang_taphoa.ui;

import ct246_ql_cuahang_taphoa.util.SessionManager;
import java.util.Scanner;
import ct246_ql_cuahang_taphoa.service.InventoryService;

public class StaffUI {
    private Scanner scanner = new Scanner(System.in);
    private InventoryService inventoryService = new InventoryService();
    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU NHÂN VIÊN BÁN HÀNG ---");
            System.out.println("Chào mừng: " + SessionManager.getCurrentUser().getUsername());
            // Sửa lại đoạn in Menu trong StaffUI.java
            System.out.println("1. Tạo hóa đơn mới (Bán hàng)");
            System.out.println("2. Kiểm tra giá & Tồn kho");
            System.out.println("3. Nhập hàng vào kho");       // Sửa lại dòng này
            System.out.println("4. Tra cứu thông tin khách hàng"); // Thêm dòng này để khớp với case 4
            System.out.println("0. Đăng xuất");
            System.out.print("Vui lòng chọn chức năng: ");
            
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Đang mở màn hình Bán hàng...");
                    // Gọi hàm từ SalesService tại đây
                    break;
                case 2:
                    System.out.println("Đang kiểm tra kho...");
                    break;
                case 3:
                    // Gọi hàm xử lý nhập hàng đã được tách riêng bên dưới
                    handleRestock();
                    break;
                case 4:
                    System.out.println("Đang mở màn hình Tra cứu khách hàng...");
                    // TODO: Gọi hàm từ CustomerService tại đây
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
            // Giả định class Employee (hoặc User) của bạn có hàm getEmployeeId()
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
}


