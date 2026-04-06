/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ct246_ql_cuahang_taphoa.service;
import ct246_ql_cuahang_taphoa.dao.EmployeeDAO;
import ct246_ql_cuahang_taphoa.model.Employee;
import ct246_ql_cuahang_taphoa.util.SessionManager;

/**
 *
 * @author vothanhdatthinh
 */

public class AuthService {
    private EmployeeDAO employeeDAO = new EmployeeDAO();
    
    public boolean login(String username, String password){
        Employee emp = employeeDAO.checkLogin(username, password);
        if(emp != null){
            SessionManager.setCurrentUser(emp);
            return true;
        }
        return false;
    }
    public void logout(){
        SessionManager.clearSession();
    }
}
