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

    public RegisterController(RegisterM view) {
        this.view = view;
        this.accountConnect = new Account_Connect();
        this.chucvuConnect = new ChucVu_Connect();
    }

   public ArrayList<String> getAllChucVu(){
        return chucvuConnect.getAllCVName();
    }
    
    
    
    
}
