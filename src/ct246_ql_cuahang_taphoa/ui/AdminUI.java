package ct246_ql_cuahang_taphoa.ui;

import ct246_ql_cuahang_taphoa.util.SessionManager;
import ct246_ql_cuahang_taphoa.service.AdminService;

import java.util.Scanner;
import java.io.PrintStream;

public class AdminUI {
    private Scanner scanner;
    private AdminService adminService = new AdminService();
    public AdminUI() {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
            this.scanner = new Scanner(System.in, "UTF-8"); // Gán bảng mã UTF-8 
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            this.scanner = new Scanner(System.in); // Nếu lỗi thì dùng scanner thường
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
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println("Đang mở chức năng Quản lý nhân viên...");
                        manageEmployeesMenu();
                        break;
                    case 2:
                        // Gọi menu con Quản lý sản phẩm
                        manageProductsMenu(); 
                        break;
                    case 3:
                        System.out.println("Đang mở chức năng Báo cáo...");
                        break;
                    case 4:
                        System.out.println("Đang mở chức năng Thiết lập...");
                        settingsMenu();
                        break;
                    case 0:
                        System.out.println("Đang đăng xuất...");
                        SessionManager.clearSession();
                        running = false;
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ! Vui lòng chọn lại.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập một số nguyên!");
            }
        }
    }
    // Hàm xử lí hiện thị Menu con QL Sản Phẩm Kho
    private void manageProductsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- QUẢN LÝ SẢN PHẨM & KHO ---");
            System.out.println("1. Thêm sản phẩm mới");
            System.out.println("2. Cảnh báo sắp hết hàng (Tính năng dự kiến)");
            System.out.println("0. Quay lại menu chính");
            System.out.print("Chọn chức năng: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        showAddProductMenu(); // Gọi form nhập liệu thêm sản phẩm
                        break;
                    case 2:
                        System.out.println("Đang mở tính năng cảnh báo kho...");
                        break;
                    case 0:
                        back = true; // Thoát vòng lặp, tự động trở về menu chính
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập một số nguyên!");
            }
        }
    }
    
    private void showAddProductMenu() {
        System.out.println("\n--- THÊM SẢN PHẨM MỚI ---");
        
        try {
            System.out.print("Nhập mã vạch Barcode: ");
            String barcode = scanner.nextLine();
            
            System.out.print("Nhập tên sản phẩm: ");
            String name = scanner.nextLine();
            
            System.out.print("Nhập ID Danh mục (VD: 1-Đồ uống / 2-Bánh kẹo / 3-Gia vị / 4-Hóa mỹ phẩm): ");
            int catId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Nhập ID Nhà cung cấp: (VD: 1-Công ty Coca-Cola VN / 2-Acecook Việt Nam / 3-Unilever / 4-Massan Group): ");
            int supId = Integer.parseInt(scanner.nextLine());
            
            System.out.print("Nhập đơn vị tính (VD: Lon, Chai, Gói, Thùng): ");
            String unit = scanner.nextLine();
            
            System.out.print("Nhập giá vốn (VNĐ): ");
            double cost = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Nhập giá bán (VNĐ): ");
            double price = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Nhập số lượng tồn kho ban đầu: ");
            int stock = Integer.parseInt(scanner.nextLine());
            
            adminService.createNewProduct(barcode, name, catId, supId, unit,cost, price, stock);
            
            System.out.println("=> Đã gửi yêu cầu thêm sản phẩm: " + name);
            
        } catch (NumberFormatException e) {
            System.out.println("Lỗi nhập liệu: Giá tiền hoặc ID, Số lượng phải là số!");
        }
    }
    private void manageEmployeesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- QUẢN LÝ NHÂN VIÊN ---");
            System.out.println("1. Thêm nhân viên mới");
            System.out.println("2. Khóa tài khoản nhân viên (Nghỉ việc)");
            System.out.println("0. Quay lại menu chính");
            System.out.print("Chọn chức năng: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        handleAddEmployee(); // Gọi hàm thêm nhân viên
                        break;
                    case 2:
                        handleLockEmployee(); // Gọi hàm khóa nhân viên
                        break;
                    case 0:
                        back = true;
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập số!");
            }
        }
    }
    private void settingsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- THIẾT LẬP HỆ THỐNG ---");
            System.out.println("1. Đổi giá sản phẩm");
            System.out.println("0. Quay lại menu chính");
            System.out.print("Chọn chức năng: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        handleChangePrice(); // Gọi form đổi giá
                        break;
                    case 0:
                        back = true; // Quay lại menu trước đó
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Lỗi: Vui lòng nhập một số nguyên!");
            }
        }
    }
    
    // Hàm xử lý logic nhập liệu đổi giá
    private void handleChangePrice() {
        System.out.println("\n--- ĐỔI GIÁ SẢN PHẨM ---");
        try {
            System.out.print("Nhập ID sản phẩm cần đổi giá: ");
            int productId = Integer.parseInt(scanner.nextLine());

            System.out.print("Nhập giá bán mới (VNĐ): ");
            double newPrice = Double.parseDouble(scanner.nextLine());

            // Gọi service và nhận thông báo kết quả
            String result = adminService.changeProductPrice(productId, newPrice);

            if (result.equals("Thành công")) {
                System.out.println("" + result);
            } else {
                // Hiển thị các thông báo lỗi cụ thể (ví dụ: Giá bán thấp hơn giá nhập)
                System.out.println("" + result);
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: ID sản phẩm và Giá bán phải là số!");
        }
    }
    private void handleAddEmployee() {
        System.out.println("\n--- THÊM NHÂN VIÊN MỚI ---");
        try {
            System.out.print("Nhập Username (Tên đăng nhập bắt buộc): ");
            String username = scanner.nextLine().trim();
            
            System.out.print("Nhập Mật khẩu (Bắt buộc): ");
            String password = scanner.nextLine().trim();
            
            System.out.print("Nhập Họ và Tên (Bắt buộc): ");
            String fullName = scanner.nextLine().trim();
            
            System.out.print("Nhập Số điện thoại (Nhấn Enter để bỏ qua): ");
            String phone = scanner.nextLine().trim();
            
            System.out.print("Nhập Email (Nhấn Enter để bỏ qua): ");
            String email = scanner.nextLine().trim();
            
            System.out.print("Nhập Chức vụ (ADMIN hoặc STAFF): ");
            String role = scanner.nextLine().trim();
            if (role.isEmpty()) role = "STAFF"; // Default nếu không nhập

            System.out.print("Nhập mức lương (VNĐ - Nhập 0 nếu chưa có): ");
            String salaryInput = scanner.nextLine().trim();
            double salary = salaryInput.isEmpty() ? 0 : Double.parseDouble(salaryInput);

            // Gọi logic từ AdminService
            String result = adminService.addNewEmployee(username, password, fullName, phone, email, role, salary);
            
            if (result.equals("Thành công")) {
                System.out.println("Đã tạo tài khoản thành công cho: " + fullName);
            } else {
                System.out.println("" + result);
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: Mức lương phải là số!");
        }
    }
    
    // Hàm xử lý khóa tài khoản
    private void handleLockEmployee() {
        System.out.println("\n--- KHÓA TÀI KHOẢN NHÂN VIÊN ---");
        try {
            System.out.print("Nhập ID nhân viên cần khóa: ");
            int empId = Integer.parseInt(scanner.nextLine());

            System.out.print("Xác nhận khóa tài khoản này? (Y/N): ");
            if (scanner.nextLine().equalsIgnoreCase("Y")) {
                int adminId = SessionManager.getCurrentUser().getId();
                String result = adminService.lockEmployeeAccount(empId, adminId);
                System.out.println(result.equals("Thành công") ? "✅ Đã khóa!" : "❌ " + result);
            }
        } catch (NumberFormatException e) {
            System.out.println("Lỗi: ID phải là số!");
        }
    }
}