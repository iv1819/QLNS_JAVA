/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Admin
 */
public class Customer {
    private String maKH;
    private String tenKH;
    private String sdt;
    private String email;
    private String duongDanAnh;
    private transient ImageIcon anhKH;

    public Customer() {
        // Constructor mặc định
    }

    public Customer(String maKH, String tenKH, String sdt) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sdt = sdt;
    }

    private void loadImageIcon() {
        if (duongDanAnh != null && !duongDanAnh.isEmpty()) {
            try {
                File file = new File(duongDanAnh);
                if (file.exists()) {
                    BufferedImage originalImage = ImageIO.read(file);
                    Image scaledImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    anhKH = new ImageIcon(scaledImage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Getters and Setters
    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDuongDanAnh() {
        return duongDanAnh;
    }

    public void setDuongDanAnh(String duongDanAnh) {
        this.duongDanAnh = duongDanAnh;
        loadImageIcon();
    }

    public ImageIcon getAnhKH() {
        return anhKH;
    }
}
