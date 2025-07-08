/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Model.Customer;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author MSI GF63
 */
public class Customer_Connect extends Connect_sqlServer {
    
    public ArrayList<Customer> layToanBoKhachHang() {
        ArrayList<Customer> dskh = new ArrayList<>();
        try {
            String sql = "SELECT * FROM KhachHang";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Customer kh = new Customer();
                kh.setMaKH(result.getString("MaKH"));
                kh.setTenKH(result.getString("TenKH"));
                kh.setSdt(result.getString("Sdt"));
                dskh.add(kh);
            }
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dskh;
    }

    public ArrayList<Customer> timKhachHang(String tenKH) {
        ArrayList<Customer> dskh = new ArrayList<>();
        try {
            String sql = "SELECT * FROM KhachHang WHERE TenKH LIKE ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, "%" + tenKH + "%");
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Customer kh = new Customer();
                kh.setMaKH(result.getString("MaKH"));
                kh.setTenKH(result.getString("TenKH"));
                kh.setSdt(result.getString("Sdt"));
                dskh.add(kh);
            }
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dskh;
    }

    public boolean themKhachHang(Customer kh) {
        try {
            String sql = "INSERT INTO KhachHang (MaKH, TenKH, Sdt) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kh.getMaKH());
            pstmt.setString(2, kh.getTenKH());
            pstmt.setString(3, kh.getSdt());
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatKhachHang(Customer kh) {
        try {
            String sql = "UPDATE KhachHang SET TenKH = ?, Sdt = ? WHERE MaKH = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kh.getTenKH());
            pstmt.setString(2, kh.getSdt());
            pstmt.setString(3, kh.getMaKH());
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhachHang(String maKH) {
        try {
            String sql = "DELETE FROM KhachHang WHERE MaKH = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maKH);
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tạo mã khách hàng tự động theo format KH001, KH002,...
     */
    public String taoMaKhachHangTuDong() {
        try {
            // Thử query đơn giản trước
            String sql = "SELECT TOP 1 MaKH FROM KhachHang WHERE MaKH LIKE 'KH%' ORDER BY MaKH DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String maKHCuoi = rs.getString("MaKH");
                System.out.println("Mã khách hàng cuối cùng: " + maKHCuoi);
                
                // Lấy số từ mã cuối cùng (VD: KH001 -> 001)
                if (maKHCuoi.length() >= 3) {
                    String soCuoi = maKHCuoi.substring(2); // Bỏ "KH"
                    try {
                        int soMoi = Integer.parseInt(soCuoi) + 1;
                        String maMoi = String.format("KH%03d", soMoi);
                        System.out.println("Tạo mã mới: " + maMoi);
                        return maMoi;
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi parse số từ mã: " + soCuoi);
                        return "KH001";
                    }
                } else {
                    return "KH001";
                }
            } else {
                // Nếu chưa có khách hàng nào, bắt đầu từ KH001
                System.out.println("Chưa có khách hàng nào, tạo mã đầu tiên: KH001");
                return "KH001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "KH001"; // Fallback
        }
    }
    
    /**
     * Test method để kiểm tra logic tạo mã
     */
    public void testTaoMaKhachHang() {
        System.out.println("=== TEST TẠO MÃ KHÁCH HÀNG ===");
        try {
            // Kiểm tra tất cả mã hiện có
            String sql = "SELECT MaKH FROM KhachHang WHERE MaKH LIKE 'KH%' ORDER BY MaKH";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("Các mã khách hàng hiện có:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("MaKH"));
            }
            rs.close();
            pstmt.close();
            
            // Test tạo mã mới
            String maMoi = taoMaKhachHangTuDong();
            System.out.println("Mã mới được tạo: " + maMoi);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Kiểm tra số điện thoại đã tồn tại chưa
     */
    public boolean tonTaiSdt(String sdt) {
        try {
            String sql = "SELECT COUNT(*) FROM KhachHang WHERE Sdt = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sdt);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                pstmt.close();
                return count > 0;
            }
            rs.close();
            pstmt.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Kiểm tra số điện thoại đã tồn tại ở khách hàng khác (dùng cho update)
     */
    public boolean tonTaiSdtKhac(String sdt, String maKH) {
        try {
            String sql = "SELECT COUNT(*) FROM KhachHang WHERE Sdt = ? AND MaKH != ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sdt);
            pstmt.setString(2, maKH);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int count = rs.getInt(1);
                rs.close();
                pstmt.close();
                return count > 0;
            }
            rs.close();
            pstmt.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
     public String timKhachHangSDT(String sdt) {
        ArrayList<Customer> dskh = new ArrayList<>();
        try {
            String sql = "SELECT TenKH FROM KhachHang WHERE Sdt LIKE ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, "%" + sdt + "%");
            ResultSet result = preparedStatement.executeQuery();
             if (result.next()) {
                String tenKH = result.getString("TenKH");
                result.close();
                preparedStatement.close();
                return tenKH;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ""; // Trả về null nếu không tìm thấy
    }

}
