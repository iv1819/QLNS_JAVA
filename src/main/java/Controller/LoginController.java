/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.Login_Connect;
import Model.Account;
import View.Login;
import View.MainMenu;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class LoginController {

    private Login view;
    private Login_Connect lg = new Login_Connect();

    public LoginController(Login view) {
        this.view = view;
    }

    public void handleLogin(String user, String pass) {
        if (user.isEmpty() || pass.isEmpty()) {
            view.showError("Vui lòng nhập đầy đủ tài khoản và mật khẩu.");
            return;
        }

        try {
            Account acc = lg.checkLogin(user, pass);

            if (acc == null) {
                view.showError("Sai tài khoản hoặc mật khẩu!");
                return;
            }

            if (!"Yes".equalsIgnoreCase(acc.getTrangThai())) {
                view.showError("Tài khoản chưa được duyệt.");
                return;
            }

            String tennv = lg.getTenNV(user);
            // ======= Đăng nhập thành công =======
            view.dispose();   // đóng form login

            if ("Quản lí".equalsIgnoreCase(acc.getTenCV())) {
                new MainMenu(true, tennv).setVisible(true);      // isManager = true
            } else {                                      // Nhân viên
                new MainMenu(false, tennv).setVisible(true);     // isManager = false
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            view.showError("Lỗi kết nối CSDL: " + ex.getMessage());
        }
    }
}

