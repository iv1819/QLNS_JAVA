/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Model.Account;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class Login_Connect extends Connect_sqlServer {
/** @return null nếu TK/MK sai; trả về Account nếu đúng  */
    public Account checkLogin(String user, String pass) throws SQLException {

        String sql = """
            SELECT  tk.TaiKhoan,
                    tk.TrangThai,
                    cv.TenCV
            FROM    TaiKhoan tk
            JOIN    ChucVu  cv ON cv.MaCV = tk.MaCV
            WHERE   tk.TaiKhoan = ?
              AND   tk.MatKhau  = ?          -- demo: plain text
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Account acc = new Account();
                acc.setUsername(rs.getString("TaiKhoan"));
                acc.setTrangThai(rs.getString("TrangThai")); // 'Yes' / 'No'
                acc.setTenCV(rs.getString("TenCV"));          // 'Quản lý' / 'Nhân viên'
                return acc;
            }
        }
        return null;   // không tìm thấy
    }
     public String getTenNV(String username) {
        try {
            String sql = "SELECT TenNV FROM TaiKhoan WHERE TaiKhoan = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String tennv = rs.getString("TenNV");
                rs.close();
                pstmt.close();
                return tennv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ""; 
    }
}
