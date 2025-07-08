/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Model.Position;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author MSI GF63
 */
public class Position_Connect extends Connect_sqlServer {

    public ArrayList<Position> getAllPositions() {
        ArrayList<Position> list = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String maCV = rs.getString("MaCV");
                String tenCV = rs.getString("TenCV");
                list.add(new Position(maCV, tenCV));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean addPosition(Position position) {
        String sql = "INSERT INTO ChucVu (MaCV, TenCV) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, position.getMaCV());
            ps.setString(2, position.getTenCV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePosition(Position position) {
        String sql = "UPDATE ChucVu SET TenCV = ? WHERE MaCV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, position.getTenCV());
            ps.setString(2, position.getMaCV());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePosition(String maCV) {
        String sql = "DELETE FROM ChucVu WHERE MaCV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<String> getAllMaCV() {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT MaCV FROM ChucVu";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("MaCV"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getTenCVByMaCV(String maCV) {
        String sql = "SELECT TenCV FROM ChucVu WHERE MaCV = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maCV);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("TenCV");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getMaCVByTenCV(String tenCV) {
        try {
            String sql = "SELECT MaCV FROM ChucVu WHERE TenCV = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tenCV);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String maCV = rs.getString("MaCV");
                rs.close();
                pstmt.close();
                return maCV;
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String taoMaChucVuTuDong() {
        try {
            String sql = "SELECT TOP 1 MaCV FROM ChucVu WHERE MaCV LIKE 'cv_%' ORDER BY MaCV DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String maCVCuoi = rs.getString("MaCV");
                String soCuoi = maCVCuoi.substring(3); // Bỏ "cv_"
                int soMoi = Integer.parseInt(soCuoi) + 1;
                return String.format("cv_%02d", soMoi);
            } else {
                return "cv_01";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "cv_01";
        }
    }
}
