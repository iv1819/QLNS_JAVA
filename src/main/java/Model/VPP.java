/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import javax.swing.ImageIcon;

/**
 *
 * @author Admin
 */
public class VPP {
    private String maVPP;
    private String tenVPP;
    private int soLuong;
    private Double giaBan;
    private String nhaCC;
    private String duongDanAnh;

    public VPP(String maVPP, String tenVPP, int soLuong, Double giaBan, String nhaCC, String duongDanAnh) {
        this.maVPP = maVPP;
        this.tenVPP = tenVPP;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.nhaCC = nhaCC;
        this.duongDanAnh = duongDanAnh;
    }

    public VPP() {
    }

    public String getMaVPP() {
        return maVPP;
    }

    public void setMaVPP(String maVPP) {
        this.maVPP = maVPP;
    }

    public String getTenVPP() {
        return tenVPP;
    }

    public void setTenVPP(String tenVPP) {
        this.tenVPP = tenVPP;
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

    public String getNhaCC() {
        return nhaCC;
    }

    public void setNhaCC(String nhaCC) {
        this.nhaCC = nhaCC;
    }

    public String getDuongDanAnh() {
        return duongDanAnh;
    }

    public void setDuongDanAnh(String duongDanAnh) {
        this.duongDanAnh = duongDanAnh;
    }
    
}
