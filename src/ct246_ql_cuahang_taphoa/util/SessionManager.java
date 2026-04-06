/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ct246_ql_cuahang_taphoa.util;

import ct246_ql_cuahang_taphoa.model.Employee;
/**
 *
 * @author vothanhdatthinh
 */
public class SessionManager {
    private static Employee currentUser;
    public static void setCurrentUser(Employee employee){
        currentUser = employee;
    }
    public static Employee getCurrentUser(){
        return  currentUser;
    }
    public static void clearSession(){
        currentUser = null;
    }
    
}
