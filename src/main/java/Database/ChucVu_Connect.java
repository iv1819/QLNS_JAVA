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
import java.util.ArrayList;

/**
 *
 * @author trang
 */
public class ChucVu_Connect extends Connect_sqlServer{
    
    public ArrayList<ChucVu> layToanBoDanhSachChucVu() throws SQLException {
        ArrayList<ChucVu> dscv = new ArrayList<> ();
        try {
            String sql = "SELECT * FROM ChucVu";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            
            while (result.next()) {
                String maCV = result.getString("MaCV");
                String tenCV = result.getString("TenCV");
                dscv.add(new ChucVu(maCV, tenCV));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return dscv;
    }
    
    public boolean themTaiKhoan(Account tk) {
    try {
        String sql = "INSERT INTO TaiKhoan (TaiKhoan, MatKhau, MaCV, TrangThai) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, tk.getTaiKhoan());
        pstmt.setString(2, tk.getMatKhau());
        pstmt.setString(3, tk.getMaCV());
        pstmt.setString(4, tk.getTrangThai());

        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}
