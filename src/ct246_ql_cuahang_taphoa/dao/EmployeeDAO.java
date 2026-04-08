/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ct246_ql_cuahang_taphoa.dao;

import ct246_ql_cuahang_taphoa.config.DatabaseConfig;
import ct246_ql_cuahang_taphoa.model.Employee;
import java.sql.*;

/**
 *
 * @author vothanhdatthinh
 */
public class EmployeeDAO {
    public Employee checkLogin(String username, String password){
        String query="select * from employees where username = ? and password = ?";
        try(Connection conn = DatabaseConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next()){
                Employee emp = new Employee();
                emp.setId(rs.getInt("employee_id"));
                emp.setUsername(rs.getString("username"));       
                emp.setRole(rs.getString("role"));
                return emp;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    
//    Thêm nhân viên mới với đầy đủ các cột
    public boolean insertEmployee(String username, String password, String fullName, 
                                  String phone, String email, String role, double salary) {
        
        String sql = "INSERT INTO employees (username, password, full_name, phone, email, role, salary, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, 'ACTIVE')";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); 
            pstmt.setString(3, fullName);
            pstmt.setString(4, phone.isEmpty() ? null : phone); // Cho phép null nếu không nhập
            pstmt.setString(5, email.isEmpty() ? null : email); // Cho phép null nếu không nhập
            pstmt.setString(6, role.toUpperCase());
            pstmt.setDouble(7, salary);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi SQL khi thêm nhân viên: " + e.getMessage());
            return false;
        }
    }
    
    //Khóa tài khoản nhân viên 
    public boolean lockEmployee(int employeeId) {
        String sql = "UPDATE employees SET status = 'INACTIVE' WHERE employee_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employeeId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; 
        } catch (SQLException e) {
            System.out.println("Lỗi SQL khi khóa nhân viên: " + e.getMessage());
            return false;
        }
    }
}
