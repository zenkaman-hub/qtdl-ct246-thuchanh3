/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ct246_ql_cuahang_taphoa.service;

import ct246_ql_cuahang_taphoa.dao.CustomerDAO;

public class CustomerService {
    private CustomerDAO customerDAO = new CustomerDAO();
    
    public Object[] searchCustomer(String phone) {
        // Kiểm tra cơ bản: Số điện thoại không được rỗng và chứa 10 số
        if (phone == null || phone.trim().isEmpty() || !phone.matches("\\d{10}")) {
            System.out.println("Lỗi: Số điện thoại không hợp lệ (phải đúng 10 số).");
            return null;
        }
        return customerDAO.getCustomerInfoByPhone(phone);
    }
}
