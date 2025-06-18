
package Model;
import java.io.IOException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
public class Book{

    private String maSach;
    private String tenSach;
    private String duongDanAnh;
    private transient ImageIcon anhSach;
    private String nhaXB;
    private String tacGia;
    private int soLuong;
    private Double giaBan;
    private int namXB;
 private String  danhMuc;
    public Book() {
        // Constructor mặc định không tham số
    }

   public Book(String maSach, String tenSach, int soLuong, Double giaBan, String tacGia, String nhaXB, String duongDanAnh, int namXB, String danhMuc) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.nhaXB = nhaXB;
        this.tacGia = tacGia;
        this.duongDanAnh = duongDanAnh;
        this.namXB = namXB;
        this.danhMuc = danhMuc; // Gán danh mục
        loadImageIcon(); // Gọi phương thức để tải và tạo ImageIcon
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
    }

    // Phương thức để tải và tạo ImageIcon
    private void loadImageIcon() {
        if (duongDanAnh != null && !duongDanAnh.isEmpty()) {
            try {
                File file = new File(duongDanAnh);
                if (file.exists()) {
                    BufferedImage image = ImageIO.read(file);
                    Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Hoặc kích thước khác
                    this.anhSach = new ImageIcon(scaledImage);
                } else {
                    this.anhSach = new ImageIcon(); // Hoặc có thể tải ảnh mặc định
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.anhSach = new ImageIcon(); // Hoặc có thể tải ảnh mặc định
            }
        } else {
            this.anhSach = new ImageIcon(); // Nếu không có đường dẫn
        }
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getDuongDanAnh() {
        return duongDanAnh;
    }

    public void setDuongDanAnh(String duongDanAnh) {
        this.duongDanAnh = duongDanAnh;
    }

    public ImageIcon getAnhSach() {
        return anhSach;
    }

    public void setAnhSach(ImageIcon anhSach) {
        this.anhSach = anhSach;
    }

    public String getNhaXB() {
        return nhaXB;
    }

    public void setNhaXB(String nhaXB) {
        this.nhaXB = nhaXB;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public Double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(Double giaBan) {
        this.giaBan = giaBan;
    }

    public int getNamXB() {
        return namXB;
    }

    public void setNamXB(int namXB) {
        this.namXB = namXB;
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        loadImageIcon(); // Gọi lại phương thức để tạo ImageIcon sau khi deserialize
    }

    @Override
    public String toString() {
        return "Book{" + "maSach=" + maSach + ", tenSach='" + tenSach + '\'' + ", duongDanAnh='" + duongDanAnh + '\'' + ", nhaXB='" + nhaXB + '\'' + ", tacGia='" + tacGia + '\'' + ", soLuong=" + soLuong + ", giaBan=" + giaBan + ", namXB=" + namXB + '}';
    }
}



