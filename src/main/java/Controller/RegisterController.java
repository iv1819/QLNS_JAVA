/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.Account_Connect;
import Database.ChucVu_Connect;
import Model.ChucVu;
import View.RegisterM;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author trang
 */
public class RegisterController {
    private RegisterM view;
    private Account_Connect accountConnect;
    private ChucVu_Connect chucvuConnect;

    public RegisterController(RegisterM view, Account_Connect accountConnect, ChucVu_Connect chucvuConnect) {
        this.view = view;
        this.accountConnect = accountConnect;
        this.chucvuConnect = chucvuConnect;
    }

    public void loadAllChucVu() throws SQLException {
        ArrayList<ChucVu> chucvu = chucvuConnect.layToanBoDanhSachChucVu();
        view.displayDangKy(chucvu);
    }

    public List<String> getAllChucVu() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
    
}
