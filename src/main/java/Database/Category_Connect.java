/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Model.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.TableModel;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 *
 * @author nam11
 */
public class Category_Connect extends Connect_sqlServer {

    public Category_Connect() {
        super();
    }
    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM DanhMuc");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category category = new Category(rs.getString("MaDM"), rs.getString("TenDM"));
                categories.add(category);
            }
            rs.close();
            ps.close();
            // You may want to process the ResultSet here to populate the categories list
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    public boolean addCategory(Category category) throws SQLException {
    String sql = "INSERT INTO DanhMuc (MaDM, TenDM) VALUES (?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        // Tạo mã tự động (ví dụ: DM001, DM002,...)
        String newMaDM = generateNewMaDM(); 
        ps.setString(1, newMaDM);
        ps.setString(2, category.getTenDanhMuc());
        return ps.executeUpdate() > 0;
    }
}

private String generateNewMaDM() throws SQLException {
    String sql = "SELECT MAX(MaDM) FROM DanhMuc";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String lastMaDM = rs.getString(1);
            if (lastMaDM != null) {
                // Xử lý cả trường hợp có dấu gạch dưới
                String numberPart = lastMaDM.replaceAll("[^0-9]", ""); // Bỏ tất cả ký tự không phải số
                if (!numberPart.isEmpty()) {
                    int number = Integer.parseInt(numberPart) + 1;
                    return "dm_" + String.format("%02d", number);
                }
            }
        }
        return "DM001"; // Mặc định nếu không lấy được mã cũ
    }
}
    public void updateCategory(Category category) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE DanhMuc SET TenDM = ? WHERE MaDM = ?");
            ps.setString(1, category.getTenDanhMuc());
            ps.setString(2, category.getMaDanhMuc());
            ps.executeUpdate();
            ps.close();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean deleteCategory(String maDanhMuc) {
        try {
            // Kiểm tra ràng buộc khóa ngoại trước
            if (isCategoryInUse(maDanhMuc)) {
                return false; // Không thể xóa vì có sách đang dùng
            }
    
            // Thực hiện xóa nếu không có ràng buộc
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM DanhMuc WHERE MaDM = ?")) {
                ps.setString(1, maDanhMuc);
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean isCategoryInUse(String maDanhMuc) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Sach WHERE MaDM = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDanhMuc);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    public Object layTatCaDanhMucInfo() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'layTatCaDanhMucInfo'");
    }
    public void exportToExcel(TableModel model, XSSFSheet sheet) {
        // TODO Auto-generated method stub
        XSSFRow headerRow = sheet.createRow(0);
        for (int i = 0; i < model.getColumnCount(); i++) {
            headerRow.createCell(i).setCellValue(model.getColumnName(i));
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            XSSFRow dataRow = sheet.createRow(i + 1);
            for(int col = 0; col < model.getColumnCount(); col++) {
                Object value = model.getValueAt(i, col);
                if (value != null) {
                    dataRow.createCell(col).setCellValue(value.toString());
                } else {
                    dataRow.createCell(col).setCellValue(""); // Tránh NullPointerException
                }
            }
        }
    }


    // Add methods specific to category operations here
    // For example, methods to add, update, delete, or retrieve categories
    // These methods will use the conn object inherited from Connect_sqlServer
    
}
