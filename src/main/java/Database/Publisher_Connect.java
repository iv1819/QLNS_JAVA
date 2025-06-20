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
}
