/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Model.Author;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author trang
 */
public class Author_Connect extends Connect_sqlServer {
    
    public ArrayList<Author> layToanBoTacGia() {
        ArrayList<Author> dstg = new ArrayList<>();
        try {
            String sql = "SELECT * FROM TacGia";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            
            while (result.next()) {
                Author tg = new Author();
                tg.setMaTG(result.getString("MaTG"));
                tg.setTenTG(result.getString("TenTG"));
                dstg.add(tg);
            }
            result.close();
            preparedStatement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return dstg;
    }
    
    public ArrayList<Author> timTacGiaTheoTen(String tenTG) {
        ArrayList<Author> dstg = new ArrayList<>();
        try {
            String sql = "SELECT * FROM TacGia WHERE TenTG LIKE ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, "%" + tenTG + "%");
            ResultSet result = preparedStatement.executeQuery();
            
            while (result.next()) {
                Author tg = new Author();
                tg.setMaTG(result.getString("MaTG"));
                tg.setTenTG(result.getString("TenTG"));
                dstg.add(tg);
            }
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dstg;
    }
    public boolean themTacGia(Author tg) {
        try {
            String sql = "INSERT INTO TacGia (MaTG, TenTG) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tg.getMaTG());
            pstmt.setString(2, tg.getTenTG());
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean capNhatTacGia(Author tg) {
        try {
            String sql = "UPDATE TacGia SET TenTG = ? WHERE MaTG = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tg.getTenTG());
            pstmt.setString(2, tg.getMaTG());

            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean xoaTacGia(String maTG) {
        try {
            String sql = "DELETE FROM TacGia WHERE MaTG = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maTG);
            
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
     public boolean tonTaiMaTG(String maTG) {
        try {
            String sql = "SELECT MaTG FROM TacGia WHERE MaTG = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maTG);
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
     
    public ArrayList<String> laySachTheoMaTG(String maTG) {
    ArrayList<String> dsSach = new ArrayList<>();
    try {
        String sql = "SELECT TenSach FROM Sach WHERE MaTG = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, maTG);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            dsSach.add(rs.getString("TenSach"));
        }
        rs.close();
        pstmt.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return dsSach;
    }
}
