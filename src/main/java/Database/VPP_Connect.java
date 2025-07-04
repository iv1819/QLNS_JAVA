/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Model.Book;
import Model.Category;
import Model.VPP;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class VPP_Connect extends Connect_sqlServer{
        	
     public ArrayList<VPP> layToanBoVPP() {
        ArrayList<VPP> dss = new ArrayList<>();
        try {
            // Đảm bảo tên cột trong SQL khớp với tên cột trong CSDL của bạn
            String sql = "SELECT MaVPP, TenVPP, SoLuong, Gia, TenNCC, Anh FROM VPP, NhaCC WHERE VPP.MaNCC = NhaCC.MaNCC";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                VPP v = new VPP();
                v.setMaVPP(result.getString("MaVPP"));
                v.setTenVPP(result.getString("TenVPP"));
                v.setSoLuong(result.getInt("SoLuong"));
                v.setGiaBan(result.getDouble("Gia"));
                v.setNhaCC(result.getString("TenNCC"));
                v.setDuongDanAnh(result.getString("Anh")); // Lấy đường dẫn ảnh từ cột "Anh"
                dss.add(v);
            }
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error fetching vpp: " + e.getMessage());
            e.printStackTrace();
        }
        return dss;
    }
    
    public ArrayList<VPP> layVppTheoTen(String maten) {
        ArrayList<VPP> dss3 = new ArrayList<VPP>();
        try {
            String sql = "SELECT MaVPP, TenVPP, SoLuong, Gia, TenNCC, Anh FROM VPP, NhaCC WHERE VPP.MaNCC = NhaCC.MaNCC and TenVPP like ? ";
            PreparedStatement pre1 = conn.prepareStatement(sql);
            pre1.setString(1, "%" + maten + "%");
            ResultSet result = pre1.executeQuery();
             while (result.next()) {
                VPP v = new VPP();
                v.setMaVPP(result.getString("MaVPP"));
                v.setTenVPP(result.getString("TenVPP"));
                v.setSoLuong(result.getInt("SoLuong"));
                v.setGiaBan(result.getDouble("Gia"));
                v.setNhaCC(result.getString("TenNCC"));
                v.setDuongDanAnh(result.getString("Anh")); // Lấy đường dẫn ảnh từ cột "Anh"
                dss3.add(v);
            }
            result.close();
            pre1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dss3;
    }
      public ArrayList<VPP> layVppTheoSL(int sl)
    {
        ArrayList<VPP> dss2 = new ArrayList<VPP>();

        try {
            String sql = "SELECT MaVPP, TenVPP, SoLuong, Gia, TenNCC, Anh FROM VPP, NhaCC WHERE VPP.MaNCC = NhaCC.MaNCC and SoLuong <= ?  " ;
            PreparedStatement pre1 = conn.prepareStatement(sql);
            pre1.setInt(1, sl);
            ResultSet result = pre1.executeQuery();
            while (result.next()) {
                VPP v = new VPP();
                v.setMaVPP(result.getString("MaVPP"));
                v.setTenVPP(result.getString("TenVPP"));
                v.setSoLuong(result.getInt("SoLuong"));
                v.setGiaBan(result.getDouble("Gia"));
                v.setNhaCC(result.getString("TenNCC"));
                v.setDuongDanAnh(result.getString("Anh")); // Lấy đường dẫn ảnh từ cột "Anh"
                dss2.add(v);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return dss2;
    }
    
     public ArrayList<VPP> layVppTheoTenNCC(String tenNCC)
    {
        ArrayList<VPP> dss2 = new ArrayList<VPP>();

        try {
            String sql = "SELECT MaVPP, TenVPP, SoLuong, Gia, TenNCC, Anh FROM VPP, NhaCC WHERE VPP.MaNCC = NhaCC.MaNCC and TenNCC like ?  " ;
            PreparedStatement pre1 = conn.prepareStatement(sql);
            pre1.setString(1, "%"+tenNCC+"%");
            ResultSet result = pre1.executeQuery();
            while (result.next()) {
                VPP v = new VPP();
                v.setMaVPP(result.getString("MaVPP"));
                v.setTenVPP(result.getString("TenVPP"));
                v.setSoLuong(result.getInt("SoLuong"));
                v.setGiaBan(result.getDouble("Gia"));
                v.setNhaCC(result.getString("TenNCC"));
                v.setDuongDanAnh(result.getString("Anh")); // Lấy đường dẫn ảnh từ cột "Anh"
                dss2.add(v);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return dss2;
    }
     
     public ArrayList<VPP> layVppTheoTenVppvaTenNCC(String maten, String tenNCC)
    {
        ArrayList<VPP> dss4 = new ArrayList<VPP>();

        try {
            String sql = "SELECT MaVPP, TenVPP, SoLuong, Gia, TenNCC, Anh FROM VPP, NhaCC WHERE VPP.MaNCC = NhaCC.MaNCC and TenVPP like ? and TenNCC like ?" ;
            PreparedStatement pre1 = conn.prepareStatement(sql);
            pre1.setString(1, "%"+maten+"%");
            pre1.setString(2, "%"+tenNCC+"%");
            ResultSet result = pre1.executeQuery();
           while (result.next()) {
                VPP v = new VPP();
                v.setMaVPP(result.getString("MaVPP"));
                v.setTenVPP(result.getString("TenVPP"));
                v.setSoLuong(result.getInt("SoLuong"));
                v.setGiaBan(result.getDouble("Gia"));
                v.setNhaCC(result.getString("TenNCC"));
                v.setDuongDanAnh(result.getString("Anh")); // Lấy đường dẫn ảnh từ cột "Anh"
                dss4.add(v);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return dss4;
    }

   
    public boolean themVPP(VPP v) {
        try {
            String maNcc = getMaNCC(v.getNhaCC());
            if (maNcc == null) {
                System.err.println("Mã nha cung cap không hợp lệ.");
                return false;
            }

            String sql = "INSERT INTO VPP (MaVPP, TenVPP, SoLuong, Gia, MaNCC, Anh) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, v.getMaVPP());
            pstmt.setString(2, v.getTenVPP());
            pstmt.setInt(3, v.getSoLuong()); // Sử dụng mã NXB
            pstmt.setDouble(4, v.getGiaBan()); // Sử dụng mã TG
            pstmt.setString(5, maNcc);
            pstmt.setString(6, v.getDuongDanAnh());
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding vpp: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaVPP(VPP v) {
        try {
            String maNcc = getMaNCC(v.getNhaCC());
            if (maNcc == null) {
                System.err.println("Mã nha cung cap không hợp lệ.");
                return false;
            }

            String sql = "UPDATE VPP SET TenVPP = ?, SoLuong = ?, Gia = ?, MaNCC = ?, Anh = ? WHERE MaVPP = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, v.getTenVPP());
            pstmt.setInt(2, v.getSoLuong());
            pstmt.setDouble(3, v.getGiaBan());
            pstmt.setString(4, maNcc);
            pstmt.setString(5, v.getDuongDanAnh());
            pstmt.setString(6, v.getMaVPP());

            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating vpp: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaVPP(String maVPP) {
        try {
            String sql = "DELETE FROM VPP WHERE MaVPP = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maVPP);

            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting vpp: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public String getMaNCC(String tenNCC) {
        try {
            String sql = "SELECT MaNCC FROM NhaCC WHERE TenNCC = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tenNCC);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String maNCC = rs.getString("MaNCC");
                rs.close();
                pstmt.close();
                return maNCC;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }
    // Phương thức để lấy danh sách tác giả (để đổ vào JComboBox)
    public ArrayList<String> getAllNhaCCNames() {
        ArrayList<String> nccNames = new ArrayList<>();
        try {
            String sql = "SELECT TenNCC FROM NhaCC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                nccNames.add(rs.getString("TenNCC"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nccNames;
    }
public int laySoLuongVPP(String tenVPP) {
    try  {
        String sql = "SELECT SoLuong FROM VPP WHERE TenVPP = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, tenVPP);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("SoLuong");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}

public void capNhatSoLuongVPP(String tenSach, int soLuongMoi) {
    try {
        String sql = "UPDATE VPP SET SoLuong = ? WHERE TenVPP = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, soLuongMoi);
        stmt.setString(2, tenSach);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    public boolean kiemTraTonTai(String maVPP)
    {
        
        try {
            String sql ="select * from VPP where MaVPP=?" ;
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, maVPP);
            ResultSet result = pre.executeQuery();
            while (result.next()) return true ;
        } catch (Exception e) {
                e.printStackTrace();
        }

        return false ;
    }
}
