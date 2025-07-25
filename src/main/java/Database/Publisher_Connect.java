/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Publisher;
import javax.swing.table.TableModel;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author nam11
 */
public class Publisher_Connect extends Connect_sqlServer {

    public Publisher_Connect() {
        super();
    }
    public ArrayList<Publisher> getAllPublishers() {
        ArrayList<Publisher> publishers = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM NhaXB");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Publisher publisher = new Publisher(rs.getString("MaNXB"), rs.getString("TenNXB"),
                        rs.getString("Sdt"), rs.getString("DiaChi"));
                publishers.add(publisher);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publishers;
    }
    public boolean addPublisher(Publisher publisher) throws SQLException {
        String sql = "INSERT INTO NhaXB (MaNXB, TenNXB, Sdt, DiaChi) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Sinh mã tự động cho MaNXB
            String newMaNXB = generateNewMaNXB(); 
            ps.setString(1, newMaNXB);
            ps.setString(2, publisher.getTenNXB());
            ps.setString(3, publisher.getSdt());
            ps.setString(4, publisher.getDiaChi());
            return ps.executeUpdate() > 0;
        }
        }
        private String generateNewMaNXB() throws SQLException {
        String sql = "SELECT MAX(MaNXB) FROM NhaXB";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String lastMaNXB = rs.getString(1);
                if (lastMaNXB != null) {
                    // Bỏ tất cả ký tự không phải số, ví dụ: NXB_001 → 001
                    String numberPart = lastMaNXB.replaceAll("[^0-9]", "");
                    if (!numberPart.isEmpty()) {
                        int number = Integer.parseInt(numberPart) + 1;
                        return "nxb_" + String.format("%02d", number);
                    }
                }
            }
            return "NXB_001"; // Mặc định nếu chưa có bản ghi nào
        }
    }
    public boolean updatePublisher(Publisher publisher) throws SQLException {
        String sql = "UPDATE NhaXB SET TenNXB = ?, Sdt = ?, DiaChi = ? WHERE MaNXB = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, publisher.getTenNXB());
            ps.setString(2, publisher.getSdt());
            ps.setString(3, publisher.getDiaChi());
            ps.setString(4, publisher.getMaNXB());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean deletePublisher(String maNXB) {
        try {
            // Kiểm tra ràng buộc khóa ngoại trước
            if (isPublisherInUse(maNXB)) {
                return false; // Không thể xóa vì có sách đang dùng
            }
    
            // Thực hiện xóa nếu không có ràng buộc
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM NhaXB WHERE MaNXB = ?")) {
                ps.setString(1, maNXB);
                int rowsAffected = ps.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean isPublisherInUse(String maNXB) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Sach WHERE MaNXB = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNXB);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Có sách đang dùng NXB này
                }
            }
        }
        return false;
    }
    
    public void exportToExcel(TableModel model, XSSFSheet sheet) {
       // Header
       XSSFRow headerRow = sheet.createRow(0);
       for (int col = 0; col < model.getColumnCount(); col++) {
           headerRow.createCell(col).setCellValue(model.getColumnName(col));
       }

       // Data
       for (int row = 0; row < model.getRowCount(); row++) {
           XSSFRow dataRow = sheet.createRow(row + 1);
           for (int col = 0; col < model.getColumnCount(); col++) {
               Object value = model.getValueAt(row, col);
               dataRow.createCell(col).setCellValue(value != null ? value.toString() : "");
           }
       }
   }

}