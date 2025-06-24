/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;
import java.sql.SQLException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JTable;
import Model.Book;
import Model.Category;
/**
 *
 * @author Admin
 */
public class Book_Connect extends Connect_sqlServer {
    	
     public ArrayList<Book> layToanBoSach() {
        ArrayList<Book> dss = new ArrayList<>();
        try {
            // Đảm bảo tên cột trong SQL khớp với tên cột trong CSDL của bạn
            String sql = "SELECT MaSach, TenSach, SoLuong, Gia, TenTG, TenNXB, Anh, NamXuatBan, TenDM FROM Sach, NhaXB, TacGia, DanhMuc WHERE Sach.MaNXB = NhaXB.MaNXB AND Sach.MaTG = TacGia.MaTG AND Sach.MaDM = DanhMuc.MaDM";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Book s = new Book();
                s.setMaSach(result.getString("MaSach"));
                s.setTenSach(result.getString("TenSach"));
                s.setSoLuong(result.getInt("SoLuong"));
                s.setGiaBan(result.getDouble("Gia"));
                s.setTacGia(result.getString("TenTG"));
                s.setNhaXB(result.getString("TenNXB"));
                s.setDuongDanAnh(result.getString("Anh")); // Lấy đường dẫn ảnh từ cột "Anh"
                s.setNamXB(result.getInt("NamXuatBan"));
                s.setDanhMuc(result.getString("TenDM"));
                dss.add(s);
                            System.err.println("fetching");

            }
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error fetching books: " + e.getMessage());
            e.printStackTrace();
        }
        return dss;
    }
        public ArrayList<Category> layTatCaDanhMucInfo() {
        ArrayList<Category> danhMucs = new ArrayList<>();
        try {
            String sql = "SELECT MaDM, TenDM FROM DanhMuc";
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                danhMucs.add(new Category(result.getString("MaDM"), result.getString("TenDM")));
            }
            result.close();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin danh mục: " + e.getMessage());
            e.printStackTrace();
        }
        return danhMucs;
    }
         public ArrayList<Book> laySachTheoMaDanhMuc(String maDanhMuc) {
        ArrayList<Book> dss = new ArrayList<>();
        try {
            String sql = "SELECT MaSach, TenSach, SoLuong, Gia, TenTG, TenNXB, Anh, NamXuatBan, TenDM FROM Sach, NhaXB, TacGia, DanhMuc WHERE Sach.MaNXB = NhaXB.MaNXB AND Sach.MaTG = TacGia.MaTG AND Sach.MaDM = DanhMuc.MaDM and Sach.MaDM = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, maDanhMuc);
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Book s = new Book();
                s.setMaSach(result.getString("MaSach"));
                s.setTenSach(result.getString("TenSach"));
                s.setSoLuong(result.getInt("SoLuong"));
                s.setGiaBan(result.getDouble("Gia"));
                s.setTacGia(result.getString("TenTG"));
                s.setNhaXB(result.getString("TenNXB"));
                s.setDuongDanAnh(result.getString("Anh"));
                s.setNamXB(result.getInt("NamXuatBan"));
                s.setDanhMuc(result.getString("TenDM")); // Gán mã danh mục
                dss.add(s);
            }
            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy sách theo mã danh mục: " + e.getMessage());
            e.printStackTrace();
        }
        return dss;
    }
    
    // ham lay doanh sach theo ten sach
    public ArrayList<Book> laySachTheoMaTen(String maten) {
        ArrayList<Book> dss3 = new ArrayList<Book>();
        try {
            String sql = "select MaSach, TenSach, SoLuong, Gia, TenTG, TenNXB, Anh, NamXuatBan, TenDM from Sach,NhaXB,TacGia,DanhMuc where Sach.MaNXB = NhaXB.MaNXB AND Sach.MaTG = TacGia.MaTG and Sach.MaDM = DanhMuc.MaDM and TenSach like ? ";
            PreparedStatement pre1 = conn.prepareStatement(sql);
            pre1.setString(1, "%" + maten + "%");
            ResultSet result = pre1.executeQuery();
            while (result.next()) {
                Book s = new Book();
                s.setMaSach(result.getString("MaSach"));
                s.setTenSach(result.getString("TenSach"));
                s.setSoLuong(result.getInt("SoLuong"));
                s.setGiaBan(result.getDouble("Gia"));
                s.setTacGia(result.getString("TenTG"));
                s.setNhaXB(result.getString("TenNXB"));
                s.setDuongDanAnh(result.getString("Anh"));
                s.setNamXB(result.getInt("NamXuatBan"));
                s.setDanhMuc(result.getString("TenDM"));
                dss3.add(s);
            }
            result.close();
            pre1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dss3;
    }
    
     public ArrayList<Book> laySachTheoTenTacGia(String tenTacGia)
    {
        ArrayList<Book> dss2 = new ArrayList<Book>();

        try {
            String sql = "select MaSach, TenSach, SoLuong, Gia, TenTG, TenNXB, Anh, NamXuatBan, TenDM from Sach,NhaXB, TacGia, DanhMuc where Sach.MaNXB = NhaXB.MaNXB and Sach.MaTG = TacGia.MaTG and Sach.MaDM = DanhMuc.MaDM and TenTG like ? " ;
            PreparedStatement pre1 = conn.prepareStatement(sql);
            pre1.setString(1, "%"+tenTacGia+"%");
            ResultSet result = pre1.executeQuery();
            while (result.next()){
                Book s = new Book();
                s.setMaSach(result.getString("MaSach"));
                s.setTenSach(result.getString("TenSach"));
                s.setSoLuong(result.getInt("SoLuong"));
                s.setGiaBan(result.getDouble("Gia"));
                s.setTacGia(result.getString("TenTG"));
                s.setNhaXB(result.getString("TenNXB"));
                s.setDuongDanAnh(result.getString("Anh")); // Lấy đường dẫn ảnh từ cột "Anh"
                s.setNamXB(result.getInt("NamXuatBan"));
                s.setDanhMuc(result.getString("TenDM"));
                dss2.add(s);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return dss2;
    }
     
     public ArrayList<Book> laySachTheoTenSachVaTenTacGia(String maten, String tenTacGia)
    {
        ArrayList<Book> dss4 = new ArrayList<Book>();

        try {
            String sql = "select MaSach, TenSach, SoLuong, Gia, TenTG, TenNXB, Anh, NamXuatBan, TenDM from Sach,NhaXB, TacGia, DanhMuc where Sach.MaNXB = NhaXB.MaNXB and Sach.MaTG = TacGia.MaTG and Sach.MaDM = DanhMuc.MaDM and TenSach like ? and TenTG like ?" ;
            PreparedStatement pre1 = conn.prepareStatement(sql);
            pre1.setString(1, "%"+maten+"%");
            pre1.setString(2, "%"+tenTacGia+"%");
            ResultSet result = pre1.executeQuery();
            while (result.next()){
                Book s = new Book();
                 s.setMaSach(result.getString("MaSach"));
                s.setTenSach(result.getString("TenSach"));
                s.setSoLuong(result.getInt("SoLuong"));
                s.setGiaBan(result.getDouble("Gia"));
                s.setTacGia(result.getString("TenTG"));
                s.setNhaXB(result.getString("TenNXB"));
                s.setDuongDanAnh(result.getString("Anh")); // Lấy đường dẫn ảnh từ cột "Anh"
                s.setNamXB(result.getInt("NamXuatBan"));
                s.setDanhMuc(result.getString("TenDM"));
                dss4.add(s);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return dss4;
    }

   
public boolean addBook(Book book) {
        try {
            String maTG = getMaTacGia(book.getTacGia()); 
            String maNXB = getMaNXB(book.getNhaXB());
            String maDM = getMaDM(book.getDanhMuc());
            if (maTG == null || maDM ==null|| maNXB == null) {
                System.err.println("Mã tác giả hoặc mã nhà xuất bản hoặc danh mục không hợp lệ.");
                return false;
            }

            String sql = "INSERT INTO Sach (MaSach, TenSach, MaNXB, MaTG, Gia, SoLuong, Anh, NamXuatBan, MaDM) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getMaSach());
            pstmt.setString(2, book.getTenSach());
            pstmt.setString(3, maNXB); // Sử dụng mã NXB
            pstmt.setString(4, maTG); // Sử dụng mã TG
            pstmt.setDouble(5, book.getGiaBan());
            pstmt.setInt(6, book.getSoLuong());
            pstmt.setString(7, book.getDuongDanAnh()); // Lưu đường dẫn ảnh
            pstmt.setInt(8, book.getNamXB());
            pstmt.setString(9, maDM);
            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBook(Book book) {
        try {
            String maTG = getMaTacGia(book.getTacGia()); 
            String maNXB = getMaNXB(book.getNhaXB());
            String maDM = getMaDM(book.getDanhMuc());
            if (maTG == null || maDM ==null|| maNXB == null) {
                System.err.println("Mã tác giả hoặc mã nhà xuất bản hoặc danh mục không hợp lệ.");
                return false;
            }

            String sql = "UPDATE Sach SET TenSach = ?, MaNXB = ?, MaTG = ?, Gia = ?, SoLuong = ?, Anh = ?, NamXuatBan = ?, MaDM = ? WHERE MaSach = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, book.getTenSach());
            pstmt.setString(2, maNXB);
            pstmt.setString(3, maTG);
            pstmt.setDouble(4, book.getGiaBan());
            pstmt.setInt(5, book.getSoLuong());
            pstmt.setString(6, book.getDuongDanAnh());
            pstmt.setInt(7, book.getNamXB());
            pstmt.setString(8, maDM);
            pstmt.setString(9, book.getMaSach());

            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBook(String maSach) {
        try {
            String sql = "DELETE FROM Sach WHERE MaSach = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maSach);

            int rowsAffected = pstmt.executeUpdate();
            pstmt.close();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public String getMaTacGia(String tenTacGia) {
        try {
            String sql = "SELECT MaTG FROM TacGia WHERE TenTG = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tenTacGia);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String maTG = rs.getString("MaTG");
                rs.close();
                pstmt.close();
                return maTG;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }public String getMaNXB(String tenNXB) {
        try {
            String sql = "SELECT MaNXB FROM NhaXB WHERE TenNXB = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tenNXB);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String maNXB = rs.getString("MaNXB");
                rs.close();
                pstmt.close();
                return maNXB;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }
    public String getMaDM(String tenDM) {
        try {
            String sql = "SELECT MaDM FROM DanhMuc WHERE TenDM = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tenDM);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String maDM = rs.getString("MaDM");
                rs.close();
                pstmt.close();
                return maDM;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy
    }
    
    // Phương thức để lấy danh sách tác giả (để đổ vào JComboBox)
    public ArrayList<String> getAllTacGiaNames() {
        ArrayList<String> tacGiaNames = new ArrayList<>();
        try {
            String sql = "SELECT TenTG FROM TacGia";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                tacGiaNames.add(rs.getString("TenTG"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tacGiaNames;
    }

    // Phương thức để lấy danh sách nhà xuất bản (để đổ vào JComboBox)
    public ArrayList<String> getAllNhaXBNames() {
        ArrayList<String> nxbNames = new ArrayList<>();
        try {
            String sql = "SELECT TenNXB FROM NhaXB";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                nxbNames.add(rs.getString("TenNXB"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nxbNames;
    }
    public ArrayList<String> getAllDanhMuc() {
        ArrayList<String> dmNames = new ArrayList<>();
        try {
            String sql = "SELECT TenDM FROM DanhMuc";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                dmNames.add(rs.getString("TenDM"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dmNames;
    }
   // Lấy số lượng sách hiện tại theo tên sách
public int laySoLuongSach(String tenSach) {
    try  {
        String sql = "SELECT SoLuong FROM Sach WHERE TenSach = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, tenSach);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("SoLuong");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}

// Cập nhật số lượng sách mới
public void capNhatSoLuongSach(String tenSach, int soLuongMoi) {
    try {
        String sql = "UPDATE Sach SET SoLuong = ? WHERE TenSach = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, soLuongMoi);
        stmt.setString(2, tenSach);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public boolean kiemTraTonTai(String maSach)
    {
        
        try {
            String sql ="select * from SACH where MaSach=?" ;
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, maSach);
            ResultSet result = pre.executeQuery();
            while (result.next()) return true ;
        } catch (Exception e) {
                e.printStackTrace();
        }

        return false ;
    }
}
