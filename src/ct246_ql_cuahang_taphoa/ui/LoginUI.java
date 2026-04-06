package ct246_ql_cuahang_taphoa.ui;
import ct246_ql_cuahang_taphoa.service.AuthService;
import ct246_ql_cuahang_taphoa.util.SessionManager;
import java.util.Scanner;
import java.io.PrintStream;

public class LoginUI {
    private AuthService authService = new AuthService();
    private Scanner scanner;
    public LoginUI() {
        try {
            // Đưa việc thiết lập vào trong hàm khởi tạo
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
            this.scanner = new Scanner(System.in, "UTF-8"); // Gán bảng mã UTF-8 khi khởi tạo
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            this.scanner = new Scanner(System.in); // Phương án dự phòng
        }
    }
    public void display(){
        while(true){
            System.out.println("=== HỆ THỐNG QUẢN LÝ TẠP HÓA ===");
            System.out.println("Username: ");
            String username = scanner.nextLine();
            System.out.println("Password: ");
            String password = scanner.nextLine();
            
            if(authService.login(username, password)){
                String role = SessionManager.getCurrentUser().getRole();
                System.out.println("Đăng nhập thành công! Xin chào" + username);
                
                if(role.equalsIgnoreCase("ADMIN")){
                    new AdminUI().display();
                } else{
                    new StaffUI().display();
                }
                break;    
            } else{
                System.out.println("Sai tài khoản hoặc mật khẩu. Vui lòng thử lại!)");
            }
        }
    }

    
}
