/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Model.Order;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class Order_Connect extends Connect_sqlServer{
   public ArrayList<Order> layToanBoDH() {
        ArrayList<Order> dss = new ArrayList<>();
        String sql = """
            SELECT  d.MaDH,
                    k.TenKH,
                    d.NgayBan,
                    d.TongTien
            FROM    DonHang d
            LEFT JOIN KhachHang k ON k.MaKH = d.MaKH
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order o = new Order();
                o.setMaDH(rs.getString("MaDH"));
                o.setTenKH(rs.getString("TenKH"));      // có thể null
                o.setNgayBan(rs.getDate("NgayBan"));
                o.setTongTien(rs.getDouble("TongTien"));
                dss.add(o);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching orders: " + e.getMessage());
            e.printStackTrace();
        }
        return dss;
    }

    public ArrayList<Order> layDonHangTheoMaDH(String maDH) {
        ArrayList<Order> dss = new ArrayList<>();
        try {
            String sql = "SELECT MaDH, TenKH, NgayBan, TongTien FROM DonHang, KhachHang WHERE KhachHang.MaKH = DonHang.MaKH and DonHang.MaDH = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, maDH);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Order o = new Order();
                o.setMaDH(result.getString("MaDH"));
                o.setTenKH(result.getString("TenKH"));
                o.setNgayBan(result.getDate("NgayBan"));
                o.setTongTien(result.getDouble("TongTien"));
                dss.add(o);
            }
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy don hang theo mã don hang: " + e.getMessage());
            e.printStackTrace();
        }
        return dss;
    }
    public boolean themDH(Order o) {

    String maKH = getMaKH(o.getTenKH());   // có thể = null

    String sql = "INSERT INTO DonHang (MaDH, MaKH, NgayBan, TongTien) VALUES (?,?,?,?)";

    try (PreparedStatement pre = conn.prepareStatement(sql)) {

        // 1) MaDH (không bao giờ null)
        pre.setString(1, o.getMaDH());

        // 2) MaKH –‑ nếu null thì setNull
        if (maKH != null) {
            pre.setString(2, maKH);
        } else {
            pre.setNull(2, java.sql.Types.VARCHAR);   // cột MaKH phải ALLOW NULL
        }

        // 3) NgayBan –‑ giả sử o.getNgayBan() là java.sql.Date
        pre.setDate(3, o.getNgayBan());               // hoặc setTimestamp nếu bạn lưu cả giờ

        // 4) TongTien
        pre.setDouble(4, o.getTongTien());

        return pre.executeUpdate() > 0;               // thành công nếu ≥1 dòng

    } catch (SQLException e) {
        System.err.println("Lỗi khi thêm đơn hàng: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

// Phương thức để lấy danh sách tác giả (để đổ vào JComboBox)
    public ArrayList<String> getAllTenKH() {
        ArrayList<String> tenKH = new ArrayList<>();
        try {
            String sql = "SELECT TenKH FROM KhachHang";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tenKH.add(rs.getString("TenKH"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tenKH;
    }
    
    public boolean xoaDH(String maDH) {
        // Câu lệnh SQL để xóa dữ liệu từ bảng CTDH dựa trên ID
        String sql = "DELETE FROM DonHang WHERE MaDH = ?";
        try (PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, maDH);

            int rowsAffected = pre.executeUpdate(); // Thực thi lệnh DELETE
            return rowsAffected > 0; // Trả về true nếu có ít nhất một hàng được xóa
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa đơn hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public String getMaKH(String tenKH) {
        try {
            String sql = "SELECT MaKH FROM KhachHang WHERE TenKH = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tenKH);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String makh = rs.getString("MaKH");
                rs.close();
                pstmt.close();
                return makh;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }
}
