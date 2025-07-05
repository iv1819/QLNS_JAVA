/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Model.Employee;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;

/**
 *
 * @author MSI GF63
 */
public class Employee_Connect extends Connect_sqlServer {
    
    public ArrayList<Employee> layToanBoNhanVien() {
        ArrayList<Employee> dsnv = new ArrayList<>();
        try {
            String sql = "SELECT nv.*, cv.TenCV FROM NhanVien nv JOIN ChucVu cv ON nv.MaCV = cv.MaCV";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            
            while (result.next()) {
                Employee nv = new Employee();
                nv.setMaNV(result.getString("MaNV"));
                nv.setTenNV(result.getString("TenNV"));
                nv.setNgaySinh(result.getDate("NgaySinh"));
                nv.setNgayVaoLam(result.getDate("NgayVaoLam"));
                nv.setMaCV(result.getString("MaCV"));
                nv.setTenCV(result.getString("TenCV"));
                nv.setSdt(result.getString("Sdt"));
                nv.setLuong(result.getDouble("Luong"));
                dsnv.add(nv);
            }
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsnv;
    }

    public ArrayList<Employee> searchEmployees(String keyword) {
        ArrayList<Employee> dsnv = new ArrayList<>();
        try {
            String sql = "SELECT nv.*, cv.TenCV FROM NhanVien nv JOIN ChucVu cv ON nv.MaCV = cv.MaCV WHERE nv.TenNV LIKE ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, "%" + keyword + "%");
            ResultSet result = preparedStatement.executeQuery();
            
            while (result.next()) {
                Employee nv = new Employee();
                nv.setMaNV(result.getString("MaNV"));
                nv.setTenNV(result.getString("TenNV"));
                nv.setNgaySinh(result.getDate("NgaySinh"));
                nv.setNgayVaoLam(result.getDate("NgayVaoLam"));
                nv.setMaCV(result.getString("MaCV"));
                nv.setTenCV(result.getString("TenCV"));
                nv.setSdt(result.getString("Sdt"));
                nv.setLuong(result.getDouble("Luong"));
                dsnv.add(nv);
            }
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsnv;
    }

    public boolean themNhanVien(Employee nv) {
        try {
            String sql = "INSERT INTO NhanVien (MaNV, TenNV, NgaySinh, NgayVaoLam, MaCV, Sdt, Luong) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nv.getMaNV());
            pstmt.setString(2, nv.getTenNV());
            pstmt.setDate(3, new Date(nv.getNgaySinh().getTime()));
            pstmt.setDate(4, new Date(nv.getNgayVaoLam().getTime()));
            pstmt.setString(5, nv.getMaCV());
            pstmt.setString(6, nv.getSdt());
            pstmt.setDouble(7, nv.getLuong());
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatNhanVien(Employee nv) {
        try {
            String sql = "UPDATE NhanVien SET TenNV = ?, NgaySinh = ?, NgayVaoLam = ?, MaCV = ?, Sdt = ?, Luong = ? WHERE MaNV = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nv.getTenNV());
            pstmt.setDate(2, new Date(nv.getNgaySinh().getTime()));
            pstmt.setDate(3, new Date(nv.getNgayVaoLam().getTime()));
            pstmt.setString(4, nv.getMaCV());
            pstmt.setString(5, nv.getSdt());
            pstmt.setDouble(6, nv.getLuong());
            pstmt.setString(7, nv.getMaNV());
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaNhanVien(String maNV) {
        try {
            String sql = "DELETE FROM NhanVien WHERE MaNV = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNV);
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean tonTaiMaNV(String maNV) {
        try {
            String sql = "SELECT MaNV FROM NhanVien WHERE MaNV = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maNV);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            rs.close();
            pstmt.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean tonTaiSdt(String sdt) {
        try {
            String sql = "SELECT Sdt FROM NhanVien WHERE Sdt = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sdt);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            rs.close();
            pstmt.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tạo mã nhân viên tự động theo format NV001, NV002,...
     */
    public String taoMaNhanVienTuDong() {
        try {
            // Thử query đơn giản trước
            String sql = "SELECT TOP 1 MaNV FROM NhanVien WHERE MaNV LIKE 'NV%' ORDER BY CAST(SUBSTRING(MaNV, 3, LEN(MaNV)-2) AS INT) DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String maNVCuoi = rs.getString("MaNV");
                System.out.println("Mã nhân viên cuối cùng: " + maNVCuoi);
                
                // Lấy số từ mã cuối cùng (VD: NV001 -> 001)
                if (maNVCuoi.length() >= 3) {
                    String soCuoi = maNVCuoi.substring(2); // Bỏ "NV"
                    try {
                        int soMoi = Integer.parseInt(soCuoi) + 1;
                        String maMoi = String.format("NV%03d", soMoi);
                        System.out.println("Tạo mã mới: " + maMoi);
                        return maMoi;
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi parse số từ mã: " + soCuoi);
                        return "NV001";
                    }
                } else {
                    return "NV001";
                }
            } else {
                // Nếu chưa có nhân viên nào, bắt đầu từ NV001
                System.out.println("Chưa có nhân viên nào, tạo mã đầu tiên: NV001");
                return "NV001";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "NV001"; // Fallback
        }
    }
    
    /**
     * Test method để kiểm tra logic tạo mã
     */
    public void testTaoMaNhanVien() {
        System.out.println("=== TEST TẠO MÃ NHÂN VIÊN ===");
        try {
            // Kiểm tra tất cả mã hiện có
            String sql = "SELECT MaNV FROM NhanVien WHERE MaNV LIKE 'NV%' ORDER BY MaNV";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            System.out.println("Các mã nhân viên hiện có:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("MaNV"));
            }
            rs.close();
            pstmt.close();
            
            // Test tạo mã mới
            String maMoi = taoMaNhanVienTuDong();
            System.out.println("Mã mới được tạo: " + maMoi);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
