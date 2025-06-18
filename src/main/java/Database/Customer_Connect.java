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
}
