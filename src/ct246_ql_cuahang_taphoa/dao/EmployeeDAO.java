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
}
