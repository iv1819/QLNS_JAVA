package Model;

public class Publisher {
    private String maNXB;
    private String tenNXB;
    private String sdt;
    private String diaChi;

    // Constructor mặc định
    public Publisher() {
    }
    // Constructor với maNXB và tenNXB
    public Publisher(String maNXB, String tenNXB) {
        this.maNXB = maNXB;
        this.tenNXB = tenNXB;
    }

    // Constructor đầy đủ
    public Publisher(String maNXB, String tenNXB, String sdt, String diaChi) {
        this.maNXB = maNXB;
        this.tenNXB = tenNXB;
        this.sdt = sdt;
        this.diaChi = diaChi;
    }

    // Getters and Setters
    public String getMaNXB() {
        return maNXB;
    }

    public void setMaNXB(String maNXB) {
        this.maNXB = maNXB;
    }

    public String getTenNXB() {
        return tenNXB;
    }

    public void setTenNXB(String tenNXB) {
        this.tenNXB = tenNXB;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "maNXB='" + maNXB + '\'' +
                ", tenNXB='" + tenNXB + '\'' +
                ", sdt='" + sdt + '\'' +
                ", diaChi='" + diaChi + '\'' +
                '}';
    }
}
