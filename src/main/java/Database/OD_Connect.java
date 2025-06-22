/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Model.OD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class OD_Connect extends Connect_sqlServer {
    
   public ArrayList<OD> layCTDHtheoMaDH(String maDH) {
        ArrayList<OD> dss = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet result = null;

        try {
            // Use LEFT JOIN to conditionally link to Sach or VPP
            // COALESCE returns the first non-null expression in its list.
            // So, if Sach.TenSach is not null, it uses that; otherwise, it uses VPP.TenVPP.
            String sql = "SELECT CTDH.ID, " +
                         "COALESCE(Sach.TenSach, VPP.TenVPP) AS TenSP, " + // Alias the combined name as TenSP
                         "CTDH.SoLuong, " +
                         "CTDH.DonGia, " +
                         "CTDH.MaDH, " +
                         "CASE WHEN Sach.MaSach IS NOT NULL THEN 'Sach' ELSE 'VPP' END AS LoaiSP " + // Determine product type
                         "FROM CTDH " +
                         "LEFT JOIN Sach ON CTDH.MaSP = Sach.MaSach " +
                         "LEFT JOIN VPP ON CTDH.MaSP = VPP.MaVPP " +
                         "WHERE CTDH.MaDH = ?";

            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, maDH);

            result = preparedStatement.executeQuery();

            while (result.next()) {
                OD od = new OD();
                od.setId(result.getInt("ID"));
                // Retrieve the aliased product name
                od.setTenSP(result.getString("TenSP")); // Use TenSP from COALESCE
                od.setSoLuong(result.getInt("SoLuong"));
                od.setDonGia(result.getDouble("DonGia"));
                od.setMaDH(result.getString("MaDH"));
                
                // You can also retrieve the product type if needed in your OD object
                // String loaiSP = result.getString("LoaiSP"); 
                // od.setLoaiSP(loaiSP); // Assuming OD has a setLoaiSP method

                dss.add(od);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving order details by MaDH: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure resources are closed in a finally block
            try {
                if (result != null) result.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Log or handle closing errors
            }
        }
        return dss;
    }
     public boolean themCTDH (OD ctdh) {
          String maSP = timMaSP(ctdh.getTenSP()); 
            if (maSP == null) {
                System.err.println("Mã san pham không hợp lệ.");
                return false;
            }
        // Câu lệnh SQL để chèn dữ liệu vào bảng CTDH
        String sql = "INSERT INTO CTDH (MaDH, MaSP, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, ctdh.getMaDH());
            pre.setString(2, maSP);
            pre.setInt(3, ctdh.getSoLuong());
            pre.setDouble(4, ctdh.getDonGia());

            int rowsAffected = pre.executeUpdate(); // Thực thi lệnh INSERT
            return rowsAffected > 0; // Trả về true nếu có ít nhất một hàng được thêm
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm chi tiết đơn hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
     public String timMaSP(String tenSP) {
        String maSP = null; // Khởi tạo mã sản phẩm là null
        PreparedStatement pre = null;
        ResultSet result = null;

        try {
            // Câu lệnh SQL sử dụng UNION để kết hợp kết quả từ cả hai bảng Sach và VPP.
            // Chúng ta alias cột kết quả thành 'MaSP' để dễ dàng truy xuất.
            String sql = "SELECT MaSach AS MaSP FROM Sach WHERE TenSach = ? " +
                         "UNION " +
                         "SELECT MaVPP AS MaSP FROM VPP WHERE TenVPP = ?";
            
            pre = conn.prepareStatement(sql);
            pre.setString(1, tenSP); // Gán giá trị cho tham số thứ nhất (cho TenSach)
            pre.setString(2, tenSP); // Gán giá trị cho tham số thứ hai (cho TenVPP)

            result = pre.executeQuery(); // Thực thi truy vấn

            // Kiểm tra xem có kết quả nào được trả về không.
            // Nếu có, lấy mã sản phẩm đầu tiên tìm được.
            if (result.next()) {
                maSP = result.getString("MaSP");
            }

        } catch (SQLException e) {
            // In ra lỗi để debug. Trong ứng dụng thực tế, bạn nên ghi log lỗi chi tiết hơn.
            System.err.println("Lỗi khi tìm mã sản phẩm theo tên: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng ResultSet và PreparedStatement để giải phóng tài nguyên.
            try {
                if (result != null) result.close();
                if (pre != null) pre.close();
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return maSP; // Trả về mã sản phẩm hoặc null
    }
    public boolean xoaCTDH(String maDH) {
        // Câu lệnh SQL để xóa dữ liệu từ bảng CTDH dựa trên ID
        String sql = "DELETE FROM CTDH WHERE MaDH = ?";
        try (PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, maDH);

            int rowsAffected = pre.executeUpdate(); // Thực thi lệnh DELETE
            return rowsAffected > 0; // Trả về true nếu có ít nhất một hàng được xóa
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa chi tiết đơn hàng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    
}
