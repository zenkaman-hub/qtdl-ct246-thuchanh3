/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ct246_ql_cuahang_taphoa.service;
import ct246_ql_cuahang_taphoa.dao.InventoryDAO;
/**
 *
 * @author vothanhdatthinh
 */
public class InventoryService {
    private InventoryDAO inventoryDAO;

    public InventoryService() {
        this.inventoryDAO = new InventoryDAO();
    }
    // Logic kho: Nhập hàng (Restock) 
    public boolean restock(int productId, int employeeId, int quantity) {
        // Kiểm tra tính hợp lệ của dữ liệu trước khi gọi DB
        if (quantity <= 0) {
            return false; 
        }
        // Gọi DAO để thực hiện Transaction cập nhật CSDL
        return inventoryDAO.restockTransaction(productId, employeeId, quantity);
    }
}
