/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ct246_ql_cuahang_taphoa.service;
import ct246_ql_cuahang_taphoa.dao.ReportDAO;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ReportService {
    private ReportDAO reportDAO = new ReportDAO();

    // Hàm xử lý logic lấy báo cáo
    public double[] getDailyReport(String dateString) {
        try {
            Date sqlDate;
            // Nếu không nhập gì (Enter), tự động lấy ngày hôm nay
            if (dateString == null || dateString.trim().isEmpty()) {
                sqlDate = Date.valueOf(LocalDate.now());
            } else {
                // Ép kiểu chuỗi dd/MM/yyyy thành java.sql.Date
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate localDate = LocalDate.parse(dateString, formatter);
                sqlDate = Date.valueOf(localDate);
            }
            
            // Trả về kết quả từ DAO
            return reportDAO.getDailyRevenueAndProfit(sqlDate);
            
        } catch (DateTimeParseException e) {
            System.out.println("Lỗi: Định dạng ngày không đúng! Vui lòng nhập theo dạng dd/MM/yyyy.");
            return null; // Trả về null nếu lỗi
        }
    }
}
