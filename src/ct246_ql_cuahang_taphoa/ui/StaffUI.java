/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ct246_ql_cuahang_taphoa.ui;

import ct246_ql_cuahang_taphoa.util.SessionManager;
import java.util.Scanner;

public class StaffUI {
    private Scanner scanner = new Scanner(System.in);

    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU NHÂN VIÊN BÁN HÀNG ---");
            System.out.println("Chào mừng: " + SessionManager.getCurrentUser().getUsername());
            System.out.println("1. Tạo hóa đơn mới (Bán hàng)");
            System.out.println("2. Kiểm tra giá & Tồn kho");
            System.out.println("3. Tra cứu thông tin khách hàng");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn chức năng: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Đang mở màn hình Bán hàng...");
                    // Gọi hàm từ SalesService tại đây
                    break;
                case 2:
                    System.out.println("Đang kiểm tra kho...");
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
}


