package ct246_ql_cuahang_taphoa.model;

import java.sql.Timestamp;

public class InventoryLog {
    private int logId;
    private String productName;
    private String employeeName;
    private int changeQuantity;
    private String reason; //"Nhập hàng", "Bán hàng", "Hủy hàng"
    private Timestamp createdAt;
    // Hàm khởi tạo
    public InventoryLog(int logId, String productName, String employeeName, 
                           int changeQuantity, String reason, Timestamp createdAt) {
        this.logId = logId;
        this.productName = productName;
        this.employeeName = employeeName;
        this.changeQuantity = changeQuantity;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    /**
     * @return the logId
     */
    public int getLogId() {
        return logId;
    }

    /**
     * @param logId the logId to set
     */
    public void setLogId(int logId) {
        this.logId = logId;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the changeQuantity
     */
    public int getChangeQuantity() {
        return changeQuantity;
    }

    /**
     * @param changeQuantity the changeQuantity to set
     */
    public void setChangeQuantity(int changeQuantity) {
        this.changeQuantity = changeQuantity;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the createdAt
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
}
