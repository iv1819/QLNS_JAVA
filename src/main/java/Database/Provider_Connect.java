/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Provider;

/**
 *
 * @author nam11
 */
public class Provider_Connect extends Connect_sqlServer{
    public Provider_Connect(){
        super();
    }
    public ArrayList<Provider> getAllProviders() {
        ArrayList<Model.Provider> providers = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM NhaCC");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Model.Provider provider = new Model.Provider(rs.getString("MaNCC"), rs.getString("TenNCC"),
                        rs.getString("Sdt"), rs.getString("DiaChi"));
                providers.add(provider);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return providers;
    }
    public boolean addProvider(Provider provider) throws SQLException {
        String sql = "INSERT INTO NhaCC (MaNCC, TenNCC, Sdt, DiaChi) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Sinh mã tự động cho MaNCC
            String newMaNCC = generateNewMaNCC(); 
            ps.setString(1, newMaNCC);
            ps.setString(2, provider.getTenNCC());
            ps.setString(3, provider.getSdt());
            ps.setString(4, provider.getDiaChi());
            return ps.executeUpdate() > 0;
        }
    }
    
    private String generateNewMaNCC() throws SQLException {
        String sql = "SELECT MAX(MaNCC) FROM NhaCC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String lastMaNCC = rs.getString(1);
                if (lastMaNCC != null) {
                    // Lấy phần số từ mã (bỏ chữ)
                    String numberPart = lastMaNCC.replaceAll("[^0-9]", "");
                    if (!numberPart.isEmpty()) {
                        int number = Integer.parseInt(numberPart) + 1;
                        return "ncc_" + String.format("%02d", number);
                    }
                }
            }
            return "NCC_01"; // Mặc định nếu chưa có bản ghi nào
        }
    }
    public boolean updateProvider(Provider provider) throws SQLException {
        String sql = "UPDATE NhaCC SET TenNCC = ?, Sdt = ?, DiaChi = ? WHERE MaNCC = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, provider.getTenNCC());
            ps.setString(2, provider.getSdt());
            ps.setString(3, provider.getDiaChi());
            ps.setString(4, provider.getMaNCC());
            return ps.executeUpdate() > 0;
        }
    }
    public boolean deleteProvider(String maNCC) throws SQLException {
        String sql = "DELETE FROM NhaCC WHERE MaNCC = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            return ps.executeUpdate() > 0;
        }
    }
    
    
}
