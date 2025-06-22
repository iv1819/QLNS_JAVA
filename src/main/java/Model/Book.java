
package Model;
public class Book{

    private String maSach;
    private String tenSach;
    private String duongDanAnh;
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
    }

    public String getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        this.danhMuc = danhMuc;
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

    @Override
    public String toString() {
        return "Book{" + "maSach=" + maSach + ", tenSach='" + tenSach + '\'' + ", duongDanAnh='" + duongDanAnh + '\'' + ", nhaXB='" + nhaXB + '\'' + ", tacGia='" + tacGia + '\'' + ", soLuong=" + soLuong + ", giaBan=" + giaBan + ", namXB=" + namXB + '}';
    }
}



