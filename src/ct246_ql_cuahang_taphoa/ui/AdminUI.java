/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ct246_ql_cuahang_taphoa.ui;

import ct246_ql_cuahang_taphoa.util.SessionManager;
import java.util.Scanner;
import java.io.PrintStream;


/**
 *
 * @author vothanhdatthinh
 */
public class AdminUI {
    private Scanner scanner;
     public AdminUI() {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
            this.scanner = new Scanner(System.in, "UTF-8"); // Gán bảng mã UTF-8 
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            this.scanner = new Scanner(System.in); //Nếu lỗi thì dùng scanner thường
        }
    }
    
    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU QUẢN LÝ (ADMIN) ---");
            System.out.println("Chào mừng: " + SessionManager.getCurrentUser().getUsername());
            System.out.println("1. Quản lý nhân viên");
            System.out.println("2. Quản lý kho hàng & Sản phẩm");
            System.out.println("3. Xem báo cáo doanh thu");
            System.out.println("4. Thiết lập hệ thống (Đổi giá, khuyến mãi)");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn chức năng: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("Đang mở chức năng Quản lý nhân viên...");
                    // Gọi hàm từ AdminService tại đây
                    break;
                case 2:
                    System.out.println("Đang mở chức năng Quản lý kho...");
                    break;
                case 3:
                    System.out.println("Đang mở chức năng Báo cáo...");
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

