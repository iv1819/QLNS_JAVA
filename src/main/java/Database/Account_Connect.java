/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Model.Account;
import Model.ChucVu;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author trang
 */
public class Account_Connect extends Connect_sqlServer{
    
    public ArrayList<Account> layToanBoTaiKhoan() {
        ArrayList<Account> dstk = new ArrayList<>();
        try {
            String sql = "SELECT tk.TaiKhoan, tk.MatKhau, cv.TenCV, tk.TrangThai, tk.TenNV " +
             "FROM TaiKhoan tk JOIN ChucVu cv ON tk.MaCV = cv.MaCV";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            
            while (result.next()) {
                Account tk = new Account();
                tk.setUsername(result.getString("TaiKhoan"));
                tk.setPassword(result.getString("MatKhau"));
                tk.setTenCV(result.getString("TenCV"));
                tk.setTrangThai(result.getString("TrangThai"));
                tk.setTenNV(result.getString("TenNV"));
                dstk.add(tk);
            }
            result.close();
            preparedStatement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return dstk;
    }
      public String getMaCV(String tenCV) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }
    public boolean themTaiKhoan(Account acc) {
        try {
            String macv = getMaCV(acc.getTenCV());
            if (macv == null) {
                System.err.println("Mã chuc vu không hợp lệ.");
                return false;
            }
            String sql = "INSERT INTO TaiKhoan (TaiKhoan, MatKhau, MaCV, TrangThai, TenNV)VALUES (?,?,?,?,?) ";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, acc.getUsername());
            stmt.setString(2, acc.getPassword());
            stmt.setString(3, macv);
            stmt.setString(4, acc.getTrangThai());
            stmt.setString(5, acc.getTenNV());
            return stmt.executeUpdate() > 0;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<Account> timTaiKhoan(String taiKhoan ) {
        ArrayList<Account> dstk = new ArrayList<>();
        try {
            String sql = "SELECT * FROM TaiKhoan WHERE TaiKhoan LIKE ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, "%" + taiKhoan + "%");
            ResultSet result = preparedStatement.executeQuery();
            
            while (result.next()) {
                Account tk = new Account();
                tk.setUsername(result.getString("TaiKhoan"));
                tk.setPassword(result.getString("MatKhau"));                
                tk.setTenCV(result.getString("TenCV"));
                tk.setTrangThai(result.getString("TrangThai"));
                dstk.add(tk);
            }
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dstk;
    }
    
    public boolean capNhatTaiKhoan(Account tk) {
        try {
             String macv = getMaCV(tk.getTenCV());
            if (macv == null) {
                System.err.println("Mã chuc vu không hợp lệ.");
                return false;
            }
            String sql = "UPDATE TaiKhoan SET MatKhau = ?, MaCV = ?, TrangThai = ? WHERE TaiKhoan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(4, tk.getUsername());
            pstmt.setString(1, tk.getPassword());
            pstmt.setString(2, macv);
            pstmt.setString(3, tk.getTrangThai());
          
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean xoaTaiKhoan(String taiKhoan) {
        try {
            String sql = "DELETE FROM TaiKhoan WHERE TaiKhoan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, taiKhoan);
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean tonTaiTaiKhoan(String taiKhoan) {
        try {
            String sql = "SELECT TaiKhoan FROM TaiKhoan WHERE TaiKhoan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, taiKhoan);
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
    
    public ArrayList<String> layDanhSachChucVu() {
    ArrayList<String> danhSachTenCV = new ArrayList<>();
    try {
        String sql = "SELECT TenCV FROM ChucVu";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            danhSachTenCV.add(rs.getString("TenCV"));
        }
        rs.close();
        stmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return danhSachTenCV;
}


    public List<String> getAllTrangThai() {
    List<String> list = new ArrayList<>();
    list.add("Yes");
    list.add("No");
    return list;
}

    public String getMaCVByTenCV(String tenCV) {
    String maCV = null;
    try {
        String sql = "SELECT MaCV FROM ChucVu WHERE TenCV = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, tenCV);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            maCV = rs.getString("MaCV");
        }
        rs.close();
        stmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return maCV;
    }

}
