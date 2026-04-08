// Cấu hình kết nối với Database

package ct246_ql_cuahang_taphoa.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    // Thông tin kết nối dành cho XAMPP
    private static final String URL = "jdbc:mysql://localhost:3306/qltaphoa";
    private static final String USER = "root";       
    private static final String PASSWORD = "";     

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Đăng ký Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Thực hiện kết nối
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            // System.out.println("-> KET NOI MYSQL THANH CONG!");
            
        } catch (ClassNotFoundException e) {
            System.out.println("Loi: Chua them thu vien MySQL Connector (file .jar) vao Libraries!");
        } catch (SQLException e) {
            System.out.println("Loi: Khong the ket noi! Ban da bat MySQL trong XAMPP chua? Hoac sai ten Database.");
        }
        return connection;
    }
}